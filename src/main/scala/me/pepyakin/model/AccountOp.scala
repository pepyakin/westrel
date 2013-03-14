package me.pepyakin.model

/**
 * 
 * @author Sergey
 */
object AccountOp extends Enumeration {
  type AccoutOp = Value

  val INCOME = Value(1, "Доход")
  val OUTCOME = Value(2, "Расход")
}
