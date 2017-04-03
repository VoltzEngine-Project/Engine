package com.builtbroken.mc.codegen.processor;

import com.builtbroken.mc.codegen.template.Template;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
public class ProcessorTileNode extends Processor
{
    public static final String TILE_WRAPPER_ANNOTATION = "TileWrapped";
    public static final String ID_KEY = "id";
    public static final String CLASS_KEY = "className";
    public static final String EMPTY_TEMPLATE_KEY = "Empty";

    public ProcessorTileNode()
    {
        super(TILE_WRAPPER_ANNOTATION);
    }

    @Override
    public void handleFile(File outputFolder, HashMap<String, String> annotations, String classPackage, String fileClassName, String spacer) throws IOException
    {
        String[] annotationData = annotations.get(annotationKey).split(",");
        String id = null;
        String className = null;
        for (String s : annotationData)
        {
            if (s.contains(ID_KEY))
            {
                id = s.split("=")[1].replace("\"", "").trim();
            }
            else if (s.contains(CLASS_KEY))
            {
                className = s.split("=")[1].replace("\"", "").trim();
            }
        }

        //Ensure we have an ID
        if (id == null)
        {
            throw new RuntimeException("Missing " + ID_KEY + " from " + TILE_WRAPPER_ANNOTATION + " annotation");
        }
        //Ensure we have a class name
        if (className == null)
        {
            throw new RuntimeException("Missing " + CLASS_KEY + " from " + TILE_WRAPPER_ANNOTATION + " annotation");
        }

        //Get template processors for this file
        List<Template> templates = new ArrayList();
        for (String key : annotations.keySet())
        {
            if (templateMap.containsKey(key))
            {
                templates.add(templateMap.get(key));
            }
        }

        //If no templates are required use the empty template
        if (templates.isEmpty())
        {
            templates.add(templateMap.get(EMPTY_TEMPLATE_KEY));
        }

        build(outputFolder, templates, classPackage, className, fileClassName, spacer);
    }

    @Override
    protected void createConstructor(StringBuilder builder, String className, String fileClassName, List<Template> templates)
    {
        //Create constructor
        builder.append("\tpublic ");
        builder.append(className);
        builder.append("()\n\t{");
        builder.append("\n\t\tsuper(new ");
        builder.append(fileClassName);
        builder.append("());\n");
        builder.append("\t}\n\n");
    }

    @Override
    protected void collectIgnoredImports(List<String> imports, List<Template> templates)
    {
        //Add ignored files
        imports.add("com.builtbroken.mc.codegen.processors.TileWrappedTemplate");

        //Check if we can ignore imports
        boolean containsITileNodeImport = false;
        for (Template template : templates)
        {
            if (template.fieldBody != null && template.fieldBody.contains("ITileNode"))
            {
                containsITileNodeImport = true;
                break;
            }
            if (template.methodBody != null && template.methodBody.contains("ITileNode"))
            {
                containsITileNodeImport = true;
                break;
            }
        }

        if (!containsITileNodeImport)
        {
            imports.add("com.builtbroken.mc.framework.logic.ITileNode");
        }
    }
}
