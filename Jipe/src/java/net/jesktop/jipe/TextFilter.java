package net.jesktop.jipe;

import java.io.*;


class TextFilter extends SpecialFileFilters
{

    public boolean accept(File f)
    {
        boolean accept = super.accept(f);
        if (!accept)
        {
            String suffix = getSuffix(f);
            if (suffix != null)
                accept = suffix.equals("txt");
        }
        return accept;
    }
    public String getDescription()
    {
        return "Text Files(*.txt)";
    }
}

