package me.pepyakin.model

import org.squeryl.KeyedEntity
import net.liftweb.squerylrecord.RecordTypeMode._
import java.util.Date
import AccountOp._


/**
 * @author Sergey
 */
case class AccountEntry(
  id: Long,
  op: AccoutOp,
  userId: Long,
  amount: Double,
  category: String,
  date: Date
) extends KeyedEntity[Long] {

  import WestrelSchema._

  def this() = this(0, INCOME, 0, 0, "", new Date)

  /**
   * @return Пользователь, который внес данную запись
   */
  def user = users.lookup(userId)
}
