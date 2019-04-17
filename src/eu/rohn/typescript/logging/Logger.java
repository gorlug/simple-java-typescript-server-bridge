package eu.rohn.typescript.logging;

public interface Logger
{

    void info(String info);
    
    void warn(String warning, Throwable cause);
    
    void debug(String message);
}
