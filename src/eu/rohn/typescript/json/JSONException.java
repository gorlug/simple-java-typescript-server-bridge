package eu.rohn.typescript.json;

public class JSONException extends Exception
{

    public JSONException(String message)
    {
        super(message);
    }

    public JSONException(String message, Throwable original)
    {
        super(message, original);
    }
}
