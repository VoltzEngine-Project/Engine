package com.builtbroken.mc.prefab.json;

import com.builtbroken.mc.core.References;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import cpw.mods.fml.common.registry.GameRegistry;
import li.cil.oc.common.block.Item;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public final class JsonContentLoader extends AbstractLoadable
{
    private final List<String> resources = new ArrayList();
    private final List<String> extentionsToLoad = new ArrayList();

    private final List<JsonProcessor> processors = new ArrayList();
    private final List<IJsonGenObject> generatedObjects = new ArrayList();
    public JsonBlockProcessor blockProcessor;

    public static JsonContentLoader INSTANCE = new JsonContentLoader();

    private JsonContentLoader()
    {
    }

    /**
     * Adds the processor to the list of processors
     *
     * @param processor
     */
    public static void registerProcessor(JsonProcessor processor)
    {
        INSTANCE.processors.add(processor);
    }

    @Override
    public void preInit()
    {
        blockProcessor = new JsonBlockProcessor();
        //Load processors
        processors.add(blockProcessor);
        blockProcessor.addSubProcessor("smeltingRecipe", new JsonBlockSmeltingProcessor());
        blockProcessor.addSubProcessor("worldGenerator", new JsonBlockWorldGenProcessor());
    }

    @Override
    public void init()
    {
        //TODO move to a thread to improve load time
        final File folder = new File(References.BBM_CONFIG_FOLDER, "json");
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        //We have an external folder we should see what it contains
        else
        {
            loadResourcesFromFolder(folder);
        }

        loadResourcesFromPackage("content/", "content");

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
     * Called to load json files from the folder
     *
     * @param folder
     */
    private void loadResourcesFromFolder(File folder)
    {
        for (File file : folder.listFiles())
        {
            if (file.isDirectory())
            {
                loadResourcesFromFolder(folder);
            }
            else if (file.getName().endsWith(".json"))
            {
                resources.add(file.getAbsolutePath());
            }
            else
            {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
                if (extentionsToLoad.contains(extension))
                {
                    resources.add(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Loads package
     *
     * @param folder   - package your looking to load data from
     * @param location - that package's location
     */
    protected void loadResourcesFromPackage(String folder, String location)
    {
        try
        {
            final List<String> files = IOUtils.readLines(JsonContentLoader.class.getClassLoader().getResourceAsStream(folder), Charsets.UTF_8);
            for (String s : files)
            {
                if (s.endsWith(".json"))
                {
                    resources.add(location + "/" + folder);
                }
                else if (s.lastIndexOf(".") > 1)
                {
                    String extension = s.substring(s.lastIndexOf(".") + 1, s.length());
                    if (extentionsToLoad.contains(extension))
                    {
                        resources.add(location + "/" + folder);
                    }
                }
                else
                {
                    loadResourcesFromPackage(s, location + "/" + folder);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();

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
        URL url = JsonContentLoader.class.getClassLoader().getResource(resource);
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

    /**
     * Takes a string and attempts to convert into into a useable
     * {@link ItemStack}. Does not support NBT due to massive
     * amount of nesting and complexity that NBT can have. If
     * you want to use this try the Json version.
     *
     * @param string - simple string
     * @return ItemStack, or null
     */
    public static ItemStack fromString(String string)
    {
        //TODO move to helper as this will be reused, not just in Json
        if (string.startsWith("item[") || string.startsWith("block["))
        {
            String out = string.substring(string.indexOf("[") + 1, string.length() - 1); //ends ]
            int meta = -1;

            //Meta handling
            if (out.contains("@"))
            {
                String[] split = out.split("@");
                out = split[0];
                try
                {
                    meta = Integer.parseInt(split[1]);
                }
                catch (NumberFormatException e)
                {
                    throw new IllegalArgumentException();
                }
            }

            //Ensure domain, default to MC
            if (!out.contains(":"))
            {
                out = "minecraft:" + out;
            }
            //TODO implement short hand eg cobble - > CobbleStone

            if (string.startsWith("item["))
            {
                Object obj = Item.itemRegistry.getObject(out);
                if (obj instanceof Item)
                {
                    if (meta > -1)
                    {
                        return new ItemStack((Item) obj);
                    }
                    else
                    {
                        return new ItemStack((Item) obj, 1, meta);
                    }
                }
            }
            else
            {
                Object obj = Block.blockRegistry.getObject(out);
                if (obj instanceof Block)
                {
                    if (meta > -1)
                    {
                        return new ItemStack((Block) obj);
                    }
                    else
                    {
                        return new ItemStack((Block) obj, 1, meta);
                    }
                }
            }
        }
        //Ore Names
        else if (OreDictionary.doesOreNameExist(string))
        {
            List<ItemStack> ores = OreDictionary.getOres(string);
            for (ItemStack stack : ores)
            {
                if (stack != null)
                {
                    return stack;
                }
            }
        }
        return null;
    }

    public static ItemStack fromJson(JsonObject json)
    {
        ItemStack output = null;
        String type = json.get("type").getAsString();
        String item = json.get("item").getAsString();

        int meta = -1;
        if (json.has("meta"))
        {
            meta = json.get("meta").getAsInt();
        }

        if (type.equalsIgnoreCase("block"))
        {
            Object obj = Item.itemRegistry.getObject(item);
            if (obj instanceof Block)
            {
                if (meta > -1)
                {
                    output = new ItemStack((Block) obj);
                }
                else
                {
                    output = new ItemStack((Block) obj, 1, meta);
                }
            }
        }
        else if (type.equalsIgnoreCase("item"))
        {
            Object obj = Item.itemRegistry.getObject(item);
            if (obj instanceof Item)
            {
                if (meta > -1)
                {
                    return new ItemStack((Item) obj);
                }
                else
                {
                    return new ItemStack((Item) obj, 1, meta);
                }
            }
        }
        else if (type.equalsIgnoreCase("dict"))
        {
            List<ItemStack> ores = OreDictionary.getOres(item);
            for (ItemStack stack : ores)
            {
                if (stack != null)
                {
                    output = stack;
                    break;
                }
            }
        }

        if (output != null && json.has("nbt"))
        {
            NBTTagCompound tag = new NBTTagCompound();
            processNBTTagCompound(json.getAsJsonObject("nbt"), tag);
        }
        return output;
    }

    /**
     * Loads NBT data from a json object
     *
     * @param json - json object, converted to entry set
     * @param tag  - tag to save the data to
     */
    public static void processNBTTagCompound(JsonObject json, NBTTagCompound tag)
    {
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            if (entry.getValue().isJsonPrimitive())
            {
                JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                if (primitive.isBoolean())
                {
                    tag.setBoolean(entry.getKey(), primitive.getAsBoolean());
                }
                else if (primitive.isNumber())
                {
                    tag.setInteger(entry.getKey(), primitive.getAsInt());
                }
                else if (primitive.isString())
                {
                    tag.setString(entry.getKey(), primitive.getAsString());
                }
            }
            else if (entry.getValue().isJsonObject())
            {
                JsonObject object = entry.getValue().getAsJsonObject();
                if (object.has("type"))
                {
                    String type = object.get("type").getAsString();
                    if (type.equalsIgnoreCase("tagCompound"))
                    {
                        NBTTagCompound nbt = new NBTTagCompound();
                        processNBTTagCompound(object, nbt);
                        tag.setTag(entry.getKey(), nbt);
                    }
                    else if (type.equalsIgnoreCase("int"))
                    {
                        tag.setInteger(entry.getKey(), entry.getValue().getAsInt());
                    }
                    else if (type.equalsIgnoreCase("double"))
                    {
                        tag.setDouble(entry.getKey(), entry.getValue().getAsDouble());
                    }
                    else if (type.equalsIgnoreCase("float"))
                    {
                        tag.setFloat(entry.getKey(), entry.getValue().getAsFloat());
                    }
                    else if (type.equalsIgnoreCase("byte"))
                    {
                        tag.setByte(entry.getKey(), entry.getValue().getAsByte());
                    }
                    else if (type.equalsIgnoreCase("short"))
                    {
                        tag.setShort(entry.getKey(), entry.getValue().getAsShort());
                    }
                    else if (type.equalsIgnoreCase("long"))
                    {
                        tag.setLong(entry.getKey(), entry.getValue().getAsLong());
                    }
                    //TODO add byte array
                    //TODO add int array
                    //TODO add tag list
                }
                else
                {
                    NBTTagCompound nbt = new NBTTagCompound();
                    processNBTTagCompound(object, nbt);
                    tag.setTag(entry.getKey(), nbt);
                }
            }
        }
    }
}
