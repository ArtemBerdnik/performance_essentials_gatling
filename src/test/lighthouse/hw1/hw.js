const fs = require('fs')
const puppeteer = require('puppeteer')
const lighthouse = require('lighthouse/lighthouse-core/fraggle-rock/api.js')

async function captureReport() {
	const browser = await puppeteer.launch({"headless": false, args: ['--allow-no-sandbox-job', '--allow-sandbox-debugging', '--no-sandbox', '--ignore-certificate-errors', '--disable-storage-reset=true']});
	const page = await browser.newPage();
	const baseURL = "http://localhost:80";
	
	await page.setViewport({"width":1920,"height":1080});
	await page.setDefaultTimeout(10000);
	
	const navigationPromise = page.waitForNavigation({timeout: 30000, waitUntil: ['domcontentloaded']});
	await page.goto(baseURL);
    await navigationPromise;
		
	const flow = await lighthouse.startFlow(page, {
		name: 'Performance Testing Essentials',
		configContext: {
		  settingsOverrides: {
			throttling: {
			  rttMs: 40,
			  throughputKbps: 10240,
			  cpuSlowdownMultiplier: 1,
			  requestLatencyMs: 0,
			  downloadThroughputKbps: 0,
			  uploadThroughputKbps: 0
			},
			throttlingMethod: "simulate",
			screenEmulation: {
			  mobile: false,
			  width: 1920,
			  height: 1080,
			  deviceScaleFactor: 1,
			  disabled: false,
			},
			formFactor: "desktop",
			onlyCategories: ['performance'],
		  },
		},
	});

  	//================================NAVIGATE================================
    await flow.navigate(baseURL, {
		stepName: 'open main page'
		});
  	console.log('main page is opened');
	
	//================================SELECTORS================================
	//Main page
	const tablesTab = "li[class='page_item page-item-13']>a";
	const cartTab = "li[class='page_item page-item-31']";

    //Tables page
	const livingRoomTable7link = "div[class='al_archive product-118 modern-grid-element green-box ic-design publish priced']";

	//Product cart page
	const addToCartButton = "button[type='submit']";
	const successfullyAddedModal  = "span[class='al-box success cart-added-info ']";

	//Cart page
	const continueShoppingButton  = "a[class='continue_shopping ic-secondary-button button green-box ic-design']";
	const placeOrderButton  = "input[value='Place an order']"

	//Order page
	const fullNameInput = "input[name='cart_name']"
	const addressInput = "input[name='cart_address']"
	const postalInput = "input[name='cart_postal']"
	const cityInput = "input[name='cart_city']"
	const countrySelect = "select[name='cart_country']"
	const phoneInput = "input[name='cart_phone']"
	const emailInput = "input[name='cart_email']"
	const submitOrderButton = "input[value='Place Order']"

    //Confirmation page
	const thankYouSign = "h1[class='entry-title']"
	
	//================================PAGE_ACTIONS================================
	await flow.startTimespan({ stepName: 'Open "Tables" tab' });
        await page.waitForSelector(tablesTab);
        await page.click(tablesTab);
        await navigationPromise;
    await flow.endTimespan();
    console.log('Tables tab is opened');

	await page.waitForSelector(livingRoomTable7link);
	await flow.startTimespan({ stepName: 'Open a table card' });
        await page.waitForSelector(livingRoomTable7link);
        await page.click(livingRoomTable7link);
        await navigationPromise;
	await flow.endTimespan();
	console.log('Table card is opened');

	await flow.startTimespan({ stepName: 'Add a table to a card' });
        await page.waitForSelector(addToCartButton);
        await page.click(addToCartButton);
        await navigationPromise;
        await page.waitForSelector(successfullyAddedModal);
	await flow.endTimespan();
	console.log('Table is added to a card');

	await flow.startTimespan({ stepName: 'Open cart tab' });
        await page.waitForSelector(cartTab);
        await page.click(cartTab);
        await navigationPromise;
        await page.waitForSelector(continueShoppingButton);
	await flow.endTimespan();
	console.log('Cart is opened');

	await flow.startTimespan({ stepName: 'Click "Place an order" button' });
        await page.waitForSelector(placeOrderButton);
        await page.click(placeOrderButton);
        await navigationPromise;
        await page.waitForSelector(fullNameInput);
	await flow.endTimespan();
	console.log('"Place an order" button is clicked');

	await flow.startTimespan({ stepName: 'Fill in required data and click "Place order button"' });
        await page.type(fullNameInput, "Test Username");
        await page.waitForSelector(addressInput);
        await page.type(addressInput, "Test Address");
        await page.waitForSelector(postalInput);
        await page.type(postalInput, "123456");
        await page.waitForSelector(cityInput);
        await page.type(cityInput, "Test city");
        await page.waitForSelector(countrySelect);
        await page.select(countrySelect, "BE");
        await page.waitForSelector(phoneInput);
        await page.type(phoneInput, "987654321");
        await page.waitForSelector(emailInput);
        await page.type(emailInput, "test@test.test");

        await page.waitForSelector(submitOrderButton);
        await page.click(submitOrderButton);

        await page.waitForSelector(thankYouSign);
	await flow.endTimespan();
	console.log('All mandatory data is filled in and order is placed');

	//================================REPORTING================================
	const reportPath = 'performance_ess_lighthouse_hw.report.html';

    const report = await flow.generateReport();

	fs.writeFileSync(reportPath, report);

    await browser.close();
}
captureReport();
