package com.builtbroken.mc.codegen.processors;

import com.builtbroken.mc.codegen.Main;
import com.builtbroken.mc.framework.logic.wrapper.TileEntityWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

    public static Pattern methodBodyPattern = Pattern.compile("(?s)#StartMethods#(.*?)#EndMethods#(?-s)");
    public static Pattern fieldBodyPattern = Pattern.compile("(?s)#StartFields#(.*?)#EndFields#(?-s)");

    private List<String> imports = new ArrayList();
    private List<String> interfaces = new ArrayList();
    private List<String> annotations = new ArrayList();
    //private HashMap<String, Method> methods = new HashMap();
    //private HashMap<String, Field> fields = new HashMap();
    private String fieldBody; //TODO replace with list of fields
    private String methodBody; //TODO replace with list of methods

    String classExtending;
    private boolean valid = true;
    private String key;

    /**
     * @param file   - file to load
     * @param spacer - spacer for debug output, makes the messages look nice
     * @return
     * @throws IOException
     */
    public Processor loadFile(File file, String spacer) throws IOException
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
            while (matcher.find())
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
            annotations.addAll(Parser.getAnnotations(string));
            Main.logger.info(spacer + "  Annotations:");
            //Output annotation and parse
            for (String annotation : annotations)
            {
                Main.logger.info(spacer + "      " + annotation);
                if (annotation.startsWith("TileWrappedTemplate"))
                {
                    String data = annotation.substring(annotation.indexOf("(") + 1, annotation.length() - 1);
                    key = data.split("=")[1].replace("\"", "").trim();
                }
            }

            if (key == null)
            {
                Main.logger.info(spacer + "Class does not contain " + TileWrappedTemplate.class.getName() + " or the key set was empty");
                valid = false;
                return null;
            }

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
                        if (!line.startsWith("{"))
                        {
                            line = line.substring(line.indexOf("{") + 1, line.length());
                        }
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
            while (matcher.find())
            {
                for (int i = 1; i <= matcher.groupCount(); i++)
                {
                    String comment = "/*" + matcher.group(i) + "*/";
                    string = string.replace(comment, "");
                    Main.logger.warn(spacer + "Found comment '" + comment + "' nested inside class header, commends should not be nested inside the class header. Remove these to improve class parsing and to improve readability.");
                }
            }

            //Match for extends
            Main.logger.info(spacer + "  Extends:");
            matcher = extendsPattern.matcher(string);
            //Check if pattern 1 works, extends class implements
            if (matcher.find())
            {
                classExtending = matcher.group(1).trim();
            }
            //else try pattern 2, extends class {
            if (classExtending == null)
            {
                matcher = extendsPattern2.matcher(string);
                if (matcher.find())
                {
                    classExtending = matcher.group(1).trim();
                }
            }

            //Validate
            if (classExtending != null)
            {
                Main.logger.info(spacer + "      " + classExtending);
                if (!classExtending.equals("TileEntityWrapper"))
                {
                    Main.logger.info(spacer + "      Error class must extend " + TileEntityWrapper.class.getName());
                    valid = false;
                    return this;
                }
            }
            else
            {
                Main.logger.info(spacer + "      none");
                Main.logger.info(spacer + "      Error class must extend something");
                valid = false;
                return this;
            }

            //Match interfaces
            Main.logger.info(spacer + "  Interfaces:");
            matcher = implementsPattern.matcher(string);
            while (matcher.find())
            {
                String[] imps = matcher.group(1).trim().split(",");
                for (String imp : imps)
                {
                    interfaces.add(imp.trim());
                    Main.logger.info(spacer + "      " + imp);
                }
            }
            if (interfaces.isEmpty())
            {
                Main.logger.info(spacer + "      none");
            }

            //Read reset of file
            builder = new StringBuilder();
            builder.append(line); //Add remaining bits of line
            while ((line = br.readLine()) != null)
            {
                builder.append(line.trim());
            }
            string = builder.toString();

            //match fields body
            matcher = fieldBodyPattern.matcher(string);
            if (matcher.find())
            {
                fieldBody = matcher.group(1);
            }

            //Match method body
            matcher = methodBodyPattern.matcher(string);
            if (matcher.find())
            {
                methodBody = matcher.group(1);
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

    public List<String> getInterfaces()
    {
        return interfaces;
    }
}
