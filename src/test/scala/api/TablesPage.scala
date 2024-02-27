package api

import api.BasePage.addProductToCart
import config.BaseHelpers.{defaultUrl, kitchenTablesUrl, tablesUrl}
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object TablesPage {

  def open(): ChainBuilder = {
    exec(
      http("Open Tables page")
        .get(tablesUrl)
        .check(css(s"a[href^='$kitchenTablesUrl']", "href").findAll.saveAs("allKitchenTables"))
    )
  }

  def openRandomTable(): ChainBuilder = {
    exec(session => {
      // Retrieve the links foo kitchen tables from the session
      val links = session("allKitchenTables").as[Seq[String]]
      // Retrieve a random link to table
      val randomLink = links(scala.util.Random.nextInt(links.size))

      println(s"Random link: $randomLink")
      // Update the session with the selected link
      session.set("randomLink", randomLink)
    })
      .exec(
        http("Open Random table card")
          .get("${randomLink}")
          .check(status.is(200))
          .check(css("td[class*='price-value']").findAll
            .transform((prices: Seq[String]) => prices.headOption.getOrElse("").replace("$", ""))
            .saveAs("productPrice"))
          .check(css("input[name='current_product'][type='hidden']", "value").saveAs("productId"))
          .check(css("h1[class='entry-title']").saveAs("productName"))
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
