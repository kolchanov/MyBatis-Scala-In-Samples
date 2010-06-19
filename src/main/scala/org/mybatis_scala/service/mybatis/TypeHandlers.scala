package org.mybatis_scala.service.mybatis

import org.apache.ibatis.`type`.{TypeHandler, JdbcType}
import java.sql.{ResultSet, CallableStatement, PreparedStatement}
import org.mybatis_scala.util.LogHelper

class OptionLongTypeHandler extends TypeHandler with LogHelper{
	def setParameter(ps:PreparedStatement, i : Int, parameter : Any, jdbcType : JdbcType) : Unit = {
		lzDebug ("OptionLongTypeHandler.setParameter called")	
		val par : Option[Long] = parameter.asInstanceOf[Option[Long]]
		lzDebug ("OptionLongTypeHandler par: "+par)	
		if (par.isDefined) {
			ps.setLong(i, par.get)
		}
		else {
			ps.setNull(i, java.sql.Types.NUMERIC)
		}
	}
	
	def getResult (rs: ResultSet, columnName: String) : java.lang.Object = {
		
		lzDebug ("OptionLongTypeHandler.getResult called")		
		val value = rs.getString(columnName)
		val res : Option[Long] =  {
			value  match  {
				case null => None
				case _ => Some(value.toLong)
			}
		}
		lzDebug ("OptionLongTypeHandler.getResult res: "+res)
		res
	}
	
	def getResult (cs : CallableStatement, columnIndex : Int) : java.lang.Object = {
		val value = cs.getString(columnIndex)
		val res : Option[Long] =  {
			value  match  {
				case null => None
				case _ => Some(value.toLong)
			}
		}
		res
	} 
	
}

class JodaTimeTypeHandler  extends TypeHandler with LogHelper{
	import org.joda.time.DateTime
	
	def setParameter(ps:PreparedStatement, i : Int, parameter : Any, jdbcType : JdbcType) : Unit = {
		val par = parameter.asInstanceOf[DateTime]
		ps.setTimestamp (i,new java.sql.Timestamp(par.getMillis()))
	}
	def getResult (rs: ResultSet, columnName: String) : java.lang.Object = {
		lzDebug ("JodaTimeTypeHandler.getResult "+columnName+": "+rs.getTimestamp(columnName))
		val res = new DateTime (rs.getTimestamp(columnName))
		lzDebug ("JodaTimeTypeHandler.getResult res: "+res)
		res
	}
	def getResult (cs : CallableStatement, columnIndex : Int) : java.lang.Object = {
		new DateTime (cs.getTimestamp(columnIndex))
	}
	
}