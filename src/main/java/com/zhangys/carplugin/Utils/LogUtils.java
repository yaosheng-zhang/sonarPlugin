package com.zhangys.carplugin.Utils;

import org.apache.log4j.Logger;

/**
 * LogUtils
 * 日志输出类
 * @author ScorpioDong
 * @version 1.0
 * @date 2020/3/14 9:48 PM
 */
public class LogUtils {
    private static final Logger debugLogger = Logger.getLogger("DEBUG");
    private static final Logger infoLogger = Logger.getLogger("INFO");
    private static final Logger warnLogger = Logger.getLogger("WARN");
    private static final Logger errorLogger = Logger.getLogger("ERROR");

    /**
     * 输出debug级别的日志
     *
     * @param msg Object 要输出的消息
     */
    public static void logDebug(Object msg) {
        debugLogger.debug(msg);
    }

    /**
     * 输出info级别的日志
     *
     * @param msg Object 要输出的消息
     */
    public static void logInfo(Object msg) {
        debugLogger.info(msg);
    }

    /**
     * 输出warn级别的日志
     *
     * @param msg Object 要输出的消息
     */
    public static void logWarn(Object msg) {
        debugLogger.warn(msg);
    }

    /**
     * 输出error级别的日志
     *
     * @param msg Object 要输出的消息
     */
    public static void logError(Object msg) {
        debugLogger.error(msg);
    }
}
