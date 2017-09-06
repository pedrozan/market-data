import com.quambo.miners._

object Main {

  def main(args:Array[String]): Unit = {
    mineData()
  }

  def mineData(): Unit = {
    // OkCoin.mineOkCoin()
    Bitfinex.mineBitfinex()
  }
}
