package com.quya.common.utils.log;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class HollyinfoLog {
    private Logger log;

    private Logger dblog;

    public HollyinfoLog() {

    }

    public final void setDblog(final Logger pdblog) {
        dblog = pdblog;
    }

    public final void setLog(final Logger plog) {
        this.log = plog;
    }

    public final void debug(final String obj) {
        if (log.isDebugEnabled()) {
            log.debug(obj);
        }
    }
    public final void debug(final String obj, final Exception ex) {
        if (log.isDebugEnabled()) {
            log.debug(obj, ex);
        }
    }

    public final void warning(final String obj) {
        log.warn(obj);
    }

    public final void warning(final String obj, final Exception ex) {
        log.warn(obj, ex);
    }

    public final void info(final String obj) {
        if (log.isInfoEnabled()) {
            log.info(obj);
        }
    }
    public final void info(final String obj, final Exception ex) {
        if (log.isInfoEnabled()) {
            log.info(obj, ex);
        }
    }

    public final void error(final String obj) {
        log.error(obj);
    }
    public final void error(final String obj, final Exception ex) {
        log.error(obj, ex);
    }

    public final void dbinfo(final String mess, final String operator) {
        if (dblog.isInfoEnabled()) {
            MDC.put("operator", operator);
            dblog.info(mess);
            MDC.remove("operator");
        }
    }

    public final void dbdebug(final String mess, final String operator) {
        if (dblog.isDebugEnabled()) {
            MDC.put("operator", operator);
            dblog.debug(mess);
            MDC.remove("operator");
        }
    }

    public final void dbwarning(final String mess, final String operator) {
        MDC.put("operator", operator);
        dblog.warn(mess);
        MDC.remove("operator");
    }

    public final void dberror(final String mess, final String operator) {
        MDC.put("operator", operator);
        dblog.error(mess);
        MDC.remove("operator");
    }

    public final boolean isDBDebugEnabled() {
        return dblog.isDebugEnabled();
    }

    public final boolean isDBInfoEnabled() {
        return dblog.isInfoEnabled();
    }

    public final boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public final boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

}
