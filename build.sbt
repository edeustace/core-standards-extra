import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

organization := "com.ee"

name := "core-standards-extra"

scalaVersion := "2.9.2"

version := "0.1.0-SNAPSHOT"

resolvers += "ed release repo" at "http://edeustace.com/repository/releases"

resolvers += "ed release snapshots" at "http://edeustace.com/repository/snapshots"

resolvers += "codehale repo" at "http://repo.codahale.com"

resolvers += "typesafe repo" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "scalatools" at "https://oss.sonatype.org/content/groups/scala-tools/"


libraryDependencies ++= Seq(
   "net.databinder" %% "unfiltered-filter" % "0.6.4",
   "net.databinder" %% "unfiltered-jetty" % "0.6.4",
   "net.databinder" %% "unfiltered-scalate" % "0.6.3",
   "com.ee" %% "core-standards-parser" % "0.1",
   "com.codahale" % "jerkson_2.9.1" % "0.5.0",
   "com.typesafe" % "config" % "1.0.0",
   "com.mongodb.casbah" % "casbah_2.9.1" % "2.1.5-1"
)


