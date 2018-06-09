package com.builtbroken.mc.core.content.tool.screwdriver;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.tool.ItemScrewdriver;
import com.builtbroken.mc.api.tile.connection.ConnectionColor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ToolModeRotation extends ToolMode
{
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, ConnectionColor connectionColor, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        int blockMeta = world.getBlockMetadata(x, y, z);
        Block block = world.getBlock(x, y, z);

        if (block != null && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)))
        {
            ((ItemScrewdriver) stack.getItem()).wrenchUsed(player, x, y, z);
            player.swingItem();
            return true;
        }

        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, ConnectionColor connectionColor, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        //TODO add IC2 support

        return false;
    }

    @Override
    public String getInfoName()
    {
        return Engine.itemWrench.getUnlocalizedName() + ".mode.rotation.info";
    }

    @Override
    public String getItemUnlocalizedName(ConnectionColor color)
    {
        return Engine.itemWrench.getUnlocalizedName() + ".rotation." + color.name().toLowerCase();
    }

    @Override
    public String getTexture(String iconString)
    {
        return iconString + ".rotation";
    }

    @Override
    public String getColorizedTexture(String iconString)
    {
        return iconString + ".rotation.color";
    }
}
