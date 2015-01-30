package com.builtbroken.mc.core.registry;

import com.builtbroken.mc.api.items.ISimpleItemRenderer;
import com.builtbroken.mc.lib.render.block.ItemRenderHandler;
import com.builtbroken.mc.lib.render.block.RenderTileDummy;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ClientRegistryProxy extends CommonRegistryProxy
{
    @Override
    public void registerTileEntity(String name, String prefix, Block block, TileEntity tile)
    {
        super.registerTileEntity(name, prefix, block, tile);
        if (tile instanceof ISimpleItemRenderer)
        {
            ItemRenderHandler.register(new ItemStack(block).getItem(), (ISimpleItemRenderer) tile);
        }
    }

    @Override
    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {
        if (!TileEntityRendererDispatcher.instance.mapSpecialRenderers.containsKey(clazz))
        {
            ClientRegistry.bindTileEntitySpecialRenderer(clazz, new RenderTileDummy());
        }
    }
}
