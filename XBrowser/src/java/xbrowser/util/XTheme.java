/****************************************************************
*              XBrowser  -  eXtended web Browser                *
*                                                               *
*           Copyright (c) 2000-2001  Armond Avanes              *
*     Refer to ReadMe & License files for more information      *
*                                                               *
*                                                               *
*                      By: Armond Avanes                        *
*          Armond@Neda.net     &    ArnoldX@MailCity.com        *
*          http://www.geocities.com/xa_arnold/index.html        *
*****************************************************************/
package xbrowser.util;

import java.util.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.*;

public class XTheme extends DefaultMetalTheme implements Comparable
{
	public XTheme(String name)
	{
		this.name = name;
		initValues();
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return name;
	}

	public boolean equals(Object obj)
	{
		if( !(obj instanceof XTheme) )
			return false;

	XTheme theme = (XTheme)obj;

		return( name.equals(theme.getName()) );
	}

	public int compareTo(Object obj)
	{
		if( !(obj instanceof XTheme) )
			return -1;

	XTheme theme = (XTheme)obj;

		return( name.compareTo(theme.getName()) );
	}

	public void activate() throws Exception
	{
        MetalLookAndFeel.setCurrentTheme(this);
	}

    private void initValues()
    {
        primary1 = super.getPrimary1();
        primary2 = super.getPrimary2();
        primary3 = super.getPrimary3();

        secondary1 = super.getSecondary1();
        secondary2 = super.getSecondary2();
        secondary3 = super.getSecondary3();

    	white = super.getWhite();
    	black = super.getBlack();

        controlFont = super.getControlTextFont();
        systemFont = super.getSystemTextFont();
        userFont = super.getUserTextFont();
        smallFont = super.getSubTextFont();
    }

    public void setPrimary1(ColorUIResource color)
    {
		primary1 = color;
	}

    public void setPrimary2(ColorUIResource color)
    {
		primary2 = color;
	}

    public void setPrimary3(ColorUIResource color)
    {
		primary3 = color;
	}

    public void setSecondary1(ColorUIResource color)
    {
		secondary1 = color;
	}

    public void setSecondary2(ColorUIResource color)
    {
		secondary2 = color;
	}

    public void setSecondary3(ColorUIResource color)
    {
		secondary3 = color;
	}

    public void setWhite(ColorUIResource color)
    {
		white = color;
	}

    public void setBlack(ColorUIResource color)
    {
		black = color;
	}

    public boolean shouldSavePrimary1()
    {
		return( !primary1.equals(super.getPrimary1()) );
	}

    public boolean shouldSavePrimary2()
    {
		return( !primary2.equals(super.getPrimary2()) );
	}

    public boolean shouldSavePrimary3()
    {
		return( !primary3.equals(super.getPrimary3()) );
	}

    public boolean shouldSaveSecondary1()
    {
		return( !secondary1.equals(super.getSecondary1()) );
	}

    public boolean shouldSaveSecondary2()
    {
		return( !secondary2.equals(super.getSecondary2()) );
	}

    public boolean shouldSaveSecondary3()
    {
		return( !secondary3.equals(super.getSecondary3()) );
	}

    public boolean shouldSaveWhite()
    {
		return( !white.equals(super.getWhite()) );
	}

    public boolean shouldSaveBlack()
    {
		return( !black.equals(super.getBlack()) );
	}

    public ColorUIResource getPrimary1() { return primary1; }
    public ColorUIResource getPrimary2() { return primary2; }
    public ColorUIResource getPrimary3() { return primary3; }

    public ColorUIResource getSecondary1() { return secondary1; }
    public ColorUIResource getSecondary2() { return secondary2; }
    public ColorUIResource getSecondary3() { return secondary3; }

    public ColorUIResource getWhite() { return white; }
    public ColorUIResource getBlack() { return black; }

    public FontUIResource getControlTextFont() { return controlFont; }
    public FontUIResource getSystemTextFont() { return systemFont; }
    public FontUIResource getUserTextFont() { return userFont; }
    public FontUIResource getSubTextFont() { return smallFont; }

// Attributes:
    private ColorUIResource primary1;
    private ColorUIResource primary2;
    private ColorUIResource primary3;

    private ColorUIResource secondary1;
    private ColorUIResource secondary2;
    private ColorUIResource secondary3;

    private ColorUIResource white;
    private ColorUIResource black;

    private FontUIResource controlFont;
    private FontUIResource systemFont;
    private FontUIResource userFont;
    private FontUIResource smallFont;

	private String name;
}
