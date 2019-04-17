package eu.rohn.typescript;

import java.util.LinkedList;
import java.util.List;

public class TypeScriptFunction extends TypeScriptParameter
{
    
    private String m_parameters;
    
    private List<String> m_parameterList = new LinkedList<>();

    public TypeScriptFunction(String clazz, String name, String parameters)
    {
        super(clazz, name);
        m_parameters = parameters;
    }

    public TypeScriptFunction(String clazz, String name)
    {
        super(clazz, name);
    }
    
    public void addParameter(TypeScriptParameter parameter)
    {
        m_parameterList.add(parameter.getName());
    }
    
    @Override
    public String toString()
    {
        if(m_parameters == null)
        {
            buildParameters();
        }
        String value = super.toString().replace(";", "");
        return value + "(" + m_parameters + ");";
    }

    private void buildParameters()
    {
        m_parameters = "";
        if(m_parameterList.size() == 0)
        {
            return;
        }
        m_parameterList.forEach(parameter -> m_parameters += parameter + ", ");
        m_parameters = m_parameters.substring(0, m_parameters.length() - 2);
    }
}
