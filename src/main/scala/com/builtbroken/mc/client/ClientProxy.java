package com.builtbroken.mc.client;

import com.builtbroken.mc.api.tile.listeners.ITileEventListener;
import com.builtbroken.mc.client.effects.VisualEffectRegistry;
import com.builtbroken.mc.client.effects.providers.VEProviderLaserBeam;
import com.builtbroken.mc.client.effects.providers.VEProviderRocketTrail;
import com.builtbroken.mc.client.effects.providers.VEProviderShockWave;
import com.builtbroken.mc.client.effects.providers.VEProviderSmokeStream;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.audio.AudioData;
import com.builtbroken.mc.client.json.audio.AudioJsonProcessor;
import com.builtbroken.mc.client.json.effects.EffectJsonProcessor;
import com.builtbroken.mc.client.json.effects.EffectListJsonProcessor;
import com.builtbroken.mc.client.json.models.ModelJsonProcessor;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.item.ItemJsonRenderer;
import com.builtbroken.mc.client.json.render.processor.RenderJsonProcessor;
import com.builtbroken.mc.client.json.render.tile.TileRenderData;
import com.builtbroken.mc.client.json.render.tile.TileRenderHandler;
import com.builtbroken.mc.client.json.texture.TextureJsonProcessor;
import com.builtbroken.mc.client.listeners.blocks.JsonIconListener;
import com.builtbroken.mc.client.listeners.blocks.RotatableIconListener;
import com.builtbroken.mc.core.CommonProxy;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.commands.json.visuals.CommandJsonRender;
import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.core.content.entity.RenderExCreeper;
import com.builtbroken.mc.core.handler.PlayerKeyHandler;
import com.builtbroken.mc.core.handler.RenderSelection;
import com.builtbroken.mc.core.network.packet.callback.chunk.PacketRequestData;
import com.builtbroken.mc.framework.access.global.gui.GuiAccessSystem;
import com.builtbroken.mc.framework.blast.emp.ExEmp;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.multiblock.MultiBlockRenderHelper;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.block.JsonBlockListenerProcessor;
import com.builtbroken.mc.lib.json.processors.block.JsonBlockProcessor;
import com.builtbroken.mc.lib.render.block.BlockRenderHandler;
import com.builtbroken.mc.lib.render.fx.FxBeam;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import com.builtbroken.mc.lib.world.map.data.ChunkData;
import com.builtbroken.mc.prefab.tile.listeners.RotatableListener;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
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

        //Load in processors for client side json
        JsonContentLoader.INSTANCE.add(new TextureJsonProcessor());
        JsonContentLoader.INSTANCE.add(new ModelJsonProcessor());
        JsonContentLoader.INSTANCE.add(new RenderJsonProcessor());
        JsonContentLoader.INSTANCE.add(new AudioJsonProcessor());
        JsonContentLoader.INSTANCE.add(EffectJsonProcessor.INSTANCE);
        JsonContentLoader.INSTANCE.add(new EffectListJsonProcessor());

        //Textures have to be loaded in pre-init or will fail
        JsonContentLoader.INSTANCE.process("texture");
        MinecraftForge.EVENT_BUS.register(ClientDataHandler.INSTANCE);

        //Register icons for explosives
        ExplosiveRegistryClient.registerIcon(new ItemStack(Items.gunpowder), References.PREFIX + "ex.icon.gunpowder");
        ExplosiveRegistryClient.registerIcon(new ItemStack(Items.skull, 1, 4), References.PREFIX + "ex.icon.creeper_head");
        ExplosiveRegistryClient.registerIcon(new ItemStack(Blocks.tnt), References.PREFIX + "ex.icon.tnt");

        VisualEffectRegistry.addEffectProvider(new VEProviderShockWave());
        VisualEffectRegistry.addEffectProvider(new VEProviderLaserBeam());
        VisualEffectRegistry.addEffectProvider(new VEProviderSmokeStream());
        VisualEffectRegistry.addEffectProvider(new VEProviderRocketTrail());

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void loadJsonContentHandlers()
    {
        super.loadJsonContentHandlers();
        JsonBlockListenerProcessor.addBuilder(new RotatableIconListener.Builder());
        JsonBlockListenerProcessor.addBuilder(new JsonIconListener.Builder());

        if (Engine.runningAsDev)
        {
            CommandVE.INSTANCE.addToDebugCommand(new CommandJsonRender());
        }
    }

    @Override
    public void init()
    {
        super.init();

        //Register client side version of blasts
        ExplosiveRegistry.registerOrGetExplosive(References.DOMAIN, "Emp", new ExEmp());

        //Register graphics
        RenderingRegistry.registerEntityRenderingHandler(EntityExCreeper.class, new RenderExCreeper());

        //Register graphics handlers
        if (Engine.multiBlock != null)
        {
            RenderingRegistry.registerBlockHandler(MultiBlockRenderHelper.INSTANCE);
        }

        //Register tile renders
        TileRenderHandler tileRenderHandler = new TileRenderHandler();
        for (RenderData data : ClientDataHandler.INSTANCE.renderData.values())
        {
            if (data instanceof TileRenderData && ((TileRenderData) data).tileClass != null)
            {
                ClientRegistry.bindTileEntitySpecialRenderer(((TileRenderData) data).tileClass, tileRenderHandler);
            }
        }
    }

    @Override
    public void postInit()
    {
        super.postInit();
        //Item that uses a model for all states
        registerItemJsonRenders(new ItemJsonRenderer(), "VE-Item", "item", "tile", "block");

        List<IJsonGenObject> objects = JsonContentLoader.INSTANCE.generatedObjects.get(JsonBlockProcessor.KEY);
        if (objects != null && !objects.isEmpty())
        {
            for (IJsonGenObject object : objects)
            {
                if (object instanceof BlockBase)
                {
                    List<ITileEventListener> listeners = ((BlockBase) object).listeners.get("placement");
                    if (listeners != null && !listeners.isEmpty())
                    {
                        for (ITileEventListener listener : listeners)
                        {
                            if (listener instanceof RotatableListener)
                            {
                                ((BlockBase) object).addListener(new RotatableIconListener((BlockBase) object));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Called to loop through all registered json content to register
     * items to item renders.
     *
     * @param keys     - keys for the render type supported
     * @param renderer - render handler
     */
    public static int registerItemJsonRenders(IItemRenderer renderer, String... keys)
    {
        int count = 0;
        for (List<IJsonGenObject> list : JsonContentLoader.INSTANCE.generatedObjects.values())
        {
            if (list != null && !list.isEmpty())
            {
                for (IJsonGenObject obj : list)
                {
                    Item item = null;
                    if (obj instanceof Item)
                    {
                        item = (Item) obj;
                    }
                    else if (obj instanceof Block)
                    {
                        item = Item.getItemFromBlock((Block) obj);
                    }
                    if (item != null)
                    {
                        if (registerItemJsonRender(renderer, item, keys))
                        {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public static boolean registerItemJsonRender(IItemRenderer renderer, Item item, String... keys)
    {
        if (item instanceof IJsonRenderStateProvider)
        {
            List<String> ids = ((IJsonRenderStateProvider) item).getRenderContentIDs();
            for (String id : ids)
            {
                RenderData data = ClientDataHandler.INSTANCE.getRenderData(id);
                if (data != null)
                {
                    for (String key : keys)
                    {
                        if (data.renderType.equalsIgnoreCase(key))
                        {
                            MinecraftForgeClient.registerItemRenderer(item, renderer);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void keyHandler(InputEvent.KeyInputEvent e)
    {
        final int key = Keyboard.getEventKey();
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) //Prevent key bind from working on loading screen and main menu
        {
            //TODO add config for key binding
            if (key == Keyboard.KEY_GRAVE && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
            {
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAccessSystem)) //TODO check previous GUI to prevent bugs (e.g. prevent opening on death screen)
                {
                    if (Minecraft.getMinecraft().currentScreen != null)
                    {
                        Minecraft.getMinecraft().currentScreen.onGuiClosed();
                    }
                    Minecraft.getMinecraft().displayGuiScreen(new GuiAccessSystem());
                    //TODO cache previous open GUI to restore that GUI
                }
            }
        }
    }

    @SubscribeEvent
    public void clientUpdate(TickEvent.WorldTickEvent event)
    {
        if (Engine.enableExtendedMetaPacketSync) ///TODO reduce check to every 10 ticks
        {
            try
            {
                if (event.side == Side.CLIENT)
                {
                    Minecraft mc = Minecraft.getMinecraft();
                    if (mc != null)
                    {
                        EntityPlayer player = mc.thePlayer;
                        World world = mc.theWorld;
                        if (player != null && world != null)
                        {
                            if (ExtendedBlockDataManager.CLIENT.dimID != world.provider.dimensionId)
                            {
                                ExtendedBlockDataManager.CLIENT.clear();
                                ExtendedBlockDataManager.CLIENT.dimID = world.provider.dimensionId;
                            }
                            int renderDistance = mc.gameSettings.renderDistanceChunks + 2;
                            int centerX = ((int) Math.floor(player.posX)) >> 4;
                            int centerZ = ((int) Math.floor(player.posZ)) >> 4;

                            //Clear out chunks outside of render distance
                            List<ChunkData> chunksToRemove = new ArrayList();
                            for (ChunkData data : ExtendedBlockDataManager.CLIENT.chunks.values())
                            {
                                if (Math.abs(data.position.chunkXPos - centerX) > renderDistance || Math.abs(data.position.chunkZPos - centerZ) > renderDistance)
                                {
                                    chunksToRemove.add(data);
                                }
                            }

                            for (ChunkData data : chunksToRemove)
                            {
                                ExtendedBlockDataManager.CLIENT.chunks.remove(data.position);
                            }

                            renderDistance = mc.gameSettings.renderDistanceChunks;
                            for (int x = centerX - renderDistance; x < centerX + renderDistance; x++)
                            {
                                for (int z = centerZ - renderDistance; z < centerZ + renderDistance; z++)
                                {
                                    ChunkData chunkData = ExtendedBlockDataManager.CLIENT.getChunk(x, z);
                                    if (chunkData == null)
                                    {
                                        Engine.instance.packetHandler.sendToServer(new PacketRequestData(world.provider.dimensionId, x, z, 0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Engine.logger().error("Unexpected error while updating client chunk data state", e);
            }
        }
    }

    @Override
    public void playJsonAudio(World world, String audioKey, double x, double y, double z, float pitch, float volume)
    {
        super.playJsonAudio(world, audioKey, x, y, z, pitch, volume);
        try
        {
            if (audioKey != null)
            {
                AudioData data = ClientDataHandler.INSTANCE.getAudio(audioKey);
                if (data != null)
                {
                    data.play(x, y, z, pitch, volume);
                }
            }
        }
        catch (Exception e)
        {
            Engine.logger().error("Unexpected error while playing audio from Key[" + audioKey + "]", e);
        }
    }

    @Override
    public void playJsonEffect(World world, String key, double x, double y, double z, double mx, double my, double mz, boolean endPoint, NBTTagCompound nbt)
    {
        super.playJsonEffect(world, key, x, y, z, mx, my, mz, endPoint, nbt);
        //Handled by packet
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
