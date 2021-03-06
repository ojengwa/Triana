package org.trianacode.enactment.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.trianacode.enactment.logging.stampede.StampedeLogger;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Sep 26, 2010
 */
public class Loggers {

    public static enum LogLevels {
        OFF,    // 0
        FATAL,  // 1
        ERROR,  // 2
        WARN,   // 3
        INFO,   // 4
        DEBUG,  // 5
        TRACE,  // 6
        ALL     // 7
    }

    /**
     * default logger
     */
    public static final Log LOGGER = LogFactory.getLog("TRIANA");

    /**
     * logs execution state changes to a running taskgraph
     */
    public static final Log EXECUTION_LOGGER = LogFactory.getLog("TRIANA.EXECUTION");

    /**
     * undocumented :-)
     */
    public static final Log EXECUTION_STATE_LOGGER = LogFactory.getLog("TRIANA.EXECUTION.STATE");

    /**
     * logger to be used by Units - see Unit.log()
     */
    public static final Log PROCESS_LOGGER = LogFactory.getLog("TRIANA.PROCESS");

    /**
     * logs configuration messages and errors
     */
    public static final Log CONFIG_LOGGER = LogFactory.getLog("TRIANA.CONFIGURATION");

    /**
     * logs tool related messages
     */
    public static final Log TOOL_LOGGER = LogFactory.getLog("TRIANA.TOOL");

    /**
     * logger for development.
     */
    public static final Log DEV_LOGGER = LogFactory.getLog("TRIANA.DEV");


    /**
     * logger for Stampede. See StampedeLogger, and pass StampedeEvents to it.
     */
    public static final StampedeLogger STAMPEDE_LOGGER = StampedeLogger.getLog();

    private static String[] LOG_NAMES = {
            "TRIANA",
            "TRIANA.EXECUTION",
            "TRIANA.EXECUTION.STATE",
            "TRIANA.PROCESS",
            "TRIANA.CONFIGURATION",
            "TRIANA.TOOL",
            "TRIANA.DEV",
    };

    public static void setLogLevel(String level) {
        int l = 4;
        try {
            l = Integer.parseInt(level);
        } catch (Exception e) {

        }
        setLogLevel(l);

    }

    public static void isolateLogger(String logger, String level) {
        int l = 4;
        try {
            l = Integer.parseInt(level);
        } catch (Exception e) {

        }
        isolateLogger(logger, l);

    }

    public static void setLogLevel(int level) {
        if (level < 0 || level > 7) {
            level = 4;
        }
        LogLevels ll = LogLevels.values()[level];
        if (ll == null) {
            ll = LogLevels.INFO;
        }
        Level l = Level.toLevel(ll.toString());
        Logger.getRootLogger().setLevel(l);
        Logger.getLogger("TRIANA").setLevel(l);
    }

    public static void isolateLogger(String logger, int level) {
        if (level < 0 || level > 7) {
            level = 4;
        }
        LogLevels ll = LogLevels.values()[level];
        if (ll == null) {
            ll = LogLevels.INFO;
        }
        Level l = Level.toLevel(ll.toString());
        for (String s : LOG_NAMES) {
            deactivateLogger(s);
        }
        Logger log = Logger.getLogger(logger);
        if (log != null) {
            log.setLevel(l);
        }

    }

    public static void deactivateLogger(String logger) {
        Level l = Level.toLevel(LogLevels.OFF.toString());
        Logger log = Logger.getLogger(logger);
        if (log != null) {
            log.setLevel(l);
        }
    }

    static {
        setLogLevel(4);
    }


}
