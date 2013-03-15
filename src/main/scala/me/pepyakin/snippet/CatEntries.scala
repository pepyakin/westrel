package me.pepyakin.snippet

import net.liftweb.common._

import me.pepyakin.model._
import xml.NodeSeq

/**
 * 
 * @author Sergey
 */
object CatEntries extends Loggable {

  import WestrelSchema._
  import org.squeryl.PrimitiveTypeMode._

  def categories: Seq[String] = from(accountEntries)( e => groupBy(e.category)).map {
    e => e.key.toString
  }.toSeq

  def render(xhtml: NodeSeq): NodeSeq = {
    logger.info("categories " + categories)

    categories.flatMap(categorySnippet(xhtml, _))
  }

  private def categorySnippet(xhtml: NodeSeq, categoryName: String): NodeSeq = {
    <legend>{ categoryName }</legend>
    <lift:ChronoEntries category={ categoryName }>{ xhtml }</lift:ChronoEntries>
  }
}
