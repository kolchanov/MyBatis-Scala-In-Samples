package org.mybatis_scala.samples

import org.mybatis_scala.util.{LogHelper,Schema}
import org.specs.{HtmlSpecification,Specification}
import org.specs.literate.Textile

import org.mybatis_scala.service.generator._
import naive._
import h2db._


class IDGeneratorSpec extends HtmlSpecification with Textile with LogHelper {
	
	val connection = Schema.conn
	
	//* number of id iteration in this test
	val idTextCount = 20
	
	doFirst {
		// Initilize database schema
		Schema.init()		
	}
	
	"IDGeneratorSpec spec" is <t>
	
	h2. Problem description
	
	I suppose that generating domain model object identificator during database insert is a realy bad idea.
	Let's check test and sequence based ID generator implementations for test and production environments.
	BTW, there is the first time when MyBatis is executed - MyBatis helps to get info from database system table.
	
	Application should <ex>allow to generate incremeted id for Genre entity in the test envirenment</ex> 
	{ eg{
			val generator : IDGenerator = GenreNaiveIdGenerator
			checkGenerator (generator)		
		}
	}
	
	Application should <ex>allow to generate incremeted id for Book entity in the test envirenment</ex> 
	{ eg {
			val generator : IDGenerator = BookNaiveIdGenerator
			checkGenerator (generator)		
		}
	}	

	Application should <ex>allow to generate incremeted id for Author entity in the test envirenment</ex> 
	{ eg {
			val generator : IDGenerator = AuthorNaiveIdGenerator
			checkGenerator (generator)		
		}
	}	

	Application should <ex>allow to generate incremeted id for Genre entity in the production envirenment</ex> 
	{ eg {
			
			val generator : IDGenerator = GenreH2IdGenerator
			checkGenerator (generator)
		}
	}

	Application should <ex>allow to generate incremeted id for Book entity in the production envirenment</ex> 
	{ eg {
			
			val generator : IDGenerator = BookH2IdGenerator
			checkGenerator (generator)
		}
	}

	Application should <ex>allow to generate incremeted id for Author entity in the production envirenment</ex> 
	{ eg {
			
			val generator : IDGenerator = AuthorH2IdGenerator
			checkGenerator (generator)
		}
	}	
	</t>
	
	def checkGenerator (generator : IDGenerator) {
		val id1 = generator.nextVal
		val id2 = generator.nextVal

		lzDebug ("id1: "+id1+" id2: "+id2)
		id1 must_!= id2	

		var prev = id2
	
		for (i<- 1 to idTextCount) {
			var nxt = generator.nextVal

			lzDebug ("next id for "+generator+": "+nxt)

			nxt must_!= prev
			prev = nxt
		}		
	}
}



