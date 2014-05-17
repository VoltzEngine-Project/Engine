package resonant.core.content.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import resonant.lib.multiblock.IBlockActivate;
import resonant.lib.prefab.tile.TileIO;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;

/** Designed to debug fluid devices by draining everything that comes in at one time
 * 
 * @author DarkGuardsman */
public class TileInfiniteFluid extends TileIO implements IFluidHandler, IBlockActivate
{

    FluidTank tank;
    boolean active = false;

    public TileInfiniteFluid()
    {
        super(UniversalElectricity.machine);
        this.saveIOMap = true;
        tank = new FluidTank(Integer.MAX_VALUE);
        ioMap = 728;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (active)
        {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                if (this.getOutputDirections().contains(direction))
                {
                    TileEntity tile = new Vector3(this).translate(direction).getTileEntity(this.worldObj);
                    if (tile instanceof IFluidHandler)
                    {
                        ((IFluidHandler) tile).fill(direction.getOpposite(), this.tank.getFluid(), true);
                    }
                }
            }
        }
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { this.tank.getInfo() };
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (getInputDirections().contains(from))
        {
            return resource.amount;
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (getOutputDirections().contains(from))
        {
            return resource;
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (getOutputDirections().contains(from))
        {
            return this.tank.drain(maxDrain, false);
        }

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return getInputDirections().contains(from);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return getOutputDirections().contains(from);
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        if (entityPlayer != null && entityPlayer.getHeldItem() != null)
        {
            if (entityPlayer.getHeldItem().getItem() == Item.stick)
            {
                this.active = !this.active;
                entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("[FluidVoid]Pumping:" + this.active));
                return true;
            }
            FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(entityPlayer.getHeldItem());
            if (stack != null)
            {
                stack = stack.copy();
                stack.amount = Integer.MAX_VALUE;
                this.tank.setFluid(stack);
                entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("[FluidVoid]Fluid:" + stack.getFluid().getName()));

                return true;
            }
        }
        return false;
    }

}
