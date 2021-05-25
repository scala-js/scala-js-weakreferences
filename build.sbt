inThisBuild(Def.settings(
  crossScalaVersions := Seq("2.12.13", "2.11.12", "2.13.5"),
  scalaVersion := crossScalaVersions.value.head,

  version := "1.0.0-SNAPSHOT",
  organization := "org.scala-js",
  scalacOptions ++= Seq(
    "-encoding", "utf-8",
    "-deprecation",
    "-feature",
    "-Xfatal-warnings",
  ),

  // Licensing
  homepage := Some(url("https://github.com/scala-js/scala-js-weakreferences")),
  startYear := Some(2013),
  licenses += (("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))),
  scmInfo := Some(ScmInfo(
      url("https://github.com/scala-js/scala-js-weakreferences"),
      "scm:git:git@github.com:scala-js/scala-js-weakreferences.git",
      Some("scm:git:git@github.com:scala-js/scala-js-weakreferences.git"))),

  // Publishing
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.endsWith("-SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <developers>
      <developer>
        <id>sjrd</id>
        <name>Sébastien Doeraene</name>
        <url>https://github.com/sjrd/</url>
      </developer>
      <developer>
        <id>gzm0</id>
        <name>Tobias Schlatter</name>
        <url>https://github.com/gzm0/</url>
      </developer>
    </developers>
  ),
  pomIncludeRepository := { _ => false },
))

val commonSettings = Def.settings(
  // sbt-header configuration
  headerLicense := Some(HeaderLicense.Custom(
    s"""scalajs-weakreferences (${homepage.value.get})
       |
       |Copyright EPFL.
       |
       |Licensed under Apache License 2.0
       |(https://www.apache.org/licenses/LICENSE-2.0).
       |
       |See the NOTICE file distributed with this work for
       |additional information regarding copyright ownership.
       |""".stripMargin
  )),
)

lazy val root: Project = project.in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "scalajs-weakreferences",
    Compile / packageBin / mappings ~= {
      _.filter(!_._2.endsWith(".class"))
    },
    exportJars := true,
  )

lazy val testSuite = crossProject(JSPlatform, JVMPlatform)
  .in(file("test-suite"))
  .jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))
  .settings(commonSettings: _*)
  .settings(
    testOptions += Tests.Argument(TestFramework("com.novocode.junit.JUnitFramework"), "-v", "-a"),
  )
  .jsConfigure(_.dependsOn(root))
  .jvmSettings(
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
  )
