package api

import config.BaseHelpers.{addProductToCartUrl, defaultUrl, thankYouUrl}
import config.CssHelper.PRODUCT_NAME
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object CheckoutPage {

  def placeOrder(): ChainBuilder = {
    exec(
      http("Select a country from dropdown")
        .post(addProductToCartUrl)
        .formParam("action", "ic_state_dropdown")
        .formParam("country_code", "${country}")
        .formParam("state_code", "")
        .check(status.is(200))
    )
    .exec(
      http("Place an Order")
        .post(defaultUrl + "/checkout")
        .formParam("ic_formbuilder_redirect", thankYouUrl)
        .formParam("cart_content", """{"${productId}__":1}""")
        .formParam("product_price_${productId}__", "${productPrice}")
        .formParam("total_net", "${productPrice}")
        .formParam("trans_id", "17079214531041")
        .formParam("shipping", "order")
        .formParam("cart_type", "order")
        .formParam("cart_inside_header_1", "<b>BILLING ADDRESS</b>")
        .formParam("cart_company", "")
        .formParam("cart_name", "${full_name}")
        .formParam("cart_address", "${address}")
        .formParam("cart_postal", "${postal}")
        .formParam("cart_city", "${city}")
        .formParam("cart_country", "${country}")
        .formParam("cart_state", "")
        .formParam("cart_phone", "${phone}")
        .formParam("cart_email", "${email}")
        .formParam("cart_comment", "")
        .formParam("cart_inside_header_2", "<b>DELIVERY ADDRESS</b> (FILL ONLY IF DIFFERENT FROM THE BILLING ADDRESS)")
        .formParam("cart_s_company", "")
        .formParam("cart_s_name", "")
        .formParam("cart_s_address", "")
        .formParam("cart_s_postal", "")
        .formParam("cart_s_city", "")
        .formParam("cart_s_country", "")
        .formParam("cart_s_state", "")
        .formParam("cart_s_phone", "")
        .formParam("cart_s_email", "")
        .formParam("cart_s_comment", "")
        .formParam("cart_submit", "Place Order")
        .check(status.is(200))
        .check(css(PRODUCT_NAME).is("Thank You"))
    )
  }

}
