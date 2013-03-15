package me.pepyakin.model

import java.util.Date

/**
 * 
 * @author Sergey
 */
object DemoData {

  private val users = Seq(
    User(0, "mama", "mama"),
    User(0, "papa", "papa"),
    User(0, "son", "son")
  )

  private val entries = {
    import AccountOp._

    Seq[AccountEntry](
// Расскоментируйте для получения тестовых данных.

      AccountEntry(0, OUTCOME, 1, 5000.0, "одежда", new Date(2012 - 1900, 12, 6)),
      AccountEntry(0, INCOME, 1, 30000.0, "зарплата", new Date(2012 - 1900, 12, 5)),
      AccountEntry(0, OUTCOME, 1, 3000.0, "одежда", new Date(2012 - 1900, 12, 13))
    )
  }

  def createDemoData = {
    users.foreach { user =>
      WestrelSchema.users.insert(user)
    }

    entries.foreach { entry =>
      WestrelSchema.accountEntries.insert(entry)
    }
  }
}
