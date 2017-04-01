package com.builtbroken.mc.codegen.processors;

import com.builtbroken.mc.codegen.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
public class Processor
{
    //regex101.com <- used to test pattterns, great site
    public static Pattern importPattern = Pattern.compile("import(.*?);");
    public static Pattern multiLineCommentPattern = Pattern.compile("\\/\\*(.*?)\\*\\/");
    public static Pattern extendsPattern = Pattern.compile("extends(.*?)implements");
    public static Pattern extendsPattern2 = Pattern.compile("extends(.*?)\\{");
    public static Pattern implementsPattern = Pattern.compile("implements(.*?)\\{");

    private List<String> imports;
    private List<String> interfaces;
    //private HashMap<String, Method> methods = new HashMap();
    //private HashMap<String, Field> fields = new HashMap();

    String classExtending;
    private boolean valid;
    private String key;

    public Processor loadFile(File file) throws IOException
    {
        //TODO load the file
        //TODO parse out all data
        //TODO ensure extends TileEntityWrapper and nothing else
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        try
        {
            String line;

            //Read until we hit the class annotations
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.startsWith("@"))
                {
                    break;
                }
                else
                {
                    builder.append(line);
                }
            }

            //Convert to string and parse imports
            String string = builder.toString();
            Matcher matcher = importPattern.matcher(string);
            if (matcher.matches())
            {
                for (int i = 1; i <= matcher.groupCount(); i++)
                {
                    imports.add(matcher.group(i).trim());
                }
            }

            //Read in remaining annotations
            builder = new StringBuilder();
            builder.append(line); //Add last line since it was not used in imports
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.contains("class"))
                {
                    if (!line.startsWith("class"))
                    {
                        //This does not need to be perfect as all that matters is
                        //  the annotations gets the rest of there parts
                        //      and the rest is keep for imports
                        builder.append(line.substring(0, line.indexOf("class")));
                        line = line.substring(line.indexOf("class"), line.length());
                    }
                    break;
                }
                else
                {
                    builder.append(line);
                }
            }

            //Match annotations from builder
            string = builder.toString();
            List<String> annotations = Parser.getAnnotations(string);

            //Read everything until start of class body
            builder = new StringBuilder();
            builder.append(line); //Add last line since it was not used in annotations
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                //Ignore comments
                if (!line.startsWith("//"))
                {
                    //if line contains a comment at end remove
                    if (line.contains("//"))
                    {
                        line = line.substring(0, line.indexOf("/"));
                        Main.logger.warn("Found comment '" + line.substring(line.indexOf("/"), line.length()) + "' nested inside class header, commends should not be nested inside the class header. Remove these to improve class parsing and to improve readability.");

                    }
                    if (line.contains("{"))
                    {
                        builder.append(line);
                        break;
                    }
                    else
                    {
                        builder.append(line);
                    }
                }
            }
            string = builder.toString();

            //Remove comments and java docs from header
            matcher = multiLineCommentPattern.matcher(string);
            if (matcher.matches())
            {
                for (int i = 1; i <= matcher.groupCount(); i++)
                {
                    String comment = "/*" + matcher.group(i) + "*/";
                    string = string.replace(comment, "");
                    Main.logger.warn("Found comment '" + comment + "' nested inside class header, commends should not be nested inside the class header. Remove these to improve class parsing and to improve readability.");
                }
            }

            //Match for extends
            matcher = extendsPattern.matcher(string);
            //Check if pattern 1 works, extends class implements
            if (matcher.matches())
            {
                 classExtending = matcher.group(1);
            }
            //else try pattern 2, extends class {
            else
            {
                matcher = extendsPattern2.matcher(string);
                if (matcher.matches())
                {
                    classExtending = matcher.group(1);
                }
            }

            //Match interfaces
            matcher = implementsPattern.matcher(string);
            if (matcher.matches())
            {
                String[] imps = matcher.group(1).trim().split(",");
                for(String imp :imps)
                {
                    interfaces.add(imp);
                }
            }

        }
        finally
        {
            br.close();
        }

        return this;
    }

    public List<String> getImports()
    {
        return imports;
    }

    public boolean isValid()
    {
        return valid;
    }

    public String getKey()
    {
        return key;
    }
}
