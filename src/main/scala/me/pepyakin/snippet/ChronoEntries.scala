package me.pepyakin.snippet

import net.liftweb.util._
import net.liftweb.common._
import Helpers._

import me.pepyakin.model._
import net.liftweb.http.S

/**
 * 
 * @author Sergey
 */
object ChronoEntries extends Loggable {

  import AccountOp._
  import WestrelSchema._
  import org.squeryl.PrimitiveTypeMode._


  private def allEntries: Seq[AccountEntry] = {
    from(accountEntries)(select(_)).toSeq
  }

  private def entriesByCategory(category: String): Seq[AccountEntry] = {
    accountEntries.where(e => e.category === category).toSeq
  }

  def render = {
    logger.info("chronoentries.render()")

    val entries = S.attr("category") match {
      case Full(a) => entriesByCategory(a.what)
      case _ => allEntries
    }

    "tbody *" #> renderEntries(entries)
  }

  def renderEntries(in: Seq[AccountEntry]) = {
    renderData(in) & renderTotal(in)
  }

  def renderTotal(in: Seq[AccountEntry]) = {
    val (income, outcome) = in.partition(_.op == INCOME)

    logger.info("income: " + income)
    logger.info("outcome: " + outcome)

    val totalIncome = income.map(_.amount).foldLeft(0.0)(_ + _)
    val totalOutcome = outcome.map(_.amount).foldLeft(0.0)(_ + _)
    val total = totalIncome - totalOutcome

    logger.info("total: " + total)

    "tr .total" #> {
      "td .total *" #> total
    }
  }

  def renderData(in: Seq[AccountEntry]) = {
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
}
