package com.builtbroken.mc.prefab.json;

import com.builtbroken.mc.content.VeContent;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.core.registry.implement.IRecipeContainer;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.prefab.json.block.processor.JsonBlockProcessor;
import com.builtbroken.mc.prefab.json.block.processor.JsonBlockSmeltingProcessor;
import com.builtbroken.mc.prefab.json.block.processor.JsonBlockWorldGenProcessor;
import com.builtbroken.mc.prefab.json.imp.IJsonGenObject;
import com.builtbroken.mc.prefab.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.crafting.IRecipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonContentLoader extends AbstractLoadable
{
    List<String> resources = new ArrayList();
    List<JsonProcessor> processors = new ArrayList();
    List<IJsonGenObject> generatedObjects = new ArrayList();
    public JsonBlockProcessor blockProcessor;

    @Override
    public void preInit()
    {
        //TODO find mods that need shit loaded - MetaInf file -> Directories -> Sift
        blockProcessor = new JsonBlockProcessor();
        //Load processors
        processors.add(blockProcessor);
        blockProcessor.addSubProcessor("smeltingRecipe", new JsonBlockSmeltingProcessor());
        blockProcessor.addSubProcessor("worldGenerator", new JsonBlockWorldGenProcessor());
    }

    @Override
    public void init()
    {
        for (String resource : resources)
        {
            try
            {
                JsonElement element = loadJsonFileFromResources(resource);
                for (JsonProcessor processor : processors)
                {
                    if (processor.canProcess(element))
                    {
                        IJsonGenObject data = processor.process(element);
                        data.register();
                        if (data instanceof IRegistryInit)
                        {
                            ((IRegistryInit) data).onRegistered();
                        }
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                //Crash as the file may be important
                throw new RuntimeException("Failed to load resource " + resource, e);
            }
        }
    }

    /**
     * Loads a json file from the resource path
     *
     * @param resource - resource location
     * @return json file as a json element object
     * @throws IOException
     */
    public static JsonElement loadJsonFileFromResources(String resource) throws IOException
    {
        URL url = VeContent.class.getClassLoader().getResource(resource);
        InputStream stream = url.openStream();
        JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
        JsonElement element = Streams.parse(jsonReader);
        stream.close();
        return element;
    }

    @Override
    public void postInit()
    {
        for (IJsonGenObject obj : generatedObjects)
        {
            if (obj instanceof IPostInit)
            {
                ((IPostInit) obj).onPostInit();
            }
            if (obj instanceof IRecipeContainer)
            {
                List<IRecipe> recipes = new ArrayList();
                ((IRecipeContainer) obj).genRecipes(recipes);
                for (IRecipe recipe : recipes)
                {
                    if (recipe != null && recipe.getRecipeOutput() != null)
                    {
                        GameRegistry.addRecipe(recipe);
                    }
                }
            }
        }
    }
}
