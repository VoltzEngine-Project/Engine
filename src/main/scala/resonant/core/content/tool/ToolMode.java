package resonant.core.content.tool;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ToolMode
{
    public static final List<ToolMode> REGISTRY = new ArrayList<ToolMode>();

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        return itemStack;
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /** The name of the tool mode.
     * 
     * @return toolmode.XXX.name */
    public abstract String getName();
}
