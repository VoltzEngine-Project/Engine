package com.builtbroken.mc.client.json.render.block;

import com.builtbroken.mc.client.json.ClientDataHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class BlockRenderHandler implements ISimpleBlockRenderingHandler
{
    public final static int ID = RenderingRegistry.getNextAvailableRenderId();

    //Render pass of the world, set by world render events
    private int pass = 0;

    public BlockRenderHandler()
    {
        ClientDataHandler.INSTANCE.addBlockRenderer("default", this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        GL11.glPushMatrix();
        //TODO render
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess access, int x, int y, int z, Block block, int modelId, RenderBlocks renderBlocks)
    {

        //TODO get tile at location
        //TODO get tile's content ID
        //TODO load json render data for ID

        //TODO get tile's state ID
        //TODO generate list of data combinations (cache if possible)
        //      state.renderPass, state
        //TODO loop list trying to locate state
        //TODO render state
        //TODO IF rendered cache state for faster render time
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return BlockRenderHandler.ID;
    }

    @SubscribeEvent
    public void postWorldRender(RenderWorldEvent.Post event)
    {
        pass = 0;
    }

    @SubscribeEvent
    public void preWorldRender(RenderWorldEvent.Pre event)
    {
        pass = event.pass;
    }
}
