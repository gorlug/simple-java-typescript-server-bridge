package eu.rohn.typescript.util.platform;

import java.io.IOException;

import eu.rohn.typescript.util.PlatformUtilInterface;

public class SimpleJavaPlatform implements PlatformUtilInterface
{

    @Override
    public void executeSimpleCommand(String cmd) throws IOException
    {
        ProcessBuilder builder = new ProcessBuilder(cmd.split(" "));
        Process process = builder.start();
        try
        {
            process.waitFor();
        }
        catch (InterruptedException e)
        {
            throw new IOException(e);
        }
    }

}
