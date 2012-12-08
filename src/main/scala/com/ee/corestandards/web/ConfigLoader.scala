package com.ee.corestandards.web

import com.typesafe.config._

object ConfigLoader {

  def get(key:String):Option[String] = {
    try{
      Some(ConfigFactory.systemEnvironment().getString(key))
    } catch {
      case e : Throwable => None
    }
  }
}