package com.builtbroken.mc.client;

import com.builtbroken.mc.client.effects.VisualEffectRegistry;
import com.builtbroken.mc.client.effects.providers.VEProviderShockWave;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.models.ModelJsonProcessor;
import com.builtbroken.mc.client.json.render.ItemJsonRenderer;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.RenderJsonProcessor;
import com.builtbroken.mc.client.json.texture.TextureJsonProcessor;
import com.builtbroken.mc.core.CommonProxy;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.core.content.entity.RenderExCreeper;
import com.builtbroken.mc.core.handler.PlayerKeyHandler;
import com.builtbroken.mc.core.handler.RenderSelection;
import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.render.block.BlockRenderHandler;
import com.builtbroken.mc.lib.render.fx.FxBeam;
import com.builtbroken.mc.imp.transform.vector.Pos;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.util.List;

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

        JsonContentLoader.INSTANCE.add(new TextureJsonProcessor());
        JsonContentLoader.INSTANCE.add(new ModelJsonProcessor());
        JsonContentLoader.INSTANCE.add(new RenderJsonProcessor());

        JsonContentLoader.INSTANCE.process("texture");
        MinecraftForge.EVENT_BUS.register(ClientDataHandler.INSTANCE);

        ExplosiveRegistryClient.registerIcon(new ItemStack(Items.gunpowder), References.PREFIX + "ex.icon.gunpowder");
        ExplosiveRegistryClient.registerIcon(new ItemStack(Items.skull, 1, 4), References.PREFIX + "ex.icon.creeper_head");
        ExplosiveRegistryClient.registerIcon(new ItemStack(Blocks.tnt), References.PREFIX + "ex.icon.tnt");

        VisualEffectRegistry.addEffectProvider(new VEProviderShockWave());
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
        postInit();
    }

    @Override
    public void postInit()
    {
        super.postInit();
        //Item that uses a model for all states
        registerItemJsonRenders("VE-Item", new ItemJsonRenderer());
    }

    /**
     * Called to loop through all registered json content to register
     * items to item renders.
     *
     * @param key      - key for the render type
     * @param renderer - render handler
     */
    public static void registerItemJsonRenders(String key, IItemRenderer renderer)
    {
        for (List<IJsonGenObject> list : JsonContentLoader.INSTANCE.generatedObjects.values())
        {
            if (list != null && !list.isEmpty())
            {
                for (IJsonGenObject object : list)
                {
                    if (object instanceof Item && object instanceof IJsonRenderStateProvider)
                    {
                        List<String> ids = ((IJsonRenderStateProvider) object).getRenderContentIDs();
                        for (String id : ids)
                        {
                            RenderData data = ClientDataHandler.INSTANCE.getRenderData(id);
                            if (data != null && data.renderType.equalsIgnoreCase(key))
                            {
                                MinecraftForgeClient.registerItemRenderer((Item) object, renderer);
                            }
                        }
                    }
                }
            }
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

    @Override
    public void spawnParticle(String name, World world, double x, double y, double z, double xx, double yy, double zz)
    {
        Minecraft.getMinecraft().renderGlobal.spawnParticle(name, x, y, z, (float) xx, (float) yy, (float) zz);
    }

    @Override
    public void spawnBeamFx(ResourceLocation location, World world, Pos start, Pos end, Color color, int ticksToLive)
    {
        FxBeam beam = new FxBeam(SharedAssets.GREY_TEXTURE, world, start, end, color, 5);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(beam);
    }
}
