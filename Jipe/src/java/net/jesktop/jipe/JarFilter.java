package net.jesktop.jipe;
import java.io.*;
/** FileFilter Class for .jar file extension.
 */
class JarFilter extends SpecialFileFilters
{
/**  *
 * @param f
 */
public boolean accept(File f)
{
boolean accept = super.accept(f);
if (!accept)
{
String suffix = getSuffix(f);
if (suffix != null)
accept = suffix.equals("jar");
}
return accept;
}
/** return description of this type of filter.
 */
public String getDescription()
{
return "Jar Files(*.jar)";
}
}

