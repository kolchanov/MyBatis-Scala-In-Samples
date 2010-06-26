package org.mybatis_scala.model
import org.mybatis_scala.util.{HashBuilder,LogHelper}


trait AbstractBook {
	def bookId: Long 
	def title : String
	def isbn : String 
	def genre : Genre 
	def authorId: Option[Long]
}

/**
* Mutable Bean-like Book
*/
class BookRecord extends AbstractBook{
	var bookId: Long = 0
	var title : String = null
	var isbn : String = null
	var genre : Genre = null
	var authorId : Option[Long] = None
		
	override def toString() = "BookRecord [bookId: "+bookId+" title: "+title+" isbn: "+isbn+" genre: "+genre+" authorId: "+authorId+"]"
	
  override def equals(other: Any) = other match {
    case that: BookRecord =>
      this.bookId == that.bookId && this.title == that.title && this.isbn == that.isbn && this.genre == that.genre && this.authorId == that.authorId
    case _ =>
      false
  }		
}

/**
* Immutable Book
*/
class Book (val bookId: Long, val title: String , val isbn: String, val genre: Genre, val authorId: Option[Long]) extends AbstractBook{
	
	def this (bookId: Long, title: String, isbn: String, genre: Genre) = this (bookId, title, isbn, genre, None)	
	def this (br: BookRecord) = this (br.bookId, br.title, br.isbn, br.genre, br.authorId)
	
	override def toString() = "Book [bookId: "+bookId+" title: "+title+" isbn: "+isbn+" genre: "+genre+" authorId: "+authorId+"]"
	
  override def equals(other: Any) = other match {
    case that: Book =>
      this.bookId == that.bookId && this.title == that.title && this.isbn == that.isbn && this.genre == that.genre && this.authorId == that.authorId
    case _ =>
      false
  }	

	def < (that : Book) : Boolean ={
		(this.bookId < that.bookId) match {
			case true => true
			case false => false
			case _ => this.title < that.title && this.isbn < that.isbn && this.genre < that.genre
		}
	}
	
}

object Book {
	def apply (bookId: Long, title: String, isbn: String, genre: Genre, author: Author) = new Book (bookId, title, isbn, genre, Some(author.authorId))
	def apply (bookId: Long, title: String, isbn: String, genre: Genre) = new Book (bookId, title, isbn, genre, None)
	def apply (bookRecord: BookRecord) = new Book (bookRecord.bookId, bookRecord.title, bookRecord.isbn, bookRecord.genre, bookRecord.authorId)
	def apply (bookRecord: BookRecord, authorId: Long) = new Book (bookRecord.bookId, bookRecord.title, bookRecord.isbn, bookRecord.genre, Some(authorId))
	def apply (bookRecord: BookRecord, author: AuthorRecord) = new Book (bookRecord.bookId, bookRecord.title, bookRecord.isbn, bookRecord.genre, Some(author.authorId))
	def apply (book: Book, authorId: Long) = new Book (book.bookId, book.title, book.isbn, book.genre, Some(authorId))
	
	def bookOption (bookRecord: Option[BookRecord]) : Option[Book] = if (bookRecord.isDefined) Some(apply(bookRecord.get)) else None
	def bookOption (bookRecord: Option[BookRecord], author: AuthorRecord) : Option[Book] = if (bookRecord.isDefined) Some(apply(bookRecord.get, author)) else None
}