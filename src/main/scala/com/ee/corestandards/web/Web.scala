package com.ee.corestandards.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty._
import unfiltered.scalate._
import util.Properties
import com.ee.corestandards.parser.CoreStandardsParser
import com.ee.corestandards.parser.models.Standard
import scala.xml.NodeSeq
import scala.xml.Node
import java.util.ArrayList
import com.typesafe.config._
import com.ee.corestandards.web.mongo._
import com.mongodb.{ BasicDBList, BasicDBObject, DBObject }
import com.mongodb.casbah._
import com.mongodb.casbah.commons.MongoDBObject


object App extends unfiltered.filter.Plan {

  val systemConfig : Config = ConfigFactory.systemEnvironment()
  
  val mongoUri : String = ConfigLoader.get("CORE_STANDARDS_EXTRA_DB")
    .getOrElse("mongodb://127.0.0.1:27017/core-standards-extra-dev")

  val initId : String = ConfigLoader.get("CORE_STANDARDS_INIT_ID")
    .getOrElse("0")

  lazy val collection : MongoCollection = MongoUtil.getCollection(mongoUri, "standards")


  //val MATH_ROOT = "http://www.corestandards.org/Math.xml"
  //val ELA_ROOT = "http://www.corestandards.org/ELA-Literacy.xml"
  val MATH_ROOT ="http://www.corestandards.org/Math/Content/3/NBT.xml"
  val ELA_ROOT = "http://www.corestandards.org/ELA-Literacy/RL/2.xml"

  def intent = {
    case GET(Path(Seg("init" :: id :: Nil))) => {
      println("id: " + id)
      if(initId.equals(id)){
        reseed
        Ok ~> ResponseString("done.")
      }
      else {
        Ok ~> ResponseString("?")
      }
    }
    case form @ POST(Path("/get-json")) => {
      form match {
        case Params(params) => {
          val url = params("url").head
          val standards = getStandardsForUrl(url) 
          Ok ~> ResponseString(com.codahale.jerkson.Json.generate(standards))
        }
        case _ => Ok ~> ResponseString("params?")
      }
    }
    case req => Ok ~> Scalate(req, "index.jade")
  }

  private def getStandardsForUrl(url:String) : Seq[Standard] = {
    val xml = scala.xml.XML.load(url)
    val nodes : NodeSeq = (xml \\ "LearningStandardItem" ) 
    val ids = nodes.map( (n:Node) => (n \ "@RefID").text)
    val dbos = ids.map(id => collection.findOne( MongoDBObject("refId" -> id))).flatten
    dbos.map(toStandard)
  }

  private def toStandard(dbo:DBObject) : Standard = {
    new Standard(
      dotNotation = dbo.get("dotNotation").asInstanceOf[String],
      subject = dbo.get("subject").asInstanceOf[String],
      category = dbo.get("category").asInstanceOf[String],
      subCategory = dbo.get("subCategory").asInstanceOf[String],
      standard = dbo.get("standard").asInstanceOf[String],
      uri = dbo.get("uri").asInstanceOf[String],
      refId = dbo.get("refId").asInstanceOf[String]
      )
  }

  private def reseed = {

    def toDBO(s:Standard) : BasicDBObject = {
      val dbo = new BasicDBObject()
      dbo.put("dotNotation", s.dotNotation)
      dbo.put("subject", s.subject)
      dbo.put("category", s.category)
      dbo.put("subCategory", s.subCategory)
      dbo.put("standard", s.standard)
      dbo.put("uri", s.uri)
      dbo.put("refId", s.refId)
      dbo
    }

    val json = CoreStandardsParser.run(MATH_ROOT,ELA_ROOT)
    val standards :List[Standard] = com.codahale.jerkson.Json.parse[List[Standard]](json)
    collection.drop()
    collection.insert(standards.map(toDBO))
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
