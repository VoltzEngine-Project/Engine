package resonant.core.content;

import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import resonant.core.ResonantEngine;
import resonant.lib.References;

/** An Base Item Class for Basic Components. Do not use this! Make your own!
 * 
 * @author Calclavia */
public class ItemBase extends Item
{
    protected final Icon[] icons = new Icon[256];

    public ItemBase(String name, int id)
    {
        super(References.CONFIGURATION.getItem(name, id).getInt(id));
        this.setUnlocalizedName(References.PREFIX + name);
        this.setTextureName(References.PREFIX + name);
        this.setNoRepair();
    }
}
