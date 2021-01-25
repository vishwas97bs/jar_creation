package entity;


import googlevision.util.GoogleVisionTextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class VisionLineBlock implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final double MIN_VERTICAL_OVERLAP = .70;
    private List<VisionWordBlock> words;
    private Vertex startTopVertex;
    private Vertex startBottomVertex;
    private Vertex endTopVertex;
    private Vertex endBottomVertex;
    private static final int HORIZONTAL_THRESHOLD = 2;
    private int pageNo;


    public VisionLineBlock()
    {
    }


    public List<VisionWordBlock> getWords()
    {
        return words;
    }


    public void setWords( List<VisionWordBlock> words )
    {
        this.words = words;
    }


    public VisionLineBlock( List<VisionWordBlock> words )
    {
        this.words = new ArrayList<>();
        this.words.addAll( words );
        updateEnds();
    }


    private void updateEnds()
    {
        if ( this.words.size() > 0 ) {
            startTopVertex = this.words.get( 0 ).getTopLeft();
            startBottomVertex = this.words.get( 0 ).getBottomLeft();
            endTopVertex = this.words.get( this.words.size() - 1 ).getTopRight();
            endBottomVertex = this.words.get( this.words.size() - 1 ).getBottomRight();
        }
    }


    public boolean appendVisionLineBlock( VisionWordBlock word )
    {
        // TODO In current logic this operation is not required
        return false;
    }


    public boolean merge( VisionLineBlock line )
    {
        boolean response = false;
        if ( line != null ) {
            if ( this.getEndBottomVertex().getX() <= line.getStartBottomVertex().getX() ) {
                int overlapStart = this.getEndTopVertex().getY() < line.getStartTopVertex().getY() ?
                    line.getStartTopVertex().getY() :
                    this.getEndTopVertex().getY();
                int overlapEnd = this.getEndBottomVertex().getY() < line.getStartBottomVertex().getY() ?
                    this.getEndBottomVertex().getY() :
                    line.getStartBottomVertex().getY();
                if ( overlapStart < overlapEnd ) {
                    double currentOverlap =
                        (double) ( overlapEnd - overlapStart ) / ( this.getEndBottomVertex().getY() - this.endTopVertex
                            .getY() );
                    double lineOverlap =
                        (double) ( overlapEnd - overlapStart ) / ( line.getStartBottomVertex().getY() - line.startTopVertex
                            .getY() );
                    double overlap = currentOverlap > lineOverlap ? currentOverlap : lineOverlap;
                    if ( overlap > GoogleVisionTextUtils.OVERLAP_PERCENT ) {
                        this.words.addAll( line.words );
                        updateEnds();
                        response = true;
                    }
                } else {
                    VisionWordBlock currentWordObj = new VisionWordBlock( this );
                    VisionWordBlock lineWordObj = new VisionWordBlock( line );
                    boolean overlap = GoogleVisionTextUtils.checkIfSameLineByLineEquation( currentWordObj, lineWordObj );
                    if ( overlap ) {
                        this.words.addAll( line.words );
                        updateEnds();
                        response = true;
                    }
                }

            } else if ( line.getEndBottomVertex().getX() <= this.getStartBottomVertex().getX() ) {
                int overlapStart = this.getStartTopVertex().getY() < line.getEndTopVertex().getY() ?
                    line.getEndTopVertex().getY() :
                    this.getStartTopVertex().getY();
                int overlapEnd = this.getStartBottomVertex().getY() < line.getEndBottomVertex().getY() ?
                    this.getStartBottomVertex().getY() :
                    line.getEndBottomVertex().getY();
                if ( overlapStart < overlapEnd ) {
                    double currentOverlap =
                        (double) ( overlapEnd - overlapStart ) / ( this.getStartBottomVertex().getY() - this.getStartTopVertex()
                            .getY() );
                    double lineOverlap =
                        (double) ( overlapEnd - overlapStart ) / ( line.getEndBottomVertex().getY() - line.getEndTopVertex()
                            .getY() );
                    double overlap = currentOverlap > lineOverlap ? currentOverlap : lineOverlap;
                    if ( overlap > GoogleVisionTextUtils.OVERLAP_PERCENT ) {
                        line.getWords().addAll( this.words );
                        this.words = line.getWords();
                        updateEnds();
                        response = true;
                    }
                } else if ( this.words.size() > 1 ) {
                    VisionWordBlock currentWordObj = new VisionWordBlock( this );
                    VisionWordBlock lineWordObj = new VisionWordBlock( line );
                    boolean overlap = GoogleVisionTextUtils.checkIfSameLineByLineEquation( currentWordObj, lineWordObj );
                    if ( overlap ) {
                        line.getWords().addAll( this.words );
                        this.words = line.getWords();
                        updateEnds();
                        response = true;
                    }
                }
            } else {
                boolean wordsMerged = mergeWords( line );
                if ( wordsMerged ) {
                    if ( line.getWords().size() > 0 )
                        wordsMerged = false;
                }

                return wordsMerged;
            }
        }
        return response;
    }


    private boolean checkVerticalOverlap( VisionLineBlock line )
    {
        int overlapStart = -1, overlapEnd = -1;
        if ( this.getVerticalStart() < line.getVerticalStart() ) {
            overlapStart = line.getVerticalStart();
        } else {
            overlapStart = this.getVerticalStart();
        }
        if ( this.getVerticalEnd() < line.getVerticalEnd() ) {
            overlapEnd = this.getVerticalEnd();
        } else {
            overlapEnd = line.getVerticalEnd();
        }
        if ( overlapStart < overlapEnd ) {
            float currentOverlap = (float) ( overlapEnd - overlapStart ) / this.getHeight();
            float lineOverlap = (float) ( overlapEnd - overlapStart ) / line.getHeight();
            float overlapPercent;
            if ( currentOverlap > lineOverlap ) {
                overlapPercent = currentOverlap;
            } else {
                overlapPercent = lineOverlap;
            }
            if ( overlapPercent > MIN_VERTICAL_OVERLAP ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public boolean mergeWords( VisionLineBlock line )
    {
        boolean response = checkVerticalOverlap( line );
        if ( response ) {
            List<VisionWordBlock> updatedWordList = new ArrayList<>();

            for ( int i = 0; i < this.words.size(); ) {
                VisionWordBlock word = this.words.get( i );
                for ( int lineIndex = 0; lineIndex < line.getWords().size(); ) {
                    VisionWordBlock nextWord = line.getWords().get( lineIndex );
                    boolean wordsVertialOverlap = word.checkVerticalOverlap( nextWord );
                    VisionWordBlock prevWord =
                        updatedWordList.size() > 0 ? updatedWordList.get( updatedWordList.size() - 1 ) : null;
                    // TODO insted of <= kind of operation we may opt for small threshold based
                    // approach
                    if ( wordsVertialOverlap && (
                        nextWord.getHorizontalStartingPoint() - HORIZONTAL_THRESHOLD <= word.getHorizontalStartingPoint()
                            && nextWord.getHorizontalEndingPoint() - HORIZONTAL_THRESHOLD <= word.getHorizontalStartingPoint() )
                        && ( prevWord == null || nextWord.getHorizontalStartingPoint()
                        >= prevWord.getHorizontalEndingPoint() - HORIZONTAL_THRESHOLD ) ) {
                        updatedWordList.add( nextWord );
                        line.getWords().remove( lineIndex );
                    } else {
                        break;
                    }
                }
                updatedWordList.add( word );
                this.words.remove( i );
            }
            this.words = updatedWordList;
        }
        return response;
    }


    public int getVerticalStart()
    {
        Vertex startVertex =
            this.getStartTopVertex().getY() < this.getEndTopVertex().getY() ? this.getStartTopVertex() : this.getEndTopVertex();
        return startVertex.getY();
    }


    public int getVerticalEnd()
    {
        Vertex endVertex = this.getStartBottomVertex().getY() > this.getEndBottomVertex().getY() ?
            this.getStartBottomVertex() :
            this.getEndBottomVertex();
        return endVertex.getY();
    }


    public int getHeight()
    {
        int verticalEnd = this.getVerticalEnd();
        int verticalStart = this.getVerticalStart();
        return verticalEnd - verticalStart;
    }


    public Vertex getStartTopVertex()
    {
        return startTopVertex;
    }


    public void setStartTopVertex( Vertex startTopVertex )
    {
        this.startTopVertex = startTopVertex;
    }


    public Vertex getStartBottomVertex()
    {
        return startBottomVertex;
    }


    public void setStartBottomVertex( Vertex startBottomVertex )
    {
        this.startBottomVertex = startBottomVertex;
    }


    public Vertex getEndTopVertex()
    {
        return endTopVertex;
    }


    public void setEndTopVertex( Vertex endTopVertex )
    {
        this.endTopVertex = endTopVertex;
    }


    public Vertex getEndBottomVertex()
    {
        return endBottomVertex;
    }


    public void setEndBottomVertex( Vertex endBottomVertex )
    {
        this.endBottomVertex = endBottomVertex;
    }


    public int getPageNo() { return pageNo; }


    public void setPageNo( int pageNo ) { this.pageNo = pageNo; }


    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        for ( VisionWordBlock word : this.words ) {
            buffer.append( " " );
            buffer.append( word.getDescription() );
        }
        return buffer.toString();
    }
}
