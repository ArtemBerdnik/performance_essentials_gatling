package scenarios

import config.BaseHelpers.thinkTimer
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure._

object BaseScenario {

  def scnBaseScneario: ScenarioBuilder = {
    scenario("Basic scenario")
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
          .group("Cart page") {
            exec(api.CartPage.open())
              .exec(thinkTimer())
              .exec(api.CartPage.placeOrder())
              .exec(thinkTimer(10, 15))
          }
          .group("Checkout page") {
            exec(api.CheckoutPage.placeOrder())
          }
      )
  }

}
