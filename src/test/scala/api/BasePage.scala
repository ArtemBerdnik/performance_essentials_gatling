package api

import config.BaseHelpers.addProductToCartUrl
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object BasePage {

  def addProductToCart(_Id: String, Quantity: String = "1"): ChainBuilder = {
   exec(
     http("Add a table to a cart")
       .post(addProductToCartUrl)
       .formParam("action", "ic_add_to_cart")
       .formParam("add_cart_data", s"current_product=${_Id}&cart_content=&current_quantity=$Quantity")
       .formParam("cart_widget", "0")
       .formParam("cart_container", "0")
       .check(regex("Added!"))
       .check(regex("See your cart"))
   )
  }
}
