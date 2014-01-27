package calclavia.components;

import universalelectricity.api.vector.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import calclavia.components.creative.BlockCreativeBuilder;
import calclavia.components.creative.GuiCreativeBuilder;
import calclavia.lib.prefab.ProxyBase;

public class ClientProxy extends ProxyBase
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		Block block = Block.blocksList[world.getBlockId(x, y, z)];

		if (block instanceof BlockCreativeBuilder)
			return new GuiCreativeBuilder(new Vector3(x, y, z));

		return null;
	}
}
