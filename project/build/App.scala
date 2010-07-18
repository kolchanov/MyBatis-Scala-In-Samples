import sbt._
import BasicScalaProject._
import java.io.File


class MybatisScalaProject(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {

	val sxr = compilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.6")

	val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
	
	val mybatis = "org.mybatis" % "mybatis" % "3.0.1" 
	val H2 = "com.h2database" % "h2" % "1.1.117" 
	val Cglib = "cglib" % "cglib" % "2.2" 
	val JodaTime = "joda-time" % "joda-time" % "1.6" 
	val Slf4jApi = "org.slf4j" % "slf4j-api" % "1.5.11" 
	val LogbackClassic = "ch.qos.logback" % "logback-classic" % "0.9.19" 
	val LogbackCore = "ch.qos.logback" % "logback-core" % "0.9.19" 
	val Junit = "junit" % "junit" % "4.4" 	
	val Wikitext = "org.eclipse.mylyn.wikitext" % "wikitext" % "0.9.4.I20090220-1600-e3x" 
	val Wikitext_textile = "org.eclipse.mylyn.wikitext" % "wikitext.textile" % "0.9.4.I20090220-1600-e3x"
	val specs = "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5" 


	lazy val graphViz = task {
		val src = "src/main/graphviz/"
		val dst = "src/main/webapp/images/"
		
		def run(cmd : String*) = if (Runtime.getRuntime().exec(cmd.toArray[String]).waitFor() != 0) throw new Exception("Execution failed")
		val dotFiles = new File (src).listFiles().toList.filter (f => f.getName().endsWith("dot")).map(f => f.getName()).map (f => f.substring(0,(f.length()-3).toInt))
		
		dotFiles.foreach (f => run("dot","-T","png","-o", dst+f+"png",src+f+"dot"))
		None
	}

	lazy val copyIndex = task {
		val source = "src/test/resources/index.html"
		val dest = "target"
		FileUtilities.copy ( Set[Path](Path.fromFile(source)), Path.fromFile(dest), true, log)
		None
	}
	
	
}