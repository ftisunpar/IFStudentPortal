name := "IFStudentPortal"

version := "1.0"

lazy val IFStudentPortal = (project in file("."))
	.enablePlugins(PlayJava)

EclipseKeys.preTasks := Seq(compile in Compile)
EclipseKeys.skipParents in ThisBuild := false

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	cache,
	javaWs,
	"org.jsoup" % "jsoup" % "1.8.2",
	"id.ac.unpar" % "SIAModels" % "3.1.0"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

fork in run := false

// For deployment in Ubuntu/SystemV
enablePlugins(JavaServerAppPackaging)
enablePlugins(UniversalPlugin)
import com.typesafe.sbt.packager.archetypes.ServerLoader.{SystemV, Upstart}
serverLoading in Debian := SystemV
