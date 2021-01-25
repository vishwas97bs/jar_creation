package googlevision.util;


import entity.Vertex;
import entity.VisionWordBlock;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//PDFBox v2.x imports

public class PDFBoxUtils extends PDFTextStripper
{

    private static final Logger LOG = LoggerFactory.getLogger( PDFBoxUtils.class );
    private List<String> desc = new ArrayList<>();
    private List<Integer> X1 = new ArrayList<>();
    private List<Integer> Y1 = new ArrayList<>();
    private List<Integer> X2 = new ArrayList<>();
    private List<Integer> Y2 = new ArrayList<>();
    private List<Integer> X3 = new ArrayList<>();
    private List<Integer> Y3 = new ArrayList<>();
    private List<Integer> X4 = new ArrayList<>();
    private List<Integer> Y4 = new ArrayList<>();
    static final int RAW_TEXT_CUT_OFF_SIZE = 200;


    public PDFBoxUtils() throws IOException
    {
        super();
        desc.clear();
        X1.clear();
        X2.clear();
        X3.clear();
        X4.clear();
        Y1.clear();
        Y2.clear();
        Y3.clear();
        Y4.clear();
    }


    public File convertToImage( File file )
    {
        File temp = null;

        PDDocument document = null;
        try {
            // Loading an existing PDF document
            document = PDDocument.load( file );

            // Rendering an image from the PDF document
            List<BufferedImage> image = getBufferedImagesFromPDDocument( document );

            // creating temporary file
            temp = File.createTempFile( "JPG", ".jpg" );

            // Writing the image to a file
            ImageIO.write( image.get( 0 ), "JPEG", temp );

        } catch ( IOException e ) {
            LOG.error( "IO Exception while creating image from PDf", e );
        } finally {
            if ( document != null )
                try {
                    document.close();
                } catch ( IOException e ) {
                    LOG.error( "Error while closing document", e );
                }
        }


        return temp;
    }


    public List<VisionWordBlock> getCharacterLocation(File file ) throws IOException
    {
        List<VisionWordBlock> blocksArray = new ArrayList<>();
        PDDocument document = null;
        try {
            document = PDDocument.load( file );
            this.setSortByPosition( true );
            this.setStartPage( 0 );
            this.setEndPage( document.getNumberOfPages() );

            Writer dummy = new OutputStreamWriter( new ByteArrayOutputStream() );
            this.writeText( document, dummy );
        } finally {
            if ( document != null ) {
                document.close();
                for ( int blockCounter = 0; blockCounter < desc.size(); blockCounter++ ) {
                    VisionWordBlock wordBlock = new VisionWordBlock();
                    wordBlock.setDescription( desc.get( blockCounter ) );
                    wordBlock.setTopLeft( vertex( X1.get( blockCounter ), Y1.get( blockCounter ) ) );
                    wordBlock.setTopRight( vertex( X2.get( blockCounter ), Y2.get( blockCounter ) ) );
                    wordBlock.setBottomRight( vertex( X3.get( blockCounter ), Y3.get( blockCounter ) ) );
                    wordBlock.setBottomLeft( vertex( X4.get( blockCounter ), Y4.get( blockCounter ) ) );
                    blocksArray.add( wordBlock );
                }
            }
        }
        return blocksArray;
    }


    protected void writeString( String string, List<TextPosition> textPositions ) throws IOException
    {
        for ( TextPosition text : textPositions ) {
            desc.add( text.getUnicode() );
            X1.add( (int) text.getX() );
            Y1.add( (int) text.getY() );
            X2.add( (int) text.getX() + (int) text.getWidth() );
            Y2.add( (int) text.getY() );
            X3.add( (int) text.getX() + (int) text.getWidth() );
            if ( (int) text.getHeight() == 0 ) {
                Y3.add( (int) text.getY() + (int) text.getFontSize() );
                Y4.add( (int) text.getY() + (int) text.getFontSize() );
            } else {
                Y3.add( (int) text.getY() + (int) text.getHeight() );
                Y4.add( (int) text.getY() + (int) text.getHeight() );
            }
            X4.add( (int) text.getX() );
        }
    }


    private static Vertex vertex(int x, int y )
    {
        return new Vertex( x, y );
    }


    public boolean canBeExtracted( File file )
    {
        try {
            getCharacterLocation( file );

            if ( desc.size() < RAW_TEXT_CUT_OFF_SIZE )
                return false;
            else
                return true;
        } catch ( IOException err ) {
            return false;
        }
    }

/**
 *  Convert pdf pages to image. New way of converting in PDFBox v2.x
 * @param pdDoc
 * @return  List<BufferedImage> 
 * @throws IOException
 */
    public List<BufferedImage> getBufferedImagesFromPDDocument( PDDocument pdDoc ) throws IOException
    {
    	LOG.trace( "Method getBufferedImagesFromPDDocument() started" );
        List<BufferedImage> bufferedImages = new ArrayList<>();
        PDFRenderer pdfRenderer = new PDFRenderer(pdDoc);
        for (int page = 0; page < pdDoc.getNumberOfPages(); ++page)
        { 
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            bufferedImages.add(bim);
        }
        LOG.trace( "Method getBufferedImagesFromPDDocument() ended" );
        return bufferedImages;    	    	    	       
    }

/**
 *  Convert pdf pages to image. New way of converting in PDFBox v2.x
 * @param pdDoc
 * @return List<BufferedImage>
 * @throws IOException
 */
    public List<BufferedImage> getPagesAsImages( PDDocument pdDoc ) throws IOException
    {
    	LOG.trace( "Method getPagesAsImages() started" );
    	List<BufferedImage> bufferedImages = new ArrayList<>();
    	PDFRenderer pdfRenderer = new PDFRenderer(pdDoc);
    	for (int page = 0; page < pdDoc.getNumberOfPages(); ++page)
    	{ 
    	    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
    	    bufferedImages.add(bim);    	    
    	}
    	LOG.trace( "Method getPagesAsImages() ended" );    	    	    	  
        return bufferedImages;
    }
}
