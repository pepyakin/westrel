package me.pepyakin.snippet

import net.liftweb.http.{StatefulSnippet, SHtml, S}
import net.liftweb.util._
import net.liftweb.common._
import Helpers._

import me.pepyakin.util.Auth

/**
 * 
 * @author Sergey
 */
object LoginScreen extends StatefulSnippet with Loggable {

  private var login = ""
  private var password = ""

  private val whence = "/"

  def dispatch = {
    case "render" => render
  }

  def render = {
    "name=username" #> SHtml.text(login, login = _) &
    "name=password" #> SHtml.password(password, password = _) &
    ":submit" #> SHtml.onSubmitUnit(process)
  }

  def process() = {
    logger.info("user: " + login + " password: " + password)

    if (Auth.login(login, password)) {
      S.notice("Вы успешно зашли!")
      S.redirectTo(whence)
    } else {
      S.error("Какой отстой")
    }
  }
}
