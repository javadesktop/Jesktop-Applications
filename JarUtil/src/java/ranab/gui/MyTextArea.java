
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.gui;



import java.io.*;

import javax.swing.*;


/*
 * This class encapsulates <code>java.io</code>.
 */

/**
 * Class MyTextArea
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MyTextArea {

    /**
     * get <code>StringReader</code> object to read text area data
     */
    public static StringReader getReader(JTextArea area) {
        return new StringReader(area.getText());
    }

    /**
     * get <code>InputStream</code> object to read text area data
     */
    public static DataInputStream getInputStream(JTextArea area) {
        return new DataInputStream(new ByteArrayInputStream(area.getText().getBytes()));
    }

    /**
     * get <code>PrintWriter<code> object to write in the text area
     */
    public static PrintWriter getWriter(final JTextArea area) {

        return new PrintWriter(new Writer() {

            public void write(char[] cbuf, int off, int len) {
                area.append(new String(cbuf, off, len));
            }

            public void close() {}

            public void flush() {}
        });
    }

    /**
     * get <code>PrintStream</code> to write in the text area
     */
    public static PrintStream getOutputStream(final JTextArea area) {

        return new PrintStream(new OutputStream() {

            public void write(int b) {

                String str = new String(new byte[]{ (byte) b });

                area.append(str);
            }

            public void write(byte[] b, int off, int len) {

                String str = new String(b, off, len);

                area.append(str);
            }
        });
    }
}
