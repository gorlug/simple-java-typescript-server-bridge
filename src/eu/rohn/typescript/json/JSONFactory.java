package eu.rohn.typescript.json;

public interface JSONFactory
{

    JSONObject createJSONObject();
    
    JSONArray createJSONArray();

    JSONObject fromObject(Object input) throws JSONException;
}
