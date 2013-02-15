package basiccomponents.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.ore.OreGenBase;

/**
 * The main class for managing Basic Component items and blocks. Reference objects from this class
 * to add them to your recipes and such.
 * 
 * @author Calclavia
 */

public class BasicComponents
{
	public static final String FILE_PATH = "/basiccomponents/textures/";
	public static final String BLOCK_TEXTURE_FILE = FILE_PATH + "blocks.png";
	public static final String ITEM_TEXTURE_FILE = FILE_PATH + "items.png";

	public static int BLOCK_ID_PREFIX = 3970;

	public static Block blockBasicOre;
	public static Block blockCopperWire;
	public static Block blockMachine;

	public static final int ITEM_ID_PREFIX = 13970;
	public static ItemElectric itemBattery;
	public static Item itemWrench;
	public static Item itemCopperIngot;
	public static Item itemTinIngot;
	public static Item itemSteelIngot;
	public static Item itemSteelDust;
	public static Item itemCircuit;
	public static Item itemBronzeIngot;
	public static Item itemBronzeDust;
	public static Item itemSteelPlate;
	public static Item itemBronzePlate;
	public static Item itemMotor;
	public static Item itemCopperPlate;
	public static Item itemTinPlate;

	public static ItemStack coalGenerator;
	public static ItemStack batteryBox;
	public static ItemStack electricFurnace;

	public static OreGenBase copperOreGeneration;
	public static OreGenBase tinOreGeneration;
}
