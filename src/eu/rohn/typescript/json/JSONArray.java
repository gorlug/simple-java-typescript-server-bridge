package eu.rohn.typescript.json;

public interface JSONArray extends Iterable<Object>
{

    int size();
    
    JSONObject getJSONObject(int index);
    
    void add(JSONObject object);
    
    void add(String value);
}
