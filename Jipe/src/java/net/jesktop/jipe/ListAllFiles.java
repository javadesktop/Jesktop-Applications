/**
 * ListAllFiles.java - ListAllFiles class for use with the Jipe Project.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package net.jesktop.jipe;

import java.io.*;
import java.util.*;

public class ListAllFiles{

    Vector filevector;
    String rootdirectory = new String();
    MasterFilter filter;

    public ListAllFiles(String rootdirectory, String ext){
        this.rootdirectory = rootdirectory;
        filter = new MasterFilter(ext);
        filevector = new Vector();
        getList(rootdirectory);
    }

    public void getList(String path){ //recursively descend into each subdirectory
        String[] filelist;
        File startfile = new File(path);
        filelist = startfile.list(filter);//Gets the File array of files with no extension (directories)
        for (int i=0;i<filelist.length;i++){  // or given extension
          File newfile=new File(path+System.getProperty("file.separator")+filelist[i]);
            if (newfile.isDirectory()){
                getList(newfile.getPath());   //gets the files within that subdirectory
            }
            else {
                filevector.addElement(newfile);
            }
        }
    }
}
