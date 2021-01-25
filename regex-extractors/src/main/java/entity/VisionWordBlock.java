package entity;

import java.io.Serializable;
import java.util.Set;


public class VisionWordBlock implements Serializable
{

    private static final double MIN_VERTICAL_OPERLAP = .70;
    private String description;
    private Vertex topLeft;
    private Vertex topRight;
    private Vertex bottomRight;
    private Vertex bottomLeft;
    private int verticalDifference = Integer.MIN_VALUE;
    private int verticalMidpoint = Integer.MIN_VALUE;
    private int verticalStartingPoint = Integer.MIN_VALUE;
    private int verticalEndingPoint = Integer.MIN_VALUE;
    private int horizontalStartingPoint = Integer.MIN_VALUE;
    private int horizontalEndingPoint = Integer.MIN_VALUE;
    private int isWordTilted = -1;


    public VisionWordBlock()
    {

    }


    public VisionWordBlock( VisionLineBlock line )
    {
        this.setTopLeft( line.getStartTopVertex() );
        this.setTopRight( line.getEndTopVertex() );
        this.setBottomLeft( line.getStartBottomVertex() );
        this.setBottomRight( line.getEndBottomVertex() );
        this.setDescription( line.toString() );
    }


    public boolean isWordTilted()
    {
        if ( isWordTilted == -1 ) {
            Set<Integer> uniqueCoordinates = new java.util.HashSet<>();
            uniqueCoordinates.add( topLeft.getX() );
            uniqueCoordinates.add( topRight.getX() );
            uniqueCoordinates.add( bottomRight.getX() );
            uniqueCoordinates.add( bottomLeft.getX() );
            uniqueCoordinates.add( topLeft.getY() );
            uniqueCoordinates.add( topRight.getY() );
            uniqueCoordinates.add( bottomRight.getY() );
            uniqueCoordinates.add( bottomLeft.getY() );

            isWordTilted = uniqueCoordinates.size() == 4 ? 0 : 1;
        }
        return isWordTilted == 1;
    }


    private void setOtherDetails()
    {
        verticalEndingPoint = bottomLeft.getY() > bottomRight.getY() ? bottomLeft.getY() : bottomRight.getY();
        verticalStartingPoint = topLeft.getY() < topRight.getY() ? topLeft.getY() : topRight.getY();
        verticalDifference = verticalEndingPoint - verticalStartingPoint;
        verticalMidpoint = verticalDifference / 2 + verticalStartingPoint;

        horizontalStartingPoint = topLeft.getX() < bottomLeft.getX() ? topLeft.getX() : bottomLeft.getX();
        horizontalEndingPoint = topRight.getX() > bottomRight.getX() ? topRight.getX() : bottomRight.getX();
    }


    public int getVerticalStartingPoint()
    {
        if ( verticalStartingPoint == Integer.MIN_VALUE ) {
            setOtherDetails();
        }
        return verticalStartingPoint;
    }


    public int getVerticalEndingPoint()
    {
        if ( verticalEndingPoint == Integer.MIN_VALUE ) {
            setOtherDetails();
        }
        return verticalEndingPoint;
    }


    public int getHorizontalStartingPoint()
    {
        if ( horizontalStartingPoint == Integer.MIN_VALUE ) {
            setOtherDetails();
        }
        return horizontalStartingPoint;
    }


    public boolean checkVerticalOverlap( VisionWordBlock word )
    {
        int overlapStart = -1, overlapEnd = -1;
        if ( this.getVerticalStartingPoint() < word.getVerticalStartingPoint() ) {
            overlapStart = word.getVerticalStartingPoint();
        } else {
            overlapStart = this.getVerticalStartingPoint();
        }
        if ( this.getVerticalEndingPoint() < word.getVerticalEndingPoint() ) {
            overlapEnd = this.getVerticalEndingPoint();
        } else {
            overlapEnd = word.getVerticalEndingPoint();
        }
        if ( overlapStart < overlapEnd ) {
            float currentOverlap = (float) ( overlapEnd - overlapStart ) / this.getVerticalDifference();
            float lineOverlap = (float) ( overlapEnd - overlapStart ) / word.getVerticalDifference();
            float overlapPercent;
            if ( currentOverlap > lineOverlap ) {
                overlapPercent = currentOverlap;
            } else {
                overlapPercent = lineOverlap;
            }
            if ( overlapPercent > MIN_VERTICAL_OPERLAP ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public int getHorizontalEndingPoint()
    {
        if ( horizontalEndingPoint == Integer.MIN_VALUE ) {
            setOtherDetails();
        }
        return horizontalEndingPoint;
    }


    public int getVerticalMidpoint()
    {
        if ( verticalMidpoint == Integer.MIN_VALUE ) {
            setOtherDetails();
        }
        return verticalMidpoint;
    }


    public int getVerticalDifference()
    {
        if ( verticalDifference == Integer.MIN_VALUE ) {
            setOtherDetails();
        }
        return verticalDifference;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription( String description )
    {
        this.description = description;
    }


    public Vertex getTopLeft()
    {
        return topLeft;
    }


    public void setTopLeft( Vertex vertex )
    {
        this.topLeft = vertex;
    }


    public Vertex getTopRight()
    {
        return topRight;
    }


    public void setTopRight( Vertex vertex )
    {
        this.topRight = vertex;
    }


    public Vertex getBottomRight()
    {
        return bottomRight;
    }


    public void setBottomRight( Vertex vertex )
    {
        this.bottomRight = vertex;
    }


    public Vertex getBottomLeft()
    {
        return bottomLeft;
    }


    public void setBottomLeft( Vertex vertex )
    {
        this.bottomLeft = vertex;
    }


    public boolean haveValidBoundingBox()
    {
        boolean response = false;
        if ( this.getTopLeft() != null && this.topRight != null && this.bottomLeft != null && this.bottomRight != null )
            response = true;
        return response;
    }


    @Override
    public String toString()
    {
        return "{\"description\":\"" + this.description + "\",\"topLeft\":" + topLeft + ",\"topRight\":" + topRight
            + ",\"bottomRight\":" + bottomRight + ",\"bottomLeft\":" + bottomLeft + ",\"verticalDifference\":"
            + verticalDifference + ",\"verticalMidpoint\":" + verticalMidpoint + ",\"verticalStartingPoint\":"
            + verticalStartingPoint + ",\"verticalEndingPoint\":" + verticalEndingPoint + ",\"horizontalStartingPoint\":"
            + horizontalStartingPoint + ",\"horizontalEndingPoint\":" + horizontalEndingPoint + ",\"isWordTilted\":"
            + isWordTilted + "}";
    }
}
