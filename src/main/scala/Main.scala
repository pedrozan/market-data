import OkCoinMiner.OkCoinMiner._

object Main {

  def main(args:Array[String]): Unit = {
    val ticker = readTicker()

    // TODO colocar um try/catch
    writeToDB(ticker)
  }

}
