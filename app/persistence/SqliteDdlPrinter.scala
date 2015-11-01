package persistence

import persistence.auth.AuthDal
import persistence.general.LearnJazzDAL

import scala.slick.driver.SQLiteDriver

object SqliteDdlPrinter {
  val driver = SQLiteDriver

  val dals = Seq(new AuthDal(driver), new LearnJazzDAL(driver))

  def main(args: Array[String]) {
    dals.foreach { d =>
      println(d.createStatements.mkString("\n"))
    }
  }
}
