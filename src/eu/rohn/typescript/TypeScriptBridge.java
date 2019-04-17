package eu.rohn.typescript;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import eu.rohn.typescript.json.JSONException;
import eu.rohn.typescript.json.JSONManager;
import eu.rohn.typescript.json.JSONObject;
import eu.rohn.typescript.logging.Logger;
import eu.rohn.typescript.logging.LoggingManager;


public class TypeScriptBridge extends Thread
{
    
    private Logger c_log = LoggingManager.getLogger(TypeScriptBridge.class);
    
    private String m_cmd;
    
    private OutputStream m_out;
    
    private InputStream m_stdout;
    
    private Process m_process;
    
    private HashMap<Integer, CompletableFuture<JSONObject>> m_listeners = new HashMap<>();

    public TypeScriptBridge(String nodejsPath, String tsserverPath)
    {
        m_cmd = nodejsPath + " " + tsserverPath;
        m_cmd = parsePath(m_cmd);
    }
    
    private String parsePath(String path)
    {
        return path.replace("\\", "/");
    }
    

    public synchronized CompletableFuture<JSONObject> execute(String input, int seq)
    {
        CompletableFuture<JSONObject> future = new CompletableFuture<JSONObject>();
        m_listeners.put(seq, future);
        try
        {
            m_out.write(input.getBytes());
            m_out.flush();
        }
        catch (IOException e)
        {
            future.completeExceptionally(e);
        }
        return future;
    }
    
    public void shutdown() throws IOException
    {
        c_log.info("performing shutdown");
        if(m_out != null)
        {
            m_out.close();
        }
    }
    
    @Override
    public void run()
    {
        c_log.info("running cmd " + m_cmd);
        ProcessBuilder builder = new ProcessBuilder(m_cmd.split(" "));
        Process process;
        try
        {
            c_log.info("starting process");
            process = builder.start();
            m_out = process.getOutputStream();
            m_stdout = process.getInputStream();
            try
            {
                int value;
                StringBuilder line = new StringBuilder();
                while((value = m_stdout.read()) != -1)
                {
                    char ch = (char) value;
                    line.append(ch);
                    if(ch == '\n')
                    {
                        handleListeners(line.toString());
                        line = new StringBuilder();
                    }
                }
                c_log.debug("doing wait for");
                process.waitFor();
            }
            catch (InterruptedException e)
            {
                throw new IOException(e.getMessage(), e);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            c_log.warn(e.getMessage(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            c_log.warn(e.getMessage(), e);
        }
    }

    private void handleListeners(String line)
    {
        try
        {
            JSONObject object = JSONManager.fromObject(line);
            int seq = getRequestSeqFromBody(object);
            if(!object.containsKey("request_seq") && seq == -1)
            {
                return;
            }
            if(seq == -1)
            {
                seq = object.getInt("request_seq");
            }
            if(m_listeners.containsKey(seq))
            {
                m_listeners.get(seq).complete(object);
                m_listeners.remove(seq);
            }
        }
        catch(JSONException e)
        {
            // ignore
        }
    }

    private int getRequestSeqFromBody(JSONObject object)
    {
        if(!object.containsKey("body"))
        {
            return -1;
        }
        if(!object.isJSONObject("body"))
        {
            return -1;
        }
        JSONObject body = object.getJSONObject("body");
        if(!body.containsKey("request_seq"))
        {
            return -1;
        }
        return body.getInt("request_seq");
    }
}
