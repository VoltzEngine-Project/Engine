package com.builtbroken.mc.seven.client;

import com.builtbroken.mc.client.ExplosiveRegistryClient;
import com.builtbroken.mc.client.effects.VisualEffectRegistry;
import com.builtbroken.mc.client.effects.providers.VEProviderLaserBeam;
import com.builtbroken.mc.client.effects.providers.VEProviderRocketTrail;
import com.builtbroken.mc.client.effects.providers.VEProviderShockWave;
import com.builtbroken.mc.client.effects.providers.VEProviderSmokeStream;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.audio.AudioJsonProcessor;
import com.builtbroken.mc.client.json.effects.EffectJsonProcessor;
import com.builtbroken.mc.client.json.effects.EffectListJsonProcessor;
import com.builtbroken.mc.client.json.models.ModelJsonProcessor;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.mc.VoltzEngineModelLoader;
import com.builtbroken.mc.client.json.render.processor.RenderJsonProcessor;
import com.builtbroken.mc.client.json.render.tile.TileRenderData;
import com.builtbroken.mc.client.json.texture.TextureJsonProcessor;
import com.builtbroken.mc.core.ConfigValues;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.commands.json.visuals.CommandJsonRender;
import com.builtbroken.mc.core.content.blast.emp.ExEmp;
import com.builtbroken.mc.core.handler.PlayerKeyHandler;
import com.builtbroken.mc.core.handler.RenderSelection;
import com.builtbroken.mc.core.network.packet.callback.chunk.PacketRequestData;
import com.builtbroken.mc.framework.access.global.gui.GuiAccessSystem;
import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import com.builtbroken.mc.lib.world.map.data.ChunkData;
import com.builtbroken.mc.seven.CommonProxy;
import com.builtbroken.mc.seven.abstraction.MinecraftWrapper;
import com.builtbroken.mc.seven.abstraction.MinecraftWrapperClient;
import com.builtbroken.mc.seven.client.json.tile.TileRenderHandler;
import com.builtbroken.mc.seven.client.listeners.blocks.JsonIconListener;
import com.builtbroken.mc.seven.client.listeners.blocks.RotatableIconListener;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockListenerProcessor;
import com.builtbroken.mc.seven.framework.block.listeners.RotatableListener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Voltz Engine client proxy
 */
public class ClientProxy extends CommonProxy
{
    public void onLoad()
    {
        Engine.minecraft = MinecraftWrapper.INSTANCE = new MinecraftWrapperClient();
    }

    @SubscribeEvent
    public void registerAllModels(ModelRegistryEvent event)
    {
        ModelLoaderRegistry.registerLoader(new VoltzEngineModelLoader());
        registerItemJsonRenders("VE-Item", "item", "tile", "block");

        ModelLoader.setCustomModelResourceLocation(Engine.itemWrench, 0, new ModelResourceLocation(Engine.itemWrench.getRegistryName(), "inventory"));
    }

    public static int registerItemJsonRenders(String... keys)
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
                        if (registerItemJsonRender(item, keys))
                        {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }


    public static boolean registerItemJsonRender(Item item, String... keys)
    {
        if (item instanceof IJsonRenderStateProvider)
        {
            List<String> ids = ((IJsonRenderStateProvider) item).getRenderContentIDs(); //TODO see if we should register all ids
            for (String id : ids)
            {
                RenderData data = ClientDataHandler.INSTANCE.getRenderData(id); //TODO see if we should register per state
                if (data != null)
                {
                    for (String key : keys) //TODO see if we should register per key
                    {
                        if (data.renderType.equalsIgnoreCase(key))
                        {
                            String mod = data.contentID.substring(0, data.contentID.indexOf(":"));
                            String itemID = data.contentID.substring(data.contentID.indexOf(":") + 1, data.contentID.length());

                            String loc = String.format("%s%s/%s", VoltzEngineModelLoader.PREFIX, mod, itemID, key);

                            final ModelResourceLocation location = new ModelResourceLocation( loc, "inventory");
                            ModelLoader.setCustomMeshDefinition(item, stack -> location);
                            ModelBakery.registerItemVariants(item, location);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void preInit()
    {
        FMLCommonHandler.instance().bus().register(new ExplosiveRegistryClient());
        MinecraftForge.EVENT_BUS.register(new ExplosiveRegistryClient());

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
        //RenderingRegistry.registerEntityRenderingHandler(EntityExCreeper.class, new RenderExCreeper());

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
        //registerItemJsonRenders(new ItemJsonRenderer(), "VE-Item", "item", "tile", "block");

        List<IJsonGenObject> objects = JsonContentLoader.INSTANCE.generatedObjects.get(References.JSON_ITEM_KEY);
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

    @SubscribeEvent
    public void keyHandler(InputEvent.KeyInputEvent e)
    {
        final int key = Keyboard.getEventKey();
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) //Prevent key bind from working on loading screen and main menu
        {
            //TODO add config for key binding
            if (Keyboard.isCreated() && key == Keyboard.KEY_GRAVE && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
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
        if (ConfigValues.enableExtendedMetaPacketSync) ///TODO reduce check to every 10 ticks
        {
            try
            {
                if (event.side == Side.CLIENT)
                {
                    Minecraft mc = Minecraft.getMinecraft();
                    if (mc != null)
                    {
                        EntityPlayer player = mc.player;
                        World world = mc.world;
                        if (player != null && world != null)
                        {
                            if (ExtendedBlockDataManager.CLIENT.dimID != world.provider.getDimension())
                            {
                                ExtendedBlockDataManager.CLIENT.clear();
                                ExtendedBlockDataManager.CLIENT.dimID = world.provider.getDimension();
                            }
                            int renderDistance = mc.gameSettings.renderDistanceChunks + 2;
                            int centerX = ((int) Math.floor(player.posX)) >> 4;
                            int centerZ = ((int) Math.floor(player.posZ)) >> 4;

                            //Clear out chunks outside of render distance
                            List<ChunkData> chunksToRemove = new ArrayList();
                            for (ChunkData data : ExtendedBlockDataManager.CLIENT.chunks.values())
                            {
                                if (Math.abs(data.position.x - centerX) > renderDistance || Math.abs(data.position.z - centerZ) > renderDistance)
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
                                        Engine.packetHandler.sendToServer(new PacketRequestData(world.provider.getDimension(), x, z, 0));
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
    public EntityPlayerSP getClientPlayer()
    {
        return Minecraft.getMinecraft().player;
    }
}
