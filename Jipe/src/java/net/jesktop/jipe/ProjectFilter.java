package net.jesktop.jipe;

import java.io.*;


class ProjectFilter extends SpecialFileFilters
{
    public boolean accept(File f)
    {
        boolean accept = super.accept(f);
        if (!accept)
        {
            String suffix = getSuffix(f);
            if (suffix != null)
                accept = suffix.startsWith("prj");
        }
        return accept;
    }
    public String getDescription()
    {
        return "Jipe Project Files(*.prj)";
    }
}

