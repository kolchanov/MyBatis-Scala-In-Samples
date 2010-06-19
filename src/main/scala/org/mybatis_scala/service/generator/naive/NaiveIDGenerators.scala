package org.mybatis_scala.service.generator.naive

import org.mybatis_scala.service.generator.IDGenerator
import java.util.concurrent.atomic.AtomicLong

/**
* Generator for testing without database
*/
trait NaiveIdGenerator extends IDGenerator {
	
	val generator = new AtomicLong (0)
	
	def nextVal : Long = {
		generator.addAndGet(1)
	}
}
/**
* Id generator for Genre Entity
*/
object GenreNaiveIdGenerator extends NaiveIdGenerator

/**
* Id generator for Book Entity
*/
object BookNaiveIdGenerator extends NaiveIdGenerator 

/**
* Id Generator for Author Entity
*/
object AuthorNaiveIdGenerator extends NaiveIdGenerator