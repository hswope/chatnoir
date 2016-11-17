package com.hms3.chatnoir.server.utility
import org.slf4j.LoggerFactory
import java.util.logging.Level
import java.util.logging.LogRecord


class JulFacade(name : String) : java.util.logging.Logger(name, null) {
    private val logger = LoggerFactory.getLogger(name)

    init{
        if (logger.isTraceEnabled) initLevel(org.slf4j.event.Level.TRACE)
        else if (logger.isDebugEnabled) initLevel(org.slf4j.event.Level.DEBUG)
        else if (logger.isInfoEnabled) initLevel(org.slf4j.event.Level.INFO)
        else if (logger.isWarnEnabled) initLevel(org.slf4j.event.Level.WARN)
        else if (logger.isErrorEnabled) initLevel(org.slf4j.event.Level.ERROR)
    }

    private fun initLevel(slf4jLevel : org.slf4j.event.Level){
        when (slf4jLevel) {
            org.slf4j.event.Level.ERROR -> level = Level.SEVERE
            org.slf4j.event.Level.WARN -> level = Level.WARNING
            org.slf4j.event.Level.INFO -> level = Level.INFO
            org.slf4j.event.Level.DEBUG -> level = Level.FINE
            org.slf4j.event.Level.TRACE -> level = Level.FINEST
        }
    }

    override fun log(record: LogRecord?) {
        if (record == null) return

        when(record.level){
            Level.SEVERE -> logger.error(record.message,record.thrown)
            Level.WARNING -> logger.warn(record.message,record.thrown)
            Level.INFO -> logger.info(record.message,record.thrown)
            Level.CONFIG -> logger.info(record.message,record.thrown)
            Level.FINE -> logger.debug(record.message,record.thrown)
            Level.FINER, Level.FINEST -> logger.trace(record.message,record.thrown)
        }
    }
}