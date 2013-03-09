package universalelectricity.components.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.IToolConfigurator;

public class ItemWrench extends Item implements IToolConfigurator
{
	public ItemWrench(int id, int texture)
	{
		super(id);
		this.setMaxStackSize(1);
		this.setMaxDamage(3000);
		this.setCreativeTab(UETab.INSTANCE);
		this.setUnlocalizedName("wrench");
	}

	@Override
	public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
	{
		if (entityPlayer.getCurrentEquippedItem() != null)
		{
			if (entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemWrench)
			{
				entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return true;
	}

	@Override
	public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z)
	{
		return true;
	}
}
