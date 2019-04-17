package eu.rohn.typescript.factory;

import eu.rohn.typescript.json.JSONManager;
import eu.rohn.typescript.json.gson.GsonFactory;
import eu.rohn.typescript.logging.LoggingManager;
import eu.rohn.typescript.logging.log4j.Log4jFactory;
import eu.rohn.typescript.util.FileUtil;
import eu.rohn.typescript.util.PlatformUtil;
import eu.rohn.typescript.util.apache.commons.io.ApacheCommonsIOImpl;
import eu.rohn.typescript.util.platform.SimpleJavaPlatform;

public class FactoryInit
{

    public static void init(FactoryConfig config)
    {
        LoggingManager.setFactory(config.loggingFactory);
        JSONManager.setFactory(config.jsonFactory);
        FileUtil.setFileUtilInterface(config.fileUtilInterface);
        PlatformUtil.setPlatformUtilInterface(config.platformUtilInterface);
    }
    
    public static FactoryConfig getDefaultConfig()
    {
        FactoryConfig config = new FactoryConfig();
        config.setLoggingFactory(new Log4jFactory());
        config.setFileUtilInterface(new ApacheCommonsIOImpl());
        config.setJSONFactory(new GsonFactory());
        config.setPlatformUtilInterface(new SimpleJavaPlatform());
        return config;
    }
}
