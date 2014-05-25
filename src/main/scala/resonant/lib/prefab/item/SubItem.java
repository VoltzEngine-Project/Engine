package resonant.lib.prefab.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Wrapper used by ItemBase to create sub items faster
 *
 * @author Darkguardsman
 */
public class SubItem
{
	private int meta = 0;
	private String unlocalizedName = "item.name";
	private String iconName = "";

	@SideOnly(Side.CLIENT)
	private IIcon icon;

	public SubItem(int meta, String unlocalizedName)
	{
		this.meta = meta;
		this.unlocalizedName = unlocalizedName;
	}

	@SideOnly(Side.CLIENT)
	public void loadIcon(IIconRegister register)
	{
		if (iconName != null && !iconName.isEmpty())
		{
			register.registerIcon(iconName);
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon icon()
	{
		return icon;
	}

	public int meta()
	{
		return meta;
	}

	public String unlocalizedName()
	{
		return unlocalizedName;
	}
}
