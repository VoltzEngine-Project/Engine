package com.builtbroken.mc.framework.block;

import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.prefab.items.ItemBlockAbstract;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2017.
 */
public class ItemBlockBase extends ItemBlockAbstract implements IJsonRenderStateProvider
{
    public ItemBlockBase(Block block)
    {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getRenderContentID(IItemRenderer.ItemRenderType renderType, Object objectBeingRendered)
    {
        return getRenderContentID(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getRenderContentIDs()
    {
        List<String> list = new ArrayList();
        list.add(getRenderContentID(0));
        return list;
    }

    public String getRenderContentID(int meta)
    {
        return getBlockBase().getContentID(meta);
    }

    public BlockBase getBlockBase()
    {
        return (BlockBase) field_150939_a;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
        Block block = world.getBlock(x, y, z);

        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
        {
            side = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z))
        {
            if (side == 0)
            {
                --y;
            }

            if (side == 1)
            {
                ++y;
            }

            if (side == 2)
            {
                --z;
            }

            if (side == 3)
            {
                ++z;
            }

            if (side == 4)
            {
                --x;
            }

            if (side == 5)
            {
                ++x;
            }
        }

        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, side, stack))
        {
            return false;
        }
        else if (y == 255 && this.field_150939_a.getMaterial().isSolid())
        {
            return false;
        }
        else if (!getBlockBase().canPlaceBlockAt(player, world, x, y, z))
        {
            return false;
        }
        else if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, side, player, stack))
        {
            int i1 = this.getMetadata(stack.getItemDamage());
            int j1 = this.field_150939_a.onBlockPlaced(world, x, y, z, side, xHit, yHit, zHit, i1);

            if (placeBlockAt(stack, player, world, x, y, z, side, xHit, yHit, zHit, j1))
            {
                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
