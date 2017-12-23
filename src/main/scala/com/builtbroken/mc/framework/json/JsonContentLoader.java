package com.builtbroken.mc.framework.json;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.registry.implement.ILoadComplete;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.core.registry.implement.IRecipeContainer;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.framework.json.conversion.IJsonConverter;
import com.builtbroken.mc.framework.json.event.JsonEntryCreationEvent;
import com.builtbroken.mc.framework.json.event.JsonProcessorRegistryEvent;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.imp.JsonLoadPhase;
import com.builtbroken.mc.framework.json.loading.JsonEntry;
import com.builtbroken.mc.framework.json.loading.JsonLoader;
import com.builtbroken.mc.framework.json.loading.ProcessorKeySorter;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.framework.mod.loadable.ILoadable;
import com.google.gson.JsonElement;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Content loading system designed to use JSON files and processors to generate game content.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public final class JsonContentLoader extends AbstractLoadable
{
    /** Processor instance */
    public static JsonContentLoader INSTANCE = new JsonContentLoader();

    /** Internal files for loading */
    public final List<URL> classPathResources = new ArrayList();
    /** External files for loading */
    public final List<File> externalFiles = new ArrayList();
    /** External jar files for loading */
    public final List<File> externalJarFiles = new ArrayList();

    /** Extensions that can be loaded by the system, defaults with .json */
    public final List<String> extensionsToLoad = new ArrayList();

    /** List of processors to handle files */
    public final HashMap<String, IJsonProcessor> processors = new HashMap();
    /** Entries loaded from file system */
    public final HashMap<String, List<JsonEntry>> jsonEntries = new HashMap();
    /** List of objects generated by processors */
    public final HashMap<String, List<IJsonGenObject>> generatedObjects = new HashMap();


    /** Used almost entirely by unit testing to disable file loading */
    public boolean ignoreFileLoading = false;

    /** Path to external content folder */
    public File externalContentFolder;

    /** Object used to wrap the logger to produce clearner debug messages */
    public DebugPrinter debug;

    protected JsonLoadPhase currentPhase = JsonLoadPhase.HANDLERS;

    /**
     * Left public for JUnit testing
     * use {@link #INSTANCE} to access system
     */
    public JsonContentLoader()
    {
        debug = new DebugPrinter(LogManager.getLogger("JsonContentLoader"));
        extensionsToLoad.add("json");
    }

    /**
     * Adds the processor to the list of processors
     *
     * @param processor
     */
    public void add(IJsonProcessor processor)
    {
        debug.start("Added Processor< " + processor.getJsonKey() + ", " + processor + " >");
        add(processor.getJsonKey(), processor);

        //Fire event to hook processors
        MinecraftForge.EVENT_BUS.post(new JsonProcessorRegistryEvent(this, currentPhase, processor));

        //Register loaders
        if (processor instanceof ILoadable)
        {
            Engine.loaderInstance.getModuleLoader().applyModule((ILoadable) processor);
            debug.log("-is loadable");
        }
        if (processor instanceof IJsonConverter)
        {
            JsonLoader.addConverter((IJsonConverter) processor);
            debug.log("-is converter");
        }

        //TODO add item sub processors
        debug.end();
    }

    protected void add(String key, IJsonProcessor processor)
    {
        processors.put(key, processor);
    }

    public IJsonProcessor get(String key)
    {
        return processors.get(key);
    }

    public IJsonProcessor find(String key)
    {
        IJsonProcessor processor = get(key);
        if (processor == null)
        {
            for (IJsonProcessor p : processors.values())
            {
                if (p.getJsonKey().equalsIgnoreCase(key))
                {
                    return p;
                }
            }
        }
        return processor;
    }

    @Override
    public void preInit()
    {
        debug.start("Phase: Pre-Init");
        //---------------------------------------------------------------------------

        debug.start("Validating file paths");
        //Init data
        externalContentFolder = new File(References.BBM_CONFIG_FOLDER, "json");
        //Validate data
        validateFilePaths();
        debug.end("Done...");
        //===========================================================================
        debug.start("Registering mod processors");
        for (ModContainer container : Loader.instance().getModList())
        {
            Object mod = container.getMod();
            if (mod instanceof IJsonGenMod)
            {
                try
                {
                    debug.log("Mod: " + container.getName() + "  " + container.getDisplayVersion());
                    ((IJsonGenMod) mod).loadJsonContentHandlers();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unexpected error while loading JSON content handlers from " + ((IJsonGenMod) mod).getDomain(), e);
                }
            }
        }
        triggerPhase(JsonLoadPhase.HANDLERS);
        debug.end("Done...");
        //===========================================================================

        debug.start("Loading files");
        //Resources are loaded before they can be processed to allow early processing
        if (!ignoreFileLoading)
        {
            //Load resources from file system
            loadResources();
        }
        else
        {
            debug.log("Resource loading is disable, this might be due to testing suits or other reasons");
            debug.log("JUnit: " + Engine.isJUnitTest());
        }
        debug.end("Done...");

        //===========================================================================
        debug.start("Process Run[1]");
        processEntries();
        debug.end("Done... " + jsonEntries.size() + " entries left");

        triggerPhase(JsonLoadPhase.BLOCKS); //TODO load at end of each processor run
        triggerPhase(JsonLoadPhase.ITEMS);
        triggerPhase(JsonLoadPhase.CONTENT);
        triggerPhase(JsonLoadPhase.ENTITIES);
        triggerPhase(JsonLoadPhase.LOAD_PHASE_ONE);
        //---------------------------------------------------------------------------
        debug.end("Done...");
    }

    /**
     * Called to trigger a load phase on an object.
     * <p>
     * Phases are used to allow objects to register
     * content at key times.
     *
     * @param phase
     */
    public void triggerPhase(JsonLoadPhase phase)
    {
        currentPhase = phase;
        for (List<IJsonGenObject> list : generatedObjects.values())
        {
            for (IJsonGenObject object : list)
            {
                object.onPhase(phase);
            }
        }
    }

    @Override
    public void init()
    {
        debug.start("Phase: Init");
        debug.start("Process Run[2]");
        processEntries();
        debug.end("Done... " + jsonEntries.size() + " entries left");

        triggerPhase(JsonLoadPhase.RECIPES);
        triggerPhase(JsonLoadPhase.LOAD_PHASE_TWO);
        debug.end("Done...");
    }

    @Override
    public void postInit()
    {
        debug.start("Phase: Post-Init");
        debug.start("Process Run[3]");
        processEntries();
        debug.end("Done...");

        debug.start("Doing post handling for generated objects");
        //Using pre-sorted processor list we can loop generated objects in order
        final List<String> sortingProcessorList = getSortedProcessorList();
        for (String processorKey : sortingProcessorList)
        {
            handlePostCalls(generatedObjects.get(processorKey));
        }
        debug.end("Done... " + jsonEntries.size() + " entries left");

        triggerPhase(JsonLoadPhase.LOAD_PHASE_THREE);
        debug.end("Done...");
    }

    @Override
    public void loadComplete()
    {
        if (currentPhase == JsonLoadPhase.LOAD_PHASE_THREE)
        {
            debug.start("Phase: Load-Complete");
            triggerPhase(JsonLoadPhase.COMPLETED);

            final List<String> sortingProcessorList = getSortedProcessorList();
            for (String proccessorKey : sortingProcessorList)
            {
                if (generatedObjects.get(proccessorKey) != null && !generatedObjects.get(proccessorKey).isEmpty())
                {
                    for (IJsonGenObject obj : generatedObjects.get(proccessorKey))
                    {
                        if (obj instanceof ILoadComplete)
                        {
                            try
                            {
                                ((ILoadComplete) obj).onLoadCompleted();
                            }
                            catch (Exception e)
                            {
                                Engine.logger().error("JsonContentLoad#loadComplete(): Unexpected error while firing load complete events for '" + obj + "' from processor group '" + proccessorKey + "'", e);
                            }
                        }
                    }
                }
            }
            debug.log("Clearing data");

            if (jsonEntries.size() > 0 && Engine.runningAsDev && !GraphicsEnvironment.isHeadless())
            {
                boolean processorExists = false;
                Engine.logger().info("Failed to process all JSON entries. This is most likely a bug if the count is high.");
                for (Map.Entry<String, List<JsonEntry>> set : jsonEntries.entrySet())
                {
                    boolean exists = get(set.getKey()) != null;
                    if (exists)
                    {
                        processorExists = exists;
                    }
                    Engine.logger().info("\tProcessor: " + set.getKey() + " has register processor '" + exists + "'");
                    for (JsonEntry entry : set.getValue())
                    {
                        Engine.logger().info("\t\tEntry: " + entry + "\n");
                    }
                }

                if (processorExists)
                {
                    int option = JOptionPane.showConfirmDialog(Display.getParent(), "Not all JSON entries have been processed. " +
                                    "\n JsonEntries left = " + jsonEntries.size() +
                                    "\n Do you want to continue loading?",
                            "JsonContentLoader Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);

                    if (option != JOptionPane.OK_OPTION)
                    {
                        FMLCommonHandler.instance().exitJava(-1, false);
                    }
                }
            }

            clear();
            debug.end("Done...");
        }
    }

    /**
     * Called to claim content that has to be
     * registered to the mod itself.
     *
     * @param mod - mod
     */
    public void claimContent(IJsonGenMod mod)
    {
        runActionOnGeneratedObjects(object -> {
            if (object.getMod() != null && object.getMod().equals(mod.getDomain()))
            {
                object.register(mod, mod.getJsonContentManager());
            }
        });
    }

    public void runActionOnGeneratedObjects(Consumer<IJsonGenObject> c)
    {
        for (List<IJsonGenObject> list : generatedObjects.values())
        {
            if (list != null && !list.isEmpty())
            {
                for (IJsonGenObject object : list)
                {
                    c.accept(object);
                }
            }
        }
    }

    /**
     * Called to processe a single set of content
     * <p>
     * This is normally called outside of this class
     * when content needs to load before a set time
     * in order for minecraft to handle the content
     * correctly. Examples of this are item/block
     * textures that load before init() when most
     * content is actually loaded.
     *
     * @param processorKey
     */
    public void process(String processorKey)
    {
        if (jsonEntries.containsKey(processorKey))
        {
            final List<JsonEntry> entries = jsonEntries.get(processorKey);
            if (entries != null)
            {
                debug.log("Entries: " + entries);
                final Iterator<JsonEntry> it = entries.iterator();
                //Process all loaded elements
                while (it.hasNext())
                {
                    final JsonEntry entry = it.next();
                    debug.log("Entry: " + entry);
                    try
                    {
                        List<IJsonGenObject> objects = new ArrayList();

                        //Call to process
                        boolean handled = process(entry.jsonKey, entry.element, objects);

                        //Register and add generated objects
                        for (IJsonGenObject genObject : objects)
                        {
                            if (genObject != null)
                            {
                                //Set author
                                if (entry.author != null && !entry.author.isEmpty())
                                {
                                    genObject.setAuthor(entry.author);
                                }

                                //Add gen data to list
                                List<IJsonGenObject> list = generatedObjects.get(processorKey);
                                if (list == null)
                                {
                                    list = new ArrayList();
                                }
                                list.add(genObject);
                                generatedObjects.put(processorKey, list);

                                //validate data, can crash
                                genObject.validate();

                                //Call registry methods
                                genObject.onCreated();
                                if (genObject instanceof IRegistryInit)
                                {
                                    ((IRegistryInit) genObject).onRegistered();
                                }

                                JsonEntryCreationEvent entryCreationEvent = new JsonEntryCreationEvent(this, currentPhase, genObject.getMod(), genObject.getUniqueID(), genObject.getContentID(), genObject);
                                MinecraftForge.EVENT_BUS.post(entryCreationEvent);
                            }
                        }

                        //If handled remove from list
                        if (handled)
                        {
                            it.remove();
                        }
                    }
                    catch (Exception e)
                    {
                        //TODO figure out who made the file
                        //Crash as the file may be important
                        throw new RuntimeException("Failed to process entry from file " + entry.fileReadFrom + ". Make corrections to the file or contact the file's creator for the issue to be fixed.\n  Entry = " + entry, e);
                    }
                }
            }
            if (entries.size() <= 0)
            {
                jsonEntries.remove(processorKey);
            }
            else
            {
                jsonEntries.put(processorKey, entries);
            }
        }
        else
        {
            debug.log("No entries for key");
        }
    }

    /**
     * Called to process current entries that are loaded.
     * Only entries that have processors will be loaded
     * and then removed from the list of entries.
     */
    protected void processEntries()
    {
        if (jsonEntries.size() > 0)
        {
            final List<String> sortingProcessorList = getSortedProcessorList();
            //Loop threw processors in order
            for (final String processorKey : sortingProcessorList)
            {
                debug.start("processEntries()", "Handling: " + processorKey, Engine.runningAsDev);
                process(processorKey);
                debug.end();
            }
        }
    }

    protected List<String> getSortedProcessorList()
    {
        //Collect all entries to sort
        final ArrayList<String> processorKeys = new ArrayList();
        for (final IJsonProcessor processor : processors.values())
        {
            String jsonKey = processor.getJsonKey();
            String loadOrder = processor.getLoadOrder();
            if (loadOrder != null && !loadOrder.isEmpty())
            {
                jsonKey += "@" + loadOrder;
            }
            processorKeys.add(jsonKey);
        }

        //Sort entries
        return sortSortingValues(processorKeys);
    }

    /** Validates file paths and makes folders as needed */
    public void validateFilePaths()
    {
        if (!externalContentFolder.exists())
        {
            externalContentFolder.mkdirs();
        }
    }

    /** Loads resources from folders and class path */
    public void loadResources()
    {
        debug.start("Loading json resources");

        //---------------------------------------------------------------------------
        debug.start("\tScanning mod packages for json data");
        for (ModContainer container : Loader.instance().getModList())
        {
            File file = container.getSource();
            debug.log("Mod: " + container.getName() + "  " + container.getDisplayVersion());
            debug.log("File: " + file);
            Object mod = container.getMod();
            if (mod != null)
            {
                loadResourcesFromPackage(mod.getClass(), "/content/" + container.getModId() + "/");
            }
        }
        debug.end();
        //===========================================================================
        debug.start("Scanning for external files");
        loadResourcesFromFolder(externalContentFolder);
        debug.end();

        //===========================================================================
        debug.start("Loading external resources");
        //Load external files
        for (File file : externalFiles)
        {
            try
            {
                debug.log("Loading resource: " + file);
                JsonLoader.loadJsonFile(file, jsonEntries);
            }
            catch (IOException e)
            {
                //Crash as the file may be important
                throw new RuntimeException("Failed to load external resource " + file, e);
            }
        }
        debug.end();
        //===========================================================================

        debug.start("Loading class path resources");
        //Load internal files
        for (URL resource : classPathResources)
        {
            try
            {
                debug.log("Loading resource: " + resource);
                JsonLoader.loadJsonFileFromResources(resource, jsonEntries);
            }
            catch (Exception e)
            {
                //Crash as the file may be important
                throw new RuntimeException("Failed to load classpath resource " + resource, e);
            }
        }
        debug.end();
        //---------------------------------------------------------------------------
        debug.end("Done....");
    }

    /**
     * Creates a map of entries used for sorting loaded files later
     *
     * @param values - values to sort, entries in list are consumed
     * @return Map of keys to sorting index values
     */
    public List<String> sortSortingValues(List<String> values)
    {
        long start = System.nanoTime();
        debug.start("ProcessorKeySorter", "Sorting processor keys", Engine.runningAsDev);
        //Run a basic sorter on the list to order it values, after:value, before:value:
        Collections.sort(values, new ProcessorKeySorter());

        final LinkedList<String> sortedValues = new LinkedList();
        while (!values.isEmpty())
        {
            //Sort out list
            sortSortingValues(values, sortedValues);

            //Exit point, prevents inf loop by removing bad entries and adding them to the end of the sorting list
            if (!values.isEmpty())
            {
                //Loop threw what entries we have left
                final Iterator<String> it = values.iterator();
                while (it.hasNext())
                {
                    final String entry = it.next();
                    debug.log("E: " + entry);
                    if (entry.contains("@"))
                    {
                        String[] split = entry.split("@");
                        final String name = entry.split("@")[0];

                        debug.log("\tName: " + name);

                        if (split[1].contains(":"))
                        {
                            split = split[1].split(":");
                            boolean found = false;

                            debug.log("\t" + split[0] + "  " + split[1]);

                            //Try too see if we have a valid entry left in our sorting list that might just contain a after: or before: preventing it from adding
                            debug.start("Check A");
                            for (final String v : values)
                            {
                                debug.log("" + v + "  " + v.startsWith(split[1]));
                                if (!v.equals(entry) && v.startsWith(split[1]))
                                {
                                    debug.log("\tFound entry");
                                    found = true;
                                    break;
                                }
                            }
                            debug.end();

                            if (!found)
                            {
                                debug.start("Check B");
                                for (final String v : sortedValues)
                                {
                                    debug.log("" + v + "  " + v.equals(split[1]));
                                    if (!v.equals(entry) && v.equals(split[1]))
                                    {
                                        debug.log("\tB:" + found);
                                        found = true;
                                        break;
                                    }
                                }
                                debug.end();
                            }

                            //If we have no category for the sorting entry add it to the master list
                            if (!found)
                            {
                                Engine.logger().error("Bad sorting value for " + entry + " could not find category for " + split[1]);
                                sortedValues.add(name);
                                it.remove();
                            }
                        }
                        //If entry is invalid add it
                        else
                        {
                            Engine.logger().error("Bad sorting value for " + entry + " has no valid sorting data");
                            sortedValues.add(name);
                            it.remove();
                        }
                    }
                    //Should never happen as entries with no sorting value should be added before here
                    else
                    {
                        sortedValues.add(entry);
                        it.remove();
                    }
                }
            }
        }
        debug.end("Done.... " + StringHelpers.formatTimeDifference(start, System.nanoTime()));
        return sortedValues;
    }

    /**
     * Sorts the string values that will later be used as an index value
     * to sort all .json files being processed
     *
     * @param sortingValues - list of unsorted values
     * @param sortedValues  - list of sorted values and where values will be inserted into
     */
    public static void sortSortingValues(List<String> sortingValues, List<String> sortedValues)
    {
        Iterator<String> it = sortingValues.iterator();
        while (it.hasNext())
        {
            String entry = it.next();
            if (entry.contains("@"))
            {
                String[] split = entry.split("@");
                String name = split[0];
                String sortValue = split[1];
                //TODO add support for ; allowing sorting of several values

                if (sortValue.contains(":"))
                {
                    split = sortValue.split(":");
                    String prefix = split[0];
                    String cat = split[1];
                    boolean catFound = false;

                    ListIterator<String> sortedIt = sortedValues.listIterator();
                    while (sortedIt.hasNext())
                    {
                        String v = sortedIt.next();
                        if (v.equalsIgnoreCase(cat))
                        {
                            catFound = true;
                            if (prefix.equalsIgnoreCase("after"))
                            {
                                sortedIt.add(name);
                            }
                            else if (prefix.equalsIgnoreCase("before"))
                            {
                                sortedIt.previous();
                                sortedIt.add(name);
                            }
                            else
                            {
                                Engine.logger().error("Bad sorting value for " + entry + " we can only read 'after' and 'before'");
                                sortedValues.add(name);
                                it.remove();
                            }
                            break;
                        }
                    }
                    if (catFound)
                    {
                        it.remove();
                    }
                }
                else
                {
                    sortedValues.add(name);
                    it.remove();
                }
            }
            else
            {
                sortedValues.add(entry);
                it.remove();
            }
        }
    }

    /**
     * Called to process a json element entry into a
     * generated object
     *
     * @param key     - json processor key
     * @param element - data
     * @return true if everything was processed
     */
    public boolean process(String key, JsonElement element, List<IJsonGenObject> objects)
    {
        final IJsonProcessor processor = get(key);
        if (processor != null)
        {
            if (processor.canProcess(key, element))
            {
                if (processor.shouldLoad(element))
                {
                    return processor.process(element, objects);
                }
            }
        }
        return false;
    }

    /**
     * Called to load json files from the folder.
     * <p>
     * Recursive call that will go through all folders
     * in a folder.
     *
     * @param folder
     */
    public void loadResourcesFromFolder(File folder)
    {
        for (File file : folder.listFiles())
        {
            if (file.isDirectory())
            {
                loadResourcesFromFolder(folder);
            }
            else
            {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
                if (extension.equalsIgnoreCase("jar"))
                {
                    externalJarFiles.add(file);
                }
                else if (extensionsToLoad.contains(extension))
                {
                    externalFiles.add(file);
                }
            }
        }
    }

    /**
     * Loads package
     *
     * @param folder - package your looking to load data from
     */
    public void loadResourcesFromPackage(Class clazz, String folder)
    {
        //Actual load process
        try
        {
            URL url = clazz.getClassLoader().getResource(folder);
            if (url == null)
            {
                url = clazz.getResource(folder);
            }
            if (url != null)
            {
                loadResourcesFromPackage(url);
            }
            else
            {
                debug.error("Could not locate folder[ " + folder + " ]");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to load resources from class path.  Class='" + clazz + "' folder= '" + folder + "'", e);
        }
    }

    /**
     * Loads package
     */
    public void loadResourcesFromPackage(URL url) throws Exception
    {
        debug.log("Loading resources from URL[" + url + "]");
        try
        {
            if ("jar".equals(url.getProtocol()))
            {
                debug.log("Jar detected, loading using JarZip entry list.");

                //Get path to jar file
                String jarPath = JsonLoader.getJarPath(url);
                String decodedPath = URLDecoder.decode(jarPath, "UTF-8");

                debug.log("\tPath: " + jarPath);
                debug.log("\tDecoded: " + decodedPath);

                //open jar and get entities
                JarFile jar = new JarFile(decodedPath);
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar

                List<String> listOfFilesParsed = new LinkedList();
                //Loop entries
                while (entries.hasMoreElements())
                {
                    final JarEntry entry = entries.nextElement();
                    final String name = entry.getName();
                    final String extension = name.substring(name.lastIndexOf(".") + 1, name.length());
                    if (extensionsToLoad.contains(extension))
                    {
                        listOfFilesParsed.add(name);
                        debug.log("Found " + name);
                        JsonLoader.loadJson(url.toExternalForm() + "/" + name, new InputStreamReader(jar.getInputStream(entry)), jsonEntries);
                    }
                }
            }
            else
            {
                walkPaths(Paths.get(url.toURI()));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to detect files from URL = " + url, e);
        }
    }

    //Old method, no longer used for .jars... keep for IDE usage
    private void walkPaths(Path filePath) throws IOException
    {
        debug.start("Loading files from " + filePath);
        Stream<Path> walk = Files.walk(filePath, 100);
        for (Iterator<Path> it = walk.iterator(); it.hasNext(); )
        {
            Path nextPath = it.next();
            String name = nextPath.getFileName().toString();
            if (name.lastIndexOf(".") > 1)
            {
                String extension = name.substring(name.lastIndexOf(".") + 1, name.length());
                if (extensionsToLoad.contains(extension))
                {
                    debug.log("Found " + name);
                    JsonLoader.loadJson(nextPath.toAbsolutePath().toString(), Files.newBufferedReader(nextPath), jsonEntries);
                }
            }
        }
        debug.end("Done...");
    }

    private FileSystem getFileSystem(URI uri) throws IOException
    {
        try
        {
            return FileSystems.getFileSystem(uri);
        }
        catch (FileSystemNotFoundException e)
        {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }

    /**
     * Called to handle post call code on generated objects.
     * <p>
     * Separated from {@link #postInit()} due to other processors
     * having special handling.
     *
     * @param generatedObjects
     */
    public void handlePostCalls(List<IJsonGenObject> generatedObjects)
    {
        if (generatedObjects != null && !generatedObjects.isEmpty())
        {
            for (IJsonGenObject obj : generatedObjects)
            {
                debug.start("Handling: " + obj);
                if (obj instanceof IPostInit)
                {
                    ((IPostInit) obj).onPostInit();
                }
                if (obj instanceof IRecipeContainer)
                {
                    List<IRecipe> recipes = new ArrayList();
                    ((IRecipeContainer) obj).genRecipes(recipes);
                    if (recipes.size() > 0)
                    {
                        debug.start("Adding recipes from gen object:");
                        for (IRecipe recipe : recipes)
                        {
                            if (recipe != null)
                            {
                                if (recipe.getRecipeOutput() != null)
                                {
                                    GameRegistry.addRecipe(recipe);
                                }
                                else
                                {
                                    debug.log("Null recipe output detected");
                                }
                            }
                            else
                            {
                                debug.log("Null recipe detected");
                            }
                        }
                        debug.end();
                    }
                }
                debug.end();
            }
        }
    }

    public static IJsonProcessor getProcessor(String type)
    {
        //Fix for lowercase
        for (String key : INSTANCE.generatedObjects.keySet())
        {
            if (key.equalsIgnoreCase(type))
            {
                type = key;
                break;
            }
        }

        return INSTANCE.get(type);
    }

    public static IJsonGenObject getContent(String type, String id)
    {
        List<IJsonGenObject> contentList = INSTANCE.generatedObjects.get(type);
        if (contentList != null)
        {
            //Search list for object
            for (IJsonGenObject content : contentList)
            {
                if (content != null)
                {
                    if (id.contains(":"))
                    {
                        if (id.equalsIgnoreCase(content.getContentID()))
                        {
                            return content;
                        }
                    }
                    else if (id.equalsIgnoreCase(content.getUniqueID()))
                    {
                        return content;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Called to trim all data that is no needed outside
     * of the loading phase to free up RAM
     */
    public void clear()
    {
        if (!Engine.runningAsDev)
        {
            debug.log("Clearing cached data to same RAM");
            externalFiles.clear();
            externalJarFiles.clear();
            classPathResources.clear();
            jsonEntries.clear();
        }
        else
        {
            debug.log("Not clearing cache in order to allow debugging of data");
        }
    }
}
