package eu.rohn.typescript.logging.log4j;

import org.apache.logging.log4j.Logger;

public class Log4jLogger implements eu.rohn.typescript.logging.Logger
{
    
    private Logger m_log;
    
    public Log4jLogger(Logger logger)
    {
        m_log = logger;
    }

    @Override
    public void info(String info)
    {
        m_log.info(info);
    }

    @Override
    public void warn(String warning, Throwable cause)
    {
        m_log.warn(warning, cause);
    }

    @Override
    public void debug(String message)
    {
        m_log.debug(message);
    }

}
