package eu.rohn.typescript;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.rohn.typescript.logging.Logger;
import eu.rohn.typescript.logging.LoggingManager;

public class JSPrototypeClassToTypeScriptDeclaration
{
    
    private static Logger c_log = LoggingManager.getLogger(JSPrototypeClassToTypeScriptDeclaration.class);

    private static final String c_parameterRegex = "\\(([^\\)]*)\\)";
    
    private static final Pattern c_classPattern = Pattern.compile("^.*function\\s+([^\\(\\s]+)\\s*" + c_parameterRegex + ".*$", Pattern.MULTILINE);
    
    private static final String c_classFunctionBegin = "([\\w]+)\\.";
    
    private static final String c_declarationEnd = "([^\\s\\.]+)\\s*=";
    
    private static final String c_classFunctionEnd = c_declarationEnd + "\\s*function\\s*" + c_parameterRegex;
    
    private static final Pattern c_prototypeFunction = Pattern.compile(c_classFunctionBegin + "prototype\\." + c_classFunctionEnd);

    private static final Pattern c_staticFunction = Pattern.compile(c_classFunctionBegin + c_classFunctionEnd);
    
    private static final Pattern c_staticParameter = Pattern.compile(c_classFunctionBegin + c_declarationEnd + "(?!\\s*function)");

    private static final Pattern c_Parameter = Pattern.compile(c_classFunctionBegin + "prototype\\." + c_declarationEnd + "(?!\\s*function)");

    public Collection<TypeScriptClass> convert(String text)
    {
        c_log.debug("running conversion");
        HashMap<String, TypeScriptClass> classes = new HashMap<>();
        List<TypeScriptFunction> constructors = findConstructors(text);
        constructors.forEach(constructor -> {
            TypeScriptClass clazz = new TypeScriptClass(constructor.getClassName());
            clazz.addParameter(constructor);
            classes.put(clazz.getName(), clazz);
        });
        addParametersToClass(findPrototypeFunctions(text), classes);
        addParametersToClass(findStaticFunctions(text), classes);
        addParametersToClass(findParameters(text), classes);
        addParametersToClass(findStaticParameters(text), classes);
        return classes.values();
    }
    
    private void addParametersToClass(List<? extends TypeScriptParameter> parameters, HashMap<String, TypeScriptClass> classes)
    {
        c_log.debug("adding these parameters to class " + parameters);
        parameters.forEach(parameter -> {
            if(classes.containsKey(parameter.getClassName()))
            {
                classes.get(parameter.getClassName()).addParameter(parameter);
            }
        });
    }

    public List<TypeScriptFunction> findConstructors(String text)
    {
        List<TypeScriptFunction> constructors = new ArrayList<>();
        Matcher matcher = c_classPattern.matcher(text);
        while(matcher.find())
        {
            String clazz = matcher.group(1);
            String parameters = matcher.group(2);
            constructors.add(new TypeScriptFunction(clazz, "constructor", parameters));
        }
        return constructors;
    }

    public List<TypeScriptFunction> findPrototypeFunctions(String text)
    {
        return findFunctions(text, c_prototypeFunction);
    }

    private List<TypeScriptFunction> findFunctions(String text, Pattern pattern)
    {
        List<TypeScriptFunction> functions = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while(matcher.find())
        {
            String clazz = matcher.group(1);
            if(clazz.equals("prototype"))
            {
                continue;
            }
            String name = matcher.group(2);
            String parameters = matcher.group(3);
            functions.add(new TypeScriptFunction(clazz, name, parameters));
        }
        return functions;
    }

    public List<TypeScriptFunction> findStaticFunctions(String text)
    {
        List<TypeScriptFunction> functions = findFunctions(text, c_staticFunction);
        functions.forEach((function) -> function.setStatic());
        return functions;
    }

    public List<TypeScriptParameter> findStaticParameters(String text)
    {
        List<TypeScriptParameter> parameters = findParameters(c_staticParameter, text);
        parameters.forEach((parameter) -> parameter.setStatic());
        return parameters;
    }

    private List<TypeScriptParameter> findParameters(Pattern pattern, String text)
    {
        List<TypeScriptParameter> parameters = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while(matcher.find())
        {
            String clazz = matcher.group(1);
            if(clazz.equals("prototype"))
            {
                continue;
            }
            String name = matcher.group(2);
            parameters.add(new TypeScriptParameter(clazz, name));
        }
        return parameters;
    }

    public List<TypeScriptParameter> findParameters(String text)
    {
        return findParameters(c_Parameter, text);
    }
    

}
