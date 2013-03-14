package me.pepyakin.snippet

import net.liftweb.util._
import Helpers._

import me.pepyakin.util.Auth


class HelloWorld {

  def render = "* *" #> {
    Auth.currentUser.toString
  }
}

