package com.internal.threadexmpl;

/**
 * Created by DerpPC on 2/7/2015.
 */
import java.util.regex.*;

/**
 * Created by DerpPC on 1/1/2015.
 */
public class UrlCheck
{
    public static CharSequence isUrlCorrect(CharSequence text)
    {

        String rex = "\\b(?:http:)|\\b(?:https://)|\\b(?:ftp://)";
        Pattern pat = Pattern.compile(rex);
        Matcher mat = pat.matcher(text.toString());

        if(mat.find())
        {
            return text;
        }
        else
        {
            return "http://"+text;
        }
    }


}
