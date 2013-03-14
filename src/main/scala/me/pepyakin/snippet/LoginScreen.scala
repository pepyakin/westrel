package me.pepyakin.snippet

import net.liftweb.http.{StatefulSnippet, LiftScreen, SHtml, S}
import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import Helpers._

import me.pepyakin.util.Auth

/**
 * 
 * @author Sergey
 */
object LoginScreen extends StatefulSnippet {

  private var login = ""
  private var password = ""

  private val whence = S.referer openOr "/"

  def dispatch = {
    case "render" => render
  }

  def render = {
    "name=login" #> SHtml.text(login, login = _) &
    "name=password" #> SHtml.text(password, password = _) &
    ":submit" #> SHtml.onSubmitUnit(process)
  }

  def process() = {
    if (Auth.login(login, password)) {
      S.notice("Вы успешно зашли!")
      S.redirectTo(whence)
    } else {
      S.error("Какой отстой")
    }
  }
}
