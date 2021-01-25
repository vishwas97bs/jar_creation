package entity;

public class Coordinates
{

    int startX;
    int endX;
    int startY;
    int endY;


    public int getStartX()
    {
        return startX;
    }


    public void setStartX( int xLeft )
    {
        this.startX = xLeft;
    }


    public int getEndX()
    {
        return endX;
    }


    public void setEndX( int xRight )
    {
        this.endX = xRight;
    }


    public int getStartY()
    {
        return startY;
    }


    public void setStartY( int yTop )
    {
        this.startY = yTop;
    }


    public int getEndY()
    {
        return endY;
    }


    public void setEndY( int yBottom )
    {
        this.endY = yBottom;
    }


}
