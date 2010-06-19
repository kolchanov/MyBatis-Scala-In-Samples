package org.mybatis_scala.samples

import org.mybatis_scala.util.{LogHelper,Schema}
import org.specs.{HtmlSpecification,Specification}
import org.specs.literate.Textile

import org.joda.time.DateTime
import java.util.Date
import org.mybatis_scala.model._
import org.mybatis_scala.service.mybatis._
import org.mybatis_scala.service.generator._
import h2db._


class AuthorCRUDSpec extends HtmlSpecification with Textile with LogHelper {
	
	val connection = Schema.conn
	val genreArray = Array (new Genre (GenreH2IdGenerator.nextVal,"Science Fiction"), new Genre (GenreH2IdGenerator.nextVal,"Love Story"))
	genreArray.foreach (g=>MyBatisDaoService.saveGenre(g))
	
	def bookList = List (Book (BookH2IdGenerator.nextVal, "TTL1" , "ISBN1", genreArray(0)), Book (BookH2IdGenerator.nextVal, "TTL2" , "ISBN2", genreArray(1)),
		Book (BookH2IdGenerator.nextVal, "TTL3" , "ISBN3", genreArray(1)), Book (BookH2IdGenerator.nextVal, "TTL4" , "ISBN4", genreArray(0)))

	
	doFirst {
		// Initilize database schema
		Schema.init()								
	}	
	
	"BookCRUDSpec spec" is <textile>
	
	h2. CRUD operations for Book object
	
	Author object has Book collection.
	
	DAO object should <ex> implement Create (save) method for AuthorRecord class</ex>{
		 eg {
			val acount1 = MyBatisDaoService.getAuthorCount 

			val a1 = new AuthorRecord 
			a1.authorId = AuthorH2IdGenerator.nextVal 
			a1.birthDay = new DateTime()
			a1.firstName = "firstName"
			a1.lastName =  "lastName"
			a1.annotation = "annotation"

			MyBatisDaoService.saveAuthorRecord (a1)

			val acount2 = MyBatisDaoService.getAuthorCount 

			// one author must be saved
			(acount2 - acount1) must_== 1

		}
	}
	
	DAO object should <ex> implement Create (save) method for Author class</ex>{
		 eg {
			val acount1 = MyBatisDaoService.getAuthorCount 
			val bcount1 = MyBatisDaoService.getBookCount 
	
			val a1 = Author (AuthorH2IdGenerator.nextVal, new DateTime(new Date()), "firstName", "lastName", "annotation", bookList)												
			MyBatisDaoService.saveAuthor (a1)			
			val books = MyBatisDaoService.lazyLoadBookByAuthor(a1.authorId)
			
	
			val acount2 = MyBatisDaoService.getAuthorCount 
			val bcount2 = MyBatisDaoService.getBookCount 
	
			// one author must be saved
			(acount2 - acount1) must_== 1
	
			// book collection must be saved
			(bcount2 - bcount1) must_== a1.books.size
	
			// saved books must be owned by this author
			books.size must_== a1.books.size
			
		}
	}	
	DAO object should <ex> implement Read method for Author class</ex>{
		eg {
			val author1 = Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(10) , "firstName", "lastName", "annotation", bookList)																	
			MyBatisDaoService.saveAuthor (author1)
			
			val author2 = Author.authorOption(MyBatisDaoService.loadAuthorById (author1.authorId))
			lzDebug ("author read method author1: "+Some(author1))
			lzDebug ("author read method author2: "+author2)
			
			author2 must_!= None
			author2 must_== Some(author1)
			
			val author3 = Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(10) , "firstName", "lastName", "annotation", List())																	
			MyBatisDaoService.saveAuthor (author3)
			
			val author4 = Author.authorOption(MyBatisDaoService.loadAuthorById (author3.authorId))
			
			lzDebug ("author read method author3: "+Some(author3))
			lzDebug ("author read method author4: "+author4)

			author4 must_!= None
			author4 must_== Some(author3)			
		}				
	}
	

	

			
	</textile>
}

	
// DAO object should <ex> implement lazy Read method for Author class</ex>{
// 	eg {
// 		// lets' create an authorlist
// 		val authorList  = List (Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(1) , "SpetialFirstName1", "lastName1", "annotation2", bookList),
// 		Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(2) , "SpetialFirstName2", "lastName2", "annotation2", bookList),
// 		Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(3) , "SpetialFirstName3", "lastName3", "annotation3", bookList))
// 		// and save this one
// 		authorList.foreach (MyBatisDaoService.saveAuthor (_))			
// 		
// 		// load saved list from database
// 		val resList = MyBatisDaoService.lazyLoadAuthorByFirstName("SpetialFirstName%")
// 		
// 		resList.size must_== authorList.size			
// 		Author(resList.head) must_== authorList.head
// 		
// 	}																			
// }	
	
	// DAO object should <ex> implement Update method for Author class. This method must 
	// <ul>
	// 	<li>update author master record</li> 
	// 	<li>delete books that does't exists in the new author object</li>
	// 	<li>insert books that does't exists in the old author object</li>
	// 	<li>update that do exists and has been changed</li>
	// </ul></ex>{
	// 	eg {
	// 		val oldBooks = bookList
	// 
	// 		// create an author
	// 		val author1 = Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(10) , "firstName1", "lastName1", "annotation1", oldBooks)																	
	// 		MyBatisDaoService.saveAuthor (author1)
	// 
	// 		// create an updated author
	// 		// newBooks list - one element from old list, one updated book and a new book
	// 		val newBooks = Book (oldBooks.last.bookId, "TTL4 updated" , "ISBN4 updated", genreArray(0)) ::
	// 			Book (BookH2IdGenerator.nextVal, "Some title" , "Some ISBN", genreArray(0), author1) ::
	// 			oldBooks.take(1) 
	// 		val author2 = Author (author1.authorId, new DateTime().minusYears(11) , "firstName2", "lastName2", "annotation2", newBooks)
	// 
	// 		MyBatisDaoService.updateAuthor (author2)			
	// 		
	// 		val author3 = Author.authorOption(MyBatisDaoService.loadAuthorById (author1.authorId))
	// 		author3 must_!= None
	// 		author3.foreach { a =>  // Option is a monad
	// 			a.firstName must_== "firstName2"
	// 			a.books.size must_== 3
	// 			a.books.filter (b=> b.title == "TTL4 updated").size must_== 1
	// 			a.books.filter (b=> b.isbn == "Some ISBN").size must_== 1
	// 			a.books.filter (b=> b.isbn == oldBooks.head.isbn).size must_== 1
	// 		}			
	// 	}		
	// }	
	// 
	// DAO object should <ex> implement Delete cascade method for Author class (delete master and slave records)</ex>{
	// 	val author1 = Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(10) , "firstName1", "lastName1", "annotation1", bookList)																	
	// 	MyBatisDaoService.saveAuthor (author1)
	// 						
	// 	MyBatisDaoService.deleteCascadeAuthor (author1)				
	// 	
	// 	val author3 = Author.authorOption(MyBatisDaoService.loadAuthorById (author1.authorId))				
	// 	author3 must_== None
	// }
	// 
	// DAO object should <ex> implement Delete restrict method for Author class (delete master only when slave book does not exists)</ex>{
	// 	eg{
	// 		val author1 = Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(10) , "firstName1", "lastName1", "annotation1", bookList)																	
	// 		MyBatisDaoService.saveAuthor (author1)
	// 
	// 		MyBatisDaoService.deleteAuthor (author1) must throwA[Exception]		
	// 
	// 		val author2 = Author.authorOption(MyBatisDaoService.loadAuthorById (author1.authorId))				
	// 		lzDebug ("delete author1: "+Some(author1))
	// 		lzDebug ("delete author2: "+author2)
	// 		
	// 		author2 must_!= None
	// 		author2 must_== Some(author1)
	// 		
	// 		val author3 = Author (AuthorH2IdGenerator.nextVal, new DateTime().minusYears(10) , "firstName3", "lastName3", "annotation3", List())																	
	// 		MyBatisDaoService.saveAuthor (author3)			
	// 		MyBatisDaoService.deleteAuthor (author3) 
	// 
	// 		val author4 = Author.authorOption(MyBatisDaoService.loadAuthorById (author3.authorId))
	// 		lzDebug ("delete author4: "+author4)
	// 		
	// 		author4 must_== None
	// 	}
	// }
