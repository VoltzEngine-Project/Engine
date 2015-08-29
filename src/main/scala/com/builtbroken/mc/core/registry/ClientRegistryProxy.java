package com.builtbroken.mc.core.registry;

import com.builtbroken.mc.api.items.ISimpleItemRenderer;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.render.block.ItemRenderHandler;
import com.builtbroken.mc.lib.render.block.RenderTileDummy;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ClientRegistryProxy extends CommonRegistryProxy
{
    @Override
    public void registerBlock(ModManager manager, String name, String modPrefix, Block block, Class<? extends ItemBlock> itemBlock)
    {
        super.registerBlock(manager, name, modPrefix, block, itemBlock);

        //Set block name if it is missing
        if (block.getUnlocalizedName() == null || block.getUnlocalizedName().contains("null"))
        {
            block.setBlockName((modPrefix != null ? modPrefix : "") + name);
        }

        //Set texture name if it is missing
        if (modPrefix != null && (block.textureName == null || block.textureName.isEmpty()))
        {
            block.setBlockTextureName(modPrefix + name);
        }

        //Sets creative tab if it is missing
        if (manager.defaultTab != null && block.getCreativeTabToDisplayOn() == null)
        {
            block.setCreativeTab(manager.defaultTab);
        }
    }

    @Override
    public void registerItem(ModManager manager, String name, String modPrefix, Item item)
    {
        super.registerItem(manager, name, modPrefix, item);
        if (modPrefix != null)
        {
            if (item.unlocalizedName == null || item.unlocalizedName.isEmpty())
            {
                item.setUnlocalizedName(modPrefix + name);
            }

            if (item.iconString == null || item.iconString.isEmpty())
            {
                item.setTextureName(modPrefix + name);
            }
        }

        if (manager.defaultTab != null && item.getCreativeTab() == null)
        {
            item.setCreativeTab(manager.defaultTab);
        }
    }

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

    @Override
    public void onRegistry(Object object)
    {
        super.onRegistry(object);
        if (object instanceof IRegistryInit)
        {
            ((IRegistryInit) object).onClientRegistered();
        }
    }
}
