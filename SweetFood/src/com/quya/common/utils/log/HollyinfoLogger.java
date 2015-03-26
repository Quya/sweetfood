package com.quya.common.utils.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class HollyinfoLogger {
    // ���һ��ͬ����Map
    private static Map loggers = Collections.synchronizedMap(new HashMap());

    private static HollyinfoLogger instance;
    private Logger dblog;
    static {
        instance = new HollyinfoLogger();
    }

    private HollyinfoLogger() {
        initialize();
    }
    /**
     * @deprecated 请使用HollyinfoLogger.getLog() 方法
     * @return
     */
    public static HollyinfoLogger getInstance() {
        if (instance == null) {
            instance = new HollyinfoLogger();
        }
        return instance;
    }
    public static HollyinfoLog getLog(final String name) {
        return instance.getLogger(name);
    }
    public static HollyinfoLog getLog(Class clazz) {
        return instance.getLogger(clazz);
    }
    private void initialize() {
         PropertyConfigurator.configureAndWatch("log4j.properties");
        dblog = Logger.getLogger("dblog");
    }
    /**
     * 通过类名得到日志
     * @param name
     * @return
     */
    public HollyinfoLog getLogger(final String name) {
        HollyinfoLog log = null;
        // ��map��ȡ��log
        log = (HollyinfoLog) loggers.get(name);
        if (log == null) {
            // �������log
            log = new HollyinfoLog();
            // �������log4j��logger
            Logger temp = Logger.getLogger(name);
            // ��������hollyinfolog�е�log
            log.setLog(temp);
            // ��������hollyinfolog�е�dblog
            log.setDblog(dblog);
            // �ٰ�����뵽����֮��
            loggers.put(name, log);
        }
        return log;
    }
    /**
     * 通过类得到日志
     * @param clzname　被记录的类
     * @return　该类的日志
     */
    public HollyinfoLog getLogger(final Class clz) {
        HollyinfoLog log = null;
        log = (HollyinfoLog) loggers.get(clz.getName());
        if (log == null) {
            log = new HollyinfoLog();
            Logger temp = Logger.getLogger(clz.getName());
            log.setLog(temp);
            log.setDblog(dblog);
            loggers.put(clz.getName(), log);
        }
        return log;
    }
}
