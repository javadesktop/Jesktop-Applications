package net.jesktop.jipe;

import java.io.*;


class JavaFilter extends SpecialFileFilters
{

    public boolean accept(File f)
    {
        boolean accept = super.accept(f);
        if (!accept)
        {
            String suffix = getSuffix(f);
            if (suffix != null)
                accept = suffix.equals("java");
        }
        return accept;
    }
    public String getDescription()
    {
        return "Java Files(*.java)";
    }
}

