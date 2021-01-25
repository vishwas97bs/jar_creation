package googlevision.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import entity.OcredData;
import entity.Vertex;
import entity.VisionLineBlock;
import entity.VisionWordBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


public class GoogleVisionTextUtils
{

    public static final double OVERLAP_PERCENT = .55;

    private static final Logger LOG = LoggerFactory.getLogger( GoogleVisionTextUtils.class );

    public static final int SPACE_THRESHOLD = 12;
    public static final int MINIMUM_DIFFERENCE_FOR_SPACE_FOR_INVOICES = 0;
    public static final int MINIMUM_DIFFERENCE_FOR_SPACE_FOR_RECEIPTS = 6;
    public static final int MIDPOINT_DIFFERENCE = 4;
    public static final double GOOGLE_VISION_MERCHANT_EXTRACTOR = 0.9;
    public static final double AVG_CHAR_WIDTH = 3;
    public static final boolean ENABLE_EMPTY_LINES = true;
    private static final int MIN_LINE_HEIGHT = 2;


    public static OcredData constructText(List<VisionWordBlock> wordBlocks, int relativeZero, boolean fromPdfBox,
                                          int spaceWidth )
    {
        List<String> output = new ArrayList<>();
        OcredData ocredData = new OcredData();

        if ( !fromPdfBox )
            sortByVerticalMidPoint( wordBlocks );

        /**
         * Traversing through mid-point to display
         */
        List<VisionWordBlock> tempBlock = new ArrayList<>();

        List<VisionLineBlock> lines = new ArrayList<>();

        int averageLineHeight = getLineBlocksFromWordBlocks( wordBlocks, fromPdfBox, tempBlock, lines );

        averageLineHeight = averageLineHeight / lines.size();
        if ( averageLineHeight == 0 )
            averageLineHeight = MIN_LINE_HEIGHT;
        int i = 0;
        for ( VisionLineBlock line : lines ) {
            StringBuilder build = new StringBuilder();
            tempBlock = line.getWords();
            for ( int tab = 0; tab < ( ( tempBlock.get( 0 ).getHorizontalStartingPoint() ) - relativeZero )
                    / spaceWidth; tab++ ) {
                build.append( " " );
            }
            build.append( tempBlock.get( 0 ).getDescription() );
            for ( int space = 1; space < tempBlock.size(); space++ ) {
                int num = tempBlock.get( space ).getHorizontalStartingPoint()
                        - tempBlock.get( space - 1 ).getHorizontalEndingPoint();
                if ( num >= spaceWidth ) {
                    for ( int addspace = 0; addspace <= ( num / spaceWidth ); addspace++ ) {
                        build.append( " " );
                    }
                }
                build.append( tempBlock.get( space ).getDescription() );
            }

            int newLineNum = 0;
            if ( i < lines.size() - 1 ) {
                VisionLineBlock nextLine = lines.get( i + 1 );
                newLineNum = nextLine.getWords().get( 0 ).getVerticalStartingPoint()
                        - line.getWords().get( 0 ).getVerticalEndingPoint();
            }

            if ( ENABLE_EMPTY_LINES ) {
                if ( newLineNum < 0 )
                    build.append( "\n" );
                else {
                    for ( int addSpace = 0; addSpace <= ( newLineNum / averageLineHeight ); addSpace++ ) {
                        build.append( "\n" );
                    }
                }
            } else {
                build.append( "\n" );
            }

            output.add( build.toString() );
            i++;
        }
        ocredData.setStrLines( output );
        ocredData.setLines( lines );
        return ocredData;
    }


    public static int getLineBlocksFromWordBlocks( List<VisionWordBlock> wordBlocks, boolean fromPdfBox,
                                                   List<VisionWordBlock> tempBlock, List<VisionLineBlock> lines )
    {
        for ( int i = 0; i < wordBlocks.size() - 1; i++ ) {
            if ( checkIfSameLine( wordBlocks.get( i ), wordBlocks.get( i + 1 ) ) && ( i != wordBlocks.size() - 2 ) ) {
                tempBlock.add( wordBlocks.get( i ) );
            } else {

                tempBlock.add( wordBlocks.get( i ) );
                if ( i == wordBlocks.size() - 2 )
                    tempBlock.add( wordBlocks.get( i + 1 ) );
                if ( !fromPdfBox )
                    sortByHorizontalStartingPoint( tempBlock );

                VisionLineBlock line = new VisionLineBlock( tempBlock );

                lines.add( line );
                tempBlock.clear();
            }
        }
        int averageLineHeight = 0;
        for ( int i = 0; i < lines.size(); ) {
            VisionLineBlock currentLine = lines.get( i );
            VisionLineBlock nextLine = i < lines.size() - 2 ? lines.get( i + 1 ) : null;
            if ( currentLine.merge( nextLine ) ) {
                lines.remove( i + 1 );
                if ( i > 0 )
                    i--;
            } else {
                i++;
            }
        }

        for ( int i = lines.size() - 1; i > 0; ) {
            VisionLineBlock currentLine = lines.get( i );

            VisionLineBlock prevLine = i > 0 ? lines.get( i - 1 ) : null;

            if ( currentLine.merge( prevLine ) ) {
                lines.remove( i - 1 );
                i--;
            } else {
                i--;
                averageLineHeight += currentLine.getStartBottomVertex().getY() - currentLine.getStartTopVertex().getY();
            }
        }
        return averageLineHeight;
    }


    private static boolean checkIfSameLine( VisionWordBlock block1, VisionWordBlock block2 )
    {
        boolean response = false;
        int endDiff = Math.abs( block1.getVerticalEndingPoint() - block2.getVerticalMidpoint() );
        int midPointDiff = Math.abs( block1.getVerticalMidpoint() - block2.getVerticalMidpoint() );

        double angle = getRotationAngleOfImage( block1.getTopLeft(), block1.getBottomLeft(), block1.getBottomRight() );

        if ( Math.abs( angle ) > 0.02 && Math.abs( angle ) < 1.0472 && block1.getDescription().length() > 4 ) {
            return checkIfSameLineByLineEquation( block1, block2 );
        }
        if ( midPointDiff < endDiff )
            response = true;
        return response;
    }


    public static boolean checkIfSameLineByLineEquation( VisionWordBlock block1, VisionWordBlock block2 )
    {
        boolean response = false;

        double block1M = (double) ( block1.getBottomRight().getY() - block1.getBottomLeft().getY() )
                / (double) ( block1.getBottomRight().getX() - block1.getBottomLeft().getX() );

        double bottomB = ( block1.getBottomRight().getY() - block1M * block1.getBottomRight().getX() );

        double topB = ( block1.getTopRight().getY() - block1M * block1.getTopRight().getX() );

        // use overlapping method for same line detection
        double m2, b2;
        Vertex block2TopVertex, block2BottomVertex;

        int topXIntersection;
        int topYIntersection;

        int bottomXIntersection;
        int bottomYIntersection;

        if ( block2.getHorizontalStartingPoint() > block1.getHorizontalEndingPoint() ) {
            int m2Denominator = ( block2.getBottomLeft().getX() - block2.getTopLeft().getX() );
            if ( m2Denominator == 0 ) {
                topXIntersection = block2.getBottomLeft().getX();
                bottomXIntersection = block2.getBottomLeft().getX();
            } else {
                m2 = ( block2.getBottomLeft().getY() - block2.getTopLeft().getY() ) / m2Denominator;
                b2 = block2.getBottomLeft().getY() - m2 * block2.getBottomLeft().getX();
                topXIntersection = (int) ( ( topB - b2 ) / ( m2 - block1M ) );
                bottomXIntersection = (int) ( ( bottomB - b2 ) / ( m2 - block1M ) );
            }
            block2TopVertex = block2.getTopLeft();
            block2BottomVertex = block2.getBottomLeft();
        } else {
            int m2Denominator = ( block2.getBottomRight().getX() - block2.getTopRight().getX() );
            if ( m2Denominator == 0 ) {
                topXIntersection = block2.getBottomRight().getX();
                bottomXIntersection = block2.getBottomRight().getX();
            } else {
                m2 = ( block2.getBottomRight().getY() - block2.getTopRight().getY() ) / m2Denominator;
                b2 = block2.getBottomRight().getY() - m2 * block2.getBottomRight().getX();
                topXIntersection = (int) ( ( topB - b2 ) / ( m2 - block1M ) );
                bottomXIntersection = (int) ( ( bottomB - b2 ) / ( m2 - block1M ) );
            }
            block2TopVertex = block2.getTopRight();
            block2BottomVertex = block2.getBottomRight();
        }
        topYIntersection = (int) ( block1M * topXIntersection + topB );
        bottomYIntersection = (int) ( block1M * bottomXIntersection + bottomB );

        boolean overlap = false;
        if ( topYIntersection <= block2TopVertex.getY() ) {
            if ( bottomYIntersection >= block2TopVertex.getY() ) {
                overlap = true;
            }
        } else {
            if ( topYIntersection < block2BottomVertex.getY() ) {
                overlap = true;
            }
        }

        int denominator = ( block2BottomVertex.getY() - block2TopVertex.getY() );
        double percent = (double) ( Math.min( block2BottomVertex.getY(), bottomYIntersection )
                - Math.max( block2TopVertex.getY(), topYIntersection ) ) / denominator;

        if ( percent >= OVERLAP_PERCENT )
            response = true;
        if ( Double.isNaN( percent ) ) {
            if ( denominator == 0 && overlap ) {
                response = true;
            }
        }
        return response;
    }


    public static OcredData getTextList( JsonArray file )
    {

        // extract the values
        List<VisionWordBlock> wordBlocks = getWordBlocksFromResponse( file );

        int relativeZero = 0;
        if ( null != file.get( 0 ).getAsJsonObject().get( "boundingPoly" ).getAsJsonObject().get( "vertices" ).getAsJsonArray()
                .get( 0 ).getAsJsonObject().get( "x" ) ) {
            relativeZero = file.get( 0 ).getAsJsonObject().get( "boundingPoly" ).getAsJsonObject().get( "vertices" )
                    .getAsJsonArray().get( 0 ).getAsJsonObject().get( "x" ).getAsInt();
        }

        int spaceApproximation = getSpaceApproximation( wordBlocks );

        return constructText( wordBlocks, relativeZero, false, spaceApproximation );
    }


    private static int getSpaceApproximation( List<VisionWordBlock> words )
    {
        int totalCharCount = 0;
        int totalCharPixelWidth = 0;
        for ( VisionWordBlock word : words ) {
            totalCharPixelWidth += Math.abs( word.getTopLeft().getX() - word.getTopRight().getX() );
            totalCharCount += word.getDescription().length();
        }
        int response = totalCharPixelWidth / totalCharCount;
        if ( response == 0 )
            response = SPACE_THRESHOLD;
        return response;
    }


    public static OcredData getPdfTextList( File file ) throws IOException
    {
        List<VisionWordBlock> wordBlocks = new PDFBoxUtils().getCharacterLocation( file );

        int spaceApproximation = getSpaceApproximation( wordBlocks );

        OcredData ocredData = constructText( wordBlocks, 0, true, spaceApproximation );

        return reconstructWords( ocredData );

    }


    private static OcredData reconstructWords( OcredData ocredData )
    {
        List<VisionLineBlock> lines = ocredData.getLines();
        for ( VisionLineBlock line : lines ) {
            List<VisionWordBlock> words = line.getWords();
            List<VisionWordBlock> wordsMerged = new ArrayList<>();
            for ( int counter = 0, flag = 0; counter < words.size(); counter++ ) {
                if ( counter + 1 < words.size() ) {
                    if ( ( words.get( counter ).getHorizontalEndingPoint() - words.get( counter ).getHorizontalStartingPoint() )
                            * 4 < ( words.get( counter + 1 ).getHorizontalStartingPoint()
                            - words.get( counter ).getHorizontalEndingPoint() ) ) {
                        VisionWordBlock word = getCombinedWord( words, flag, counter );
                        if ( word.getDescription() == null ) {
                            flag = counter + 1;
                            continue;
                        }

                        if ( word.getDescription().trim().equals( "" ) ) {
                            flag = counter + 1;
                            continue;
                        }
                        flag = counter + 1;
                        wordsMerged.add( word );
                        continue;
                    }
                }
                if ( words.get( counter ).getDescription().equals( " " ) || counter + 1 == words.size() ) {
                    VisionWordBlock word = getCombinedWord( words, flag, counter );
                    if ( word.getDescription() == null ) {
                        flag = counter + 1;
                        continue;
                    }
                    if ( word.getDescription().trim().equals( "" ) ) {
                        flag = counter + 1;
                        continue;
                    }
                    flag = counter + 1;
                    wordsMerged.add( word );
                }
            }
            line.setWords( wordsMerged );
        }

        return ocredData;
    }


    private static VisionWordBlock getCombinedWord( List<VisionWordBlock> words, int start, int end )
    {
        VisionWordBlock word = new VisionWordBlock();

        for ( int counter = start; counter <= end; counter++ ) {
            if ( words.get( counter ).getDescription().equals( " " ) )
                break;
            word.setDescription(
                    ( word.getDescription() == null ? "" : word.getDescription() ) + ( words.get( counter ).getDescription() ) );
        }

        word.setTopLeft( words.get( start ).getTopLeft() );
        word.setBottomLeft( words.get( start ).getBottomLeft() );
        if ( !words.get( end ).getDescription().equals( " " ) || end == 0 ) {
            word.setTopRight( words.get( end ).getTopRight() );
            word.setBottomRight( words.get( end ).getBottomRight() );
        } else {
            word.setTopRight( words.get( end - 1 ).getTopRight() );
            word.setBottomRight( words.get( end - 1 ).getBottomRight() );
        }
        return word;
    }


    public static <T> void swap( int i, int j, List<T> list )
    {
        T temp = list.get( i );
        list.set( i, list.get( j ) );
        list.set( j, temp );
    }


    public static OcredData getText( JsonArray file ) throws IOException
    {
        String text = "";
        OcredData ocredData = getTextList( file );
        List<String> out = ocredData.getStrLines();
        for ( int x = 0; x < out.size(); x++ )
            text = text + out.get( x );
        text = text.replaceAll( "\n", "\r\n" );
        ocredData.setText( text );
        return ocredData;
    }


    public static OcredData getTextUsingPDFBox( File file ) throws IOException
    {
        String text = "";
        OcredData ocredData = getPdfTextList( file );
        List<String> out = ocredData.getStrLines();
        for ( int x = 0; x < out.size(); x++ )
            text = text + out.get( x );
        text = text.replaceAll( "\n", "\r\n" );
        ocredData.setText( text );
        return ocredData;
    }


    public static List<VisionWordBlock> getWordBlocksFromResponse( JsonArray blocksJsonArray )
    {
        LOG.trace( "Method: getWordBlocksFromResponse called." );
        List<VisionWordBlock> blocksArray = new ArrayList<>();
        for ( int blockCounter = 1; blockCounter < blocksJsonArray.size(); blockCounter++ ) {
            VisionWordBlock wordBlock = new VisionWordBlock();
            wordBlock
                    .setDescription( blocksJsonArray.get( blockCounter ).getAsJsonObject().get( "description" ).getAsString() );
            JsonArray vertices = blocksJsonArray.get( blockCounter ).getAsJsonObject().get( "boundingPoly" ).getAsJsonObject()
                    .get( "vertices" ).getAsJsonArray();
            wordBlock.setTopLeft( getVertex( vertices, 0 ) );
            wordBlock.setTopRight( getVertex( vertices, 1 ) );
            wordBlock.setBottomRight( getVertex( vertices, 2 ) );
            wordBlock.setBottomLeft( getVertex( vertices, 3 ) );
            if ( wordBlock.haveValidBoundingBox() )
                blocksArray.add( wordBlock );
        }
        LOG.trace( "Method: getWordBlocksFromResponse finished." );
        return blocksArray;
    }


    private static Vertex getVertex( JsonArray verticesArray, int index )
    {
        Integer x = null;
        Integer y = null;
        JsonElement element = verticesArray.get( index ).getAsJsonObject().get( "x" );
        if ( element != null )
            x = element.getAsInt();
        element = verticesArray.get( index ).getAsJsonObject().get( "y" );
        if ( element != null )
            y = element.getAsInt();
        if ( x == null || y == null )
            return null;
        Vertex vertex = new Vertex( x, y );
        return vertex;
    }


    public static void sortByVerticalMidPoint( List<VisionWordBlock> wordBlocks )
    {
        for ( int blockCounter = 0; blockCounter < wordBlocks.size() - 1; blockCounter++ ) {
            for ( int innerBlockCounter = 0; innerBlockCounter < wordBlocks.size() - blockCounter - 1; innerBlockCounter++ ) {
                VisionWordBlock currWordBlock = wordBlocks.get( innerBlockCounter );
                VisionWordBlock nextWordBlock = wordBlocks.get( innerBlockCounter + 1 );
                if ( currWordBlock.getVerticalMidpoint() > nextWordBlock.getVerticalMidpoint() ) {
                    swap( innerBlockCounter, innerBlockCounter + 1, wordBlocks );
                }
            }
        }
    }


    public static void sortByHorizontalStartingPoint( List<VisionWordBlock> wordBlocks )
    {
        for ( int blockCounter = 0; blockCounter < wordBlocks.size() - 1; blockCounter++ ) {
            for ( int innerBlockCounter = 0; innerBlockCounter < wordBlocks.size() - blockCounter - 1; innerBlockCounter++ ) {
                VisionWordBlock currWordBlock = wordBlocks.get( innerBlockCounter );
                VisionWordBlock nextWordBlock = wordBlocks.get( innerBlockCounter + 1 );
                if ( currWordBlock.getHorizontalStartingPoint() > nextWordBlock.getHorizontalStartingPoint() ) {
                    swap( innerBlockCounter, innerBlockCounter + 1, wordBlocks );
                }
            }
        }
    }


    public static double getRotationAngleOfImage( JsonArray vertices )
    {
        Vertex vertex0 = getVertex( vertices, 0 );
        Vertex vertice1 = getVertex( vertices, 3 );
        Vertex vertice2 = getVertex( vertices, 2 );
        return getRotationAngleOfImage( vertex0, vertice1, vertice2 );

    }


    public static double getRotationAngleOfImage( Vertex topLeft, Vertex bottomLeft, Vertex bottomRight )
    {
        int xDiff = bottomRight.getX() - bottomLeft.getX();
        int yDiff = bottomRight.getY() - bottomLeft.getY();
        double a;
        if ( topLeft.getX() < bottomRight.getX() ) {
            if ( topLeft.getY() < bottomRight.getY() ) {
                // straight image case
                a = (double) yDiff / xDiff;
                a = Math.atan( a );
            } else {
                // left tilted
                a = (double) xDiff / yDiff;
                a = Math.atan( a );
                a = 3 * Math.PI / 2 - a;// -a to make text parallel to y axis then 270 degree
            }
        } else {
            if ( topLeft.getY() < bottomRight.getY() ) {
                // right tilted
                a = (double) xDiff / yDiff;
                a = Math.atan( a );
                a = ( Math.PI / 2 ) - a;// -a to make text parallel to y axis then 270 degree
            } else {
                // upside Down
                a = (double) yDiff / xDiff;
                a = Math.atan( a );
                a = Math.PI - a;// -a to make text parallel to x axis then 270 degree
            }
        }
        return a;
    }


    public static double frequency( JsonArray file )
    {
        Double sum = 0.0;
        Map<Double, Integer> frequencyMap = new TreeMap<Double, Integer>();
        for ( int counter = 1; counter < file.size(); counter++ ) {
            JsonArray vertices = file.get( counter ).getAsJsonObject().get( "boundingPoly" ).getAsJsonObject().get( "vertices" )
                    .getAsJsonArray();
            if ( file.get( counter ).getAsJsonObject().get( "description" ).getAsString().length() > 4
                    && checkVertices( vertices ) ) {
                double s = getRotationAngleOfImage( vertices );
                if ( frequencyMap.containsKey( s ) ) {
                    int count = frequencyMap.get( s ) + 1;
                    frequencyMap.put( s, count );
                } else {
                    frequencyMap.put( s, 1 );
                }
            }
        }
        Set<Double> angles = frequencyMap.keySet();
        sum = getHighestValue( angles, frequencyMap );

        if ( sum < 0.01 && sum > -0.01 )
            // skipping small rotation angle as it is covered using threshold value
            sum = 0.0;
        return sum;
    }


    private static double getHighestValue( Set<Double> angles, Map<Double, Integer> frequency )
    {
        Double[] vals = angles.toArray( new Double[angles.size()] );
        if ( vals.length == 0 )
            return 0;
        double leastValue = vals[0];
        double highestVal = vals[vals.length - 1];
        double degreePartition = 0.0122173;
        int highestCount = 0;
        double angle = 0;
        do {
            double newLeeastVal = leastValue + degreePartition;
            int countfreq = 0;
            for ( Entry<Double, Integer> entry : frequency.entrySet() ) {
                if ( entry.getKey() >= leastValue && entry.getKey() < newLeeastVal ) {
                    int freq = entry.getValue();
                    countfreq += freq;
                }
                if ( entry.getKey() > newLeeastVal )
                    break;
            }
            if ( countfreq > highestCount ) {
                highestCount = countfreq;
                angle = newLeeastVal;
            }
            leastValue = newLeeastVal;
        } while ( leastValue < highestVal );
        return angle;

    }


    private static Double getMedian( List<Double> frequency )
    {
        Double response = 0.0;
        Collections.sort( frequency );
        int remainder = frequency.size() % 2;
        if ( remainder == 0 ) {
            response = frequency.get( frequency.size() / 2 );
        } else {
            response = ( frequency.get( frequency.size() / 2 ) + frequency.get( ( frequency.size() / 2 ) + 1 ) ) / 2;
        }
        return response;
    }


    public static boolean checkVertices( JsonArray vertices )
    {
        for ( int vertCounter = 0; vertCounter < 4; vertCounter++ ) {
            if ( vertices.get( vertCounter ).getAsJsonObject().get( "x" ) == null
                    || vertices.get( vertCounter ).getAsJsonObject().get( "y" ) == null ) {
                return false;
            }
        }
        return true;
    }


    public static JsonArray changeJson( JsonArray textArray, double angle )
    {
        JsonArray updated = new JsonArray();
        if ( angle == 0 )
            return textArray;
        updated.add( textArray.get( 0 ) );
        int leastX = 9999;
        int leastY = 9999;
        int maxX = -9999;
        int maxY = -9999;
        Integer cx = null;
        Integer cy = null;
        for ( int counter = 1; counter < textArray.size(); counter++ ) {
            int flg = 0;
            JsonArray vert = textArray.get( counter ).getAsJsonObject().get( "boundingPoly" ).getAsJsonObject()
                    .get( "vertices" ).getAsJsonArray();
            JsonObject obj = new JsonObject();
            JsonObject vertice = new JsonObject();
            JsonArray vertices = new JsonArray();
            for ( int vertCounter = 0; vertCounter < 4; vertCounter++ ) {
                if ( vert.get( vertCounter ).getAsJsonObject().get( "x" ) == null
                        || vert.get( vertCounter ).getAsJsonObject().get( "y" ) == null ) {
                    // TO-DO : Put some value for empty
                    flg = 1;
                    break;
                } else {
                    int x = vert.get( vertCounter ).getAsJsonObject().get( "x" ).getAsInt();
                    int y = vert.get( vertCounter ).getAsJsonObject().get( "y" ).getAsInt();
                    if ( cx == null ) {
                        cx = x;
                        cy = y;
                    }

                    JsonObject values = rotatePointAroundAnother( cx, cy, x, y, angle );
                    x = values.get( "x" ).getAsInt();
                    y = values.get( "y" ).getAsInt();
                    if ( leastX > x )
                        leastX = x;
                    if ( leastY > y )
                        leastY = y;
                    if ( maxX < x )
                        maxX = x;
                    if ( maxY < y )
                        maxY = y;
                    vertices.add( values );
                }
            }
            if ( flg != 1 ) {
                vertice.add( "vertices", vertices );
                obj.addProperty( "description", textArray.get( counter ).getAsJsonObject().get( "description" ).getAsString() );
                obj.add( "boundingPoly", vertice );
                updated.add( obj );
            }
        }
        JsonObject values = updated.get( 0 ).getAsJsonObject();
        JsonObject vertice = getBoundingBox( leastX, leastY, maxX, maxY );
        values.add( "boundingPoly", vertice );
        return updated;
    }


    private static JsonObject getBoundingBox( int leastX, int leastY, int maxX, int maxY )
    {
        JsonObject vertice = new JsonObject();

        JsonObject topLeft = new JsonObject();
        JsonObject topright = new JsonObject();
        JsonObject bottomRight = new JsonObject();
        JsonObject bottomLeft = new JsonObject();
        JsonArray vertices = new JsonArray();
        topLeft.addProperty( "x", leastX );
        topLeft.addProperty( "y", leastY );

        topright.addProperty( "x", maxX );
        topright.addProperty( "y", leastY );

        bottomRight.addProperty( "x", maxX );
        bottomRight.addProperty( "y", maxY );

        bottomLeft.addProperty( "x", leastX );
        bottomLeft.addProperty( "y", maxY );

        vertices.add( topLeft );
        vertices.add( topright );
        vertices.add( bottomRight );
        vertices.add( bottomLeft );

        vertice.add( "vertices", vertices );
        return vertice;
    }


    private static JsonObject rotatePointAroundAnother( int cx, int cy, int x, int y, double angle )
    {
        JsonObject rotatedVertex = new JsonObject();
        double s = Math.sin( angle );
        double c = Math.cos( angle );

        // translate point back to origin:
        x -= cx;
        y -= cy;

        // rotate point
        int xnew = (int) ( x * c - y * s );
        int ynew = (int) ( x * s + y * c );

        // translate point back:
        x = xnew + cx;
        y = ynew + cy;
        rotatedVertex.addProperty( "x", x );
        rotatedVertex.addProperty( "y", y );
        return rotatedVertex;
    }


    public static Map<String, Double> getMerchantLogos( Map<String, Double> logos )
    {
        Map<String, Double> merchantLogos = new HashMap<>();
        String merchantName;
        for ( Entry<String, Double> logo : logos.entrySet() ) {
            double baseScore = GOOGLE_VISION_MERCHANT_EXTRACTOR;
            merchantName = logo.getKey();
            double gVScore = logo.getValue();
            double confidence = getConfidence( merchantName, baseScore, gVScore );
            if ( merchantName != null )
                merchantLogos.put( merchantName, confidence );
        }
        return merchantLogos;
    }


    public static double getConfidence( String name, double base, double gVScore )
    {
        double score = base;
        // TODO need to have a better way to reduce confidence for these merchants
        if ( name.toLowerCase().contains( "gmail" ) || name.toLowerCase().contains( "google" )
                || name.toLowerCase().contains( "yahoo" ) ) {
            score = .1;
        }
        score = score + ( gVScore / 10 );

        return score;
    }
}
