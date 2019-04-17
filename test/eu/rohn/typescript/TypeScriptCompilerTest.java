package eu.rohn.typescript;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.rohn.typescript.factory.FactoryConfig;
import eu.rohn.typescript.factory.FactoryInit;
import eu.rohn.typescript.util.FileUtil;

public class TypeScriptCompilerTest
{

    public static final File TEST_DIR = new File("test/ts/" + TypeScriptCompilerTest.class.getSimpleName());

    public static final File INPUT_DIR = new File(TEST_DIR, "input");
    
    public static final File EXPECTED_DIR = new File(TEST_DIR, "expected");

    public static final File TMP_DIR = new File(TEST_DIR, "tmp");
    
    private static TypeScriptCompiler c_compiler;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        FactoryConfig config = FactoryInit.getDefaultConfig();
        config.loggingFactory.setOutputToDebug();
        FactoryInit.init(config);
        String nodejsPath = new File("etc/node").getAbsolutePath();
        String compilerPath = new File("etc/node_modules/typescript/bin/tsc").getAbsolutePath();
        c_compiler = new TypeScriptCompiler(nodejsPath, compilerPath);
    }
    
    @Before
    public void emptyTmp()
    {
        TMP_DIR.mkdirs();
        File[] tmpFiles = TMP_DIR.listFiles();
        for(File tmpFile : tmpFiles)
        {
            if(!tmpFile.isDirectory())
            {
                tmpFile.delete();
            }
        }
    }
    
    private File prepareInput(String input) throws IOException
    {
        File tmp = new File(TMP_DIR, input);
        FileUtil.copyFile(new File(INPUT_DIR, input), tmp);
        return tmp;
    }

    @Test
    public void test() throws IOException
    {
        File tsFile = prepareInput("car.ts");
        c_compiler.compile(tsFile);
        assertExpectedContents("car.js");
        assertExpectedContents("car.js.map");
    }

    private void assertExpectedContents(String name) throws IOException
    {
        File file = new File(TMP_DIR, name);
        assertFileExists(file);
        File expected = new File(EXPECTED_DIR, file.getName());
        Assert.assertEquals(FileUtil.readFileIntoString(expected), FileUtil.readFileIntoString(file));
    }

    private void assertFileExists(File jsFile)
    {
        Assert.assertTrue("file " + jsFile.getName() + " does not exist", jsFile.exists());
    }

}
