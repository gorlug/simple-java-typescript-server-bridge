package eu.rohn.typescript;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.rohn.typescript.JSPrototypeClassToTypeScriptDeclaration;
import eu.rohn.typescript.TypeScriptClass;
import eu.rohn.typescript.TypeScriptFunction;
import eu.rohn.typescript.TypeScriptParameter;
import eu.rohn.typescript.factory.FactoryConfig;
import eu.rohn.typescript.factory.FactoryInit;
import eu.rohn.typescript.logging.log4j.Log4jFactory;
import eu.rohn.typescript.util.FileUtil;
import eu.rohn.typescript.util.apache.commons.io.ApacheCommonsIOImpl;

public class JSPrototypeClassToTypeScriptDeclarationTest
{
    
    @BeforeClass
    public static void initFactories()
    {
        FactoryConfig config = new FactoryConfig();
        config.fileUtilInterface = new ApacheCommonsIOImpl();
        config.loggingFactory = new Log4jFactory();
        config.loggingFactory.setOutputToDebug();
        FactoryInit.init(config);
    }
    
    @Test
    public void testDetectEmptyClassCunstructor()
    {
        String text = "function Custom()\n" + 
            "{\n" + 
            "    \n" + 
            "};";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptFunction> constructors = converter.findConstructors(text);
        assertEquals(1, constructors.size());
        TypeScriptFunction constructor = constructors.get(0);
        assertEquals("Custom", constructor.getClassName());
        assertEquals("constructor();", constructor.toString());
    }

    @Test
    public void testDetectClassCunstructor()
    {
        String text = "function Custom(a, b, c)\n" + 
            "{\n" + 
            "    \n" + 
            "};";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptFunction> constructors = converter.findConstructors(text);
        assertEquals(1, constructors.size());
        TypeScriptFunction constructor = constructors.get(0);
        assertEquals("Custom", constructor.getClassName());
        assertEquals("constructor(a, b, c);", constructor.toString());
    }

    @Test
    public void testDetectPrototypeFunction()
    {
        String text = "Custom.validateStaffId = function(staff_id)\n" + 
            "{\n" + 
            "    staff_id = jutil.trim(staff_id);\n" + 
            "    return staff_id;\n" + 
            "}\n" + 
            "\n" + 
            "Custom.prototype.initStaff = function(staff_id)\n" + 
            "{\n" + 
            "    var st = new Staff(staff_id, this.tcon);\n" + 
            "    return st;\n" + 
            "}";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptFunction> functions = converter.findPrototypeFunctions(text);
        assertEquals(1, functions.size());
        TypeScriptFunction function = functions.get(0);
        assertEquals("Custom", function.getClassName());
        assertEquals("initStaff(staff_id);", function.toString());
    }

    @Test
    public void testDetectStaticParameter()
    {
        String text = "Custom.prototype.DEBUG = false; \n" + 
            "Custom.STATIC_DEBUG = false;\n" + 
            "\n" + 
            "Custom.validateStaffId = function(staff_id)\n" + 
            "{\n" + 
            "    return staff_id;\n" + 
            "}";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptParameter> parameters = converter.findStaticParameters(text);
        assertEquals(1, parameters.size());
        TypeScriptParameter parameter = parameters.get(0);
        assertEquals("Custom", parameter.getClassName());
        assertEquals("static STATIC_DEBUG;", parameter.toString());
    }

    @Test
    public void testDetectParameter()
    {
        String text = "Custom.prototype.DEBUG = false; \n" + 
            "Custom.STATIC_DEBUG = false;\n" + 
            "\n" + 
            "Custom.validateStaffId = function(staff_id)\n" + 
            "{\n" + 
            "    return staff_id;\n" + 
            "}";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptParameter> parameters = converter.findParameters(text);
        assertEquals(1, parameters.size());
        TypeScriptParameter parameter = parameters.get(0);
        assertEquals("Custom", parameter.getClassName());
        assertEquals("DEBUG;", parameter.toString());
    }
    
    @Test
    public void testDetectStaticFunction()
    {
        String text = "Custom.validateStaffId = function(staff_id)\n" + 
            "{\n" + 
            "    staff_id = jutil.trim(staff_id);\n" + 
            "    return staff_id;\n" + 
            "}\n" + 
            "\n" + 
            "Custom.prototype.initStaff = function(staff_id)\n" + 
            "{\n" + 
            "    var st = new Staff(staff_id, this.tcon);\n" + 
            "    return st;\n" + 
            "}";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptFunction> functions = converter.findStaticFunctions(text);
        assertEquals(1, functions.size());
        TypeScriptFunction function = functions.get(0);
        assertEquals("Custom", function.getClassName());
        assertEquals("static validateStaffId(staff_id);", function.toString());
    }

    @Test
    public void testDetectTypeScriptClass() throws IOException
    {
        File custom = new File("test/ts/example/Logic.Custom.txt");
        String text = FileUtil.readFileIntoString(custom);
        //System.out.println(text);
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        Collection<TypeScriptClass> classes = converter.convert(text);
        assertEquals(1, classes.size());
        classes.forEach(clazz -> { 
            assertEquals("declare class Custom {\r\n" + 
                "constructor();\r\n" + 
                "initStaff(staff_id);\r\n" + 
                "initWorkplace(workplace_id);\r\n" + 
                "static validateStaffId(staff_id);\r\n" + 
                "DEBUG;\r\n" + 
                "static VERSION;\r\n" + 
                "static STATIC_DEBUG;\r\n" + 
                "}\r\n", clazz.toString());
        });
    }

    @Test
    public void testDetectClassCunstructorWithFunctionsTrapLaterOn()
    {
        String text = "function SVGObject(src)\n" + 
            "{\n" + 
            "    this.functions = \"\";\n" + 
            "    this.script = \"\";\n" + 
            "    this.src = src;\n" + 
            "    this.function_references = [];\n" + 
            "}\n" + 
            "\n" + 
            "SVGObject.prototype.addFunction = function(name, reference)\n" + 
            "{\n" + 
            "    for(var i = 0; i < this.function_references.length; i++)\n" + 
            "    {\n" + 
            "        if(reference == this.function_references[i])\n" + 
            "            return;\n" + 
            "    }\n" + 
            "    this.function_references.push(reference);\n" + 
            "    this.functions += name + \" = \" + reference + \"\\r\\n\";\n" + 
            "}\n" + 
            "";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptFunction> constructors = converter.findConstructors(text);
        System.out.println(constructors);
        assertEquals(1, constructors.size());
        TypeScriptFunction constructor = constructors.get(0);
        assertEquals("SVGObject", constructor.getClassName());
        assertEquals("constructor(src);", constructor.toString());
    }

    @Test
    public void testDoNotDetectClassCunstructorWithFunctionsThrownIn()
    {
        String text = "SomeTransactional.prototype.callTrans = function(fname, id1, id2, id3, id4, id5)\n" + 
            "{\n" + 
            "    if (typeof fname == \"undefined\")\n" + 
            "        throw new \"SomeTransactional.callTransaction: first parameter must be name of defined function\";\n" + 
            "\n" + 
            "    var callName = \"SomeTransactional.callTransaction(\"+fname+\")\";\n" + 
            "    this.tcon = jdb.exclusiveCon(\"somecom\", callName, false);\n" + 
            "    var fobj = this[fname];\n" + 
            "\n" + 
            "    if (typeof fobj != \"function\")\n" + 
            "        throw new callName + \": function with name '\"\n" + 
            "                 + fname + \"' was not defined or is not a function \";\n" + 
            "\n" + 
            "        this.tcon.commit();\n" + 
            "        var retval = fobj.call(this, id1, id2, id3, id4, id5);\n" + 
            "        this.tcon.commit();\n" + 
            "\n" + 
            "}";
        JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
        List<TypeScriptFunction> constructors = converter.findConstructors(text);
        assertEquals(0, constructors.size());
    }
}
