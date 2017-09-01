package OkCoinMiner

import awscala.dynamodbv2.{DynamoDB, Table}
import play.api.libs.json.{JsValue, Json}
import awscala._

object OkCoinMiner {

  def readTicker(): JsValue = {
    val url = "https://www.okcoin.com/api/v1/ticker.do?symbol=ltc_us"
    val result = scala.io.Source.fromURL(url).mkString

    Json.parse(result)
  }

  def writeToDB(ticker: JsValue): Unit = {
    implicit val dynamoDB: DynamoDB = DynamoDB.at(Region.US_EAST_1)

    val table: Table = dynamoDB.table("OkCoinReads").get
    println(ticker)
    table.put(
      (ticker \ "date").get,
      "buy" -> Json.stringify((ticker \ "ticker" \ "buy").get),
      "sell" -> Json.stringify((ticker \ "ticker" \ "sell").get),
      "high" -> Json.stringify((ticker \ "ticker" \ "high").get),
      "low" -> Json.stringify((ticker \ "ticker" \ "low").get),
      "last" -> Json.stringify((ticker \ "ticker" \ "last").get),
      "vol" -> Json.stringify((ticker \ "ticker" \ "last").get)
    )
  }

}
