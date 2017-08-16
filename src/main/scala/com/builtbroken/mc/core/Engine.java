package com.builtbroken.mc.core;

import com.builtbroken.mc.abstraction.EngineLoader;
import com.builtbroken.mc.api.abstraction.imp.IMinecraftInterface;
import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.gems.GemTypes;
import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
import com.builtbroken.mc.core.content.tool.ItemSimpleCraftingTool;
import com.builtbroken.mc.core.network.netty.PacketManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mod class for Voltz Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman
 */


public class Engine
{
    protected static Logger logger = LogManager.getLogger("VoltzEngine");

    public static IMinecraftInterface minecraft;
    public static EngineLoader loaderInstance;

    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    @Deprecated
    public static Block ore = null;
    @Deprecated
    public static Block gemOre = null;
    @Deprecated
    public static Block heatedStone;
    @Deprecated
    public static Block multiBlock;
    @Deprecated
    public static Block blockInfInventory;

    @Deprecated
    public static Item itemWrench;
    @Deprecated
    public static ItemSimpleCraftingTool itemSimpleCraftingTools;
    @Deprecated
    public static ItemSheetMetalTools itemSheetMetalTools;
    @Deprecated
    public static Item itemSheetMetal;
    @Deprecated
    public static Item itemCircuits;
    @Deprecated
    public static Item itemDevTool;
    @Deprecated
    public static Item itemCraftingParts;

    //Interal trigger booleans
    @Deprecated
    public static boolean metallicOresRequested = false;
    @Deprecated
    public static boolean gemOresRequested = false;
    @Deprecated
    public static boolean sheetMetalRequested = false;
    @Deprecated
    public static boolean multiBlockRequested = false;
    @Deprecated
    public static boolean craftingPartsRequested = false;
    @Deprecated
    public static boolean heatedRockRequested = false;
    @Deprecated
    public static boolean simpleToolsRequested = false;
    @Deprecated
    public static boolean circuitsRequested = false;

    @Deprecated
    public final static PacketManager packetHandler = new PacketManager(References.CHANNEL); //TODO move to internal packet calls


    /** List of content that has been requested to load, replaces old load system */
    protected static List<String> requestedContent = new ArrayList();

    /**
     * Requests that all ores are generated
     * Must be called in pre-init
     */
    @Deprecated
    public static void requestOres()
    {
        requestMetalOres();
        requestGemOres();
        requestedContent.add("ore");
    }

    /**
     * Requests that all metal ores load
     * Must be called in pre-init
     */
    @Deprecated
    public static void requestMetalOres()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Ores can only be requested in Pre-Init phase!");
        }
        metallicOresRequested = true;
    }

    @Deprecated
    public static void requestGemOres()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Ores can only be requested in Pre-Init phase!");
        }
        gemOresRequested = true;
    }

    /**
     * Requests that resources like ingots and dust are loaded
     */
    @Deprecated
    public static void requestResources()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Resources can only be requested in Pre-Init phase");
        }
        DefinedGenItems.DUST.requestToLoad();
        DefinedGenItems.DUST_IMPURE.requestToLoad();
        DefinedGenItems.RUBBLE.requestToLoad();
        DefinedGenItems.INGOT.requestToLoad();
        DefinedGenItems.PLATE.requestToLoad();
        DefinedGenItems.ROD.requestToLoad();
        DefinedGenItems.GEAR.requestToLoad();
        DefinedGenItems.NUGGET.requestToLoad();
        DefinedGenItems.WIRE.requestToLoad();
        DefinedGenItems.SCREW.requestToLoad();
        //TODO remove if statement when gems are nice
        GemTypes.UNCUT.requestToLoad();
    }

    @Deprecated
    public static void requestToolParts()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Tool Parts can only be requested in Pre-Init phase");
        }
        DefinedGenItems.AX_HEAD.requestToLoad();
        DefinedGenItems.SHOVEL_HEAD.requestToLoad();
        DefinedGenItems.HOE_HEAD.requestToLoad();
        DefinedGenItems.PICK_HEAD.requestToLoad();
        DefinedGenItems.SWORD_BLADE.requestToLoad();
    }

    /**
     * Requests circuits to be loaded up
     * Must be called in pre-init
     */
    @Deprecated
    public static void requestCircuits()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Circuit content can only be requested in Pre-Init phase");
        }
        circuitsRequested = true;
    }

    /**
     * Requests crafting parts to be loaded up
     * Must be called in pre-init
     */
    @Deprecated
    public static void requestCraftingParts()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Crafting parts can only be requested in Pre-Init phase");
        }
        craftingPartsRequested = true;
    }

    /**
     * Requests basic multiblock code to be loaded up
     * Must be called in pre-init
     */
    @Deprecated
    public static void requestMultiBlock()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Multi block content can only be requested in Pre-Init phase");
        }
        multiBlockRequested = true;
    }

    /**
     * Requests simple tool code to be loaded up
     * Must be called in pre-init
     */
    @Deprecated
    public static void requestSimpleTools()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Simple tool content can only be requested in Pre-Init phase");
        }
        simpleToolsRequested = true;
    }

    /**
     * Requests sheet metal content to be loaded up
     * Must be called in pre-init
     */
    @Deprecated
    public static void requestSheetMetalContent()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Sheet metal content can only be requested in Pre-Init phase");
        }
        sheetMetalRequested = true;
    }

    /**
     * Requests that the main modules be loaded
     */
    @Deprecated
    public static void requestBaseModules()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
        {
            throw new RuntimeException("Modules can only be requested to load in the pre-init phase");
        }
        requestOres();
        requestResources();
        requestCraftingParts();
        requestCircuits();
        requestSimpleTools();
        requestSheetMetalContent();
    }

    public static Logger logger()
    {
        return logger;
    }

    /**
     * Use to print errors to the logger. If engine instance is null it will
     * throw the error message as an exception. This is designed for JUnit
     * tests that want to handle exceptions rather than see printlns.
     *
     * @param msg   - message that describes the issue
     * @param error - error to throw
     * @throws Throwable - throws an exception if {@link Engine#loaderInstance} is null
     */
    public static void error(String msg, Throwable error) throws Throwable
    {
        if (loaderInstance == null)
        {
            throw new RuntimeException(msg, error);
        }
        else
        {
            logger().error(msg, error);
        }
    }

    /**
     * Use to print errors to the logger. If engine instance is null it will
     * throw the error message as an exception. This is designed for JUnit
     * tests that want to handle exceptions rather than see printlns.
     *
     * @param msg - message that describes the issue
     * @throws Throwable - throws an exception if {@link Engine#loaderInstance} is null
     */
    public static void error(String msg)
    {
        if (loaderInstance == null)
        {
            throw new RuntimeException(msg);
        }
        else
        {
            logger().error(msg);
        }
    }

    public static boolean isJUnitTest()
    {
        //TODO do boolean flag from VoltzTestRunner to simplify solution
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list)
        {
            if (element.getClassName().startsWith("org.junit.") || element.getClassName().startsWith("com.builtbroken.mc.testing.junit.VoltzTestRunner"))
            {
                return true;
            }
        }
        return false;
    }


    //=====================================================
    //======= Abstraction Layer ===========================
    //=====================================================

    public static IWorld getWorld(int dim)
    {
        return minecraft != null ? minecraft.getWorld(dim) : null;
    }

    public static boolean isShiftHeld()
    {
        return minecraft.isShiftHeld();
    }
}
