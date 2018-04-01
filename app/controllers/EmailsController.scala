package controllers

import com.google.inject.Inject
import model.Title
import play.twirl.api.Html
import securesocial.CustomRuntimeEnvironment
import securesocial.core._
import titles.TitleRepository

class EmailsController @Inject() (titleRepository: TitleRepository,
                                  override implicit val env: CustomRuntimeEnvironment)
  extends SecureSocial {

  def sendRandom = SecuredAction { implicit r =>

    r.user.main.email match {
      case Some(email) =>
        val randomTitle = titleRepository.random
        val subject = s"You should learn ${randomTitle.title}"
        val body = populateTemplate(r, randomTitle)
        env.mailer.sendEmail(subject, email, (None, Some(Html(body))))
        Ok(s"email sent to $email")
      case None =>
        Ok("Could not send email to current user")
    }
  }

  private def populateTemplate(request: SecuredRequest[_], title: Title): String = {
    //TODO add youtube search link
    s"""You can learn ${title.title} at http://${request.host}/titles/${title.id}"""
  }
}
