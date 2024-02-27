package api

import config.BaseHelpers.defaultUrl
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object HomePage {

  def PerfTestingEssentialsHome(): ChainBuilder = {
    exec(
      http("Open Performance Testing Essentials Home page")
        .get(defaultUrl)
        .check(regex("A theme by Gradient Themes"))
    )
  }
}
