package me.pepyakin.snippet

import net.liftweb.util._
import net.liftweb.common._
import Helpers._

import me.pepyakin.model._
import net.liftweb.http.{S, SHtml}
import xml.NodeSeq

/**
 * 
 * @author Sergey
 */
object CatEntries extends Loggable {

  import WestrelSchema._
  import org.squeryl.PrimitiveTypeMode._

//  def categories = from(accountEntries)( e => groupBy(e.category)).map {
//    e => e.key.toString
//  }

  def categories = Seq(
    "Одежда",
    "Зарплата"
  )

  def render(xhtml: NodeSeq): NodeSeq = {
    logger.info("categories " + categories)

    categories.map { categoryName =>
      <lift:ChronoEntries category={ categoryName }>{ xhtml }</lift:ChronoEntries>
    }
  }
}
