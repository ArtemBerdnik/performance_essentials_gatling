package api

import api.BasePage.addProductToCart
import config.BaseHelpers.tablesUrl
import config.CssHelper.{GET_ALL_KITCHEN_TABLES, PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE}
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object TablesPage {

  def open(): ChainBuilder = {
    exec(
      http("Open Tables page")
        .get(tablesUrl)
        .check(css(GET_ALL_KITCHEN_TABLES, "href").findAll.saveAs("allKitchenTables"))
    )
  }

  def openRandomTable(): ChainBuilder = {
    exec(session => {
      // Retrieve the links foo kitchen tables from the session
      val links = session("allKitchenTables").as[Seq[String]]
      // Retrieve a random link to table
      val randomLinkToTable = links(scala.util.Random.nextInt(links.size))

      println(s"Random link to a table: $randomLinkToTable")
      // Update the session with the selected link
      session.set("randomLinkToTable", randomLinkToTable)
    })
      .exec(
        http("Open Random table card")
          .get("${randomLinkToTable}")
          .check(status.is(200))
          .check(css(PRODUCT_PRICE).findAll
            .transform((prices: Seq[String]) => prices.headOption.getOrElse("").replace("$", ""))
            .saveAs("productPrice"))
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

  def addTableToCart(): ChainBuilder = {
    exec(
      addProductToCart("${productId}", "1")
    )
  }
}
