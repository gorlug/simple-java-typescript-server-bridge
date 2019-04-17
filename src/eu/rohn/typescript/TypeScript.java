package eu.rohn.typescript;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import eu.rohn.typescript.json.JSONArray;
import eu.rohn.typescript.json.JSONFactory;
import eu.rohn.typescript.json.JSONManager;
import eu.rohn.typescript.json.JSONObject;

public class TypeScript
{
    private TypeScriptBridge m_bridge;
    
    private int m_seq = 0;
    
    public TypeScript() { }

    public TypeScript(TypeScriptBridge bridge, File dir) throws IOException
    {
        this(bridge);
        openProject(dir);
    }

    public TypeScript(TypeScriptBridge bridge) throws IOException
    {
        m_bridge = bridge;
    }

    public CompletableFuture<List<String>> getCompletions(File file, int line, int offset) throws IOException
    {
        JSONObject arguments = createLineOffsetArguments(line, offset);
        JSONObject completionsSequence = buildSequence("completions", file, arguments);
        return runSequence(completionsSequence).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            JSONArray body = result.getJSONArray("body");
            return completionsBodyToList(body);
        }));
    }

    private JSONObject createLineOffsetArguments(int line, int offset)
    {
        JSONObject arguments = JSONManager.createJSONObject();
        arguments.put("line", line);
        arguments.put("offset", offset);
        return arguments;
    }

    private List<String> completionsBodyToList(JSONArray body)
    {
        List<String> completions = new ArrayList<>();
        for(int index = 0; index < body.size(); index++)
        {
            JSONObject completion = body.getJSONObject(index);
            completions.add(completion.getString("name"));
        }
        return completions;
    }

    private CompletableFuture<JSONObject> runSequence(JSONObject sequence)
    {
        //JSONObject seq0 = buildOpenSequence(file);
        StringBuilder input = new StringBuilder();
        //input.append(seq0.toString());
        //input.append("\n");
        input.append(sequence.toString());
        input.append("\n");
        return m_bridge.execute(input.toString(), sequence.getInt("seq"));
    }

    private File getOutFile(File file)
    {
        return new File(file.getParentFile(), file.getName() + "-out");
    }
    
    public CompletableFuture<Boolean> openFile(File file) throws IOException
    {
        JSONObject openSequence = buildOpenSequence(file);
        return runSequence(openSequence).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            return true;
        }));
    }
    public CompletableFuture<Boolean> openProject(File dir) throws IOException
    {
        JSONObject seq = buildRequestSequence("openExternalProject");
        
        JSONObject arguments = JSONManager.createJSONObject();
        arguments.put("projectFileName", "test");
        
        JSONArray rootFiles = JSONManager.createJSONArray();
        for(File file : dir.listFiles())
        {
            JSONObject externalFile = JSONManager.createJSONObject();
            externalFile.put("fileName", file.getAbsolutePath());
            rootFiles.add(externalFile);
        }
        
        arguments.put("rootFiles", rootFiles);
        JSONObject options = JSONManager.createJSONObject();
        /* options.put("strictFunctionTypes", true);
        options.put("strictPropertyInitialization", true);
        options.put("alwaysStrict", true);*/
        arguments.put("options", options);
        seq.put("arguments", arguments);

        return runSequence(seq).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            return true;
        }));
 
    }
    
    public CompletableFuture<Boolean> reload(File file) throws IOException
    {
        JSONObject arguments = JSONManager.createJSONObject();
        arguments.put("tmpFile", file.getAbsolutePath());
        JSONObject seq = buildSequence("reload", file, arguments);
        return runSequence(seq).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            return true;
        }));
    }

    private JSONObject buildOpenSequence(File file)
    {
        JSONObject arguments = JSONManager.createJSONObject();
        return buildSequence("open", file, arguments);
    }
    
    private JSONObject buildRequestSequence(String command)
    {
        JSONObject seq = JSONManager.createJSONObject();
        seq.put("seq", m_seq++);
        seq.put("type", "request");
        seq.put("command", command);
        return seq;
    }
    
    private JSONObject buildSequence(String command, File file, JSONObject arguments)
    {
        JSONObject seq = buildRequestSequence(command);
        if(file != null)
        {
            arguments.put("file", getPath(file));
        }
        seq.put("arguments", arguments);
        return seq;
    }

    private String getPath(File file)
    {
        return file.getAbsolutePath().replace("\\", "/");
    }

    public CompletableFuture<List<String>> getParameters(File file, int line, int offset) throws IOException
    {
        JSONObject arguments = createLineOffsetArguments(line, offset);
        JSONObject seq1 = buildSequence("signatureHelp", file, arguments);
        return runSequence(seq1).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            return createParameterList(result);
        }));
    }
    
    private List<String> createParameterList(JSONObject result)
    {
        JSONObject body = result.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        JSONObject object = items.getJSONObject(0);
        JSONArray parameters = object.getJSONArray("parameters");
        
        List<String> parameterList = new ArrayList<>();
        
        for(int index = 0; index < parameters.size(); index++)
        {
            JSONObject parameter = parameters.getJSONObject(index);
            JSONArray displayParts = parameter.getJSONArray("displayParts");
            StringBuffer parameterText = new StringBuffer();
            for(int partsIndex = 0; partsIndex < displayParts.size(); partsIndex++)
            {
                JSONObject parts = displayParts.getJSONObject(partsIndex);
                parameterText.append(parts.getString("text"));
            }
            parameterList.add(parameterText.toString());
        }
        parameterList = filterForAny(parameterList);
        return parameterList;
    }

    private List<String> filterForAny(List<String> parameterList)
    {
        List<String> list = new ArrayList<>();
        parameterList.forEach(parameter -> { 
            if(parameter.endsWith(": any"))
            {
                list.add(parameter.substring(0, parameter.indexOf(":"))); 
            }
            else
            {
                list.add(parameter);
            }
        });
        return list;
    }

    public CompletableFuture<List<TypeScriptError>> getErrors(File file) throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        JSONObject arguments = JSONManager.createJSONObject();
        JSONArray files = JSONManager.createJSONArray();
        files.add(getPath(file));
        arguments.put("files", files);
        arguments.put("delay", 200);
        arguments = JSONManager.createJSONObject();
        JSONObject seq = buildSequence("semanticDiagnosticsSync", file, arguments);
        return runSequence(seq).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            return generateErrors(result);
        }));
    }
    
    private List<TypeScriptError> generateErrors(JSONObject result)
    {
        List<TypeScriptError> errors = new ArrayList<>();
        JSONArray body = result.getJSONArray("body");
        for(Object object : body)
        {
            JSONObject error = (JSONObject) object;
            TypeScriptError tsError = new TypeScriptError(error);
            errors.add(tsError);
        }
        return errors;
    }

}
