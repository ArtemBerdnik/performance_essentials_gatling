package api

import config.BaseHelpers.defaultUrl
import config.CssHelper.{PRODUCT_NAME_IN_CART, PRODUCT_PRICE_IN_CART}
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object CartPage {

  def open(): ChainBuilder = {
    exec(
      http("Open Cart page")
        .get(defaultUrl + "/cart")
        .check(css(PRODUCT_NAME_IN_CART).saveAs("productNameInCart"))
        .check(css(PRODUCT_PRICE_IN_CART).saveAs("productPriceInCart"))
    ).exec(session => {
      val productPriceInCart = session("productPriceInCart").as[String]
      val productNameInCart = session("productNameInCart").as[String]
      val expectedProductName = session("productName").as[String]
      val expectedProductPrice = session("productPrice").as[String]
      assert(productNameInCart.equals(expectedProductName), s"Expected: $productNameInCart. Actual $expectedProductName")
      assert(productPriceInCart.equals(expectedProductPrice), s"Expected: $productPriceInCart. Actual $expectedProductPrice")
      session
    })
  }

  def placeOrder(): ChainBuilder = {
    exec(
      http("Place an Order")
        .post(defaultUrl + "/checkout")
        .formParam("cart_content", """{"${productId}__":1}""")
        .formParam("p_id[]", "${productId}__")
        .formParam("p_quantity[]", "1")
        .formParam("total_net", "${productPrice}")
        .formParam("trans_id", "17079214514842")
        .formParam("shipping", "order")
        .check(css("input[value='Place Order']").exists)
    )
  }
}
