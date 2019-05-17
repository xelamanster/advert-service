import sbt._

object Dependencies {

  lazy val java8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"
  lazy val scanamo = "org.scanamo" %% "scanamo" % "0.10"
  lazy val cats = "org.typelevel" %% "cats-core" % "1.6.0"

  lazy val circeVersion = "0.11.1"
  lazy val circeCore = "io.circe" %% "circe-core" % circeVersion
  lazy val circeParser = "io.circe" %% "circe-parser" % circeVersion
  lazy val circeJava8 = "io.circe" %% "circe-java8" % circeVersion
  
  lazy val enumeratumCirceVersion = "1.5.13"
  lazy val enumeratum = "com.beachape" %% "enumeratum" % enumeratumCirceVersion
  lazy val enumeratumCirce = "com.beachape" %% "enumeratum-circe" % enumeratumCirceVersion

  lazy val scalatestplus = "org.scalatestplus" %% "play" % "1.4.0"
  lazy val mockito = "org.mockito" % "mockito-core" % "2.25.1"
  
  lazy val mainDependencies = Seq(
    java8Compat,
    scanamo,
    cats,
    circeCore,
    circeParser,
    circeJava8,
    enumeratum,
    enumeratumCirce
  )
  
  lazy val testDependencies = Seq(
    scalatestplus,
    mockito
  )
}
