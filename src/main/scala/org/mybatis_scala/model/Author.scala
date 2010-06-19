package org.mybatis_scala.model

import org.mybatis_scala.util.{HashBuilder,LogHelper}
import java.util.{Date,ArrayList}
import org.joda.time.DateTime
import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer

trait AbstractAuthor {
	def authorId: Long 
	def birthDay: DateTime 
	def firstName: String
	def lastName: String
	def annotation: String
}

/**
* Mutable bean-like class that represent record in the AUTHOR table.
*/
class AuthorRecord extends AbstractAuthor{
	var authorId: Long = 0
	var birthDay: DateTime = null
	var firstName: String = null
	var lastName: String = null
	var annotation: String = null
	var books: ArrayList[BookRecord] = new ArrayList[BookRecord]()
	
	override def toString() = "AuthorRecord [authorId: "+authorId+" birthDay: "+birthDay+" firstName: "+firstName+" lastName: "+lastName+ " annotation: "+annotation+" books: "+books+"]"
	
  override def equals(other: Any) = other match {
    case that: AuthorRecord =>
      this.authorId == that.authorId && this.birthDay == that.birthDay && this.firstName == that.firstName &&
				this.lastName == that.lastName && this.annotation == that.annotation && this.books == that.books 
    case _ =>
      false
  }	

}

/** 
* Immutable Scala style Author
*/ 

class Author protected (val authorId: Long, val birthDay: DateTime, val firstName: String, val lastName: String, val annotation: String, val books: List[Book] ) extends AbstractAuthor {
	
	require (books != null)
	
	protected def this (a : AuthorRecord) = this (a.authorId, new DateTime(a.birthDay), a.firstName, a.lastName, a.annotation, Author.convertBookList(a.authorId,a.books))
	
	override def toString() = "Author [authorId: "+authorId+" birthDay: "+birthDay+" firstName: "+firstName+" lastName: "+lastName+ " annotation: "+annotation+" books: "+books+"]"	
		
  override def equals(other: Any) = other match {
    case that: Author =>
      this.authorId == that.authorId && this.birthDay == that.birthDay && this.firstName == that.firstName &&
				this.lastName == that.lastName && this.annotation == that.annotation && this.books.sort ((e1, e2) => e1 < e2) == that.books.sort ((e1, e2) => e1 < e2)  
    case _ =>
      false
  }
	
}

object Author extends LogHelper{
	
	def apply (authorId: Long, birthDay: DateTime, firstName: String, lastName: String, annotation: String, books: List[Book] ) = 
		new Author  (authorId, birthDay, firstName, lastName, annotation, books.map (b => Book (b,authorId)))
		
	def apply (a : AuthorRecord) = new Author (a)
	
	def convertBookList (authorId: Long, books: ArrayList[BookRecord]) : List[Book]= {
		lzDebug ("convertBookList authorId: "+authorId+" books: "+books)
		if (books == null) return List()
		val buffer : Buffer[BookRecord] = books
		buffer.filter( b => b.bookId > 0).map (br => Book(br,authorId)).toList
		// buffer.map (br => Book(br,authorId)).toList
	}	
	
	def authorOption (authorRecord : Option[AuthorRecord]): Option [Author] = if (authorRecord.isDefined) Some (apply(authorRecord.get)) else None
}