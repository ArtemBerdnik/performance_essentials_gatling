package config

import config.BaseHelpers.{kitchenTablesUrl, modernChairsUrl}

object CssHelper {

  val GET_ALL_KITCHEN_TABLES = s"a[href^='$kitchenTablesUrl']"
  val GET_ALL_CHAIRS = s"a[href^='$modernChairsUrl']"
  val PRODUCT_PRICE = "td[class*='price-value']"
  val PRODUCT_ID = "input[name='current_product'][type='hidden']"
  val PRODUCT_NAME = "h1[class='entry-title']"
  val PRODUCT_NAME_IN_CART = "td[class='td-name']>a"
  val PRODUCT_PRICE_IN_CART = "td[class='td-price']"
}
