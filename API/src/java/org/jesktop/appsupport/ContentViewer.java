
/*****************************************************************************
 * Copyright (C) Jesktop Organization. All rights reserved.                  *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the jesktop-bsd-license.html file.                                        *
 *****************************************************************************/
package org.jesktop.appsupport;



import java.net.URL;


/**
 * Apps that implement this can view certain types of content.  When the mime
 * registry is finished the manifest will register the app for a particulr mime
 * type at installation time.
 *
 * @author  <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
 * @version 1.0
 */
public interface ContentViewer extends ContentAction {

    /**
     * Method viewContent is used for viewing content specified by the URL.  If the content
* is not visible, then the default non-edit action for that type should
     * be done.  E.g. for MP3s it is "listen".
     *
     * @param url the content to be viewed
     * @param thumbNail the content should be viewed in thumbnail view.
     *
     */
    void viewContent(URL url, boolean thumbNail);
}
