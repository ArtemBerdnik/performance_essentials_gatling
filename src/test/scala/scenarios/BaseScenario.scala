package scenarios

import config.BaseHelpers.thinkTimer
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure._

object BaseScenario {
  def scnEndToEnd: ScenarioBuilder = {
    scenario("End-to-End scenario for Performance testing Essentials")
      .exec(flushHttpCache)
      .exec(flushCookieJar)
      .exitBlockOnFail(
        group("Home page") {
          exec(api.HomePage.PerfTestingEssentialsHome())
            .exec(thinkTimer())
        }
          .group("Tables page") {
            exec(api.TablesPage.open())
              .exec(thinkTimer(5, 10))
              .exec(api.TablesPage.openRandomTable())
              .exec(thinkTimer())
              .exec(api.TablesPage.addTableToCart())
              .exec(thinkTimer())
          }
      )
      .randomSwitch(
        50.0 -> exitBlockOnFail(
          group("Chairs page") {
            exec(api.ChairsPage.open())
              .exec(thinkTimer(5, 10))
              .exec(api.ChairsPage.openRandomChair())
              .exec(thinkTimer())
              .exec(api.ChairsPage.addChairToCart())
              .exec(thinkTimer())
          }),
        30.0 -> exitBlockOnFail(
          group("Cart page") {
            exec(api.CartPage.open())
              .exec(thinkTimer())
              .exec(api.CartPage.placeOrder())
              //Give customers more time to think and fill required fields in
              .exec(thinkTimer(20, 30))
          }
            .group("Checkout page") {
              exec(api.CheckoutPage.placeOrder())
            }))
  }
}
