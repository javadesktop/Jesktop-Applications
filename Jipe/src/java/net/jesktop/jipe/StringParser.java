package net.jesktop.jipe;

import java.util.*;

public class StringParser{
    int num,num1;
    String path, colorString;
    int[] colorcode, colorval;
    String ModifyPath(String filepath){
        num=filepath.length();
        if(filepath.trim().length()<1)return "";
        Character ch= new Character('\\');
        for (int i=0;i<num;i++){
            Character char1=new Character(filepath.charAt(i));
            if (char1.equals(ch)) {
                StringBuffer bf=new StringBuffer(filepath);
                bf.insert(i,"\\");
                ++i;
                ++num;
                filepath= new String(bf);
            }
            path=new String(filepath);
        }
        return path;
    }
    int[] getColorCode(String colorstring){
        colorcode = new int[10];
        num1=0;
        StringTokenizer st = new StringTokenizer(colorstring,",");
        while(st.hasMoreTokens()){
            colorcode[num1]= Integer.parseInt(st.nextToken());
            num1++;
        }
        return colorcode;
    }
    String makeString(int[] rgbval){
        colorval=rgbval;
        colorString=new String();
        for (int i=0;i<colorval.length;i++){
            Integer temp=new Integer(colorval[i]);
            colorString=colorString + temp.toString();
            if (i<colorval.length-1)
                colorString =colorString + ",";
        }
        return colorString;
    }
    String makeAppletCommand(String s){
        String scommand = new String(s.replace('\\','/'));
        String pcommand = new String("file:///");
        scommand = pcommand + scommand;
        return scommand;
    }

}