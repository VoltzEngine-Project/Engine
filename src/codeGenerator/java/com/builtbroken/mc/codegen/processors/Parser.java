package com.builtbroken.mc.codegen.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
public class Parser
{
    public static Pattern annotationPattern = Pattern.compile("@(.*?)\\)");

    public static List<String> getAnnotations(String line)
    {
        List<String> annotations = new ArrayList();

        final Matcher matcher = annotationPattern.matcher(line);
        while(matcher.find())
        {
            for (int i = 1; i <= matcher.groupCount(); i++)
            {
                annotations.add((matcher.group(i) + ")").trim());
            }
        }
        return annotations;
    }
}
