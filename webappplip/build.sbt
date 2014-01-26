name := "plip"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.21",
  "commons-io" % "commons-io" % "2.3"
) 

play.Project.playJavaSettings
