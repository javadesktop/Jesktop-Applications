/**
 * What's NEW - First of all, this section and versioning of this little app -
 * The Constructor to pass a javax.swing.text.PlainDocument (Thanks to Sandip
 * Chitale) - Better organization (too much was done in the constructor
 * before) - Not new in this version but worth mentioning, the convert
 * unprintables method come from
 *   Ronald Sinkgraven, which comes from someone else in Germany (can I get a name? Thanks)
 */

// If you've gotten to this point, go back to the beginning Do not pass GO Do
// not Collect $200
//  -- The Management

// Print.java Version 1.1

// Ideas for next release - Not have constructor call print method, have some
// kind of GO method
//   to start printing (cut down on the number of constructors)
// - Have setFont() and setPageFormat() setText() methods for more flexibility
//   and reusability
// - Have the option to show the Page Format dialog or not

// A simple Printing class
//  Pass the constructos an array of strings (each string is a line of text)
//  and it does the rest
// Thanks to Sandip Chitale for the latest constructor to pass a
// javax.swing.text.PlainDocument from a JTextField, or a JTextArea Note: As
// of java 1.2.1 landscape printing is only available on Solaris
//    due to a bug in the Windows code.



// Just a thanks to all who contributed, and I hope that I did not miss anyone
// in the credits


package net.jesktop.jipe;

import java.awt.print.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;


public class Print
{

    private PageFormat pgfmt;                                // The Pageformat
    private int numberOfpages = 0;                           // The number of
    // pages
    private Book pages = new Book();                         // This holds
    // each page
    private static Font fnt = new Font("Courier", Font.PLAIN, 10); // the page
    // font
    Print(PlainDocument d)
    {
        this(d, fnt);
    }
    Print(PlainDocument d, Font font)
    {

        // Get Root element of the document
        Element root = d.getDefaultRootElement();

        // get the number of lines (i.e.  child elements)
        int count = root.getElementCount();

        // Allocate the array
        String[] lines = new String[count];
        Segment s = new Segment();

        // Get each line element, get its text and put it in the string array
        for (int i = 0; i < count; i++)
        {
            Element lineElement = (Element)root.getElement(i);
            try
            {
                d.getText(lineElement.getStartOffset(),
                          lineElement.getEndOffset() - lineElement.getStartOffset(), s);
                lines[i] = s.toString();
            }
            catch (BadLocationException ble)
            {
            }
        }
        printTextArray(lines, font);
    }
    Print(String[] text)
    {
        this(text, fnt);                                     // if no font
        // specified, use
        // my font
    }
    Print(String[] text, Font font)
    {
        printTextArray(text, font);
    }
    void printTextArray(String[] text, Font font)
    {
        try
        {
            PrinterJob job = PrinterJob.getPrinterJob();     // create a
            // printjob
            pgfmt = job.defaultPage();                       // get the
            // default page
            pgfmt = job.pageDialog(pgfmt);                   // set a page
            // format.  Comment
            // this if you do
            // not want this
            // to show
            pgfmt = job.validatePage(pgfmt);                 // make sure the
            // pageformat is
            // ok
            pages = pageinateText(text, pgfmt, font);        // do the
            // pagination
            try
            {
                job.setPageable(pages);                      // set the book
                // pageable so
                // the printjob
                // knows we are
                // printing more
                // than one page
                // (maybe)
                if (job.printDialog())
                {
                    job.print();                             // print.  This
                    // calls each
                    // Page object's
                    // print method
                }
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(
                    null, "Printer Error", "Error", JOptionPane.OK_OPTION);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(
                null, "Printer Error", "Error", JOptionPane.OK_OPTION);
        }
    }

    /**
     * The pagination method.
     *
     * Paginate the text onto Printable page objects
     */
    public Book pageinateText(String[] text, PageFormat pgfmt, Font fnt)
    {
        Book book = new Book();
        int lines = 0;                                       // lines on one
        // page
        int line = 0;                                        // line I am
        // currently
        // reading
        int pageNum = 0;                                     // page #
        int height = (int)pgfmt.getImageableHeight();        // height of a
        // page
        int pages = 0;                                       // number of pages
        lines = height / (fnt.getSize() + 2);                // number of
        // lines on a page
        pages = ((int)text.length / lines);                  // set number of
        // pages
        String[] pageText;                                   // one page of
        // text
        String readString;                                   // a temporary
        // string to read
        // from master
        // string
        convertUnprintables(text);                           // method to keep
        // out errors
        while (pageNum <= pages)
        {
            pageText = new String[lines];                    // clear the
            // string
            for (int x = 0; x < lines; x++)
            {
                line++;
                try
                {
                    readString = text[line - 1];             // read the string
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    readString = " ";
                }
                pageText[x] = readString;                    // add to the page
            }
            book.append(new Page(pageText, fnt), pgfmt);     // create a new
            // page object
            // with the text
            // and add it to
            // the book
            line--;
            pageNum++;                                       // increase the
            // page number I
            // am on
        }
        return book;                                         // return the
        // completed book
    }

    /**
     * Converts unprintable things to a space.
     *
     * stops some errors.
     */
    public void convertUnprintables(String[] myText)
    {
        String tempString;
        int i = myText.length;
        while (i > 0)
        {
            i--;
            tempString = myText[i];
            if (tempString == null || "".equals(tempString))
            {
                myText[i] = " ";
            }
        }
    }

    /**
     * a temp main method
     */
    public static void main(String[] args)
    {
        String[] string = {"Hello", "World", "How", "Are", "You"};
        Print print = new Print(string, new Font("Courier", Font.PLAIN, 12));
    }


    /**
     * An inner class that defines one page of text based on data about the
     * PageFormat etc.
     *
     * from the book defined in the parent class
     */
    class Page implements Printable
    {

        private String[] text;                               // the text for
        // the page
        private Font font;
        Page(String[] text, Font font)
        {
            this.text = text;                                // set the page's
            // text
            this.font = font;                                // set the page's
            // font
        }
        public int print(Graphics g, PageFormat pf, int pageIndex) // the
        // printing
        // part
        throws PrinterException
        {
            int pos;
            g.setFont(this.font);                            // Set the font
            g.setColor(Color.black);                         // set color
            for (int x = 0; x < (this.text.length - 1); x++)
            {
                pos = (int)pf.getImageableY() + (this.font.getSize() + 2) * (
                          x + 1);
                g.drawString(this.text[x], (int)pf.getImageableX(), pos); // draw
                // a
                // line
                // of
                // text
            }
            return Printable.PAGE_EXISTS;                    // print the page
        }
    }
}

