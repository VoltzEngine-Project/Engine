package resonant.core;

import java.lang.reflect.Field;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import resonant.api.IBoilHandler;
import resonant.core.content.BlockBase;
import resonant.core.content.ComponentRegistry;
import resonant.core.content.ItemBase;
import resonant.core.content.ItemIngot;
import resonant.core.content.ItemPlate;
import resonant.core.content.ItemScrewdriver;
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
import resonant.lib.content.IDManager;
import resonant.lib.flag.CommandFlag;
import resonant.lib.flag.FlagRegistry;
import resonant.lib.flag.ModFlag;
import resonant.lib.grid.UECommand;
import resonant.lib.grid.UpdateTicker;
import resonant.lib.modproxy.ProxyHandler;
import resonant.lib.multiblock.BlockMultiBlockPart;
import resonant.lib.multiblock.TileMultiBlockPart;
import resonant.lib.network.PacketHandler;
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
import universalelectricity.api.net.IUpdate;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import universalelectricity.core.net.NetworkTickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;

/** Mob class for Resonant Engine that handles common loading
 * 
 * @author Calclavia, DarkGuardsman */
@Mod(modid = References.NAME, name = References.NAME, version = References.VERSION, dependencies = "required-after:UniversalElectricity")
@NetworkMod(channels = References.CHANNEL, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class ResonantEngine
{

    public static final IDManager idManager = new IDManager(3970, 13970);
    public static final ContentRegistry contentRegistry = new ContentRegistry(References.CONFIGURATION, idManager, References.NAME).setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);

    @SidedProxy(clientSide = "resonant.core.ClientProxy", serverSide = "resonant.lib.prefab.ProxyBase")
    public static ProxyBase proxy;

    @Mod.Metadata(References.NAME)
    public static ModMetadata metadata;

    @Instance(References.NAME)
    public static ResonantEngine INSTANCE;

    /** Auto-incrementing configuration IDs. Use this to make sure no config ID is the same. */
    public static final int idBlockOreCopper = idManager.getNextBlockID();
    public static final int idBlockOreTin = idManager.getNextBlockID();
    public static final int idItemWrench = idManager.getNextItemID();
    public static final int idItemMotor = idManager.getNextItemID();
    public static final int idItemCircuitBasic = idManager.getNextItemID();
    public static final int idItemCircuitAdvanced = idManager.getNextItemID();
    public static final int idItemCircuitElite = idManager.getNextItemID();
    public static final int idItemPlateCopper = idManager.getNextItemID();
    public static final int idItemPlateTin = idManager.getNextItemID();
    public static final int idItemPlateBronze = idManager.getNextItemID();
    public static final int idItemPlateSteel = idManager.getNextItemID();
    public static final int idItemPlateIron = idManager.getNextItemID();
    public static final int idItemPlateGold = idManager.getNextItemID();
    public static final int idItemIngotCopper = idManager.getNextItemID();
    public static final int idItemIngotTin = idManager.getNextItemID();
    public static final int idItemIngotSteel = idManager.getNextItemID();
    public static final int idItemIngotBronze = idManager.getNextItemID();
    public static final int idItemDustSteel = idManager.getNextItemID();
    public static final int idItemDustBronze = idManager.getNextItemID();

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

    private static ThermalGrid thermalGrid;

    public ResonantEngine()
    {
        this.modproxies = new ProxyHandler();
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
    public static Item requireItem(String fieldName, int id)
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
                        itemIngot = Item.ingotIron;
                    }
                    else if (name.equals("plateGold"))
                    {
                        itemIngot = Item.ingotGold;
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
                            GameRegistry.addSmelting(item.itemID, OreDictionary.getOres("ingotBronze").get(0), 0.6f);
                        }
                    }
                    else if (name.equals("dustSteel"))
                    {
                        RecipeUtility.addShaplessRecipe(new ItemStack(item), new Object[] { new ItemStack(Item.coal, 1), new ItemStack(Item.coal, 1), new ItemStack(Item.ingotIron) }, References.CONFIGURATION, true);
                        RecipeUtility.addShaplessRecipe(new ItemStack(item), new Object[] { new ItemStack(Item.coal, 1, 1), new ItemStack(Item.coal, 1, 1), new ItemStack(Item.ingotIron) }, References.CONFIGURATION, true);
                        RecipeUtility.addShaplessRecipe(new ItemStack(item), new Object[] { new ItemStack(Item.coal, 1, 1), new ItemStack(Item.coal, 1), new ItemStack(Item.ingotIron) }, References.CONFIGURATION, true);

                        if (OreDictionary.getOres("ingotSteel").size() > 0)
                        {
                            GameRegistry.addSmelting(item.itemID, OreDictionary.getOres("ingotSteel").get(0), 0.8f);
                        }
                    }
                }
                else if (name.equals("wrench"))
                {
                    field.set(null, new ItemScrewdriver(id));
                    Item item = (Item) field.get(null);

                    if (OreDictionary.getOres("ingotSteel").size() > 0)
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", "W  ", 'S', "ingotSteel", 'W', Block.wood), References.CONFIGURATION, true);
                    }
                    else
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", "W  ", 'S', Item.ingotIron, 'W', Block.wood), References.CONFIGURATION, true);
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
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateBronze", '#', Item.redstone, '!', "copperWire"), References.CONFIGURATION, true);
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateSteel", '#', Item.redstone, '!', "copperWire"), References.CONFIGURATION, true);
                            }
                            else
                            {
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateBronze", '#', Item.redstone, '!', Item.comparator), References.CONFIGURATION, true);
                                RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateSteel", '#', Item.redstone, '!', Item.comparator), References.CONFIGURATION, true);
                            }
                        }
                        else
                        {
                            RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', Block.blockIron, '#', Item.redstone, '!', Item.comparator), References.CONFIGURATION, true);
                        }
                    }
                    else if (name.equals("circuitAdvanced"))
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', "circuitBasic"), References.CONFIGURATION, true);
                    }
                    else if (name.equals("circuitElite"))
                    {
                        RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@@@", "?#?", "@@@", '@', Item.ingotGold, '?', "circuitAdvanced", '#', Block.blockLapis), References.CONFIGURATION, true);
                    }
                    else if (name.equals("motor"))
                    {
                        if (OreDictionary.getOres("copperWire").size() > 0)
                        {
                            RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', "copperWire"), References.CONFIGURATION, true);
                        }
                        else
                        {
                            RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', Item.comparator), References.CONFIGURATION, true);
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
    public static Item requestItem(String name, int id)
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
    public static Block requireBlock(String fieldName, int id)
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
                    field.set(null, new BlockBase(name, id));
                    Block block = (Block) field.get(null);
                    GameRegistry.registerBlock(block, name);
                    OreDictionary.registerOre(name, block);

                    String ingotName = name.replaceAll("ore", "ingot");

                    if (OreDictionary.getOres(ingotName).size() > 0)
                    {
                        GameRegistry.addSmelting(block.blockID, OreDictionary.getOres(ingotName).get(0), 0.6f);
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

    public static Block requestBlock(String name, int id)
    {
        if (OreDictionary.getOres(name).size() <= 0)
        {
            return requireBlock(name, id);
        }

        FMLLog.info("Resonant Engine: " + name + " already exists in Ore Dictionary, using the ore instead.");

        if (OreDictionary.getOres(name).get(0).getItem() instanceof ItemBlock)
        {
            return Block.blocksList[((ItemBlock) OreDictionary.getOres(name).get(0).getItem()).getBlockID()];
        }

        return null;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        References.LOGGER.setParent(FMLLog.getLogger());
        ConfigScanner.instance().generateSets(evt.getAsmData());

        /* Does a system check to see if we are running in an IDE as a Dev
         * http://andydunkel.net/java/2011/06/22/java-check-if-your-code-is-running-in-eclipse.html */
        String inEclipse = System.getProperty("uedev");

        if (inEclipse != null && inEclipse.equals("true"))
        {
            References.LOGGER.fine("\n######Running mod as a dev IDE######\n");
            ResonantEngine.runningAsDev = true;
        }

        NetworkRegistry.instance().registerGuiHandler(this, proxy);

        modproxies.applyModule(Waila.class, true);

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
    public void init(FMLInitializationEvent evt)
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

        References.LOGGER.fine("Attempting to load " + ComponentRegistry.requests.size() + " items.");

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
                References.LOGGER.severe("Failed to load Resonant Engine item: " + request);
            }
        }

        References.CONFIGURATION.save();

        References.LOGGER.fine("Loaded: " + LanguageUtility.loadLanguages(References.LANGUAGE_DIRECTORY, References.LANGUAGES) + " Languages.");

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
    public void postInit(FMLPostInitializationEvent evt)
    {

        // TODO: Move to UE
        if (!UpdateTicker.INSTANCE.isAlive())
        {
            UpdateTicker.INSTANCE.start();
        }

        ConfigHandler.configure(References.CONFIGURATION, References.DOMAIN);

        // Register Thermal Grid
        UpdateTicker.addNetwork(ResonantEngine.thermalGrid);

        modproxies.postInit();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
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

        // TODO: Move to UE
        serverCommandManager.registerCommand(new UECommand());
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent evt)
    {
        SaveManager.saveAll();
    }

    /** Default handler. */
    @ForgeSubscribe
    public void boilEventHandler(BoilEvent evt)
    {
        World world = evt.world;
        Vector3 position = evt.position;
        int blockID = position.getBlockID(world);

        for (int height = 1; height <= evt.maxSpread; height++)
        {
            TileEntity tileEntity = world.getBlockTileEntity(position.intX(), position.intY() + height, position.intZ());

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

        evt.setResult(Result.DENY);
    }

    /** Default handler. */
    @ForgeSubscribe
    public void thermalEventHandler(EventThermalUpdate evt)
    {
        final VectorWorld pos = evt.position;

        synchronized (pos.world)
        {
            Block block = Block.blocksList[pos.getBlockID()];
            Material mat = pos.world.getBlockMaterial(pos.intX(), pos.intY(), pos.intZ());

            if (mat == Material.air)
            {
                evt.heatLoss = 0.15f;
            }

            if (block == Block.waterMoving || block == Block.waterStill)
            {
                if (evt.temperature >= 373)
                {
                    if (FluidRegistry.getFluid("steam") != null)
                    {
                        // TODO: INCORRECT!
                        int volume = (int) (FluidContainerRegistry.BUCKET_VOLUME * (evt.temperature / 373) * steamMultiplier);
                        MinecraftForge.EVENT_BUS.post(new BoilEvent(pos.world, pos, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(FluidRegistry.getFluid("steam"), volume), 2, evt.isReactor));
                    }

                    evt.heatLoss = 0.2f;
                }
            }

            if (block == Block.ice)
            {
                if (evt.temperature >= 273)
                {
                    NetworkTickHandler.addNetwork(new IUpdate()
                    {
                        @Override
                        public void update()
                        {
                            pos.setBlock(Block.waterMoving.blockID);
                        }

                        @Override
                        public boolean canUpdate()
                        {
                            return true;
                        }

                        @Override
                        public boolean continueUpdate()
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
