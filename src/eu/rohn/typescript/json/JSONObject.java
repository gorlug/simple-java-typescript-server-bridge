package eu.rohn.typescript.json;

public interface JSONObject
{
    void put(String key, int value);
    
    void put(String key, String value);
    
    void put(String key, JSONObject object);
    
    void put(String key, JSONArray array);
    
    JSONArray getJSONArray(String key);
    
    String getString(String key);
    
    int getInt(String key);
    
    JSONObject getJSONObject(String key);
    
    boolean containsKey(String key);
    
    public String toJSONString();
    
    public boolean isJSONObject(String key);

    public boolean isJSONArray(String key);
}
