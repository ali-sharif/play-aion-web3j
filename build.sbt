name := """play-aion-web3j"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.0"

libraryDependencies += guice

// un-managed dependencies automatically get picked from from the "lib" folder in sbt