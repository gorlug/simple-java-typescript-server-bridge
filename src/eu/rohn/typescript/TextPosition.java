package eu.rohn.typescript;

public class TextPosition
{

    private int m_line, m_offset;
    
    public TextPosition(int line, int offset)
    {
        m_line = line;
        m_offset = offset;
    }
    
    public int getLine()
    {
        return m_line;
    }
    
    public int getOffset()
    {
        return m_offset;
    }
    
    public void setLine(int line)
    {
        m_line = line;
    }
    
    public void setOffset(int offset)
    {
        m_offset = offset;
    }
    
    @Override
    public String toString()
    {
        return m_line + ";" + m_offset;
    }
}
