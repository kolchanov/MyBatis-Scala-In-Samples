package org.mybatis_scala.util

import java.lang.reflect.{Array => JArray}
import java.lang.{Float => JFloat, Double => JDouble}

/**
* Scala Hash Builder <p/> 
* Thanks for jboner http://gist.github.com/939
*/
object HashBuilder {
	val SEED = 23

	def hash(seed: Int, value: Boolean): Int = firstTerm(seed) + (if (value) 1 else 0)
	def hash(seed: Int, value: Char): Int = firstTerm(seed) + value.asInstanceOf[Int]
	def hash(seed: Int, value: Int): Int = firstTerm(seed) + value
	def hash(seed: Int, value: Long): Int = firstTerm(seed) + (value ^ (value >>> 32) ).asInstanceOf[Int]
	def hash(seed: Int, value: Float): Int = hash(seed, JFloat.floatToIntBits(value))
	def hash(seed: Int, value: Double): Int = hash(seed, JDouble.doubleToLongBits(value))
	def hash(seed: Int, anyRef: AnyRef): Int = {
	  var result = seed
	  if (anyRef == null) result = hash(result, 0)
	  else if (!isArray(anyRef)) result = hash(result, anyRef.hashCode())
	  else { // is an array
	    for (id <- 0 until JArray.getLength(anyRef)) {
	      val item = JArray.get(anyRef, id)
	      result = hash(result, item)
	    }
	  }
	  result
	}
	private def firstTerm(seed: Int): Int = PRIME * seed
	private def isArray(anyRef: AnyRef): Boolean = anyRef.getClass.isArray
	private val PRIME = 37	
}

