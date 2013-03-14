package me.pepyakin.util

import net.liftweb.common.{Full, Empty, Box}
import net.liftweb.http.{S, SessionVar}
import me.pepyakin.model.{User, WestrelSchema}

import org.squeryl.PrimitiveTypeMode._

/**
 * 
 * @author Sergey
 */
object Auth {
  import WestrelSchema._

  private object sessionUserId extends SessionVar[Box[Long]](Empty)
  private object sessionUser extends SessionVar[Box[User]](currentUserId.flatMap(findUserById(_)))

  /**
   * @return Коробка с ID текущего пользователя (произведевший текущий запрос)
   */
  def currentUserId = sessionUserId.is

  /**
   * @return Коробка с экземпляром текущего пользователя.
   */
  def currentUser = sessionUser.is

  /**
   * @param userId
   * @return Пользователя по заданному ID
   */
  def findUserById(userId: Long) = {
    users.lookup(userId)
  }

  def findByUsername(username: String): Option[User] = {
    users.where(u => u.username === username).headOption
  }

  def isLoggedIn = currentUserId.isDefined

  /**
   * @return Пользователь авторизирован?
   */
  def login(username: String, password: String): Boolean = {
    import WestrelSchema._

    findByUsername(username).map { user =>
      if (user.password != password) false
      else {
        loginUser(user)
        true
      }
    } getOrElse false
  }

  def loginUser(user: User) {
    removeSessionVars

    sessionUserId(Full(user.id))
  }

  def logout {
    removeSessionVars

    S.session.foreach(_.destroySession())
  }

  def removeSessionVars {
    sessionUser.remove()
    sessionUserId.remove()
  }
}
