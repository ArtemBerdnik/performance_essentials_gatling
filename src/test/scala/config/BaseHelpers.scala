package config

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

object BaseHelpers {

  val defaultUrl = "http://localhost"
  val tablesUrl = defaultUrl + "/products-category/tables-2"
  val kitchenTablesUrl = defaultUrl + "/products/kitchen-table"
  val modernChairsUrl = defaultUrl + "/products/modern-chair"
  val addProductToCartUrl = defaultUrl + "/wp-admin/admin-ajax.php"
  val thankYouUrl = defaultUrl + "/thank-you"

  def thinkTimer(Min: Int = 2, Max: Int = 5): ChainBuilder = {
    pause(Min, Max)
  }

  val httpProtocol: HttpProtocolBuilder = http
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
    .acceptEncodingHeader("gzip, deflate, br, zstd")
}
