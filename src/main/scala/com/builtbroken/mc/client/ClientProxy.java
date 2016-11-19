package com.builtbroken.mc.client;

import com.builtbroken.mc.core.CommonProxy;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.core.content.entity.RenderExCreeper;
import com.builtbroken.mc.core.handler.PlayerKeyHandler;
import com.builtbroken.mc.core.handler.RenderSelection;
import com.builtbroken.mc.lib.render.block.BlockRenderHandler;
import com.builtbroken.mc.prefab.tile.multiblock.MultiBlockRenderHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * The Voltz Engine client proxy
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        FMLCommonHandler.instance().bus().register(new ExplosiveRegistryClient());
        MinecraftForge.EVENT_BUS.register(new ExplosiveRegistryClient());

        RenderingRegistry.registerBlockHandler(new BlockRenderHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerKeyHandler());
        MinecraftForge.EVENT_BUS.register(new RenderSelection());

        ExplosiveRegistryClient.registerIcon(new ItemStack(Items.gunpowder), References.PREFIX + "ex.icon.gunpowder");
        ExplosiveRegistryClient.registerIcon(new ItemStack(Items.skull, 1, 4), References.PREFIX + "ex.icon.creeper_head");
        ExplosiveRegistryClient.registerIcon(new ItemStack(Blocks.tnt), References.PREFIX + "ex.icon.tnt");
    }

    @Override
    public void init()
    {
        super.init();
        RenderingRegistry.registerEntityRenderingHandler(EntityExCreeper.class, new RenderExCreeper());
        if (Engine.multiBlock != null)
        {
            RenderingRegistry.registerBlockHandler(MultiBlockRenderHelper.INSTANCE);
        }
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
        return null;
    }

    @Override
    public EntityClientPlayerMP getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    public World getClientWorld()
    {
        return getClientPlayer() != null ? getClientPlayer().worldObj : null;
    }

    @Override
    public int getPlayerDim()
    {
        return getClientWorld() != null ? getClientWorld().provider.dimensionId : 0;
    }
}
