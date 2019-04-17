package eu.rohn.typescript;

import eu.rohn.typescript.json.JSONObject;

public class TypeScriptError
{

    private String m_text;
    private int m_code;
    private String m_category;
    
    private TextPosition m_start, m_end;
    
    public TypeScriptError(String text, int code, String category, TextPosition start, TextPosition end)
    {
        m_text = text;
        m_code = code;
        m_category = category;
        m_start = start;
        m_end = end;
    }
    
    public TypeScriptError(JSONObject error)
    {
        m_start = createTextPosition("start", error);
        m_end = createTextPosition("end", error);
        m_text = error.getString("text");
        m_code = error.getInt("code");
        m_category = error.getString("category");
    }

    private TextPosition createTextPosition(String type, JSONObject error)
    {
        JSONObject startObject = error.getJSONObject(type);
        return new TextPosition(startObject.getInt("line"), startObject.getInt("offset"));
    }

    public String getText()
    {
        return m_text;
    }
    
    public int getCode()
    {
        return m_code;
    }
    
    public String getCategory()
    {
        return m_category;
    }
    
    public TextPosition getStart()
    {
        return m_start;
    }
    
    public TextPosition getEnd()
    {
        return m_end;
    }
    
    @Override
    public String toString()
    {
        return m_start + " - " + m_end + " " + m_text;
    }
}
