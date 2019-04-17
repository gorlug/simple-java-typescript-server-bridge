package eu.rohn.typescript.json;

public class JSONManager
{

    private static JSONFactory c_factory;
    
    public static void setFactory(JSONFactory factory)
    {
        c_factory = factory;
    }
    
    public static JSONObject createJSONObject()
    {
        return c_factory.createJSONObject();
    }
    
    public static JSONArray createJSONArray()
    {
        return c_factory.createJSONArray();
    }
    
    public static JSONObject fromObject(Object object) throws JSONException
    {
        return c_factory.fromObject(object);
    }
}
