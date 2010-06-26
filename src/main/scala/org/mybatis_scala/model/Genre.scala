package org.mybatis_scala.model



trait AbstractGenre {
	def genreId : Long
	def name: String
}


/**
*	Simple mutable bean-like class that represents record in the database 
*/
class GenreRecord extends AbstractGenre {
	var genreId:Long = 0
	var name: String = null
	
	override def toString() = "GenreRecord[genreId: "+genreId+" name: "+name+"]"
	
  override def equals(other: Any) = other match {
    case that: GenreRecord =>
      this.genreId == that.genreId && this.name == that.name
    case _ =>
      false
  }	

}

/**
* Immutable class for the book genre
*/

class Genre (val genreId: Long, val name: String) extends AbstractGenre {
	
	// have to have protected constructor due to MyBastis issue #11 "Immutable POJO fails to load when lazyLoading is enabled"
	// waiting new version
	protected def this () = this (0,"")
	
	def this (genre: GenreRecord) =  this (genre.genreId, genre.name)
	
	override def toString() = "GenreRecord[genreId: "+genreId+" name: "+name+"]"
	
  override def equals(other: Any) = other match {
    case that: Genre =>
      this.genreId == that.genreId && this.name == that.name
    case _ =>
      false
  }	

	def < (that : Genre) : Boolean ={
		(this.genreId < that.genreId) match {
			case true => true
			case false => false
			case _ => this.name < that.name
		}
	}

}