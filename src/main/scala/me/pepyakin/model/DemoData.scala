package me.pepyakin.model

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

  def createDemoData = {
    users.foreach { user =>
      WestrelSchema.users.insert(user)
    }
  }
}
