package resonant.lib.prefab.tile;

import java.util.HashSet;

import resonant.api.IPlayerUsing;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/** A TileEntity with some pre-added functionalities.
 * 
 * @author Calclavia */
@Deprecated
public abstract class TileAdvanced extends TileEntity implements IPlayerUsing
{
    private final HashSet<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();

    protected long ticks = 0;

    @Override
    public void updateEntity()
    {
        if (this.ticks == 0)
        {
            this.initiate();
        }

        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 1;
        }

        this.ticks++;
    }

    /** Called on the TileEntity's first tick. */
    public void initiate()
    {
    }

    @Override
    public int getBlockMetadata()
    {
        if (this.blockMetadata == -1)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata;
    }

    @Override
    public Block getBlockType()
    {
        if (this.worldObj != null)
        {
            if (this.blockType == null)
            {
                this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
            }
        }

        return this.blockType;
    }

    @Override
    public HashSet<EntityPlayer> getPlayersUsing()
    {
        return this.playersUsing;
    }

    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
    }

    public void setDirection(ForgeDirection direction)
    {
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, direction.ordinal(), 3);
    }
}
