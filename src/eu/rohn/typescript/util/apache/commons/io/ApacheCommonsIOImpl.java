package eu.rohn.typescript.util.apache.commons.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import eu.rohn.typescript.util.FileUtilInterface;

public class ApacheCommonsIOImpl implements FileUtilInterface
{

    @Override
    public String readFileIntoString(File file) throws IOException
    {
        return FileUtils.readFileToString(file, "UTF-8");
    }

    @Override
    public void writeStringintoFile(File file, String input) throws IOException
    {
        FileUtils.writeStringToFile(file, input, "UTF-8");
    }

    @Override
    public void copyFile(File from, File to) throws IOException
    {
        FileUtils.copyFile(from, to);
    }

}
