package me.pepyakin.snippet

import net.liftweb.common._
import me.pepyakin.model._
import xml.NodeSeq

/**
 * Сниппет для вывода всех доступных категорий.
 * @author Sergey
 */
object CatEntries extends Loggable {

  def render(toWrap: NodeSeq): NodeSeq = {
    categories.flatMap(categorySnippet(toWrap, _))
  }

  private def categorySnippet(toWrap: NodeSeq, categoryName: String): NodeSeq = {
    <legend class="category-name">{ categoryName }</legend>
    <lift:ChronoEntries category={ categoryName }>{ toWrap }</lift:ChronoEntries>
  }

  /**
   * @return Список всех категорий.
   */
  private def categories: Seq[String] = {
    import WestrelSchema._
    import org.squeryl.PrimitiveTypeMode._

    from(accountEntries)(e => groupBy(e.category)).map {
      e => e.key.toString
    }.toSeq
  }
}
