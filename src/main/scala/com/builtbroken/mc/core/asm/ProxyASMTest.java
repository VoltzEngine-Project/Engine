package com.builtbroken.mc.core.asm;

import com.builtbroken.mc.core.EngineCoreMod;
import com.builtbroken.mc.lib.mod.AbstractProxy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public class ProxyASMTest extends AbstractProxy
{
    private boolean chunkASM = false;

    @Override
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(this);
        CheckFakeWorld world = CheckFakeWorld.newWorld("Test");
        Chunk chunk = new Chunk(world, 0, 0);
        chunk.func_150807_a(1, 1, 1, Blocks.stone, 0);

        if(!chunkASM)
        {
            EngineCoreMod.logger.error("\n\n\nFailed to get chunk update event when fired. This may mean the ASM for injecting this event didn't function. Code depending on this event may fail to function correctly.\n\n\n");
        }
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void chunkSetBlockEvent(ChunkSetBlockEvent event)
    {
        chunkASM = true;
    }
}
