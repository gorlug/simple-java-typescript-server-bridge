package eu.rohn.typescript.util;

import java.io.File;
import java.io.IOException;

public interface FileUtilInterface
{

    String readFileIntoString(File file) throws IOException;

    void writeStringintoFile(File file, String input) throws IOException;

    void copyFile(File from, File to) throws IOException;
}
