package eu.rohn.typescript.json.gson;

import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rohn.typescript.json.JSONArray;
import eu.rohn.typescript.json.JSONObject;

public class GsonArray implements JSONArray
{
    
    private JsonArray m_gsonArray;
    
    public GsonArray(JsonArray jsonArray)
    {
        m_gsonArray = jsonArray;
    }
    
    public GsonArray()
    {
        this(new JsonArray());
    }
    
    protected JsonArray getJsonArray()
    {
        return m_gsonArray;
    }

    @Override
    public Iterator<Object> iterator()
    {
        Iterator<JsonElement> gsonIterator = m_gsonArray.iterator();
        return new GsonIterator(gsonIterator);
    }

    @Override
    public int size()
    {
        return m_gsonArray.size();
    }

    @Override
    public JSONObject getJSONObject(int index)
    {
        JsonObject object = m_gsonArray.get(index).getAsJsonObject();
        return new GsonObject(object);
    }

    @Override
    public void add(JSONObject object)
    {
        GsonObject gsonObject = (GsonObject) object;
        m_gsonArray.add(gsonObject.getGsonObject());
    }

    @Override
    public void add(String value)
    {
        m_gsonArray.add(value);
    }

}
