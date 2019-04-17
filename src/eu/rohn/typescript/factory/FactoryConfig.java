package eu.rohn.typescript.factory;

import eu.rohn.typescript.json.JSONFactory;
import eu.rohn.typescript.logging.LoggingFactory;
import eu.rohn.typescript.util.FileUtilInterface;
import eu.rohn.typescript.util.PlatformUtilInterface;

public class FactoryConfig
{

    public JSONFactory jsonFactory;

    public LoggingFactory loggingFactory;

    public FileUtilInterface fileUtilInterface;
    
    public PlatformUtilInterface platformUtilInterface;
    
    public void setJSONFactory(JSONFactory jsonFactory)
    {
        this.jsonFactory = jsonFactory;
    }
    
    public void setLoggingFactory(LoggingFactory loggingFactory)
    {
        this.loggingFactory = loggingFactory;
    }
    
    public void setFileUtilInterface(FileUtilInterface fileUtilInterface)
    {
        this.fileUtilInterface = fileUtilInterface;
    }
    
    public void setPlatformUtilInterface(PlatformUtilInterface platformUtilInterface)
    {
        this.platformUtilInterface = platformUtilInterface;
    }
}
