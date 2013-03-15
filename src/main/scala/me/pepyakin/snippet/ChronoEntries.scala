package me.pepyakin.snippet

import net.liftweb.util._
import net.liftweb.common._
import Helpers._

import me.pepyakin.model._
import net.liftweb.http.S

/**
 * Сниппет для вывода всех записей в заданной категории. Если категория не задана
 * то выводит все записи.
 *
 * @author Sergey
 */
object ChronoEntries extends Loggable {

  import AccountOp._

  def render = {
    val entries = S.attr("category") match {
      case Full(a) => entriesByCategory(a.what)
      case _ => allEntries
    }

    "tbody *" #> renderEntries(entries)
  }

  private def renderEntries(in: Seq[AccountEntry]) = {
    renderData(in) & renderTotal(in)
  }

  private def renderTotal(in: Seq[AccountEntry]) = {
    val (income, outcome) = in.partition(_.op == INCOME)

    val totalIncome = income.map(_.amount).foldLeft(0.0)(_ + _)
    val totalOutcome = outcome.map(_.amount).foldLeft(0.0)(_ + _)
    val total = totalIncome - totalOutcome

    "tr .total" #> {
      "td .total *" #> total
    }
  }

  private def renderData(in: Seq[AccountEntry]) = {
    "tr .data" #> in.map {
      entry =>
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

  import WestrelSchema._
  import org.squeryl.PrimitiveTypeMode._

  private def allEntries: Seq[AccountEntry] = {
    from(accountEntries)(select(_)).toSeq
  }

  private def entriesByCategory(category: String): Seq[AccountEntry] = {
    accountEntries.where(e => e.category === category).toSeq
  }
}
