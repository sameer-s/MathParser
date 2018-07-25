package ssuri.cassystem.util;

import static ssuri.cassystem.util.LogLevel.*;
public class Log
{
    private static boolean debugEnabled = false;
    
    public static void enableDebug()
    {
        debugEnabled = true;
    }
    
    public static void disableDebug()
    {
        debugEnabled = false;
    }
    
    public static boolean isDebugEnabled()
    {
        return debugEnabled;
    }
    
    private static void log(LogLevel level, Object msg, Object...params)
    {
        String message = null;
        if(msg != null)
        {
            message = String.format(msg.toString(), params);
        }

        String logStatement = String.format("[%s] %s", level.name(), message);
        switch(level)
        {
            case SEVERE:
            case ERROR:
                System.err.println(logStatement);
                break;
            case DEBUG:
                if(!debugEnabled) { break; }
            default:
                System.out.println(logStatement);
        }
    }
    
    public static void severe(Object msg, Object...params)
    {
        log(SEVERE, msg, params);
    }
    
    public static void s(Object msg, Object... params)
    {
        severe(msg, params);
    }

    public static void error(Object msg, Object...params)
    {
        log(ERROR, msg, params);
    }
    
    public static void e(Object msg, Object... params)
    {
        error(msg, params);
    }
    
    public static void warn(Object msg, Object...params)
    {
        log(WARN, msg, params);
    }
    
    public static void w(Object msg, Object... params)
    {
        warn(msg, params);
    }
    
    public static void info(Object msg, Object...params)
    {
        log(INFO, msg, params);
    }
    
    public static void i(Object msg, Object... params)
    {
        info(msg, params);
    }
    
    public static void debug(Object msg, Object...params)
    {
        log(DEBUG, msg, params);
    }
    
    public static void d(Object msg, Object... params)
    {
        debug(msg, params);
    }
}
