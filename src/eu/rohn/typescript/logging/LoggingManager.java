package eu.rohn.typescript.logging;

public class LoggingManager
{
    private static LoggingFactory c_factory;
    
    public static void setFactory(LoggingFactory factory)
    {
        c_factory = factory;
    }

    public static Logger getLogger(Class classToLog)
    {
        return c_factory.getLogger(classToLog);
    }
}
