name := """sis-evasao"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies += evolutions


libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "commons-io" % "commons-io" % "2.4",
  "com.google.code.gson" % "gson" % "2.7",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.apache.commons" % "commons-email" % "1.3.1"
)

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  // Use .class files instead of generated .scala files for views and routes