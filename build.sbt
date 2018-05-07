name := "multibot"
version := "1.0"
fork := true
connectInput := true
mainClass in Compile := Some("org.multibot.Multibottest")
updateOptions := updateOptions.value.withCachedResolution(true).withLatestSnapshots(false)
publishArtifact in(Compile, packageDoc) := false
enablePlugins(JavaAppPackaging)
scalaVersion := "2.12.6"

libraryDependencies ++= {
  val scalazVersion = "7.2.22"
  val fs2Version = "0.9.6"
  val shapelessVersion = "2.3.3"
  val monocleVersion = "1.5.0"
  val spireVersion = "0.14.1"
  Seq(
    "org.scalaz" %% "scalaz-iteratee" % scalazVersion,
    "org.scalaz" %% "scalaz-effect" % scalazVersion,
    "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
    "org.scalaz" %% "scalaz-ioeffect" % "2.1.0",
    "com.chuusai" %% "shapeless" % shapelessVersion,
    "com.github.julien-truffaut" %% "monocle-generic" % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-law" % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
    "org.typelevel" %% "spire" % spireVersion
//    "co.fs2" %% "fs2-io" % fs2Version
//    "org.pelotom" %% "effectful" % "1.1-SNAPSHOT"
  )
}

libraryDependencies ++= Seq(
  "org.pircbotx" % "pircbotx" % "2.1",
  "org.slf4j" % "slf4j-simple" % "1.7.10",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.json4s" %% "json4s-native" % "3.5.0",
  "com.google.guava" % "guava" % "18.0",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)


scalacOptions ++= Seq("-feature:false", "-language:_", "-deprecation", "-Xexperimental")

autoCompilerPlugins := true

addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.17")
libraryDependencies += "org.psywerx.hairyfotr" %% "linter" % "0.1.17"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
libraryDependencies += "org.spire-math" %% "kind-projector" % "0.9.6"

//scalacOptions += "-P:linter:disable:OLOLOUseHypot+CloseSourceFile+OptionOfOption"

//addCompilerPlugin("org.brianmckenna" %% "wartremover" % "0.14")

//libraryDependencies += "org.brianmckenna" %% "wartremover" % "0.14"
//
//scalacOptions += "-P:wartremover:only-warn-traverser:org.brianmckenna.wartremover.warts.Unsafe"

enablePlugins(BuildInfoPlugin)
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, scalacOptions in(Compile, compile), libraryDependencies in(Compile, compile))
buildInfoPackage := "org.multibot"


