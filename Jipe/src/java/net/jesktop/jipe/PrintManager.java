/**
 * PrintManager.java - Printing class for use with the Jipe Project.  Copyright
 * (c) 1996-1999 Steve Lawson.  E-Mail steve@e-i-s.co.uk Latest version can be
 * found at http://e-i-s.co.uk/jipe
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */
package net.jesktop.jipe;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;


public class PrintManager extends JFrame
{

    BufferedReader br;
    StringReader sr;
    boolean PrintSwitch = true;
    String str = null;
    String textarea;
    public PrintManager(Frame parent, String text)
    {
        textarea = text;
        PrintJob pjob = getToolkit().getPrintJob(parent, "Printing", null);
        if (pjob != null)
        {
            Graphics pg = null;
            while (PrintSwitch == true)
            {
                pg = pjob.getGraphics();
                printPage(pg, pjob);
                if (str == null)
                    PrintSwitch = false;
                pg.dispose();                                // flush page
            }
            pjob.end();
        }
        dispose();
    }
    public void printPage(Graphics pg, PrintJob pj)
    {
        Dimension page_size = pj.getPageDimension();
        int width = page_size.width;
        int height = page_size.height;
        pg.setFont(new Font("Helvetica", Font.PLAIN, 10));
        FontMetrics metrics = pg.getFontMetrics();
        int line_height = metrics.getHeight() + metrics.getLeading();
        int line = 20;
        try
        {
            if (br == null)
            {
                sr = new StringReader(textarea);
                br = new BufferedReader(sr);
                str = "";
            }
            while (str != null && line <= (height - 20))
            {
                str = br.readLine();
                pg.drawString(str, 10, line);
                line += line_height;
            }
        }
        catch (Exception err)
        {
        }
    }
}

