package api

import config.BaseHelpers.defaultUrl
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object ChairsPage {

  def open(): ChainBuilder = {
    exec(
      http("Open Chairs page")
        .get(defaultUrl + "/chairs")
    )
  }

}
