package me.pepyakin.model

import org.squeryl.KeyedEntity

/**
 * 
 * @author Sergey
 */
case class User(
  id: Long,
  username: String,
  password: String) extends KeyedEntity[Long] {

  def this() = this(0, "", "")
}