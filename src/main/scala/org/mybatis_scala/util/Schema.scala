package org.mybatis_scala.util

import java.util.Properties
import org.mybatis_scala.ApplicationContext
import javax.sql._
import java.sql._

/**
* Initial database shcema
*/
object Schema extends LogHelper {
	val props = ApplicationContext.getProperties
	
	lzDebug ("Schema object initialization")
	lzDebug ("database driver: "+props.getProperty("driver") )
	lzDebug ("database url: "+props.getProperty("url") )	
	lzDebug ("database username: "+props.getProperty("username") )	
	lzDebug ("database password: "+props.getProperty("password") )		

	Class.forName(props.getProperty("driver"))
	def conn = DriverManager.getConnection (props.getProperty("url"), props.getProperty("username"), props.getProperty("password"))		
	
	lzDebug ("connection "+conn)
	
	init()
	
	lzDebug ("Schema object initialization completed")
	
	def init () {
		try {
		
			conn.createStatement().executeUpdate("CREATE SEQUENCE IF NOT EXISTS GENRE_SEQUENCE INCREMENT BY 10")			
			conn.createStatement().executeUpdate("CREATE SEQUENCE IF NOT EXISTS BOOK_SEQUENCE INCREMENT BY 10")						
			conn.createStatement().executeUpdate("CREATE SEQUENCE IF NOT EXISTS AUTHOR_SEQUENCE INCREMENT BY 10")						

			conn.createStatement().executeUpdate("""
			CREATE TABLE GENRE (
				GENRE_ID	number primary key,
				NAME	varchar(128)
			)
			""")						

			conn.createStatement().executeUpdate("""
			CREATE TABLE BOOK (
				BOOK_ID 	number primary key,
				TITLE	varchar (255),
				ISBN varchar (32),				
				GENRE_ID number,
				AUTHOR_ID number
			)
			""")						

			conn.createStatement().executeUpdate("""
			CREATE TABLE AUTHOR (
				AUTHOR_ID number primary key,
				BIRTH_DAY	DATETIME,
				FIRST_NAME varchar (255),				
				LAST_NAME varchar (255),
				ANNOTATION varchar(1024)
			)
			""")						
				
			conn.createStatement().executeUpdate("ALTER TABLE BOOK ADD CONSTRAINT BOOK_GENRE_REF FOREIGN KEY (GENRE_ID) REFERENCES GENRE (GENRE_ID)")
			conn.createStatement().executeUpdate("ALTER TABLE BOOK ADD CONSTRAINT BOOK_AUTHOR_REF FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR (AUTHOR_ID)")			
			
		} finally {
			conn.close()
		}
	}
}
