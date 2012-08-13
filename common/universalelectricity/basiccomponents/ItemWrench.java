package universalelectricity.basiccomponents;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import buildcraft.api.tools.IToolWrench;

public class ItemWrench extends BCItem implements IToolWrench
{
    public ItemWrench(int par1, int par2)
    {
        super("Wrench", par1, par2);
        this.setIconIndex(par2);
        this.setMaxStackSize(1);
        this.setTabToDisplayOn(CreativeTabs.tabTools);
    }

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z)
	{
		
	}
}
