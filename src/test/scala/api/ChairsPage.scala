package api

import api.BasePage.addProductToCart
import config.BaseHelpers.defaultUrl
import config.CssHelper.{GET_ALL_CHAIRS, PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE}
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object ChairsPage {

  def open(): ChainBuilder = {
    exec(
      http("Open Chairs page")
        .get(defaultUrl + "/chairs")
        .check(css(GET_ALL_CHAIRS, "href").findAll.saveAs("allModernChairs"))
    )
  }

  def openRandomChair(): ChainBuilder = {
    exec(session => {
      // Retrieve the links for chairs from the session
      val links = session("allModernChairs").as[Seq[String]]
      // Retrieve a random link to table
      val randomLinkToChair = links(scala.util.Random.nextInt(links.size))

      println(s"Random link to a chair: $randomLinkToChair")
      // Update the session with the selected link
      session.set("randomLinkToChair", randomLinkToChair)
    })
      .exec(
        http("Open Random chair card")
          .get("${randomLinkToChair}")
          .check(status.is(200))
          .check(css(PRODUCT_PRICE).findAll
            .transform((prices: Seq[String]) => prices.headOption.getOrElse("").replace("$", ""))
            .saveAs("chairPrice"))
          .check(css(PRODUCT_ID, "value").saveAs("productId"))
          .check(css(PRODUCT_NAME).saveAs("productName"))
      )
      .exec(session => {
        // Retrieve the price for the opened kitchen table
        val price = session("productPrice").as[String]
        val productId = session("productId").as[String]
        val productName = session("productName").as[String]

        println(s"Product Id: $productId")
        println(s"Product Name: $productName")
        println(s"Product Price: $price")
        // Update the session with the fetched price
        session.set("productPrice", price)
        session.set("productId", productId)
        session.set("productName", productName)
      })
  }

  def addChairToCart(): ChainBuilder = {
    exec(
      addProductToCart("${productId}", "1")
    )
  }
}
