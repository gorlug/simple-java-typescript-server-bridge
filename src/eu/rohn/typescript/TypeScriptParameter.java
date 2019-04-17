package eu.rohn.typescript;

public class TypeScriptParameter implements Comparable<TypeScriptParameter>
{

    protected String m_clazz, m_name;
    protected boolean m_static;

    public TypeScriptParameter(String clazz, String name)
    {
        m_clazz = clazz;
        m_name = name;
    }
    
    public String getClassName()
    {
        return m_clazz;
    }
    
    public void setStatic()
    {
        m_static = true;
    }
    
    public boolean isStatic()
    {
        return m_static;
    }
    
    public String getName()
    {
        return m_name;
    }
    
    @Override
    public String toString()
    {
        String value = "";
        if(isStatic())
        {
            value = "static ";
        }
        return value + m_name +";";
    }

    @Override
    public int compareTo(TypeScriptParameter o)
    {
        return this.m_name.compareTo(o.getName());
    }
}
