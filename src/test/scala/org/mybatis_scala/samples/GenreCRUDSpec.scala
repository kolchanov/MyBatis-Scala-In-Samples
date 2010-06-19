package org.mybatis_scala.samples

import org.mybatis_scala.util.{LogHelper,Schema}
import org.specs.{HtmlSpecification,Specification}
import org.specs.literate.Textile

import org.mybatis_scala.model._
import org.mybatis_scala.service.mybatis._
import org.mybatis_scala.service.generator._
import h2db._


class GenreCRUDSpec extends HtmlSpecification with Textile with LogHelper {
	
	val connection = Schema.conn
	
	doFirst {
		// Initilize database schema
		Schema.init()		
	}	
	
	"GenreCRUDSpec spec" is <t>
	
	h2. CRUD operations for simple object
	
	The Genre entity is very simple and has not assoiations or collections, so very basic CRUD operations should be implemented.
	Also list operations are covered.
	
	DAO object should <ex> implement Create (save) method for Genre class</ex>{
		 eg {
			val count1 = MyBatisDaoService.getGenreCount
			count1 must_== 0 

			val genre1 = new Genre (GenreH2IdGenerator.nextVal,"Name1")
			MyBatisDaoService.saveGenre(genre1).isExpectation 					
			//isExpectation is a Spec method to be sure that Excepion was  not thrown
			
			val count2 = MyBatisDaoService.getGenreCount
			count2 must_== 1			
		}
	}

	DAO object should <ex> allow save Genre list in a single tansaction</ex>{
		 eg {
			val count1 = MyBatisDaoService.getGenreCount
			val lst : List[Genre] = List (new Genre (GenreH2IdGenerator.nextVal,"Name1"), new Genre (GenreH2IdGenerator.nextVal,"Name2"))			
			
			MyBatisDaoService.exec { session =>
				lst.map (g => session.insert ("insertGenre",g))
			}
			
			val count2 = MyBatisDaoService.getGenreCount
			count2 must_== lst.size + count1 
			
			// next transaction must be rollbacked
			val lst2 = new Genre (GenreH2IdGenerator.nextVal,"Name3") :: lst
			MyBatisDaoService.exec { session =>
				lst.map (g => session.insert ("insertGenre",g))
			}	must throwA[Exception]
			
		 	MyBatisDaoService.getGenreCount must_== count2			
		}
	}


	DAO object should <ex> implement Read method for Genre class</ex>{
		eg {
			val genre1 = new Genre (GenreH2IdGenerator.nextVal,"Name1")
			MyBatisDaoService.saveGenre(genre1).isExpectation			
			
			val genre2 = MyBatisDaoService.loadGenreById (genre1.genreId)
			genre2.get must_== genre1	
			
			val genre3 = MyBatisDaoService.loadGenreById (-1)
			genre3 must_== None			
		}				
	}

	DAO object should <ex> implement Read method for GenreRecord class</ex>{
		eg {
			val genre1 = new Genre (GenreH2IdGenerator.nextVal,"Name1")
			MyBatisDaoService.saveGenre(genre1).isExpectation			

			val gr1 = MyBatisDaoService.loadGenreRecordById (genre1.genreId)
			
			val genre2 = new Genre (gr1.getOrElse(new GenreRecord))
			genre2 must_== genre1	
			
			val gr2 = MyBatisDaoService.loadGenreRecordById (-1)
			gr2 must_== None			
		}				
	}
	
	DAO object should <ex> implement method for get Genre list by name</ex>{
		eg {
			val lst : List[Genre] = List (new Genre (GenreH2IdGenerator.nextVal,"Q1"), new Genre (GenreH2IdGenerator.nextVal,"Q2"))
			lst.foreach (g => MyBatisDaoService.saveGenre(g))
		
			val glist : List[Genre] = MyBatisDaoService.loadGenreByName ("Q%")
			glist.size must_== 2			
		}				
	}	

	DAO object should <ex> implement Update method for the Genre Entity</ex>{
		eg {
			val genre1 = new Genre (GenreH2IdGenerator.nextVal,"Name1")
			MyBatisDaoService.saveGenre(genre1).isExpectation
			
			val genre2 = new Genre (genre1.genreId, "Name2")
			MyBatisDaoService.updateGenre(genre2).isExpectation
			
			val genre3 = MyBatisDaoService.loadGenreById (genre1.genreId)
			
			genre2 must_== genre3.get					
		}
	}

	DAO object should <ex> implement Delete method for the Genre Entity</ex>{
		eg {
			val genre1 = new Genre (GenreH2IdGenerator.nextVal,"Name1")
			MyBatisDaoService.saveGenre(genre1).isExpectation

			val genre2 = MyBatisDaoService.loadGenreById (genre1.genreId)
			genre2.get must_== genre1

			MyBatisDaoService.deleteGenre(genre1).isExpectation
			val genre3 = MyBatisDaoService.loadGenreById (genre1.genreId)

			genre3 must_== None			
		}
	}
	
	</t>
}
