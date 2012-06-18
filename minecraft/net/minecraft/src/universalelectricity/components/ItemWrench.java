package net.minecraft.src.universalelectricity.components;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.buildcraft.api.tools.IToolWrench;

public class ItemWrench extends UCItem implements IToolWrench
{
    public ItemWrench(int par1, int par2)
    {
        super("Wrench", par1, par2);
        this.iconIndex = par2;
    }

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z) { }
}
