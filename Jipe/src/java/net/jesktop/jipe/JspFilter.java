/**
 * JSPFilter
 * This file is used by the Jipe class to identify recognized file types
 * for the Jipe editor
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
 *
 * @author Jeff Davies
 */
package net.jesktop.jipe;

import java.io.*;


public class JspFilter extends SpecialFileFilters
{
    /**
     * Determines if the filter can accept the file type based on the name of the file
     * @param f The File in question
     * @return <b>true</b> if the given File has the .jsp extension. Otherwise it returns <b>false</b>
     */
    public boolean accept(File f)
    {
        boolean accept = super.accept(f);
        if (!accept)
        {
            String suffix = getSuffix(f);
            if (suffix != null)
                accept = suffix.startsWith("jsp");
        }
        return accept;
    }

    /**
     * Returns the description of the file type(s) that this filter recognizes.
     * @return String Description of the file types that this filter will accept.
     */
    public String getDescription()
    {
        return "Java Server Pages (*.jsp)";
    }
}

