import Dependencies._

version := "0.1-SNAPSHOT"
scalaVersion := "2.11.12"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "advert-service",
    organization := "com.github.xelamanster",
    scalacOptions += "-target:jvm-1.8",
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= mainDependencies,
    libraryDependencies ++= testDependencies.map(_ % Test)
  )
