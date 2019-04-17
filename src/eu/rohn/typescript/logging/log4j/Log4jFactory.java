package eu.rohn.typescript.logging.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import eu.rohn.typescript.logging.Logger;
import eu.rohn.typescript.logging.LoggingFactory;

public class Log4jFactory implements LoggingFactory
{

    @Override
    public Logger getLogger(Class classToLog)
    {
        org.apache.logging.log4j.Logger logger = LogManager.getLogger(classToLog);
        return new Log4jLogger(logger);
    }

    @Override
    public void setOutputToDebug()
    {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig("eu.rohn");
        loggerConfig.setLevel(Level.DEBUG);
        context.updateLoggers();
    }

}
