package basiccomponents;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.mod_BasicComponents;
import universalelectricity.UniversalElectricity;
import universalelectricity.network.PacketManager;


/**
 * The Class BasicComponents.
 */
public class BasicComponents
{
    
    /** The Constant filePath. */
    public static final String filePath = "/basiccomponents/textures/";
    
    /** The Constant blockTextureFile. */
    public static final String blockTextureFile = "/basiccomponents/textures/blocks.png";
    
    /** The Constant packetManager. */
    public static final PacketManager packetManager = new PacketManager("BasicComponents");
    
    /** The instance. */
    public static mod_BasicComponents instance;
    
    /** The Constant CopperOreID. */
    public static final int CopperOreID;
    
    /** The Constant TinOreID. */
    public static final int TinOreID;
    
    /** The Constant blockIDPrefix. */
    public static final int blockIDPrefix = 3970;
    
    /** The Constant BlockCopperWire. */
    public static final Block BlockCopperWire;
    
    /** The Constant BlockBatteryBox. */
    public static final Block BlockBatteryBox;
    
    /** The Constant BlockCoalGenerator. */
    public static final Block BlockCoalGenerator;
    
    /** The Constant BlockElectricFurnace. */
    public static final Block BlockElectricFurnace;
    
    /** The Constant ItemBattery. */
    public static final Item ItemBattery;
    
    /** The Constant ItemWrench. */
    public static final Item ItemWrench;
    
    /** The Constant ItemCopperIngot. */
    public static final Item ItemCopperIngot;
    
    /** The Constant ItemTinIngot. */
    public static final Item ItemTinIngot;
    
    /** The Constant ItemSteelIngot. */
    public static final Item ItemSteelIngot;
    
    /** The Constant ItemSteelClump. */
    public static final Item ItemSteelClump;
    
    /** The Constant ItemCircuit. */
    public static final Item ItemCircuit;
    
    /** The Constant ItemCopperWire. */
    public static final Item ItemCopperWire;
    
    /** The Constant ItemBronzeIngot. */
    public static final Item ItemBronzeIngot;
    
    /** The Constant ItemBronzeClump. */
    public static final Item ItemBronzeClump;
    
    /** The Constant ItemSteelPlate. */
    public static final Item ItemSteelPlate;
    
    /** The Constant ItemMotor. */
    public static final Item ItemMotor;

    /**
     * Instantiates a new basic components.
     */
    public BasicComponents()
    {
    }

    /**
     * Gets the single instance of BasicComponents.
     *
     * @return single instance of BasicComponents
     */
    public static mod_BasicComponents getInstance()
    {
        if (instance == null)
        {
            instance = new mod_BasicComponents();
        }

        return instance;
    }

    static
    {
        CopperOreID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Copper Ore Metadata", 0, true);
        TinOreID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Tin Ore Metadata", 1, true);
        BlockCopperWire = new BlockCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Copper Wire", 3970, true));
        BlockBatteryBox = new BlockBatteryBox(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Battery Box", 3971, true), 0);
        BlockCoalGenerator = new BlockCoalGenerator(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Coal Generator", 3972, true), 0);
        BlockElectricFurnace = new BlockElectricFurnace(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Electric Furnace", 3973, true), 0);
        ItemBattery = new ItemBattery(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Battery", 1586, false), 0);
        ItemWrench = new ItemWrench(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Wrench", 1587, false), 20);
        ItemCopperIngot = new BCItem("Copper Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "CopperIngot", 1588, false), 1);
        ItemTinIngot = new BCItem("Tin Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "TinIngot", 1589, false), 2);
        ItemSteelIngot = new BCItem("Steel Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "SteelIngot", 1590, false), 3);
        ItemSteelClump = new BCItem("Steel Alloy", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "SteelClump", 1591, false), 5);
        ItemCircuit = new ItemCircuit(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Circuit", 1592, false), 16);
        ItemCopperWire = new ItemCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "ItemCopperWire", 1593, false), 21);
        ItemBronzeIngot = new BCItem("Bronze Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "BronzeIngot", 1594, false), 7);
        ItemBronzeClump = new BCItem("Bronze Alloy", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "BronzeClump", 1595, false), 6);
        ItemSteelPlate = new BCItem("Steel Plate", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Steel Plate", 1597, false), 8);
        ItemMotor = new BCItem("Motor", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Motor", 1598, false), 9);
    }
}
