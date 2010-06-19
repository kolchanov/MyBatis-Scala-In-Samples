package org.mybatis_scala.service.mybatis

import java.io.{BufferedReader,InputStreamReader,FileInputStream}
import org.mybatis_scala.util.LogHelper
import org.mybatis_scala.ApplicationContext
import org.mybatis_scala.model.{GenreRecord, BookRecord, AuthorRecord, Genre, Book, Author}
import org.apache.ibatis.session.{SqlSessionFactoryBuilder,SqlSession}
import scala.collection.JavaConversions._
import scala.collection.mutable._

/**
* Trait that incapsulates all DAO operations
*/
trait MyBatisDaoService {
	//* Get increment for sequence. Used in H2IDGenerator.
	def getSequenceIncremet (name: String) : Int
	
	//* get genre sequence nextval
	def getGenreSequenceNextVal : Long
	
	//* get book sequence nextval
	def getBookSequenceNextVal : Long

	//* get author sequence nextval
	def getAuthorSequenceNextVal : Long
	
	
	//* Genre methods
	
	//* get GENRE row count 
	def getGenreCount: Int
	//* save immutable genre to database
	def saveGenre (genre: Genre) : Unit
	//* load immutable genre from database
	def loadGenreById (id: Long) : Option[Genre]
	//* load immutable genre from database
	def loadGenreByName (name: String) : List[Genre]
	//* load mutable GenreRecord from Database
	def loadGenreRecordById (id: Long) : Option[GenreRecord]
	//* update genre
	def updateGenre (genre: Genre ) : Unit
	//* delete genre
	def deleteGenre (genre: Genre) : Unit
	
	
	//* Book methods

	//* get BOOK row count 
	def getBookCount: Int
	//* save immutable genre to database
	def saveBook (book: Book) : Unit	
	//* load mutable book from database by Id
	def loadBookById (id: Long) : Option[BookRecord]
	//* lazy load mutable book from database
	def lazyLoadBookByTitle (title: String) : List[BookRecord]
	//* lazy load mutable book from database
	def lazyLoadBookByAuthor (authorId: Long) : List[BookRecord]	
	//* update book
	def updateBook (book: Book) : Unit
	//* delete book
	def deleteBook (book: Book) : Unit
	
	
	//* Author methods
	//* get AUTHOR row count 
	def getAuthorCount: Int
	//* save mutable author to database
	def saveAuthorRecord (author: AuthorRecord) : Unit		
	//* save immutable author to database
	def saveAuthor (author: Author) : Unit	
	//* load author from database by id
	def loadAuthorById (id: Long) : Option[AuthorRecord]
	//* lazy load author from database by id
	def lazyLoadAuthorByFirstName (name: String) : List[AuthorRecord]
	//* update author
	def updateAuthor (author: Author) : Unit
	//* delete cascade author
	def deleteCascadeAuthor (author: Author) : Unit	
	//* delete restrict author
	def deleteAuthor (author: Author) : Unit	
}

/**
* Default implementation of MyBatisDaoService trait
*/
object MyBatisDaoService extends MyBatisDaoService with LogHelper {
	
	// MyBatis file is in the classpath
	val reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/MyBatis.xml")))

	val props = ApplicationContext.getProperties	
	val sqlMapper = new SqlSessionFactoryBuilder().build(reader,props)	

	/**
   * Execute instructions within a SQLSession with autocommit. 
   */
  def exec[T<:Any](operation: SqlSession => T): T = {	
		val session = sqlMapper.openSession(true) 
		try { 
		  operation(session)
		} finally { 
		  session.close()
		}	
  }
	
	//* Get one object from database
	def getOne [T] (myBatisId : String) ( obj : Any) : T = exec{ s =>	s.selectOne (myBatisId,obj).asInstanceOf[T]}	
	// * Get Option [T] object from database
	def getOneOption[T] (myBatisId : String) ( obj : Any) : Option[T] = {
		val res = getOne[T] (myBatisId) (obj)
		if (res != null) Some(res) else None
	}	
	//* Get one object from database
	def getOneObject [T] (myBatisId : String) : T = exec{ s =>	s.selectOne (myBatisId).asInstanceOf[T]}
	//* get list from database
	def getList [T] (myBatisId : String) ( obj : Any) : List[T] = {
		val res : Buffer[T] = exec{ s => s.selectList (myBatisId,obj).asInstanceOf[java.util.List[T]]}
		res.toList
	}
	
	
	//* insert object into database
	def insert [T] (myBatisId : String) ( obj : T) = exec{ s =>	s.insert (myBatisId,obj)}
	//* update object 
	def update [T] (myBatisId : String) ( obj : T) = exec{ s =>	s.update (myBatisId,obj)}
	//* delete object 
	def delete [T] (myBatisId : String) ( obj : T) = exec{ s =>	s.delete (myBatisId,obj)}	

	//* methods for id generators
	def getSequenceIncremet (name: String) : Int = getOne[Int] ("selectSequenceIncrement") (name)
	def getGenreSequenceNextVal : Long = getOneObject[Long] ("genreNextVal")
	def getBookSequenceNextVal : Long = getOneObject[Long] ("bookNextVal")
	def getAuthorSequenceNextVal : Long = getOneObject[Long] ("authorNextVal")


	//* Genre methods
	def getGenreCount: Int = getOneObject[Int] ("countGenre")
	def saveGenre (genre: Genre) = insert [Genre] ("insertGenre") (genre)
	def loadGenreById (id: Long) : Option[Genre] = getOneOption[Genre] ("selectGenreById") (id)
	def loadGenreByName (name: String) : List[Genre] = getList [Genre] ("selectGenreByName") (name)
	def loadGenreRecordById (id: Long) : Option[GenreRecord] = getOneOption[GenreRecord] ("selectGenreRecordById") (id)
	def updateGenre (genre: Genre) : Unit = update [Genre] ("updateGenre") (genre)
	def deleteGenre (genre: Genre) : Unit = delete [Genre] ("deleteGenre") (genre)
	
	//* Book methods
	def getBookCount: Int = getOneObject[Int] ("countBook")	
	def saveBook (book: Book) : Unit = insert [Book] ("insertBook") (book)
	def loadBookById (id: Long) : Option[BookRecord] = getOneOption[BookRecord] ("selectBookById") (id)
	def lazyLoadBookByTitle (title: String) : List[BookRecord] = getList [BookRecord] ("lazySelectBookByTitle") (title)	
	def lazyLoadBookByAuthor (authorId: Long) : List[BookRecord]	=	getList [BookRecord] ("selectBookByAuthor") (authorId)	
	def updateBook (book: Book) : Unit = update [Book] ("updateBook") (book)
	def deleteBook (book: Book) : Unit = delete [Book] ("deleteBook") (book)

	//* Author methods
	def getAuthorCount: Int = getOneObject[Int] ("countAuthor")
	def saveAuthorRecord (author: AuthorRecord) : Unit = insert [AuthorRecord] ("insertAuthor") (author)
	def saveAuthor (author: Author) : Unit = exec { s =>
		s.insert ("insertAuthor",author)		
		author.books.foreach (b=> s.insert ("insertBook",b))		
	}
	def loadAuthorById (id: Long) : Option[AuthorRecord] = getOneOption[AuthorRecord] ("selectAuthorById") (id)
	def lazyLoadAuthorByFirstName (name: String) : List[AuthorRecord] = getList[AuthorRecord] ("lazySelectAuthorByFirstName") (name)	
	def updateAuthor (author: Author) : Unit = exec { s =>
		//update author record
		s.update ("updateAuthor",author)
		
		// delete books that does't exists in the new author object
		val oldBooksBuffer : Buffer[BookRecord] = s.selectList ("selectBookByAuthor",author.authorId).asInstanceOf[java.util.List[BookRecord]]			
		val oldBooks = oldBooksBuffer.toList.map (r => Book(r))	// convert from java List[BookRecord] to Scald List[Book]
		(oldBooks.map ( b => b.bookId) -- (author.books.map (b => b.bookId))).foreach ( b=> s.delete ("deleteBookById",b))

		// insert books that does't exists in the old author object and update that do exists and has been changed
		author.books.partition (e => oldBooks.map ( b => b.bookId).exists (o => o == e.bookId)) match {
			case (updateList, insertList) =>
				insertList.foreach {b => 
					lzDebug ("to insert: "+b)
					s.insert ("insertBook",b)
				}				
				// update only changed books
				(updateList -- oldBooks).foreach { b=> 
					lzDebug ("toUpdate: "+b)
					s.update ("updateBook",b)
				}
		}						
	}
	def deleteCascadeAuthor (author: Author) : Unit	= exec { s =>	
		s.delete ("deleteBookByAuthor",author)
		s.delete ("deleteAuthor",author)
	}	
	def deleteAuthor (author: Author) : Unit = delete [Author] ("deleteAuthor") (author)
}