package service

import org.scalatest.{Matchers, FunSpec}
import persistence.{TestAuthPersistenceService, MyUserService}
import scala.concurrent._
import ExecutionContext.Implicits.global

class UserServiceSpec extends FunSpec with Matchers {
  val service = new MyUserService(new TestAuthPersistenceService())

  describe("Find") {
    it("does not find a non existent user") {
      service.find("userpass", "hello@gmail.com").foreach { result =>
        result should be(None)
      }
    }
  }
}
