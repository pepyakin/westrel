package me.pepyakin.snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import Helpers._

import me.pepyakin.lib.DependencyFactory
import me.pepyakin.model.{AccountEntry, WestrelSchema, User}
import me.pepyakin.util.Auth

/**
 * 
 * @author Sergey
 */
object ChronoEntries {

  import WestrelSchema._
  import org.squeryl.PrimitiveTypeMode._

  def render = {
    val entries = from(accountEntries)(select(_)).toSeq

    "tbody *" #> renderEntries(entries)
  }

  def renderEntries(in: Seq[AccountEntry]) = {
    import me.pepyakin.model.AccountOp._

    "tr" #> in.map { entry =>
      "@amount *" #> entry.amount &
      "@date *" #> entry.date.toString &
      "@category *" #> entry.category &
      "tr [class]" #> {
        entry.op match {
          case INCOME => "success"
          case OUTCOME => "error"
        }
      }
    }
  }
}
