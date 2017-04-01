package com.builtbroken.mc.codegen;

import com.builtbroken.mc.codegen.processors.Processor;
import com.builtbroken.mc.framework.logic.annotations.TileWrapped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
public class Main
{
    public static Logger logger;
    public static Pattern annotationPattern = Pattern.compile("@(.*?)\\)");
    public static Pattern packagePattern = Pattern.compile("package(.*?);");
    public static final String TILE_WRAPPER_ANNOTATION = TileWrapped.class.getName();

    public static void main(String... args)
    {
        logger = LogManager.getRootLogger();

        logger.info("VoltzEngine Code Generator v0.1.0");
        logger.info("Parsing arguments...");

        //Load arguments
        HashMap<String, String> launchSettings = loadArgs(args);

        if (launchSettings.containsKey("src"))
        {
            File runFolder = new File(".");
            File targetFolder;
            String path = launchSettings.get("src");
            if (path.startsWith("."))
            {
                targetFolder = new File(runFolder, path.substring(1, path.length()));
            }
            else
            {
                targetFolder = new File(path);
            }
            List<Processor> processors = getProcessors();

            if (targetFolder.exists() && targetFolder.isDirectory())
            {
                logger.info("Scanning files");
                handleDirectory(targetFolder, processors, 0);
            }
            else
            {
                logger.info("The target folder does not exist. Folder: " + targetFolder);
                System.exit(1);
            }
        }
        else
        {
            logger.info("In order for code to be parsed and generator you need to specify: src=\"path/to/source/files\"");
            System.exit(1);
        }

        logger.info("Exiting...");
    }

    public static void handleDirectory(File directory, List<Processor> processors, int depth)
    {
        //Generate spacer to make debug look nice
        String spacer;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= depth; i++)
        {
            builder.append("  ");
        }
        spacer = builder.toString();

        logger.info(spacer + "*Directory: " + directory.getName());

        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                handleDirectory(file, processors, ++depth);
            }
            else
            {
                logger.info(spacer + "--File: " + file.getName());
                try
                {
                    handleFile(file, processors, spacer);
                }
                catch (IOException e)
                {
                    logger.error("Unexpected exception while parsing " + file, e);
                    System.exit(1);
                }
            }
        }
    }

    public static void handleFile(File file, List<Processor> processors, String spacer) throws IOException
    {
        if (file.getName().endsWith(".java"))
        {
            String classPackage = null;
            List<String> annotations = new ArrayList();
            BufferedReader br = new BufferedReader(new FileReader(file));
            try
            {
                String line;

                while ((line = br.readLine()) != null)
                {
                    //Ignore all import lines so not to parse {} or @ in imports
                    if (!line.contains("import"))
                    {
                        if (line.contains("package"))
                        {
                            final Matcher matcher = packagePattern.matcher(line);
                            if (matcher.matches())
                            {
                                classPackage = matcher.group(1).trim();
                            }
                        }
                        else if (line.contains("@"))
                        {
                            final Matcher matcher = annotationPattern.matcher(line);
                            if (matcher.matches())
                            {
                                for (int i = 1; i <= matcher.groupCount(); i++)
                                {
                                    annotations.add((matcher.group(i) + ")").trim());
                                }
                            }
                        }
                        //First { should be the end of the class header
                        else if (line.contains("{"))
                        {
                            break;
                        }
                    }
                }
            }
            finally
            {
                br.close();
            }

            HashMap<String, String> annotationToData = new HashMap();
            //Debug data
            logger.info(spacer + "  Package: " + classPackage);
            logger.info(spacer + "  Annotations:");

            //Output annotation and parse
            for (String string : annotations)
            {
                logger.info(spacer + "      " + string);

                int firstParn = string.indexOf("(");
                String annotation = string.substring(0, firstParn);
                String data = string.substring(firstParn + 1, string.length() - 1);
                annotationToData.put(annotation, data);
            }

            //Process annotations
            if (annotationToData.containsKey(TILE_WRAPPER_ANNOTATION))
            {
                String[] data = annotationToData.get(TILE_WRAPPER_ANNOTATION).split(",");
                String id = null;
                String className = null;
                for (String s : data)
                {
                    if (s.contains("id"))
                    {
                        id = s.split("=")[1].trim();
                    }
                    else if (s.contains("className"))
                    {
                        className = s.split("=")[1].trim();
                    }
                }

                if (id == null)
                {
                    throw new RuntimeException("Missing id from " + TILE_WRAPPER_ANNOTATION + " annotation");
                }
                if (className == null)
                {
                    throw new RuntimeException("Missing className from " + TILE_WRAPPER_ANNOTATION + " annotation");
                }

                StringBuilder builder = new StringBuilder();
                builder.append("//THIS IS A GENERATED CLASS FILE\n");
                builder.append("package " + classPackage + ";\n");
                builder.append("\n");

                createImports(builder, processors);
                builder.append("\n");

                createClassHeader(builder, className, processors);
                builder.append("{\n");
                createBody(builder, processors);
                builder.append("}");

            }
            else
            {
                logger.info(spacer + "  Does not contain " + TILE_WRAPPER_ANNOTATION);
            }

            //TODO match annotations to processors
            //TODO generate files
            //TODO if file already exists append
            //TODO build list of all generated data to be registered
        }
    }

    public static void createImports(StringBuilder builder, List<Processor> processors)
    {
        List<String> imports = new ArrayList();
        for(Processor processor : processors)
        {
            List<String> importsFromProcessor = processor.getImports();
        }
    }

    public static void createClassHeader(StringBuilder builder, String className, List<Processor> processors)
    {

    }

    public static void createBody(StringBuilder builder, List<Processor> processors)
    {

    }

    public static List<Processor> getProcessors()
    {
        //TODO replace with plugin system
        List<Processor> list = new ArrayList();
        //TODO create processor
        //TODO load processor data
        //TODO parse processor templates

        return list;
    }

    /**
     * Converts arguments into a hashmap for usage
     *
     * @param args
     * @return
     */
    public static HashMap<String, String> loadArgs(String... args)
    {
        final HashMap<String, String> map = new HashMap();
        if (args != null)
        {
            String currentArg = null;
            String currentValue = "";
            for (int i = 0; i < args.length; i++)
            {
                String next = args[i].trim();
                if (next == null)
                {
                    throw new IllegalArgumentException("Null argument detected in launch arguments");
                }
                else if (next.startsWith("-"))
                {
                    if (currentArg != null)
                    {
                        map.put(currentArg, currentValue);
                        currentValue = "";
                    }

                    if (next.contains("="))
                    {
                        String[] split = next.split("=");
                        currentArg = split[0].substring(1).trim();
                        currentValue = split[1].trim();
                    }
                    else
                    {
                        currentArg = next.substring(1).trim();
                    }
                }
                else if (currentArg != null)
                {
                    if (!currentValue.isEmpty())
                    {
                        currentValue += ",";
                    }
                    currentValue += next.replace("\"", "").replace("'", "").trim();
                }
                else
                {
                    throw new IllegalArgumentException("Value has no argument associated with it [" + next + "]");
                }
            }
            //Add the last loaded value to the map
            if (currentArg != null)
            {
                map.put(currentArg, currentValue);
            }
        }
        return map;
    }
}
