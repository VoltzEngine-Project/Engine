package calclavia.components.tool;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.Event.Result;
import universalelectricity.api.vector.Vector3;
import calclavia.components.ItemMultitool;
import calclavia.lib.utility.inventory.InventoryUtility;

public class ToolModeGeneral extends ToolMode
{
	@Override
	public String getName()
	{
		return "toolmode.general.name";
	}
}
