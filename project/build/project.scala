import sbt._
import reaktor.scct.ScctProject

class Project(info: ProjectInfo) extends DefaultProject(info) with ScctProject {
  
  /** Paths */
  override def outputDirectoryName = "target"
  override def managedDependencyPath = "project" / "lib_managed"

  /** Dependencies */
  val mavenLocal = "Local Maven Repository" at "file://D:/mvn-repository"
  val snapshotsRepo = "snapshots-repo" at "http://scala-tools.org/repo-snapshots"

  val scalacheck    = "org.scalacheck" % "scalacheck_2.9.2" % "1.9" % "optional"
  val testinterface = "org.scala-tools.testing" % "test-interface" % "0.5" % "optional"
  val scalazcore    = "org.specs2" % "specs2-scalaz-core_2.9.1" % "6.0.1"
  val hamcrest      = "org.hamcrest" % "hamcrest-all" % "1.1" % "optional"
  val mockito 	    = "org.mockito" % "mockito-all" % "1.9.0" % "optional"
  val junit         = "junit" % "junit" % "4.7" % "optional"
  val pegdown       = "org.pegdown" % "pegdown" % "1.0.2" % "optional"
  val specs2        = "org.specs2" % "classycle" % "1.4.1" % "optional"
  
  /** Compiling */
  override def compileOptions = Unchecked :: super.compileOptions.toList
  override def javaCompileOptions = JavaCompileOption("-Xmx256m -Xms64m -Xss4m -XX:ReservedCodeCacheSize=96m") :: Nil

  /** Testing */
  override def testFrameworks = super.testFrameworks ++ Seq(new TestFramework("org.specs2.runner.SpecsFramework"))

  override def testJavaCompileOptions = JavaCompileOption("-Xmx256m -Xms64m -Xss4m -XX:ReservedCodeCacheSize=96m") :: Nil
  override def includeTest(s: String) = Seq("Spec", "Suite", "Unit", "all").exists(s.endsWith(_)) && !s.endsWith("FeaturesSpec") || 
	                                            s.contains("UserGuide") || 
	                                            s.matches("org.specs2.guide.*") 

  /** Sources */
  val sourceArtifact = Artifact.sources(artifactID)
  override def packageSrcJar = defaultJarPath("-sources.jar")
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)
  
  /** Publishing */
  override def managedStyle = ManagedStyle.Maven
  override def defaultPublishRepository = {
    val nexusDirect = "http://nexus-direct.scala-tools.org/content/repositories/"
    if (version.toString.endsWith("SNAPSHOT")) 
	  Some("scala-tools snapshots" at nexusDirect + "snapshots/")
	else
	  Some("scala-tools releases" at nexusDirect + "releases/")
  }
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)

}