package eu.rohn.typescript.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eu.rohn.typescript.json.JSONArray;
import eu.rohn.typescript.json.JSONObject;

public class GsonObject implements JSONObject
{
    
    private JsonObject m_gsonObject;
    
    public GsonObject(JsonObject jsonObject)
    {
        m_gsonObject = jsonObject;
    }
    
    public GsonObject()
    {
        this(new JsonObject());
    }
    
    protected JsonObject getGsonObject()
    {
        return m_gsonObject;
    }

    @Override
    public void put(String key, int value)
    {
        m_gsonObject.addProperty(key, value);
    }

    @Override
    public void put(String key, String value)
    {
        m_gsonObject.addProperty(key, value);
    }

    @Override
    public void put(String key, JSONObject object)
    {
        GsonObject gsonObject = (GsonObject) object;
        m_gsonObject.add(key, gsonObject.getGsonObject());
    }

    @Override
    public void put(String key, JSONArray array)
    {
        GsonArray gsonArray = (GsonArray) array;
        m_gsonObject.add(key, gsonArray.getJsonArray());
    }

    @Override
    public JSONArray getJSONArray(String key)
    {
        JsonArray jsonArray = m_gsonObject.get(key).getAsJsonArray();
        return new GsonArray(jsonArray);
    }

    @Override
    public String getString(String key)
    {
        return m_gsonObject.get(key).getAsString();
    }

    @Override
    public int getInt(String key)
    {
        return m_gsonObject.get(key).getAsInt();
    }

    @Override
    public JSONObject getJSONObject(String key)
    {
        JsonObject object = m_gsonObject.get(key).getAsJsonObject();
        return new GsonObject(object);
    }

    @Override
    public boolean containsKey(String key)
    {
        return m_gsonObject.has(key);
    }

    @Override
    public String toJSONString()
    {
        return new Gson().toJson(m_gsonObject);
    }
    
    @Override
    public String toString()
    {
        return toJSONString();
    }

    @Override
    public boolean isJSONObject(String key)
    {
        return m_gsonObject.get(key).isJsonObject();
    }

    @Override
    public boolean isJSONArray(String key)
    {
        return m_gsonObject.get(key).isJsonArray();
    }

}
