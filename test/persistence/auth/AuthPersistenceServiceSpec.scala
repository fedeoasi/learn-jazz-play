package persistence.auth

import org.joda.time.DateTime
import org.scalatest.{FunSpec, Matchers}
import securesocial.core.providers.MailToken
import securesocial.core.{AuthenticationMethod, BasicProfile, PasswordInfo}
import service.User

class AuthPersistenceServiceSpec extends FunSpec with Matchers {
  describe("User Persistence Service") {
    describe("Users") {
      val somePasswordInfo = Some(PasswordInfo("bcrypt", "hashedpwd", None))

      it("should not find a non existent user") {
        val ps = buildPersistenceService()
        ps.findUserByUserId("uid") should be(None)
      }

      it("should add a new user and find it by user id") {
        val ps = buildPersistenceService()
        val profile = buildProfile("pid", "uid", AuthenticationMethod.UserPassword, Some("First"), Some("Last"),
          Some("First Last"), Some("abc@gmail.com"), Some("avatar"), None, None, somePasswordInfo)
        ps.saveUser(profile)
        ps.findUserByUserId("uid") should be(Some(profile))
      }

      it("does not recreate the same user") {
        val ps = buildPersistenceService()
        val profile = buildProfile("pid", "uid", AuthenticationMethod.UserPassword, Some("First"), Some("Last"),
          Some("First Last"), Some("abc@gmail.com"), Some("avatar"), None, None, somePasswordInfo)
        ps.userCount should be(0)
        ps.saveUser(profile)
        ps.userCount should be(1)
        ps.saveUser(profile)
        ps.userCount should be(1)
      }

      it("should add a new user and find it by email") {
        val ps = buildPersistenceService()
        val profile = buildProfile("pid", "uid", AuthenticationMethod.UserPassword, email = Some("abc@gmail.com"))
        ps.saveUser(profile)
        ps.findUserByUserId("uid") should be(Some(profile))
      }

      it("should find the password info for an existing user") {
        val ps = buildPersistenceService()
        val profile = buildProfile("pid", "uid", AuthenticationMethod.UserPassword, Some("First"), Some("Last"),
          Some("First Last"), Some("abc@gmail.com"), Some("avatar"), None, None, somePasswordInfo)
        ps.saveUser(profile)
        val user = User(profile, List(profile), 1)
        ps.passwordInfoFor(user.main) should be(somePasswordInfo)
      }
    }

    describe("Tokens") {
      val uuid = "asdfasdf"
      val token = MailToken(uuid, "abc@gmail.com", DateTime.now(), DateTime.now(), isSignUp = true)

      it("should not find a non existent token") {
        val ps = buildPersistenceService()
        ps.findToken("asasdjfhkasdf") should be(None)
      }

      it("should add and find a token") {
        val ps = buildPersistenceService()
        ps.saveToken(token)
        ps.findToken(uuid) should be(Some(token))
      }

      it("should not delete a non existent token") {
        val ps = buildPersistenceService()
        ps.deleteToken("aksdfaasdf") should be(None)
      }

      it("should add and delete token") {
        val ps = buildPersistenceService()
        ps.saveToken(token)
        ps.deleteToken(uuid) should be(Some(token))
      }
    }
  }
  
  private def buildPersistenceService(): AuthPersistenceService = {
    new TestAuthPersistenceService()
  }
  
  private def buildProfile(providerId: String,
                   userId: String,
                   authMethod : securesocial.core.AuthenticationMethod,
                   firstName: scala.Option[String] = None,
                   lastName: scala.Option[String] = None,
                   fullName: scala.Option[String] = None,
                   email : scala.Option[String] = None,
                   avatarUrl : scala.Option[String] = None,
                   oAuth1Info : scala.Option[securesocial.core.OAuth1Info] = None,
                   oAuth2Info : scala.Option[securesocial.core.OAuth2Info] = None,
                   passwordInfo : scala.Option[securesocial.core.PasswordInfo] = None): BasicProfile = {
    BasicProfile(providerId, userId, firstName, lastName, fullName, email, avatarUrl, authMethod, oAuth1Info, oAuth2Info, passwordInfo)
  }
}
