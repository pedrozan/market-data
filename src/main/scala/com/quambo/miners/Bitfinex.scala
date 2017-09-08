package com.quambo.miners

import java.util.Calendar

import awscala.Region
import awscala.dynamodbv2.{DynamoDB, Table}
import play.api.libs.json.{JsValue, Json}

/**
  * Created by pedro on 06/09/2017.
  */
object Bitfinex {

  def readTicker(): JsValue = {
    val url = "https://api.bitfinex.com/v1/pubticker/btcusd"
    val result = scala.io.Source.fromURL(url).mkString

    Json.parse(result)
  }

  def writeToDB(ticker: JsValue): Unit = {
    implicit val dynamoDB: DynamoDB = DynamoDB.at(Region.US_EAST_1)

    val table: Table = dynamoDB.table("bitfinexBitcoinSpot").get

    println(ticker)
    table.put(
      Json.stringify((ticker \ "timestamp").get),
      "mid" -> Json.stringify((ticker \ "mid").get),
      "bid" -> Json.stringify((ticker \ "bid").get),
      "ask" -> Json.stringify((ticker \ "ask").get),
      "last_price" -> Json.stringify((ticker \ "last_price").get),
      "low" -> Json.stringify((ticker \ "low").get),
      "high" -> Json.stringify((ticker \ "high").get),
      "volume" -> Json.stringify((ticker \ "volume").get),
      "current_date" -> Calendar.getInstance.getTime.toString
    )
  }

  def mineBitfinex(): Unit = {
    def go(oldTicker: JsValue): Unit = {
      val ticker = readTicker()
      if ((oldTicker \ "timestamp").get != (ticker \ "timestamp").get) writeToDB(ticker)
      Thread.sleep(1000)
      go(ticker)
    }

    go(readTicker())
  }

}
