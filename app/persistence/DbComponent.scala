package persistence

import com.github.tototoshi.slick.GenericJodaSupport
import scala.slick.driver.JdbcDriver

trait Profile {
  val driver: JdbcDriver
}

trait DBComponent {
  this: Profile =>
  import driver.simple._

  object JodaSupport extends GenericJodaSupport(driver)

  lazy val onDeleteAction: ForeignKeyAction = ForeignKeyAction.Cascade
}
