package service

import org.scalatest.{Matchers, FunSpec}
import persistence.auth.{TestAuthPersistenceService, CustomUserService}
import scala.concurrent._
import ExecutionContext.Implicits.global

class UserServiceSpec extends FunSpec with Matchers {
  val service = new CustomUserService(new TestAuthPersistenceService())

  describe("Find") {
    it("does not find a non existent user") {
      service.find("userpass", "hello@gmail.com").foreach { result =>
        result shouldBe None
      }
    }
  }
}
