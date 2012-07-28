package net.minecraft.src.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.mod_BasicComponents;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.network.PacketManager;

/**
 * A static class where you can reference variables from here
 * @author Calclavia
 *
 */
public class BasicComponents
{
    public static final String filePath = "/basiccomponents/textures/";

    public static final String blockTextureFile = filePath + "blocks.png";

    public static final PacketManager packetManager = new PacketManager("BasicComponents");

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
    public static final int CopperOreID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Copper Ore Metadata", 0, true);
    public static final int TinOreID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Tin Ore Metadata", 1, true);

    public static final int blockIDPrefix = 3970;
    public static final Block BlockCopperWire = new BlockCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Copper Wire", blockIDPrefix, true));
    public static final Block BlockBatteryBox = new BlockBatteryBox(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Battery Box", blockIDPrefix + 1, true), 0);
    public static final Block BlockCoalGenerator = new BlockCoalGenerator(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Coal Generator", blockIDPrefix + 2, true), 0);
    public static final Block BlockElectricFurnace = new BlockElectricFurnace(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Electric Furnace", blockIDPrefix + 3, true), 0);

    public static final Item ItemBattery = new ItemBattery(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Battery", 1586, false), 0);
    public static final Item ItemWrench = new ItemWrench(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Wrench", 1587, false), 20);
    public static final Item ItemCopperIngot = new BCItem("Copper Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "CopperIngot", 1588, false), 1);
    public static final Item ItemTinIngot = new BCItem("Tin Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "TinIngot", 1589, false), 2);
    public static final Item ItemSteelIngot = new BCItem("Steel Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "SteelIngot", 1590, false), 3);
    public static final Item ItemSteelClump = new BCItem("Steel Alloy", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "SteelClump", 1591, false), 5);
    public static final Item ItemCircuit = new ItemCircuit(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Circuit", 1592, false), 16);
    public static final Item ItemCopperWire = new ItemCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "ItemCopperWire", 1593, false), 21);
    public static final Item ItemBronzeIngot = new BCItem("Bronze Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "BronzeIngot", 1594, false), 7);
    public static final Item ItemBronzeClump = new BCItem("Bronze Alloy", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "BronzeClump", 1595, false), 6);
    public static final Item ItemSteelPlate = new BCItem("Steel Plate", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Steel Plate", 1597, false), 8);
    public static final Item ItemMotor = new BCItem("Motor", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Motor", 1598, false), 9);
}
