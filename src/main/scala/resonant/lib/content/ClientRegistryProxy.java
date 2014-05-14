package resonant.lib.content;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import resonant.lib.References;
import resonant.lib.content.module.RenderTileDummy;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientRegistryProxy extends CommonRegistryProxy
{
    @Override
    public void registerBlock(Block block, Class<? extends ItemBlock> itemClass, String name, String modID)
    {
        super.registerBlock(block, itemClass, name, modID);

        BlockInfo blockInfo = block.getClass().getAnnotation(BlockInfo.class);

        if (blockInfo != null)
        {
            for (int i = 0; i < blockInfo.renderer().length; i++)
            {
                try
                {
                    Class<? extends TileEntity> tileClass = (Class<? extends TileEntity>) Class.forName(blockInfo.tileEntity()[i]);
                    Class<? extends TileEntitySpecialRenderer> rendererClass = (Class<? extends TileEntitySpecialRenderer>) Class.forName(blockInfo.renderer()[i]);
                    ClientRegistry.bindTileEntitySpecialRenderer(tileClass, rendererClass.newInstance());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    throw new RuntimeException("[ClientContentRegistry]Failed to register block for: " + name);
                }
            }
        }
    }

    @Override
    public void registerTileEntity(String name, Class<? extends TileEntity> tileClass)
    {
        super.registerTileEntity(name, tileClass);

        TileEntitySpecialRenderer tileRenderer = null;

        try
        {
            String rendererName = tileClass.getName().replaceFirst("Tile", "Render");
            Class renderClass = Class.forName(rendererName);
            tileRenderer = (TileEntitySpecialRenderer) renderClass.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            // From DarkGuardsman: Ignore these types of exceptions as this just means the block has
            // no renderer for the tile given
        }
        catch (Exception e)
        {
            References.LOGGER.severe("[ClientContentRegistry]Failed to register TileEntity renderer for " + name);
            e.printStackTrace();

        }

        if (tileRenderer != null)
        {
            ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
        }
    }

    @Override
    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {
        if (!TileEntityRenderer.instance.specialRendererMap.containsKey(clazz))
            ClientRegistry.bindTileEntitySpecialRenderer(clazz, new RenderTileDummy());
    }
}
