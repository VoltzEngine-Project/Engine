package com.builtbroken.mc.prefab.entity.cart;

import com.builtbroken.mc.api.rails.ITransportCart;
import com.builtbroken.mc.api.rails.ITransportRail;
import com.builtbroken.mc.api.rails.ITransportRailBlock;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.lib.helper.MathUtility;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.EntityBase;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2016.
 */
public abstract class EntityAbstractCart extends EntityBase implements IPacketIDReceiver, IEntityAdditionalSpawnData, ITransportCart
{
    /** Speed the cart wants to be pushed at */
    public static final float PUSH_SPEED = 0.05f;

    /** Side of the cart that the rail exists */
    public ForgeDirection railSide;
    /** Direction the rail below us is facing. */
    public ForgeDirection railDirection;
    /** Direction the cart is facing */
    public ForgeDirection facingDirection = ForgeDirection.NORTH;
    /** Last measure height of a the rail. */
    public float railHeight = 0.3f;

    /** Length of the cart */
    public float length = 3;
    /** Toggle to invalidate the collision box and have it reset next tick */
    private boolean invalidBox = false;
    /** Called to trigger a packet update next tick */
    private boolean updateClient = true;

    @SideOnly(Side.CLIENT)
    public double lastRenderX;
    @SideOnly(Side.CLIENT)
    public double lastRenderY;
    @SideOnly(Side.CLIENT)
    public double lastRenderZ;

    public EntityAbstractCart(World world)
    {
        super(world);
        height = 0.7f;
        width = .95f;
    }

    @Override
    public boolean hitByEntity(Entity entity)
    {
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)
        {
            kill();
            return false;
        }
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        //isPushedByWater()
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        invalidBox = true;
    }

    @Override
    public void setRotation(float yaw, float pitch)
    {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
        invalidBox = true;

        float yaw2 = (float) MathUtility.clampAngleTo180(this.rotationYaw);
        if (yaw2 >= -45 && yaw2 <= 45)
        {
            facingDirection = ForgeDirection.NORTH;
        }
        else if (yaw2 <= -135 || yaw2 >= 135)
        {
            facingDirection = ForgeDirection.SOUTH;
        }
        else if (yaw2 >= 45 && yaw2 <= 135)
        {
            facingDirection = ForgeDirection.EAST;
        }
        else if (yaw2 >= -135 && yaw2 <= -45)
        {
            facingDirection = ForgeDirection.WEST;
        }
    }

    @Override
    public float getDesiredPushVelocity()
    {
        return PUSH_SPEED;
    }

    /**
     * Updates the collision box to match it's rotation
     */
    protected void validateBoundBox()
    {
        invalidBox = false;

        float halfWidth = this.width / 2.0F;
        float halfLength = this.length / 2.0F;
        float yaw = (float) Math.abs(MathUtility.clampAngleTo180(this.rotationYaw));
        if (yaw >= 45 && yaw <= 135)
        {
            halfWidth = this.length / 2.0F;
            halfLength = this.width / 2.0F;
        }
        this.boundingBox.setBounds(
                posX - (double) halfWidth,
                posY - (double) this.yOffset + (double) this.ySize,
                posZ - (double) halfLength,

                posX + (double) halfWidth,
                posY - (double) this.yOffset + (double) this.ySize + this.height,
                posZ + (double) halfLength);
    }

    /**
     * Called to have the collision box
     * update next tick.
     */
    protected void markBoundsInvalid()
    {
        this.invalidBox = true;
    }


    @Override
    protected void setSize(float p_70105_1_, float p_70105_2_)
    {
        //Empty to prevent issues
    }

    @Override
    public void onUpdate()
    {
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        //Updates the collision box
        if (invalidBox || boundingBox == null)
        {
            validateBoundBox();
        }

        //Grab rail side if null
        if (railSide == null)
        {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            {
                Pos pos = new Pos((Entity) this).add(side);
                Block block = pos.getBlock(worldObj);
                final TileEntity tile = pos.getTileEntity(worldObj);
                if (!(block instanceof ITransportRailBlock || tile instanceof ITransportRail))
                {
                    railSide = side;
                    break;
                }
            }
        }

        Pos pos = new Pos(Math.floor(posX), Math.floor(posY), Math.floor(posZ)).floor();
        Block block = pos.getBlock(world());
        if (block == Blocks.air)
        {
            pos = pos.add(railSide);
            block = pos.getBlock(worldObj);
        }
        final TileEntity tile = pos.getTileEntity(worldObj);

        if (!worldObj.isRemote)
        {
            //Kills entity if it falls out of the world, should never happen
            if (this.posY < -64.0D)
            {
                this.kill();
                return;
            }
            //Breaks entity if its not on a track
            else if (!(block instanceof ITransportRailBlock || tile instanceof ITransportRail))
            {
                destroyCart();
                return;
            }
            //Moves the entity
            if (motionX != 0 || motionY != 0 || motionZ != 0)
            {
                moveEntity(motionX, motionY, motionZ);
                recenterCartOnRail();
            }
        }

        if (isAirBorne)
        {
            motionY -= (9.8 / 20);
            motionY = Math.max(-2, motionY);
        }

        //Handles pushing entities out of the way
        doCollisionLogic();

        //Updates the rail and cart position
        if (tile instanceof ITransportRail)
        {
            ((ITransportRail) tile).tickRailFromCart(this);
        }
        else if (block instanceof ITransportRailBlock)
        {
            ((ITransportRailBlock) block).tickRailFromCart(this, world(), pos.xi(), pos.yi(), pos.zi(), worldObj.getBlockMetadata(pos.xi(), pos.yi(), pos.zi()));
        }

        if (!worldObj.isRemote && updateClient)
        {
            sentDescriptionPacket();
        }
    }

    /**
     * Called to have a packet sent
     * to the client next tick
     */
    protected void markForClientSync()
    {
        this.updateClient = true;
    }

    @Override
    public void recenterCartOnRail()
    {
        this.recenterCartOnRail(railSide, railDirection, railHeight, false);
    }

    @Override
    public void recenterCartOnRail(ForgeDirection side, ForgeDirection facing, double railHeight, boolean trueCenter)
    {
        //Update cached data
        this.railDirection = facing;
        this.railHeight = (float) railHeight;
        this.railSide = side;

        if (side == ForgeDirection.UP)
        {
            posY = Math.floor(posY) + railHeight;
            motionY = 0;
        }
        else if (side == ForgeDirection.DOWN)
        {
            posY = Math.floor(posY) + 1 - railHeight;
            motionY = 0;
        }

        if (facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH)
        {
            motionX = 0;
            posX = Math.floor(posX) + 0.5;
            if (trueCenter)
            {
                posZ = Math.floor(posZ) + 0.5;
            }
        }
        else if (facing == ForgeDirection.EAST || facing == ForgeDirection.WEST)
        {
            motionZ = 0;
            posZ = Math.floor(posZ) + 0.5;
            if (trueCenter)
            {
                posX = Math.floor(posX) + 0.5;
            }
        }
    }

    @Override
    public void setMotion(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    protected void doCollisionLogic()
    {
        AxisAlignedBB box = boundingBox.expand(0.2D, 0.0D, 0.2D);

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

        if (list != null && !list.isEmpty())
        {
            for (Object aList : list)
            {
                Entity entity = (Entity) aList;

                if (entity != this.riddenByEntity && entity.canBePushed())
                {
                    entity.applyEntityCollision(this);
                }
            }
        }
    }

    @Override
    public void applyEntityCollision(Entity entity)
    {
        //TODO toggle a center rail method to call next tick
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0;
    }

    public void destroyCart()
    {
        InventoryUtility.dropItemStack(this, toStack());
        setDead();
    }

    public abstract ItemStack toStack();

    @Override
    public void onEntityUpdate()
    {
        //Empty to ignore default entity code
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {

    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {

    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return toStack();
    }
}
