import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

organization := "com.ee"

name := "core-standards-extra"

scalaVersion := "2.9.2"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
   "net.databinder" %% "unfiltered-filter" % "0.6.4",
   "net.databinder" %% "unfiltered-jetty" % "0.6.4",
   "net.databinder" %% "unfiltered-scalate" % "0.6.3"
)

resolvers ++= Nil
