package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.BaseScenario.scnBaseScneario

class PerfTestSimulation extends Simulation {

  //mvn gatling:test

  setUp(
    scnBaseScneario
      .inject(atOnceUsers(System.getProperty("amountOfUsers", "1").toInt))
      .protocols(httpProtocol)
  )
}
