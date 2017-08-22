package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.data.Direction;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by robert on 8/7/2014.
 */
public class BlockUtility
{
    private static HashMap<Block, Float> BLOCK_HARDNESS = new HashMap();
    private static HashMap<Block, Float> BLOCK_RESISTANCE = new HashMap();

    /**
     * Obfuscation names for reflection
     */
    public static final String[] CHUNK_RELIGHT_BLOCK = {"relightBlock", "func_76615_h"};
    public static final String[] CHUNK_PROPOGATE_SKY_LIGHT_OCCLUSION = {"propagateSkylightOcclusion", "func_76595_e"};

    /**
     * Gets the block's resistance without using location data required by the
     * block's getResistance method. Uses a combination of a cache and reflection
     * to get access to the data.
     *
     * @param block - block, can't be null
     * @return
     */
    public static float getBlockResistance(Block block)
    {
        //Get block hardness from cache so we don't need to use reflection every time
        if (BLOCK_RESISTANCE.containsKey(block))
        {
            return BLOCK_RESISTANCE.get(block);
        }

        //Get the block using reflection as the field is private
        try
        {
            Field field = ReflectionUtility.getMCField(Block.class, "blockResistance", "field_149781_w");
            BLOCK_RESISTANCE.put(block, field.getFloat(block));
            return BLOCK_RESISTANCE.get(block);
        }
        catch (Exception e)
        {
            Engine.logger().info("Failed to reflect into Block.class to get block resistance");
            Engine.logger().catching(e);
        }
        return 0;
    }

    /**
     * Gets the block's hardness without using location data required by the
     * block's getHardness method. Uses a combination of a cache and reflection
     * to get access to the data.
     *
     * @param block - block, if null will return 0
     * @return
     */
    public static float getBlockHardness(Block block)
    {
        //Get block hardness from cache so we don't need to use reflection every time
        if (BLOCK_HARDNESS.containsKey(block))
        {
            return BLOCK_HARDNESS.get(block);
        }

        //Get the block using reflection as the field is private
        try
        {
            Field field = ReflectionUtility.getMCField(Block.class, "blockHardness", "field_149782_v");
            BLOCK_HARDNESS.put(block, field.getFloat(block));
            return BLOCK_HARDNESS.get(block);
        }
        catch (Exception e)
        {
            Engine.logger().info("Failed to reflect into Block.class to get block hardness");
            Engine.logger().catching(e);
        }
        return 0;
    }

    /**
     * Gets the hardness value of the block represented by
     * the itemstack. Doesn't check if the item is not an ItemBlock
     *
     * @param stack - stack
     * @return hardness value
     */
    public static float getBlockHardness(ItemStack stack)
    {
        return getBlockHardness(Block.getBlockFromItem(stack.getItem()));
    }

    @Deprecated
    public static byte determineOrientation(int x, int y, int z, Entity entityLiving)
    {
        if (entityLiving != null)
        {
            //TODO see what the math function does
            if (MathUtility.func_154353_e(entityLiving.posX - x) < 2.0F && MathUtility.func_154353_e(entityLiving.posZ - z) < 2.0F)
            {
                double var5 = entityLiving.posY + 1.82D - entityLiving.getYOffset();
                if (var5 - y > 2.0D)
                {
                    return 1;
                }
                if (y - var5 > 0.0D)
                {
                    return 0;
                }
            }
            final int rotation = MathHelper.floor(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            if (rotation == 0)
            {
                return 2;
            }
            else if (rotation == 1)
            {
                return 5;
            }
            else if (rotation == 2)
            {
                return 3;
            }
            else if (rotation == 3)
            {
                return 4;
            }
            else
            {
                return 0;
            }
        }
        return 0;
    }

    /**
     * Gets the orientation from the entity in sides of a block
     *
     * @param x
     * @param y
     * @param z
     * @param entityLiving
     * @return 0-5 @see {@link Direction}
     */
    public static byte determineOrientation(int x, int y, int z, IEntityData entityLiving)
    {
        if (entityLiving != null)
        {
            //TODO see what the math function does
            if (MathUtility.func_154353_e(entityLiving.x() - x) < 2.0F && MathUtility.func_154353_e(entityLiving.z() - z) < 2.0F)
            {
                double var5 = entityLiving.y() + 1.82D - entityLiving.getYOffset();
                if (var5 - y > 2.0D)
                {
                    return 1;
                }
                if (y - var5 > 0.0D)
                {
                    return 0;
                }
            }
            final int rotation = MathHelper.floor(entityLiving.yaw() * 4.0F / 360.0F + 0.5D) & 3;
            if (rotation == 0)
            {
                return 2;
            }
            else if (rotation == 1)
            {
                return 5;
            }
            else if (rotation == 2)
            {
                return 3;
            }
            else if (rotation == 3)
            {
                return 4;
            }
            else
            {
                return 0;
            }
        }
        return 0;
    }

    /**
     * Gets the rotation from the entity in sides of a block
     * ignores pitch of the entity so will never return 0 or 1
     * unless invalid
     *
     * @param rotationYaw - rotation of the object
     * @return 2-5 @see {@link Direction}
     */
    public static byte determineRotation(double rotationYaw)
    {
        int rotation = MathHelper.floor(rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        if (rotation == 0)
        {
            return 3;
        }
        else if (rotation == 1)
        {
            return 4;
        }
        else if (rotation == 2)
        {
            return 2;
        }
        else if (rotation == 3)
        {
            return 5;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Gets the forge direction from the facing value of the entity
     *
     * @param entityLiving
     * @return {@link Direction#NORTH} {@link Direction#SOUTH} {@link Direction#EAST} {@link Direction#WEST}
     */
    public static Direction determineDirection(IEntityData entityLiving)
    {
        return Direction.getOrientation(determineRotation(entityLiving.yaw()));
    }

    /**
     * Gets the forge direction for the facing direction of the entity
     * <p>
     * If vars are invalid it will return {@link Direction#NORTH} due
     * to method calls return zero
     *
     * @param x
     * @param y
     * @param z
     * @param entityLiving
     * @param ignorePitch  - will not return {@link Direction#UP} or {@link Direction#DOWN} if true as
     *                     these depend on the pitch of the entity's view. Pitch is not actually calculated from
     *                     the entity's head position. Instead it is calculated from delta Y
     * @return forge direction
     */
    public static Direction determineDirection(int x, int y, int z, IEntityData entityLiving, boolean ignorePitch)
    {
        if (ignorePitch)
        {
            return Direction.getOrientation(determineRotation(entityLiving.yaw()));
        }
        return Direction.getOrientation(determineOrientation(x, y, z, entityLiving));
    }
}
