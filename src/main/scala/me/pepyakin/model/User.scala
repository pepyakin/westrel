package me.pepyakin.model

import org.squeryl.annotations.Column
import net.liftweb.record.field._
import net.liftweb.record._
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.http.{S, SessionVar, RequestVar, CleanRequestVarOnSessionTransition, RedirectResponse, RedirectWithState, RedirectState}
import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.util.Helpers._
import net.liftweb.sitemap.Loc._
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