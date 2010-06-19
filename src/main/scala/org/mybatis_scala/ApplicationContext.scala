package org.mybatis_scala

import org.mybatis_scala.util._
import org.mybatis_scala.service.mybatis.MyBatisDaoService
import java.util.Properties
import java.io.{BufferedReader,InputStreamReader,FileInputStream}

/**
* Basic configuration object for application <p/>
* All application dependencies should be resolved through this object.
*/
object ApplicationContext {

	//* Path to properties file
	def getPropertiesPath = ".properties"
	
	//* MyBatisService implementation
	def getMyBatisService : MyBatisDaoService = MyBatisDaoService
	
	//* initial properties
	val props = new Properties()
	props.load (new FileInputStream(ApplicationContext.getPropertiesPath))

	//* initial properties	
	def getProperties = props	
	
}
