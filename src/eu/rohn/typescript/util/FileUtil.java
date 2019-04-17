package eu.rohn.typescript.util;

import java.io.File;
import java.io.IOException;

public class FileUtil
{
    
    private static FileUtilInterface c_fileUtilInterface;
    
    public static void setFileUtilInterface(FileUtilInterface fileUtilinterface)
    {
        c_fileUtilInterface = fileUtilinterface;
    }

    public static String readFileIntoString(File file) throws IOException
    {
        return c_fileUtilInterface.readFileIntoString(file);
    }
    
    public static void writeStringIntoFile(File file, String input) throws IOException
    {
        c_fileUtilInterface.writeStringintoFile(file, input);
    }

    public static void copyFile(File from, File to) throws IOException
    {
        c_fileUtilInterface.copyFile(from, to);
    }
}
