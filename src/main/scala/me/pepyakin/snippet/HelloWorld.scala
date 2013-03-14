package me.pepyakin.snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import Helpers._

import me.pepyakin.lib.DependencyFactory
import me.pepyakin.model.{WestrelSchema, User}
import me.pepyakin.util.Auth

import org.squeryl.PrimitiveTypeMode._

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date]

  def render = "* *" #> {
    Auth.currentUser.toString
  }
}

