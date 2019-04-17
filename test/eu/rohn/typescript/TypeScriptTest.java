package eu.rohn.typescript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.rohn.typescript.factory.FactoryConfig;
import eu.rohn.typescript.factory.FactoryInit;
import eu.rohn.typescript.util.FileUtil;

public class TypeScriptTest
{
    private static TypeScriptBridge c_bridge;

    private static final long WAIT_TIME = 10;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        FactoryConfig config = FactoryInit.getDefaultConfig();
        config.loggingFactory.setOutputToDebug();
        FactoryInit.init(config);
        String nodejsPath = new File("etc/node").getAbsolutePath();
        String tsserverPath = new File("etc/node_modules/typescript/bin/tsserver").getAbsolutePath();
        c_bridge = new TypeScriptBridge(nodejsPath, tsserverPath);
        c_bridge.start();
        Thread.sleep(1000);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException
    {
        c_bridge.shutdown();
    }
    
    @Test
    public void testGetCompletionsOfLargeSourceQuicklyAfterChange() throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        TypeScript typeScript = new TypeScript(c_bridge);

        File file = new File("test/ts/example/UsesAce.ts");
        FileUtil.writeStringIntoFile(file, "/// <reference path=\"../ace.ts\" />\n" + 
            "const editor: AceAjax.Editor = ace.edit(\"editor\");\n" + 
            "const someClass = new AceAjax.SomeClass();\n" + 
            "someClass.");
        typeScript.openFile(file);
        List<String> completions = typeScript.getCompletions(file, 4, 11).get(WAIT_TIME, TimeUnit.SECONDS);
        assertEquals(4, completions.size());

        FileUtil.writeStringIntoFile(file, "/// <reference path=\"../ace.ts\" />\n" + 
            "const editor: AceAjax.Editor = ace.edit(\"editor\");\n" + 
            "const someClass = new AceAjax.SomeClass();\n" + 
            "someClass.doSomething();\n" + 
            "someClass.");
        typeScript.openFile(file);
        completions = typeScript.getCompletions(file, 5, 11).get(WAIT_TIME, TimeUnit.SECONDS);
        assertEquals(4, completions.size());

        FileUtil.writeStringIntoFile(file, "/// <reference path=\"../ace.ts\" />\n" + 
            "const editor: AceAjax.Editor = ace.edit(\"editor\");\n" + 
            "const someClass = new AceAjax.SomeClass();\n" + 
            "someClass.doSomething();\n" + 
            "someClass.doSomethingElse();\n" + 
            "someClass.");
        typeScript.openFile(file);
        completions = typeScript.getCompletions(file, 6, 11).get(WAIT_TIME, TimeUnit.SECONDS);
        assertEquals(4, completions.size());
    }

    @Test
    public void getCompletions() throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        File example = new File("test/ts/example/logger.example.ts");
        int line = 3;
        int offset = 8;
        TypeScript typeScript = new TypeScript(c_bridge);
        typeScript.openFile(example);
        List<String> completions = typeScript.getCompletions(example, line, offset).get(WAIT_TIME, TimeUnit.SECONDS);
        assertTrue(completions.contains("info"));
        assertTrue(completions.contains("error"));
        assertTrue(completions.contains("warning"));
    }

    @Test
    public void getSignatureHelp() throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        File example = new File("test/ts/example/logger.signature.example.ts");
        int line = 3;
        int offset = 16;
        TypeScript typeScript = new TypeScript(c_bridge);
        typeScript.openFile(example);
        List<String> parameters = typeScript.getParameters(example, line, offset).get(WAIT_TIME, TimeUnit.SECONDS);
        assertEquals("text: string", parameters.get(0));
        assertEquals("index: number", parameters.get(1));
    }

    @Test
    public void getSignatureHelpWithPrototypeClass() throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        File example = new File("test/ts/example/prototype.signature.example.ts");
        int line = 19;
        int offset = 18;
        TypeScript typeScript = new TypeScript(c_bridge);
        typeScript.openFile(example);
        List<String> parameters = typeScript.getParameters(example, line, offset).get(WAIT_TIME, TimeUnit.SECONDS);
        assertEquals(3, parameters.size());
        assertEquals("a", parameters.get(0));
        assertEquals("b", parameters.get(1));
        assertEquals("c", parameters.get(2));
    }

    @Test
    public void getErrors() throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        File example = new File("test/ts/example/error.example.ts");
        TypeScript typeScript = new TypeScript(c_bridge);
        typeScript.openFile(example);
        List<TypeScriptError> errors = typeScript.getErrors(example).get(WAIT_TIME, TimeUnit.SECONDS);
        assertEquals(3, errors.size());
        TypeScriptError error = errors.get(0);
        assertEquals(20, error.getStart().getLine());
        assertEquals(5, error.getStart().getOffset());
        assertEquals(20, error.getEnd().getLine());
        assertEquals(10, error.getEnd().getOffset());
        assertEquals("Property 'close' does not exist on type 'Connection'.", error.getText());
        assertEquals(2339, error.getCode());
        assertEquals("error", error.getCategory());
    }

    @Test
    public void getErrorsFromHugeFile() throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        File example = new File("test/ts/ace.ts");
        TypeScript typeScript = new TypeScript(c_bridge);
        typeScript.openFile(example);
        List<TypeScriptError> errors = typeScript.getErrors(example).get(WAIT_TIME, TimeUnit.SECONDS);
        assertTrue(errors.size() > 0);
    }

}
