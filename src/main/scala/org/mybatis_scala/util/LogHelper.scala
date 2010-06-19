package org.mybatis_scala.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait LogHelper {
    val loggerName = this.getClass.getName
    lazy val l = LoggerFactory.getLogger(loggerName)

		/**
		*	Lazy debug
		*/
		def lzDebug ( msg: => String) = {
			if (l.isDebugEnabled())
				l.debug (msg)                             
		}

		/**
		*	Lazy info
		*/
		def lzInfo ( msg: => String) = {
			if (l.isInfoEnabled())
				l.info (msg)
		}

		/**
		*	Lazy warn
		*/
		def lzWarn ( msg: => String) = {
				l.warn (msg)
		}

		/**
		*	Lazy error
		*/
		def lzError ( msg: => String) = {
				l.error (msg)
		}

}

