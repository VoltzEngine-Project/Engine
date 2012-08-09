package net.minecraft.src.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.network.PacketManager;

/**
 * A static class where you can reference variables from here
 * @author Calclavia
 *
 */
public class BasicComponents
{
    public static final String FILE_PATH = "/basiccomponents/textures/";

    public static final String blockTextureFile = FILE_PATH + "blocks.png";

    public static final PacketManager PACKET_MANAGER = new PacketManager("BasicComponents");

    public static mod_BasicComponents instance;

    public static mod_BasicComponents getInstance()
    {
        if (instance == null)
        {
            instance = new mod_BasicComponents();
        }

        return instance;
    }

    /**
     * Here is where all the Universal Components are defined. You may reference to these variables.
     */
    public static final int COPPER_ORE_ID = UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Copper Ore Metadata", 0, true);
    public static final int TIN_ORE_ID = UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Tin Ore Metadata", 1, true);

    public static final int BLOCK_ID_PREFIX = 3970;
    public static final Block blockCopperWire = new BlockCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Copper Wire", BLOCK_ID_PREFIX, true));
    public static final Block blockBatteryBox = new BlockBatteryBox(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Battery Box", BLOCK_ID_PREFIX + 1, true), 0);
    public static final Block blockCoalGenerator = new BlockCoalGenerator(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Coal Generator", BLOCK_ID_PREFIX + 2, true), 0);
    public static final Block blockElectricFurnace = new BlockElectricFurnace(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Electric Furnace", BLOCK_ID_PREFIX + 3, true), 0);

    public static final Item itemBattery = new ItemBattery(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Battery", 1586, false), 0);
    public static final Item itemWrench = new ItemWrench(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Wrench", 1587, false), 20);
    public static final Item itemCopperIngot = new BCItem("Copper Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "CopperIngot", 1588, false), 1);
    public static final Item itemTinIngot = new BCItem("Tin Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "TinIngot", 1589, false), 2);
    public static final Item itemSteelIngot = new BCItem("Steel Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "SteelIngot", 1590, false), 3);
    public static final Item itemSteelClump = new BCItem("Steel Alloy", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "SteelClump", 1591, false), 5);
    public static final Item itemCircuit = new ItemCircuit(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Circuit", 1592, false), 16);
    public static final Item itemCopperWire = new ItemCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "ItemCopperWire", 1593, false), 21);
    public static final Item itemBronzeIngot = new BCItem("Bronze Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "BronzeIngot", 1594, false), 7);
    public static final Item itemBronzeClump = new BCItem("Bronze Alloy", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "BronzeClump", 1595, false), 6);
    public static final Item itemSteelPlate = new BCItem("Steel Plate", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Steel Plate", 1597, false), 8);
    public static final Item itemMotor = new BCItem("Motor", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Motor", 1598, false), 9);
}
