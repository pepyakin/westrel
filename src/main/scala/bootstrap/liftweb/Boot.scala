package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._
import net.liftmodules.JQueryModule
import net.liftweb.http.js.jquery._
import java.sql.DriverManager
import me.pepyakin.model.{User, SchemaHelper}

import net.liftweb.squerylrecord.RecordTypeMode._
import me.pepyakin.util.Auth


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {


    val logbackConfUrl = getClass().getClassLoader().getResource("logback.xml")

    Logger.setup = Full(Logback.withFile(logbackConfUrl))

    // where to search snippet
    LiftRules.addToPackages("me.pepyakin")

    SchemaHelper.initH2()


    def loggedIn = () => {
      if (Auth.isLoggedIn) Empty else {
        Full(
          S.redirectTo(
            "/login",
            () => S.notice("","Представьтесь пожалуйста!")
          )
        )
      }
    }

    def userLinkText = Auth.currentUser.map(_.username).openOr("???")

    // Build SiteMap
    val entries = List(
      Menu.i("Домой") / "index" >> LocGroup("main") >> EarlyResponse(loggedIn),
      Menu.i("Список") / "list" >> LocGroup("main") >> EarlyResponse(loggedIn),
      Menu.i("Категории") / "by-category" >> LocGroup("main") >> EarlyResponse(loggedIn),

      Menu("user", userLinkText) / "#" >> LocGroup("user") >> PlaceHolder submenus(
        Menu.i("Выйти") / "logout" >> EarlyResponse ( { () => {
          Auth.logout
          S.redirectTo("/login", () => S.notice("", "Вы успешно вышли"))
        } })
      ),

      Menu.i("Логин") / "login" >> Hidden
    )


    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery=JQueryModule.JQuery172
    JQueryModule.init()

    // Make a transaction span the whole HTTP request
    S.addAround(new LoanWrapper {
      override def apply[T](f: => T): T =
      {
        inTransaction { f }
      }
    })

  }


}
