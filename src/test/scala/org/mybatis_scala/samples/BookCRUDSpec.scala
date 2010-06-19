package org.mybatis_scala.samples

import org.mybatis_scala.util.{LogHelper,Schema}
import org.specs.{HtmlSpecification,Specification}
import org.specs.literate.Textile
import org.joda.time.DateTime

import org.mybatis_scala.model._
import org.mybatis_scala.service.mybatis._
import org.mybatis_scala.service.generator._
import h2db._


class BookCRUDSpec extends HtmlSpecification with Textile with LogHelper {
	
	val connection = Schema.conn
	val genreArray = List (new Genre (GenreH2IdGenerator.nextVal,"Science Fiction"), new Genre (GenreH2IdGenerator.nextVal,"Love Story"))
	genreArray.foreach (g=>MyBatisDaoService.saveGenre(g))	
	val author = new AuthorRecord()
	author.authorId = AuthorH2IdGenerator.nextVal
	author.birthDay = new DateTime()
	author.firstName = "firstName"
	author.lastName =  "lastName"
	author.annotation = "annotation"	
	MyBatisDaoService.saveAuthorRecord (author)
	
	doFirst {
		// Initilize database schema
		Schema.init()						
	}	
	
	"BookCRUDSpec spec" is <textile>
	
	h2. CRUD operations for Book object
	
	Book object has associated object Genre so this spec covered CRUD operations and operations with associated object.
	Also, Book table has a nullable column AUTHOR_ID and Book class has Option[Long] property.
	
	DAO object should <ex> implement Create (save) method for Book class without optional Author</ex>{
		 eg {			
			lzDebug ("Book save without Author")			
			val count1 = MyBatisDaoService.getBookCount

			val book = Book (BookH2IdGenerator.nextVal, "Title" , "ISBN", genreArray(0))
			MyBatisDaoService.saveBook (book)
			
			val count2 = MyBatisDaoService.getBookCount
			(count2 - count1) must_== 1			
		}
	}

	DAO object should <ex> implement Create (save) method for Book class with optional Author</ex>{
		 eg {			
			lzDebug ("Book save with Author")			
			val count1 = MyBatisDaoService.getBookCount

			val book = Book (BookH2IdGenerator.nextVal, "Title" , "ISBN", genreArray(0), Author(author))
			MyBatisDaoService.saveBook (book)
			
			val count2 = MyBatisDaoService.getBookCount
			(count2 - count1) must_== 1			
		}
	}

	
	DAO object should <ex> implement Read method for Book class without optional Author</ex>{
		eg {
			lzDebug ("Book read without Author")	
			val book1 = new Book (BookH2IdGenerator.nextVal, "Title" , "ISBN", genreArray(0))
			MyBatisDaoService.saveBook (book1)		
			
			val book2 : Option[Book] = Book.bookOption (MyBatisDaoService.loadBookById (book1.bookId))
			lzDebug ("book1: "+book1)
			lzDebug ("book2: "+book2)
			
			book2 must_!= None
			book2 must_== Some(book1)
		}
	}
	
	DAO object should <ex> implement Read method for Book class with optional Author</ex>{
		eg {
			lzDebug ("Book read with Author")				
			val book1 = Book (BookH2IdGenerator.nextVal, "Title" , "ISBN", genreArray(0), Author(author))
			MyBatisDaoService.saveBook (book1)		
			
			val book2 : Option[Book] = Book.bookOption (MyBatisDaoService.loadBookById (book1.bookId), author)
			lzDebug ("book1: "+book1)
			lzDebug ("book2: "+book2)
			
			book2 must_!= None
			book2 must_== Some(book1)
		}
	}
	
	DAO object should <ex> implement lazy Read method for Book class</ex>{
		eg {
			
			val bookList = List (Book (BookH2IdGenerator.nextVal, "TTL1" , "ISBN1", genreArray(0)), Book (BookH2IdGenerator.nextVal, "TTL2" , "ISBN2", genreArray(1)),
				Book (BookH2IdGenerator.nextVal, "TTL3" , "ISBN3", genreArray(1)), Book (BookH2IdGenerator.nextVal, "TTL4" , "ISBN4", genreArray(0)))
			bookList.foreach (b => MyBatisDaoService.saveBook(b))
			
			val resList : List[BookRecord] = MyBatisDaoService.lazyLoadBookByTitle ("TTL%")
			val book = Book(resList.head)
			
			book.genre must_== genreArray(0)			
		}
	}

	DAO object should <ex> implement Update method for Book class</ex>{
		eg {			

			val book1 = Book (BookH2IdGenerator.nextVal, "TTL1" , "ISBN1", genreArray(0)) 
			MyBatisDaoService.saveBook(book1)
			
			val book2 = Book (book1.bookId, "TTL2" , "ISBN2", genreArray(1)) 
			MyBatisDaoService.updateBook(book2)
			
			// get updated book by id
			val book3 = Book (MyBatisDaoService.loadBookById (book1.bookId).get)
			
			book1 must_!= book3
			book2 must_== book3
			
		}
	}

	DAO object should <ex> implement Delete method for Book class</ex>{
		eg {

			val book = Book (BookH2IdGenerator.nextVal, "TTL" , "ISBN", genreArray(0)) 
			MyBatisDaoService.saveBook (book)


			val count = MyBatisDaoService.getBookCount
			MyBatisDaoService.deleteBook (book)
			
			MyBatisDaoService.getBookCount must_== count -1
			
		}
	}
	
	</textile>
}


