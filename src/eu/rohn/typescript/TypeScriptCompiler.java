package eu.rohn.typescript;

import java.io.File;
import java.io.IOException;

import eu.rohn.typescript.util.PlatformUtil;

public class TypeScriptCompiler
{
    
    private String m_cmd;

    public TypeScriptCompiler(String nodejsPath, String compilerPath)
    {
        m_cmd = nodejsPath + " " + compilerPath + " --sourceMap";
    }

    public void compile(File file) throws IOException
    {
        PlatformUtil.executeSimpleCommand(m_cmd + " " + file.getAbsolutePath());
    }

}

