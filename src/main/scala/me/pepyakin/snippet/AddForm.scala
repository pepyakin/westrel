package me.pepyakin.snippet

import net.liftweb.http.{StatefulSnippet, SHtml, S}
import net.liftweb.util._
import net.liftweb.common._
import Helpers._
import java.util.{Calendar, Date}


import me.pepyakin.util.{Auth, DateUtil}
import me.pepyakin.model.{AccountEntry, AccountOp, WestrelSchema}

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

  private def process() {
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
      succeededCheck => {
        val (amount, category, date) = succeededCheck

        insertEntry(amount, category, date)

        S.redirectTo("/list")
      }
    )
  }


  private def insertEntry(amount: Double, category: String, date: Date) {
    import scala.math.abs

    /**
     * User.currentUser.openOrThrowException("This snippet is used on pages where the user is logged in")
     * Документация сказала что я полностью оправдан.
     */
    val userId = Auth.currentUserId.openOrThrowException(
      "This snippet is used on pages where the user is logged in")


    val op = {
      import AccountOp._
      if (amount > 0) INCOME else OUTCOME
    }

    val entry = AccountEntry(
      0,
      op,
      userId,
      abs(amount),
      category,
      date
    )

    {
      import WestrelSchema._
      accountEntries.insert(entry)
    }
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
      case c if c.length == 0 => Left("Категория должна быть заполнена")
      case c if c.trim.length == 0 => Left("Категория не может состоять из пробелов")
      case c => Right(c.trim)
    }
  }

  private def processDate(): Either[String, Date] = {
    GoodDate.unapply(date).map(updateYear).toRight("Дата должна быть в формате дд/ММ")
  }

  private def updateYear(date: Date) = {
    // Не подключать же JodaTime только из-за этого кода.
    val now = Calendar.getInstance()
    val cal = Calendar.getInstance()

    cal.setTime(date)
    cal.setYear(now.get(Calendar.YEAR))

    cal.getTime
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
