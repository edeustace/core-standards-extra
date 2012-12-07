package com.ee

import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty._
import unfiltered.scalate._
import util.Properties

object App extends unfiltered.filter.Plan {

  def intent = {
    case form @ POST(Path("/get-json")) => {
      form match {
        case Params(params) => Ok ~> ResponseString(params("url").toString)
        case _ => Ok ~> ResponseString("params?")
      }
    }
    case req => Ok ~> Scalate(req, "index.jade")
  }
}

object Web {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    val http = unfiltered.jetty.Http(port)
    http.context("/assets") { _.resources(new java.net.URL(getClass().getResource("/"), ".")) }
    .filter(App).run
  }
}
