package eu.rohn.typescript.util;

import java.io.IOException;

public class PlatformUtil
{
    
    private static PlatformUtilInterface c_platformUtilInterface;
    
    public static void setPlatformUtilInterface(PlatformUtilInterface platformUtilInterface)
    {
        c_platformUtilInterface = platformUtilInterface;
    }

    public static void executeSimpleCommand(String cmd) throws IOException
    {
        c_platformUtilInterface.executeSimpleCommand(cmd);
    }

}
