package com.builtbroken.mc.codegen.processor;

import com.builtbroken.mc.codegen.data.BuildData;
import com.builtbroken.mc.codegen.template.Template;
import com.builtbroken.mc.codegen.templates.processor.Processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
public class ProcessorItemNode extends Processor
{
    public static final String TILE_WRAPPER_ANNOTATION = "ItemWrapped";
    public static final String TEMPLATE_ANNOTATION = "ItemWrappedTemplate";
    public static final String CLASS_KEY = "className";
    public static final String EMPTY_TEMPLATE_KEY = "Empty";

    public ProcessorItemNode()
    {
        super(TILE_WRAPPER_ANNOTATION, TEMPLATE_ANNOTATION, "ItemBase");
    }

    @Override
    public void handleFile(File outputFolder, BuildData data, String spacer) throws IOException
    {
        String[] annotationData = data.annotations.get(annotationKey).split(",");
        List<String> wrapperKeys = new ArrayList();
        for (String s : annotationData)
        {
            //Find class name to create
            if (s.contains(CLASS_KEY))
            {
                data.outputClassName = s.split("=")[1];
                data.outputClassName = data.outputClassName.replace("\"", "").trim();
            }
            //Get wrappers to use
            else if (s.contains("wrappers"))
            {
                String wrappers = s.split("=")[1].replace("\"", "").trim(); //Remove spaces
                String[] split = wrappers.split(";"); //Split to get contents
                for (String w : split)
                {
                    String wrapperKey = w.trim(); //Remove " "
                    if (!wrapperKey.isEmpty())
                    {
                        wrapperKeys.add(wrapperKey);
                    }
                }
            }
        }
        //Ensure we have a class name
        if (data.outputClassName == null)
        {
            throw new RuntimeException("Missing " + CLASS_KEY + " from " + TILE_WRAPPER_ANNOTATION + " annotation");
        }

        //Get template processors for this file
        List<Template> templates = new ArrayList();
        for (String key : data.annotations.keySet())
        {
            if (templateMap.containsKey(key))
            {
                templates.add(templateMap.get(key));
            }
        }
        for (String key : wrapperKeys)
        {
            if (templateMap.containsKey(key))
            {
                templates.add(templateMap.get(key));
            }
        }

        //If no templates are required use the empty template
        if (templates.isEmpty())
        {
            Template template = templateMap.get(EMPTY_TEMPLATE_KEY);
            if (template != null)
            {
                templates.add(template);
            }
            else
            {
                throw new RuntimeException("Failed to load empty template");
            }
        }

        build(outputFolder, templates, data, spacer);
    }

    @Override
    protected void createConstructor(StringBuilder builder, List<Template> templates, BuildData data)
    {
        //Create constructor
        builder.append("\tpublic ");
        builder.append(data.outputClassName);
        builder.append("()\n\t{");
        builder.append("\n\t\tsuper(new ");
        builder.append(data.className);
        builder.append("());\n");
        builder.append("\t}\n\n");
    }

    @Override
    protected void collectIgnoredImports(List<String> imports, List<Template> templates, BuildData data)
    {
        //Add ignored files
        imports.add(ItemWrappedTemplate.class.getName());
    }
}
