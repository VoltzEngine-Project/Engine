package resonant.lib.prefab.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IIO;

/** For blocks that can be set to input/output for their sides.
 * 
 * @author Calclavia */
public abstract class BlockSidedIO extends BlockTile
{
    public BlockSidedIO(int id, Material material)
    {
        super(id, material);
    }

    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        if (!entityPlayer.isSneaking())
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile instanceof IIO)
            {
                int newIO = (((IIO) tile).getIO(ForgeDirection.getOrientation(side)) + 1) % 3;
                ((IIO) tile).setIO(ForgeDirection.getOrientation(side), newIO);

                if (!world.isRemote)
                {
                    entityPlayer.addChatMessage("Side changed to: " + (newIO == 0 ? "None" : (newIO == 1 ? "Input" : "Output")));
                }

                world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
                return true;
            }
        }

        return false;
    }
}
