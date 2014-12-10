package resonant.lib.factory.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import resonant.engine.References;

/**
 * Created by robert on 11/28/2014.
 */
public class GeneratedOreItem
{
    @SideOnly(Side.CLIENT)
    private IIcon icon;

    private String name;
    private String textureName;
    private String unlocalizedName;

    public GeneratedOreItem(String name)
    {
        this.name = name;
        this.textureName = References.PREFIX + name;
        this.unlocalizedName = "item." + References.PREFIX + name;
    }

    public void setTextureName(String name)
    {
        this.textureName = name;
    }

    public void setUnlocalizedName(String name)
    {
        this.unlocalizedName = name;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.icon = reg.registerIcon(textureName);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(String ore)
    {
        return icon;
    }
}
