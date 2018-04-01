package securesocial

import com.google.inject.{Inject, Provider}
import scala.slick.driver.JdbcDriver.simple._

class MyRuntimeEnvironmentProvider @Inject() (database: Database) extends Provider[MyRuntimeEnvironment] {
  override def get(): MyRuntimeEnvironment = {
    new MyRuntimeEnvironment(database)
  }
}