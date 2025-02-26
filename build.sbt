
lazy val commonSettings = Seq(
  organization := "ch.epfl.gsn",
  version := "2.0.4",
  scalaVersion := "2.12.4",
  javacOptions in (Compile, compile) ++= Seq("-source", "11", "-target", "11"),
  resolvers ++= Seq(
    DefaultMavenRepository,
    "Typesafe Repository" at "https://repo.maven.apache.org/maven2/",
    "osgeo" at "https://repo.osgeo.org/repository/release/",
    "play-authenticate (release)" at "https://oss.sonatype.org/content/repositories/releases/",
    "play-authenticate (snapshot)" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Local ivy Repository" at ""+Path.userHome.asFile.toURI.toURL+"/.ivy2/local",
    "Local cache" at ""+file(".").toURI.toURL+"lib/cache"
  ),
    publishTo := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"),
   //   publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
/*
publishTo &lt;&lt;= version { v: String =&gt;
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}*/

  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials-sonatype"),
  publishMavenStyle := true,
  publishArtifact in (Compile) := false,
  publishArtifact in (Test) := false,
  publishArtifact in (Compile, packageBin) := true,
  publishArtifact in (Compile, packageSrc) := true,
  publishArtifact in (Compile, packageDoc) := false,
  pomIncludeRepository := { x => false },
  pomExtra := (
  <url>http://gsn.epfl.ch</url>
  <licenses>
    <license>
      <name>GPL-3.0+</name>
      <url>https://opensource.org/licenses/GPL-3.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:LSIR/gsn.git</url>
    <connection>scm:git:git@github.com:LSIR/gsn.git</connection>
  </scm>
  <developers>
    <developer>
      <id>EPFL-LSIR</id>
      <name>The GSN Team</name>
      <url>http://gsn.epfl.ch</url>
    </developer>
  </developers>
),
  crossPaths := false,
  useGpg := true,
  parallelExecution in Test := false,
  EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
)

usePgpKeyHex("DC900B5F")

lazy val root = (project in file(".")).
  aggregate(core, tools,services)


lazy val core = (project in file("gsn-core")).
  dependsOn(tools).
  settings(commonSettings: _*).
  enablePlugins(JavaServerAppPackaging, DebianPlugin)

lazy val services = (project in file("gsn-services")).
  dependsOn(tools, core).
  settings(
    coverageEnabled := true,
    coverageExcludedPackages := "<empty>;views.*;router.*;models.gsn.data"
  ).
  settings(commonSettings: _*).
  enablePlugins(PlayJava, PlayEbean, DebianPlugin)

lazy val tools = (project in file("gsn-tools")).
  settings(commonSettings: _*)

lazy val webui = (project in file("gsn-webui")).
  enablePlugins(JavaServerAppPackaging, DebianPlugin)


lazy val startAll = taskKey[Unit]("Start all the GSN modules")


//startAll := {
  //(webui/startDjango in webui).value
//  (re-start in core).value
//  (run in services).value
//}

