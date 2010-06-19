package org.mybatis_scala.service.generator.h2db

import org.mybatis_scala.ApplicationContext
import org.mybatis_scala.service.generator.IDGenerator
import org.mybatis_scala.service.mybatis.MyBatisDaoService
import java.util.concurrent.atomic.AtomicLong
import org.mybatis_scala.util.LogHelper

/**
* Generator that uses h2 database sequence
*/
trait H2IdGenerator extends IDGenerator  with LogHelper {
	//* interval before call sequence NextVal	
	var generator = new AtomicLong (0)
	
	//* Abstract method to get next val from sequence
	def getSequenceNextVal : Long
	
	//* Abstract method for sequence name
	def sequenceName : String

	var pool : Int = 0
	val myBatisService = ApplicationContext.getMyBatisService

	var sequenceIncrement  = getIncrement() 
	
	def getIncrement() : Int = {
		myBatisService.getSequenceIncremet (sequenceName)
	}
	
	def nextVal : Long = {
		if ( pool == 0) {
			lzDebug ("identificator pool is empty")
			val nv = getSequenceNextVal
			lzDebug ("sequence nextval is: "+nv)
			pool = sequenceIncrement 
			generator = new AtomicLong (nv - 1)
		}
			pool = pool - 1
			generator.addAndGet(1)
	}
}

object GenreH2IdGenerator extends H2IdGenerator {	
	def sequenceName : String = "GENRE_SEQUENCE"	
	def getSequenceNextVal = myBatisService.getGenreSequenceNextVal	
}

object BookH2IdGenerator extends H2IdGenerator {	
	def sequenceName : String = "BOOK_SEQUENCE"	
	def getSequenceNextVal = myBatisService.getBookSequenceNextVal	
}

object AuthorH2IdGenerator extends H2IdGenerator {	
	def sequenceName : String = "AUTHOR_SEQUENCE"	
	def getSequenceNextVal = myBatisService.getAuthorSequenceNextVal	
}