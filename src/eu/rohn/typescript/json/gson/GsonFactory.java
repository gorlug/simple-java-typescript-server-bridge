package eu.rohn.typescript.json.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import eu.rohn.typescript.json.JSONArray;
import eu.rohn.typescript.json.JSONException;
import eu.rohn.typescript.json.JSONFactory;
import eu.rohn.typescript.json.JSONObject;

public class GsonFactory implements JSONFactory
{

    @Override
    public JSONObject createJSONObject()
    {
        return new GsonObject();
    }

    @Override
    public JSONArray createJSONArray()
    {
        return new GsonArray();
    }

    @Override
    public JSONObject fromObject(Object input) throws JSONException
    {
        try
        {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse((String) input);
            if(jsonElement.isJsonNull())
            {
                throw new JSONException("parsing was null");
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return new GsonObject(jsonObject);
        }
        catch(JsonSyntaxException e)
        {
            throw new JSONException(e.getMessage(), e);
        }
    }

}
