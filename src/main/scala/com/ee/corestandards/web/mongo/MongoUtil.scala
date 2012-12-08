package com.ee.corestandards.web.mongo

import com.mongodb.casbah.{MongoConnection, MongoURI, MongoCollection}

object MongoUtil{


  /**
   * Note: there is something up with the MongoURI string parsing
   * see: https://jira.mongodb.org/browse/JAVA-504
   * So will extract the username password out manually first.
   */
  def getCollection(uriString: String, collection: String): MongoCollection = {
    val (maybeUserPass, url) = uriParts(uriString)
    print(maybeUserPass)
    print(url)
    val uri = MongoURI("mongodb://" + url)
    val db = uri.connectDB
    maybeUserPass match {
      case Some((u,p)) => db.authenticate(u,p)
    }
    db(collection)
  }

  private def uriParts(uri:String):(Option[(String,String)], String) = {

    if(uri.contains("@")){
      val WithUsernamePasswordRegex = """mongodb://(.*?):(.*?)@(.*)""".r
      val WithUsernamePasswordRegex(username,password,url) = uri
      (Some((username,password)), url) 
    } else {
      val DbRegex = """mongodb://(.*)""".r
      val DbRegex(url) = uri
      (None,url)
    }
  }
}