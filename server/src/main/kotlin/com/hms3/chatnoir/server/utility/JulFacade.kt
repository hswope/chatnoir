package com.hms3.chatnoir.server.utility

import org.apache.commons.logging.LogFactory
import java.util.logging.Level
import java.util.logging.LogRecord


class JulFacade(name : String) : java.util.logging.Logger(name, null) {
    var logger = LogFactory.getLog(name)

    init{
        var level = java.util.logging.Level.SEVERE
        if (logger.isFatalEnabled) level = Level.SEVERE
        if (logger.isErrorEnabled) level = Level.SEVERE
        if (logger.isWarnEnabled) level = Level.WARNING
        if (logger.isInfoEnabled) level = Level.INFO
        if (logger.isDebugEnabled) level = Level.FINE
        if (logger.isTraceEnabled) level = Level.FINEST

        this.level = level
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