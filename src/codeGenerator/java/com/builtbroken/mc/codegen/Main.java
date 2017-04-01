package com.builtbroken.mc.codegen;

import com.builtbroken.mc.codegen.processors.Parser;
import com.builtbroken.mc.codegen.processors.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
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
    public static Pattern packagePattern = Pattern.compile("package(.*?);");
    public static final String TILE_WRAPPER_ANNOTATION = "TileWrapped";

    public static void main(String... args)
    {
        logger = LogManager.getRootLogger();

        logger.info("VoltzEngine Code Generator v0.1.0");
        logger.info("Parsing arguments...");

        //Load arguments
        HashMap<String, String> launchSettings = loadArgs(args);

        if (launchSettings.containsKey("src") && launchSettings.containsKey("templates") && launchSettings.containsKey("output"))
        {
            File runFolder = new File(".");
            File targetFolder;
            File templateFolder;
            File outputFolder;

            //Get source folder path
            String path = launchSettings.get("src");
            if (path.startsWith("."))
            {
                targetFolder = new File(runFolder, path.substring(1, path.length()));
            }
            else
            {
                targetFolder = new File(path);
            }

            //Get template folder path
            path = launchSettings.get("templates");
            if (path.startsWith("."))
            {
                templateFolder = new File(runFolder, path.substring(1, path.length()));
            }
            else
            {
                templateFolder = new File(path);
            }

            path = launchSettings.get("output");
            if (path.startsWith("."))
            {
                outputFolder = new File(runFolder, path.substring(1, path.length()));
            }
            else
            {
                outputFolder = new File(path);
            }


            //Ensure we have a template folder
            if (!templateFolder.exists() || !templateFolder.isDirectory())
            {
                logger.info("The template folder does not exist. Folder: " + templateFolder);
                System.exit(1);
            }

            //Ensure we have an output folder
            if (outputFolder.exists() && !templateFolder.isDirectory())
            {
                logger.info("output folder is not a directory: " + outputFolder);
                System.exit(1);
            }
            else if (!outputFolder.exists())
            {
                outputFolder.mkdirs();
            }

            //Ensure we have a target source folder
            if (targetFolder.exists() && targetFolder.isDirectory())
            {
                logger.info("");
                logger.info("Loading templates from " + templateFolder);
                //Load processors
                HashMap<String, Processor> processors = getProcessors(templateFolder, 0);

                //Ensure we have templates to use
                if (processors.isEmpty())
                {
                    logger.info("No templates were loaded, can not continue with templates to use");
                    System.exit(1);
                }

                //Load classes
                logger.info("");
                logger.info("Loading classes from " + targetFolder);
                handleDirectory(targetFolder, processors, outputFolder, 0);
            }
            else
            {
                logger.info("The target folder does not exist. Folder: " + targetFolder);
                System.exit(1);
            }
        }
        else
        {
            logger.info("In order for code to be parsed and generator you need to specify in the program arguments: -src=\"path/to/source/files\" -templates=\"path/to/source/templates\" -output=\"path/to/source/output\"");
            System.exit(1);
        }

        logger.info("Exiting...");
    }

    public static void handleDirectory(File directory, HashMap<String, Processor> processors, File outputFolder, int depth)
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
                handleDirectory(file, processors, outputFolder, ++depth);
            }
            else
            {
                logger.info("");
                logger.info(spacer + "--File: " + file.getName());
                logger.info(spacer + " |------------------------->");
                try
                {
                    handleFile(file, processors, outputFolder, spacer + " |");
                }
                catch (IOException e)
                {
                    logger.error("Unexpected exception while parsing " + file, e);
                    System.exit(1);
                }
                logger.info(spacer + " |------------------------->");
            }
        }
    }

    public static void handleFile(File file, HashMap<String, Processor> allProcessors, File outputFolder, String spacer) throws IOException
    {
        String fileClassName = file.getName();
        if (fileClassName.endsWith(".java"))
        {
            fileClassName = fileClassName.substring(0, fileClassName.length() - 5);
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
                            annotations.addAll(Parser.getAnnotations(line));
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
                        id = s.split("=")[1].replace("\"", "").trim();
                    }
                    else if (s.contains("className"))
                    {
                        className = s.split("=")[1].replace("\"", "").trim();
                    }
                }

                //Ensure we have an ID
                if (id == null)
                {
                    throw new RuntimeException("Missing id from " + TILE_WRAPPER_ANNOTATION + " annotation");
                }
                //Ensure we have a class name
                if (className == null)
                {
                    throw new RuntimeException("Missing className from " + TILE_WRAPPER_ANNOTATION + " annotation");
                }

                //Get template processors for this file
                List<Processor> processors = new ArrayList();
                for (String key : annotationToData.keySet())
                {
                    if (allProcessors.containsKey(key))
                    {
                        processors.add(allProcessors.get(key));
                    }
                }

                //If no templates are required use the empty template
                if (processors.isEmpty())
                {
                    processors.add(allProcessors.get("Empty"));
                }

                //Start building file
                StringBuilder builder = new StringBuilder();
                builder.append("//THIS IS A GENERATED CLASS FILE\n");
                builder.append("package " + classPackage + ";\n");
                builder.append("\n");

                createImports(builder, processors);
                builder.append("\n");

                createClassHeader(builder, className, processors);
                builder.append("\n{\n");

                //Create constructor
                builder.append("\tpublic ");
                builder.append(className);
                builder.append("()\n\t{");
                builder.append("\n\t\tsuper(new ");
                builder.append(fileClassName);
                builder.append("()\n");
                builder.append("\t}\n\n");

                createBody(builder, processors);
                builder.append("}");

                //Write file to disk
                try
                {
                    File outFile = new File(outputFolder, classPackage.replace(".", File.separator) + File.separator + className + ".java");
                    logger.info(spacer + "  Writing file to disk, file = " + outFile);
                    if (!outFile.getParentFile().exists())
                    {
                        outFile.getParentFile().mkdirs();
                        logger.info(spacer + "   Created directories");
                    }
                    else if (outFile.exists())
                    {
                        logger.info(spacer + "   Overriding existing file");
                    }

                    FileWriter fileWriter = new FileWriter(outFile);
                    fileWriter.write(builder.toString());
                    fileWriter.flush();
                    fileWriter.close();
                }
                catch (Exception e)
                {
                    logger.info(spacer + "    Error writing file", e);
                    System.exit(1);
                }
            }
            else
            {
                logger.info(spacer + "  Does not contain " + TILE_WRAPPER_ANNOTATION);
            }
            //TODO build list of all generated data to be registered
        }
    }

    public static void createImports(StringBuilder builder, List<Processor> processors)
    {
        List<String> imports = new ArrayList();
        for (Processor processor : processors)
        {
            List<String> importsFromProcessor = processor.getImports();
            for (String imp : importsFromProcessor)
            {
                //Prevent duplication
                if (!imports.contains(imp))
                {
                    imports.add(imp);
                    builder.append("import ");
                    builder.append(imp);
                    builder.append(";\n");
                }
            }
        }

    }

    public static void createClassHeader(StringBuilder builder, String className, List<Processor> processors)
    {
        //TODO implement annotations
        //Create header
        builder.append("public class " + className + " extends TileEntityWrapper");

        //Add implements
        List<String> interfaces = new ArrayList();
        for (Processor processor : processors)
        {
            List<String> interfacesFromProcessor = processor.getInterfaces();
            for (String imp : interfacesFromProcessor)
            {
                //Prevent duplication
                if (!interfaces.contains(imp))
                {
                    interfaces.add(imp);
                }
            }
        }
        if (interfaces != null)
        {
            builder.append(" implements ");
            for (int i = 0; i < interfaces.size(); i++)
            {
                builder.append(interfaces.get(i));
                if (i != (interfaces.size() - 1))
                {
                    builder.append(", ");
                }
            }
        }
    }

    public static void createBody(StringBuilder builder, List<Processor> processors)
    {

    }

    public static HashMap<String, Processor> getProcessors(File directory, int depth)
    {
        String spacer;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= depth; i++)
        {
            builder.append("  ");
        }
        spacer = builder.toString();

        logger.info(spacer + "*Directory: " + directory.getName());

        HashMap<String, Processor> map = new HashMap();
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                map.putAll(getProcessors(file, ++depth));
            }
            else
            {
                logger.info("");
                logger.info(spacer + "--File: " + file.getName());
                logger.info(spacer + " |------------------------->");
                Processor processor = new Processor();
                try
                {

                    processor = processor.loadFile(file, spacer + " | ");
                    //If returns null the file was not a template
                    if (processor != null)
                    {
                        if (processor.isValid())
                        {
                            map.put(processor.getKey(), processor);
                        }
                        else
                        {
                            logger.info("Template file is invalid, exiting to prevent issues " + file);
                            System.exit(1);
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.info("Unexpected error while loading template from file " + file, e);
                    System.exit(1);
                }
                logger.info(spacer + " |------------------------->");
            }
        }
        return map;
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
