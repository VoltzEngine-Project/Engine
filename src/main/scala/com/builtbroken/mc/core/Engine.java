package com.builtbroken.mc.core;

import com.builtbroken.mc.abstraction.EngineLoader;
import com.builtbroken.mc.api.abstraction.imp.IMinecraftInterface;
import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.core.network.netty.PacketManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public static Block heatedStone;
    
    @Deprecated
    @GameRegistry.ObjectHolder("voltzengine:multiblock")
    public static Block multiBlock;

    @Deprecated
    public static Item itemWrench;
    @Deprecated
    public static Item itemDevTool;

    @Deprecated
    public final static PacketManager packetHandler = new PacketManager(References.CHANNEL); //TODO move to internal packet calls

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
