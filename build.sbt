name := "MarketData"
version := "0.1.3.1"
scalaVersion := "2.11.11"

libraryDependencies += "com.github.seratch" %% "awscala" % "0.6.+"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.3"

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}