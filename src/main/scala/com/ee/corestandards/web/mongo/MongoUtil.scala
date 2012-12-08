package com.ee.corestandards.web.mongo

import com.mongodb.casbah.{MongoConnection, MongoURI, MongoCollection}

object MongoUtil{


  def getCollection(uriString: String, collection: String): MongoCollection = {
    val uri = MongoURI(uriString)
    val mongo = MongoConnection(uri)
    val db = mongo(uri.database)

    println("username: " + uri.username)
    if(uri.username != null){
      println("authenticating")
      db.authenticate(uri.username, uri.password.mkString(""))
    } 
    println("db: " + db)
    db(collection)
  }
}