package me.pepyakin.snippet

import net.liftweb.http.{StatefulSnippet, SHtml, S}
import net.liftweb.util._
import net.liftweb.common._
import Helpers._
import java.util.Date

import me.pepyakin.util.DateUtil

/**
 * 
 * @author Sergey
 */
class AddForm extends StatefulSnippet {

  private var amount = "0"
  private var category = ""
  private var date = ""

  def dispatch = {
    case "render" => render
  }

  def render = {
    "name=amount" #> SHtml.text(amount, amount = _) &
    "name=category" #> SHtml.text(category, category = _) &
    "name=date" #> SHtml.text(date, date = _) &
    ":submit" #> SHtml.onSubmitUnit(process)
  }

  private def process() = {
    // Очень не помешала бы конструкция Try из 2.10.

    val r = processAmount().right.flatMap { amount =>
      processCategory().right.flatMap { category =>
        processDate().right.flatMap { date =>
          Right((amount, category, date))
        }
      }
    }

    r.fold(
      errorHappened => S.error("Ошибка: " + errorHappened),
      succeededCheck => succeededCheck match {
        case (amount, category, date) => {
          S.notice("Успешно")
        }
      }
    )
  }

  private def processAmount(): Either[String, Double] = {
    amount match {
      case GoodDouble(d) if d == 0.0 =>
        // Сравнивание с нулем оправданно. Нам важно отследить именно нуль,
        // бесконечно маленькие суммы подерживаются.
        Left("Сумма не может быть равна 0")

      case GoodDouble(d) => Right(d)
      case _ => Left("Сумма - не число")
    }
  }

  private def processCategory(): Either[String, String] = {
    category.toLowerCase match {
      case c if c.length == 0 => Left("Строка пуста")
      case c if c.trim.length == 0 => Left("Строка состоит из пробелов")
      case c => Right(c.trim)
    }
  }

  private def processDate(): Either[String, Date] = {
    GoodDate.unapply(date).toRight("Строка не является датой")
  }

  private object GoodDate {

    def unapply(s: String): Option[Date] ={
      import DateUtil._
      import java.text.ParseException

      try {
        Some(ruSimpleDate.parse(s))
      } catch {
        case _: ParseException => None
      }
    }
  }

  private object GoodDouble {
    // TODO: Move to .util?

    def unapply(s: String): Option[Double] = try {
      val d = s.toDouble

      if (d.isNaN || d.isInfinity) None else Some(d)
    } catch {
      case _: NumberFormatException => None
    }
  }
}
