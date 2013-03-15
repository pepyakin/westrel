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
object LoginScreen extends Loggable {

  def render = {
    var username = ""
    var password = ""

    def process() = {
      if (Auth.login(username, password)) {
        S.notice("Вы успешно зашли!")
        S.redirectTo("/")
      } else {
        S.error("Какой отстой")
      }
    }

    "name=username" #> SHtml.onSubmit(username = _) &
    "name=password" #> SHtml.onSubmit(password = _) &
    ":submit" #> SHtml.onSubmitUnit(process)
  }
}
