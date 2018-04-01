package securesocial

import com.google.inject.{Inject, Provider}
import scala.slick.driver.JdbcDriver.simple._

class CustomRuntimeEnvironmentProvider @Inject() (database: Database) extends Provider[CustomRuntimeEnvironment] {
  override def get(): CustomRuntimeEnvironment = {
    new CustomRuntimeEnvironment(database)
  }
}