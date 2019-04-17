package eu.rohn.typescript.json.gson;

import java.util.Iterator;

import com.google.gson.JsonElement;

public class GsonIterator implements Iterator<Object>
{
    
    private Iterator<JsonElement> m_gsonIterator;
    
    public GsonIterator(Iterator<JsonElement> jsonIterator)
    {
        m_gsonIterator = jsonIterator;
    }

    @Override
    public boolean hasNext()
    {
        return m_gsonIterator.hasNext();
    }

    @Override
    public Object next()
    {
        JsonElement nextJsonElement = m_gsonIterator.next();
        Object next = nextJsonElement;
        if(nextJsonElement.isJsonObject())
        {
            next = new GsonObject(nextJsonElement.getAsJsonObject());
        }
        else if(nextJsonElement.isJsonArray())
        {
            next = new GsonArray(nextJsonElement.getAsJsonArray());
        }
        return next;
    }

}
