package calclavia.components;

import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import calclavia.lib.Calclavia;

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
		super(Calclavia.CONFIGURATION.getItem(name, id).getInt(id));
		this.setUnlocalizedName(CalclaviaComponents.PREFIX + name);
		this.setTextureName(CalclaviaComponents.PREFIX + name);
		this.setNoRepair();
	}
}
