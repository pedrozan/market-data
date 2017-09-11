package com.quambo.miners

import java.util.Calendar

import awscala.Region
import awscala.dynamodbv2.{DynamoDB, Table}
import play.api.libs.json.{JsValue, Json, __, _}
import play.api.libs.functional.syntax._

import com.quambo.utils.io

/**
  * Created by pedro on 06/09/2017.
  */

case class Ticker(
                   timestamp: String,
                   mid: String,
                   bid: String,
                   ask: String,
                   las: String,
                   low: String,
                   high: String,
                   volume: String
                 )

object Bitfinex {

  def readTicker(): JsValue = {
    val url = "https://api.bitfinex.com/v1/pubticker/btcusd"
    val result = scala.io.Source.fromURL(url).mkString

    Json.parse(result)
  }

  def validateTicker(ticker: JsValue): JsResult[Ticker] = {
    implicit val tickerReads = (
        (__ \ "timestamp").read[String] and
        (__ \ "mid").read[String] and
        (__ \ "bid" ).read[String] and
        (__ \ "ask").read[String] and
        (__ \ "last_price").read[String] and
        (__ \ "low").read[String] and
        (__ \ "high").read[String] and
        (__ \ "volume").read[String]
      )(Ticker.apply _)

    ticker.validate[Ticker]
  }

  def writeToDB(ticker: JsValue): Unit = {
    implicit val dynamoDB: DynamoDB = DynamoDB.at(Region.US_EAST_1)

    val table: Table = dynamoDB.table("bitfinexBitcoinSpot").get

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
    io.log(Array("mining..."))
    def go(oldTicker: JsValue): Unit = {
      Thread.sleep(1000)
      val ticker = readTicker()
      if (validateTicker(ticker).isSuccess) {
        if ((oldTicker \ "timestamp").get != (ticker \ "timestamp").get) {
          writeToDB(ticker)
        }
      } else {
        io.log(Array("ticker error", validateTicker(ticker).toString))
        io.log(Array("bad ticker", ticker.toString()))
      }
      go(ticker)
    }

    go(readTicker())
  }

}
