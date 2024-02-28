package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.BaseScenario.scnEndToEnd

class PerfTestSimulation extends Simulation {

  //mvn gatling:test

    setUp(
        scnEndToEnd
        .inject(atOnceUsers(System.getProperty("amountOfUsers", "100").toInt))
        .protocols(httpProtocol)
    )

}
