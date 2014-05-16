package resonant.core;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import resonant.core.content.debug.BlockCreativeBuilder;
import resonant.core.content.debug.GuiCreativeBuilder;
import resonant.lib.prefab.ProxyBase;
import resonant.lib.render.block.BlockRenderingHandler;
import resonant.lib.render.model.TechneAdvancedModelLoader;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ProxyBase
{
    static
    {
        AdvancedModelLoader.registerModelHandler(new TechneAdvancedModelLoader());
    }

    @Override
    public void preInit()
    {
        RenderingRegistry.registerBlockHandler(BlockRenderingHandler.INSTANCE);
    }

    @Override
    public boolean isPaused()
    {
        if (FMLClientHandler.instance().getClient().isSingleplayer() && !FMLClientHandler.instance().getClient().getIntegratedServer().getPublic())
        {
            GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;

            if (screen != null)
            {
                if (screen.doesGuiPauseGame())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        if (block instanceof BlockCreativeBuilder)
            return new GuiCreativeBuilder(new Vector3(x, y, z));

        return null;
    }
}
