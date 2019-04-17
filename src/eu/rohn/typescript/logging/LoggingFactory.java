package eu.rohn.typescript.logging;

public interface LoggingFactory
{

    Logger getLogger(Class classToLog);
    
    void setOutputToDebug();
}
