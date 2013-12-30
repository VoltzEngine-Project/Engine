package calclavia.components;

import net.minecraft.item.Item;
import net.minecraft.util.Icon;

/**
 * An Base Item Class for Basic Components. Do not use this! Make your own!
 * 
 * @author Calclavia
 * 
 */
public class ItemBase extends Item
{
	protected final Icon[] icons = new Icon[256];

	public ItemBase(String name, int id)
	{
		super(CalclaviaCore.CONFIGURATION.getItem(name, id).getInt(id));
		this.setUnlocalizedName(CalclaviaCore.PREFIX + name);
		this.setTextureName(CalclaviaCore.PREFIX + name);
		this.setNoRepair();
	}
}
