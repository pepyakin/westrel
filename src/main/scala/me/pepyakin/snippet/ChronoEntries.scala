package me.pepyakin.snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import Helpers._

import me.pepyakin.lib.DependencyFactory
import me.pepyakin.model._
import me.pepyakin.util.Auth

/**
 * 
 * @author Sergey
 */
object ChronoEntries extends Loggable {

  import AccountOp._
  import WestrelSchema._
  import org.squeryl.PrimitiveTypeMode._

  def render = {
    val entries = from(accountEntries)(select(_)).toSeq

    "tbody *" #> renderEntries(entries)
  }

  def renderEntries(in: Seq[AccountEntry]) = {
    renderData(in) & renderTotal(in)
  }

  def renderTotal(in: Seq[AccountEntry]) = {
    val (income, outcome) = in.partition(_.op == INCOME)

    logger.info("income: " + income)
    logger.info("outcome: " + outcome)

    val totalIncome = income.map(_.amount).reduce(_ + _)
    val totalOutcome = outcome.map(_.amount).reduce(_ - _)
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
