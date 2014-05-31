package resonant.core;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;
import resonant.api.IBoilHandler;
import resonant.core.content.*;
import resonant.core.content.debug.BlockCreativeBuilder;
import resonant.core.content.debug.BlockInfiniteBlock;
import resonant.core.content.tool.ToolMode;
import resonant.core.content.tool.ToolModeGeneral;
import resonant.core.content.tool.ToolModeRotation;
import resonant.lib.References;
import resonant.lib.compat.waila.Waila;
import resonant.lib.config.Config;
import resonant.lib.config.ConfigHandler;
import resonant.lib.config.ConfigScanner;
import resonant.lib.content.ContentRegistry;
import resonant.lib.flag.CommandFlag;
import resonant.lib.flag.FlagRegistry;
import resonant.lib.flag.ModFlag;
import resonant.lib.modproxy.ProxyHandler;
import resonant.lib.multiblock.BlockMultiBlockPart;
import resonant.lib.multiblock.TileMultiBlockPart;
import resonant.lib.network.PacketAnnotation;
import resonant.lib.network.PacketEntity;
import resonant.lib.network.PacketPlayerItem;
import resonant.lib.network.PacketTile;
import resonant.lib.network.netty.*;
import resonant.lib.prefab.ProxyBase;
import resonant.lib.prefab.item.ItemBlockMetadata;
import resonant.lib.prefab.ore.OreGenBase;
import resonant.lib.prefab.ore.OreGenReplaceStone;
import resonant.lib.prefab.ore.OreGenerator;
import resonant.lib.recipe.RecipeUtility;
import resonant.lib.schematic.SchematicTestRoom;
import resonant.lib.thermal.BoilEvent;
import resonant.lib.thermal.EventThermal.EventThermalUpdate;
import resonant.lib.thermal.ThermalGrid;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.PlayerInteractionHandler;
import resonant.lib.utility.PotionUtility;
import resonant.lib.utility.nbt.NBTUtility;
import resonant.lib.utility.nbt.SaveManager;
import universalelectricity.core.grid.IUpdate;
import universalelectricity.core.grid.UpdateTicker;
import universalelectricity.core.transform.vector.Vector3;
import universalelectricity.core.transform.vector.VectorWorld;

import java.lang.reflect.Field;
import java.util.Arrays;

/** Mob class for Resonant Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman */
@Mod(modid = References.NAME, name = References.NAME, version = References.VERSION, dependencies = "required-after:UniversalElectricity")
//@NetworkMod(channels = References.CHANNEL, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class ResonantEngine
{

    public static final ContentRegistry contentRegistry = new ContentRegistry(References.CONFIGURATION, References.NAME).setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);

    @SidedProxy(clientSide = "resonant.core.ClientProxy", serverSide = "resonant.lib.prefab.ProxyBase")
    public static ProxyBase proxy;

    @Mod.Metadata(References.NAME)
    public static ModMetadata metadata;

    @Instance(References.NAME)
    public static ResonantEngine INSTANCE;

    public static boolean runningAsDev = false;
    /** Blocks */
    public static Block blockOreCopper;
    public static Block blockOreTin;
    /** Items */
    public static Item itemWrench;
    public static Item itemMotor;
    public static Item itemCircuitBasic;
    public static Item itemCircuitAdvanced;
    public static Item itemCircuitElite;
    public static Item itemPlateCopper;
    public static Item itemPlateTin;
    public static Item itemPlateBronze;
    public static Item itemPlateSteel;
    public static Item itemPlateIron;
    public static Item itemPlateGold;
    public static Item itemIngotCopper;
    public static Item itemIngotTin;
    public static Item itemIngotSteel;
    public static Item itemIngotBronze;
    public static Item itemDustSteel;
    public static Item itemDustBronze;
    public static OreGenBase generationOreCopper, generationOreTin;
    /** Resonant Engine Blocks for mods */

    public static BlockMultiBlockPart blockMulti;
    public static BlockCreativeBuilder blockCreativeBuilder;
    public static Block blockInfinite;
    @Config
    public static double steamMultiplier = 1;

    private ProxyHandler modproxies;
    public final PacketPipelineHandler packetHandler;

    private static ThermalGrid thermalGrid;

    public ResonantEngine ()
    {
        this.modproxies = new ProxyHandler();
        this.packetHandler = new PacketPipelineHandler();
        ResonantEngine.thermalGrid = new ThermalGrid();
    }

    /** Call all of this in Init stage. Use "requestItem" or "requestBlock" instead to make it so
     * then if another mod adds in a item with the same name, it will use that mod's item instead.
     * <p/>
     * Creates a specific Basic Component item/Block.
     * <p/>
     * If you want correct recipes, make sure you register required items in the following order:
     * <p/>
     * Ingot, Ores, Dust, Plate, Copper Wire, Circuits, Motor, Wrench
     *
     * @param fieldName - Name of the item: e.g ingotCopper, ingotSteel
     * @param id - The specified ID of the item. Use 0 for a default value to be used.
     * @return The Item/Block class. */
    @Deprecated
    public static Item requireItem (String fieldName, int id)
    {
        try
        {
            String name = fieldName.replace("item", "");
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

            Field field = ReflectionHelper.findField(ResonantEngine.class, fieldName);
            Item f = (Item) field.get(null);

            // Grabs the default ID.
            Field idField = ReflectionHelper.findField(ResonantEngine.class, "id" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
            id = id <= 0 ? (Integer) idField.get(null) : id;

            if (f == null)
            {
                if (name.contains("ingot"))
                {
                    ItemIngot ingot = new ItemIngot(name, id);
                    field.set(null, ingot);
                    OreDictionary.registerOre(name, ingot);
                }
                else if (name.contains("plate"))
                {
                    field.set(null, new ItemPlate(name, id));
                    Item item = (Item) field.get(null);

                    String ingotName = name.replaceAll("plate", "ingot");

                    Item itemIngot = null;

                    if (OreDictionary.getOres(ingotName).size() > 0)
                    {
                        itemIngot = OreDictionary.getOres(ingotName).get(0).getItem();
                    }

                    if (name.equals("plateIron"))
                    {
                        itemIngot = Items.iron_ingot;
                    }
                    else if (name.equals("plateGold"))
                    {
                        itemIngot = Items.gold_ingot;
                    }

                    if (itemIngot != null)
                    {
                        if (OreDictionary.getOres(ingotName).size() == 0)
                        {
                            RecipeUtility.addRecipe(new ShapedOreRecipe(item, "II", "II", 'I', itemIngot), References.CONFIGURATION, true);
                        }
                    }

                    RecipeUtility.addRecipe(new ShapedOreRecipe(item, "II", "II", 'I', ingotName), References.CONFIGURATION, true);
                }
                else if (name.contains("dust"))
                {
                    field.set(null, new ItemBase(name, id).setCreativeTab(CreativeTabs.tabMaterials));
                    Item item = (Item) field.get(null);

                    if (name.equals("dustBronze"))
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(item, "!#!", '!', "ingotCopper", '#', "ingotTin"), References.CONFIGURATION, true);

                        if (OreDictionary.getOres("ingotBronze").size() > 0)
                        {
                            GameRegistry.addSmelting(item, OreDictionary.getOres("ingotBronze").get(0), 0.6f);
                        }
                    }
                    else if (name.equals("dustSteel"))
                    {
                        RecipeUtility.addShaplessRecipe(new ItemStack(item), new Object[] { new ItemStack(Items.coal, 1), new ItemStack(Items.coal, 1), new ItemStack(Items.iron_ingot) }, References.CONFIGURATION, true);
                        RecipeUtility.addShaplessRecipe(new ItemStack(item), new Object[] { new ItemStack(Items.coal, 1, 1), new ItemStack(Items.coal, 1, 1), new ItemStack(Items.iron_ingot) }, References.CONFIGURATION, true);
                        RecipeUtility.addShaplessRecipe(new ItemStack(item), new Object[] { new ItemStack(Items.coal, 1, 1), new ItemStack(Items.coal, 1), new ItemStack(Items.iron_ingot) }, References.CONFIGURATION, true);

                        if (OreDictionary.getOres("ingotSteel").size() > 0)
                        {
                            GameRegistry.addSmelting(item, OreDictionary.getOres("ingotSteel").get(0), 0.8f);
                        }
                    }
                }
                else if (name.equals("wrench"))
                {
                    field.set(null, new ItemScrewdriver(id));
                    Item item = (Item) field.get(null);

                    if (OreDictionary.getOres("ingotSteel").size() > 0)
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", "W  ", 'S', "ingotSteel", 'W', Blocks.log), References.CONFIGURATION, true);
                    }
                    else
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", "W  ", 'S', Items.iron_ingot, 'W', Blocks.log), References.CONFIGURATION, true);
                    }
                }
                else
                {
                    field.set(null, new ItemBase(name, id).setCreativeTab(CreativeTabs.tabMaterials));
                    Item item = (Item) field.get(null);

                    if (name.equals("circuitBasic"))
                    {
                        if (OreDictionary.getOres("plateBronze").size() > 0)
                        {
                            if (OreDictionary.getOres("copperWire").size() > 0)
                            {
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateBronze", '#', Items.redstone, '!', "copperWire"), References.CONFIGURATION, true);
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateSteel", '#', Items.redstone, '!', "copperWire"), References.CONFIGURATION, true);
                            }
                            else
                            {
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateBronze", '#', Items.redstone, '!', Items.comparator), References.CONFIGURATION, true);
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateSteel", '#', Items.redstone, '!', Items.comparator), References.CONFIGURATION, true);
                            }
                        }
                        else
                        {
                            RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', Blocks.iron_block, '#', Items.redstone, '!', Items.comparator), References.CONFIGURATION, true);
                        }
                    }
                    else if (name.equals("circuitAdvanced"))
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@@@", "#?#", "@@@", '@', Items.redstone, '?', Items.diamond, '#', "circuitBasic"), References.CONFIGURATION, true);
                    }
                    else if (name.equals("circuitElite"))
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@@@", "?#?", "@@@", '@', Items.gold_ingot, '?', "circuitAdvanced", '#', Blocks.lapis_block), References.CONFIGURATION, true);
                    }
                    else if (name.equals("motor"))
                    {
                        if (OreDictionary.getOres("copperWire").size() > 0)
                        {
                            RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Items.iron_ingot, '@', "copperWire"), References.CONFIGURATION, true);
                        }
                        else
                        {
                            RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Items.iron_ingot, '@', Items.comparator), References.CONFIGURATION, true);
                        }
                    }
                }

                Item item = (Item) field.get(null);
                GameRegistry.registerItem(item, name);
                OreDictionary.registerOre(name, item);

                FMLLog.info("Resonant Engine: Successfully requested item: " + name);
                return item;
            }

            return f;
        }
        catch (Exception e)
        {
            FMLLog.severe("Resonant Engine: Failed to require item: " + fieldName);
            e.printStackTrace();
        }

        return null;
    }

    @Deprecated
    public static Item requestItem (String name, int id)
    {
        if (OreDictionary.getOres(name).size() <= 0)
        {
            return requireItem(name, id);
        }

        FMLLog.info("Resonant Engine: " + name + " already exists in Ore Dictionary, using the ore instead.");

        if (OreDictionary.getOres(name).size() > 0)
        {
            return OreDictionary.getOres(name).get(0).getItem();
        }

        return null;
    }

    @Deprecated
    public static Block requireBlock (String fieldName, int id)
    {
        try
        {
            String name = fieldName.replace("block", "");
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

            Field field = ReflectionHelper.findField(ResonantEngine.class, fieldName);
            Block f = (Block) field.get(null);
            Field idField = ReflectionHelper.findField(ResonantEngine.class, "id" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
            id = id <= 0 ? (Integer) idField.get(null) : id;

            if (f == null)
            {
                if (name.contains("ore"))
                {
                    field.set(null, new BlockBase(name, Material.rock));
                    Block block = (Block) field.get(null);
                    GameRegistry.registerBlock(block, name);
                    OreDictionary.registerOre(name, block);

                    String ingotName = name.replaceAll("ore", "ingot");

                    if (OreDictionary.getOres(ingotName).size() > 0)
                    {
                        GameRegistry.addSmelting(block, OreDictionary.getOres(ingotName).get(0), 0.6f);
                    }

                    Field generationField = ReflectionHelper.findField(ResonantEngine.class, "generation" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
                    generationField.set(null, new OreGenReplaceStone(name, name, new ItemStack(block), 60, 25, 4).enable(References.CONFIGURATION));
                    OreGenerator.addOre((OreGenReplaceStone) generationField.get(null));
                }

                Block block = (Block) field.get(null);

                FMLLog.info("Resonant Engine: Successfully requested block: " + name);
                return block;
            }

            return f;
        }
        catch (Exception e)
        {
            FMLLog.severe("Resonant Engine: Failed to require block: " + fieldName);
            e.printStackTrace();
        }

        return null;
    }

    public static Block requestBlock (String name, int id)
    {
        if (OreDictionary.getOres(name).size() <= 0)
        {
            return requireBlock(name, id);
        }

        FMLLog.info("Resonant Engine: " + name + " already exists in Ore Dictionary, using the ore instead.");

        if (OreDictionary.getOres(name).get(0).getItem() instanceof ItemBlock)
        {
            return Block.getBlockFromItem(OreDictionary.getOres(name).get(0).getItem());
        }

        return null;
    }

    @EventHandler
    public void preInit (FMLPreInitializationEvent evt)
    {
        ConfigScanner.instance().generateSets(evt.getAsmData());

        /* Does a system check to see if we are running in an IDE as a Dev
         * http://andydunkel.net/java/2011/06/22/java-check-if-your-code-is-running-in-eclipse.html */
        String inEclipse = System.getProperty("uedev");

        if (inEclipse != null && inEclipse.equals("true"))
        {
            References.LOGGER.info("\n######Running mod as a dev IDE######\n");
            ResonantEngine.runningAsDev = true;
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        modproxies.applyModule(Waila.class, true);
        modproxies.applyModule(this.packetHandler);

        // Potion Array resized to Current potion array, +32, Allows to miss conflicting ID's
        PotionUtility.resizePotionArray();

        SaveManager.registerClass("ModFlag", ModFlag.class);

        //EventHandlers
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(SaveManager.instance());
        MinecraftForge.EVENT_BUS.register(new PlayerInteractionHandler());

        ToolMode.REGISTRY.add(new ToolModeGeneral());
        ToolMode.REGISTRY.add(new ToolModeRotation());

        blockMulti = (BlockMultiBlockPart) contentRegistry.createTile(BlockMultiBlockPart.class, TileMultiBlockPart.class).setCreativeTab(null);
        blockMulti.setPacketType(References.PACKET_TILE);

        //TODO: Calclavia - Return the prefabs as we still need them. Using traits is nice and all but we still need java classes

        if (References.CONFIGURATION.get("CreaiveModeTools", "CreativeBuilder", runningAsDev).getBoolean(true))
        {
            blockCreativeBuilder = (BlockCreativeBuilder) contentRegistry.createBlock(BlockCreativeBuilder.class);
        }
        if (References.CONFIGURATION.get("CreaiveModeTools", "InfiniteSource", runningAsDev).getBoolean(true))
        {
            blockInfinite = contentRegistry.createBlock(BlockInfiniteBlock.class, ItemBlockMetadata.class);
        }
        BlockCreativeBuilder.register(new SchematicTestRoom());
        //Finish and close all resources
        References.CONFIGURATION.load();
        References.CONFIGURATION.save();
        proxy.preInit();
        modproxies.preInit();
    }

    @EventHandler
    public void init (FMLInitializationEvent evt)
    {
        References.CONFIGURATION.load();

        boolean defaultDoLoad = !(Loader.isModLoaded("ThermalExpansion") || Loader.isModLoaded("IC2"));

        if (References.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Enable_Calclavia_Core_Resources", defaultDoLoad).getBoolean(defaultDoLoad))
        {
            ComponentRegistry.registerAll();
            ComponentRegistry.register("itemIngotCopper");
            ComponentRegistry.register("itemIngotTin");

            ComponentRegistry.register("blockOreCopper");
            ComponentRegistry.register("blockOreTin");

            ComponentRegistry.register("itemIngotSteel");
            ComponentRegistry.register("itemDustSteel");
            ComponentRegistry.register("itemPlateSteel");

            ComponentRegistry.register("itemIngotBronze");
            ComponentRegistry.register("itemDustBronze");
            ComponentRegistry.register("itemPlateBronze");

            ComponentRegistry.register("itemPlateCopper");
            ComponentRegistry.register("itemPlateTin");
            ComponentRegistry.register("itemPlateIron");
            ComponentRegistry.register("itemPlateGold");
        }

        if (References.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Enable_Calclavia_Core_Tools", true).getBoolean(true))
        {
            ComponentRegistry.register("itemCircuitBasic");
            ComponentRegistry.register("itemCircuitAdvanced");
            ComponentRegistry.register("itemCircuitElite");

            // ComponentRegistry.register("itemMotor");
            ComponentRegistry.register("itemWrench");
        }

        References.LOGGER.log(Level.INFO, "Attempting to load " + ComponentRegistry.requests.size() + " items.");

        for (String request : ComponentRegistry.requests)
        {
            if (request.contains("block"))
            {
                requestBlock(request, 0);
            }
            else if (request.contains("item"))
            {
                requestItem(request, 0);
            }
            else
            {
                References.LOGGER.fatal("Failed to load Resonant Engine item: " + request);
            }
        }

        References.CONFIGURATION.save();

        this.packetHandler.registerPacket(PacketEntity.class);
        this.packetHandler.registerPacket(PacketTile.class);
        this.packetHandler.registerPacket(PacketPlayerItem.class);
		this.packetHandler.registerPacket(PacketAnnotation.class);

        References.LOGGER.info("Loaded: " + LanguageUtility.loadLanguages(References.LANGUAGE_DIRECTORY, References.LANGUAGES) + " Languages.");

        ResonantEngine.metadata.modId = References.NAME;
        ResonantEngine.metadata.name = References.NAME;
        ResonantEngine.metadata.description = References.NAME + " is a mod developement framework designed to assist in creation of mods. It provided basic classes for packet handling, tile creation, inventory handling, saving/loading of NBT, and general all around prefabs.";
        ResonantEngine.metadata.url = "https://github.com/Universal-Electricity/Resonant-Engine";
        ResonantEngine.metadata.version = References.VERSION + References.BUILD_VERSION;
        ResonantEngine.metadata.authorList = Arrays.asList(new String[] { "Calclavia", "DarkCow", "tgame14", "Maxwolf" });
        ResonantEngine.metadata.autogenerated = false;
        proxy.init();
        modproxies.init();
    }

    @EventHandler
    public void postInit (FMLPostInitializationEvent evt)
    {
        ConfigHandler.configure(References.CONFIGURATION, References.DOMAIN);

        // Register Thermal Grid
        UpdateTicker.addNetwork(ResonantEngine.thermalGrid);

        proxy.postInit();
        modproxies.postInit();
    }

    @EventHandler
    public void serverStarting (FMLServerStartingEvent event)
    {
        // Load ModFlag from world save
        Object object = SaveManager.createAndLoad(NBTUtility.loadData(FlagRegistry.DEFAULT_NAME));
        if (!(object instanceof ModFlag))
        {
            object = new ModFlag(FlagRegistry.DEFAULT_NAME);
        }
        FlagRegistry.registerModFlag(FlagRegistry.DEFAULT_NAME, (ModFlag) object);

        // Setup command
        ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        serverCommandManager.registerCommand(new CommandFlag(FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME)));
    }

    @EventHandler
    public void onServerStopping (FMLServerStoppingEvent evt)
    {
        SaveManager.saveAll();
    }

    /** Default handler. */
    @SubscribeEvent
    public void boilEventHandler (BoilEvent evt)
    {
        World world = evt.world;
        Vector3 position = evt.position;
        Block block = position.getBlock(world);

        for (int height = 1; height <= evt.maxSpread; height++)
        {
            TileEntity tileEntity = world.getTileEntity(position.xi(), position.yi() + height, position.zi());

            if (tileEntity instanceof IBoilHandler)
            {
                IBoilHandler handler = (IBoilHandler) tileEntity;
                FluidStack fluid = evt.getRemainForSpread(height);

                if (fluid.amount > 0)
                {
                    if (handler.canFill(ForgeDirection.DOWN, fluid.getFluid()))
                    {
                        fluid.amount -= handler.fill(ForgeDirection.DOWN, fluid, true);
                    }
                }
            }
        }

        // Reactors will not actually remove water source blocks, however weapons will.
        //        if ((blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID) && position.getBlockMetadata(world) == 0 && !evt.isReactor)
        //        {
        //            position.setBlock(world, 0);
        //        }

        evt.setResult(Event.Result.DENY);
    }

    /** Default handler. */
    @SubscribeEvent
    public void thermalEventHandler (EventThermalUpdate evt)
    {
        final VectorWorld pos = evt.position;

        synchronized (pos.world())
        {
            Block block = pos.getBlockID();
            Material mat = pos.getBlockID().getMaterial();

            if (mat == Material.air)
            {
                evt.heatLoss = 0.15f;
            }

            if (block == Blocks.flowing_water || block == Blocks.water)
            {
                if (evt.temperature >= 373)
                {
                    if (FluidRegistry.getFluid("steam") != null)
                    {
                        // TODO: INCORRECT!
                        int volume = (int) (FluidContainerRegistry.BUCKET_VOLUME * (evt.temperature / 373) * steamMultiplier);
                        MinecraftForge.EVENT_BUS.post(new BoilEvent(pos.world(), pos, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(FluidRegistry.getFluid("steam"), volume), 2, evt.isReactor));
                    }

                    evt.heatLoss = 0.2f;
                }
            }

            if (block == Blocks.ice)
            {
                if (evt.temperature >= 273)
                {

                    UpdateTicker.addNetwork(new IUpdate()
                    {
                        @Override
                        public void update (double delta)
                        {
                            pos.setBlock(Blocks.flowing_water);
                        }

                        @Override
                        public boolean canUpdate ()
                        {
                            return true;
                        }

                        @Override
                        public boolean continueUpdate ()
                        {
                            return false;
                        }
                    });
                }

                evt.heatLoss = 0.4f;
            }
        }
    }
}
