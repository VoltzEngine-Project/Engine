package resonant.lib.prefab.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Wrapper used by ItemBase to create sub items faster
 * 
 * @author Darkguardsman */
public class SubItem
{
    private int meta = 0;
    private String unlocalizedName = "item.name";
    private String iconName = "";

    @SideOnly(Side.CLIENT)
    private Icon icon;

    public SubItem(int meta, String unlocalizedName)
    {
        this.meta = meta;
        this.unlocalizedName = unlocalizedName;
    }

    @SideOnly(Side.CLIENT)
    public void loadIcon(IconRegister register)
    {
        if (iconName != null && !iconName.isEmpty())
            register.registerIcon(iconName);
    }
    
    @SideOnly(Side.CLIENT)
    public Icon icon()
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
