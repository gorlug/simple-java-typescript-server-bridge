package eu.rohn.typescript;

import java.util.LinkedList;

public class TypeScriptClass
{

    private LinkedList<TypeScriptParameter> m_parameters = new LinkedList<>();
    
    private String m_name;
    
    public TypeScriptClass(String name)
    {
        m_name = name;
    }
    
    public void addParameter(TypeScriptParameter parameter)
    {
        m_parameters.add(parameter);
    }
    
    public String getName()
    {
        return m_name;
    }
    
    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append("declare class ");
        output.append(m_name);
        output.append(" {\r\n");
        m_parameters.forEach(parameter -> output.append(parameter.toString() + "\r\n"));
        output.append("}\r\n");
        return output.toString();
    }
}
