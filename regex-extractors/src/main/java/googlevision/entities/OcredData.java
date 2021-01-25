package googlevision.entities;

import entity.VisionLineBlock;

import java.util.List;


public class OcredData
{

    private List<VisionLineBlock> lines;
    private String text;
    private List<String> strLines;


    public List<String> getStrLines()
    {
        return strLines;
    }


    public void setStrLines( List<String> strLines )
    {
        this.strLines = strLines;
    }


    public List<VisionLineBlock> getLines()
    {
        return lines;
    }


    public void setLines( List<VisionLineBlock> lines )
    {
        this.lines = lines;
    }


    public String getText()
    {
        return text;
    }


    public void setText( String text )
    {
        this.text = text;
    }
}
