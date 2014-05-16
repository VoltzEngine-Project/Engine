package resonant.lib.content.module;

import java.util.HashSet;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IPlayerUsing;
import resonant.core.ResonantEngine;
import resonant.lib.References;

/** All tiles inherit this class.
 * 
 * @author Calclavia */
public abstract class TileBase extends TileBlock implements IPlayerUsing
{
    public TileBase(String name, Material material)
    {
        super(name, material);
    }

    public TileBase(Material material)
    {
        super(material);
    }

    @Override
    public TileBlock tile()
    {
        return this;
    }

    private final HashSet<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();

    protected long ticks = 0;

    /** Called on the TileEntity's first tick. */
    public void initiate()
    {
    }

    @Override
    public void updateEntity()
    {
        if (this.ticks == 0)
        {
            initiate();
        }

        if (ticks >= Long.MAX_VALUE)
        {
            ticks = 1;
        }

        ticks++;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return References.PACKET_ANNOTATION.getPacket(this);
    }

    @Override
    public HashSet<EntityPlayer> getPlayersUsing()
    {
        return this.playersUsing;
    }

    @Override
    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
    }

    @Override
    public void setDirection(ForgeDirection direction)
    {
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, direction.ordinal(), 3);
    }

}
