package net.jesktop.jipe;
import java.io.*;
/** FileFilter Class for .class file extension.
 */
class ClassFilter extends SpecialFileFilters
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
accept = suffix.equals("class");
}
return accept;
}
/** return description of this type of filter.
 */
public String getDescription()
{
return "Class Files(*.class)";
}
}

