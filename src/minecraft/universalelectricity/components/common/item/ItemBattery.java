package universalelectricity.components.common.item;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.item.ItemElectric;
import universalelectricity.prefab.UETab;

public class ItemBattery extends ItemElectric
{
	public ItemBattery(int id)
	{
		super(id);
		this.setUnlocalizedName("battery");
		this.setCreativeTab(UETab.INSTANCE);
	}

	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		this.iconIndex = iconRegister.func_94245_a(this.getUnlocalizedName().replace("item.", BasicComponents.TEXTURE_NAME_PREFIX));
	}

	@Override
	public double getMaxJoules(ItemStack itemStack)
	{
		return 1000000;
	}

	@Override
	public double getVoltage(ItemStack itemStack)
	{
		return 25;
	}
}
