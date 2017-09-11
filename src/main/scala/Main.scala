import com.quambo.miners._
import com.quambo.utils.io

object Main {

  def main(args:Array[String]): Unit = {
    io.createLogFile()
    mineData()
  }

  def mineData(): Unit = {
    io.log(Array("Called mining function"))
    // OkCoin.mineOkCoin()
    Bitfinex.mineBitfinex()
  }
}
