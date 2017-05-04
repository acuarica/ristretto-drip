scalaVersion := "2.12.0"

name := "ristretto"

organization := "ch.usi.ristretto"

version := "0.1"

sbtRatsSettings

ratsScalaRepetitionType := Some (ListType)

ratsUseScalaOptions := true

ratsUseScalaPositions := true

ratsUseKiama := 2

ratsDefineASTClasses := true

ratsDefinePrettyPrinter := true

ratsUseDefaultSpacing := true

ratsUseDefaultLayout := true

ratsUseDefaultComments := true

ratsUseDefaultWords := true

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.bitbucket.inkytonik.kiama" %% "kiama" % "2.0.0"
