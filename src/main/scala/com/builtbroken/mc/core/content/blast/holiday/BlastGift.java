package com.builtbroken.mc.core.content.blast.holiday;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.framework.explosive.blast.Blast;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.util.List;

import static net.minecraftforge.common.ChestGenHooks.MINESHAFT_CORRIDOR;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/24/2017.
 */
public class BlastGift extends Blast<BlastGift>
{
    public BlastGift(IExplosiveHandler handler)
    {
        super(handler);
    }

    @Override
    public int shouldThreadAction()
    {
        return NO_THREAD;
    }

    @Override
    public void getEffectedBlocks(final List<IWorldEdit> list)
    {
        Location pos = new Location(oldWorld, x, y, z); //TODO make place more than 1
        if (pos.isReplaceable())
        {
            list.add(new ChestPlacement(pos));
        }
    }

    @Override
    public void doEffectOther(boolean beforeBlocksPlaced)
    {
        //Overrides to replace default action
    }

    @Override
    public void playAudioForEdit(IWorldEdit blocks)
    {
        //used only to override default action
    }

    @Override
    public void doStartAudio()
    {
        //used only to override default action
    }

    @Override
    public void doEndAudio()
    {
        //TODO happy upbeat music
    }

    @Override
    public void displayEffectForEdit(IWorldEdit blocks)
    {
        //used only to override default action
    }

    @Override
    public void doStartDisplay()
    {
        //used only to override default action
    }

    @Override
    public void doEndDisplay()
    {
        //used only to override default action
    }

    public static class ChestPlacement extends BlockEdit
    {
        public ChestPlacement(IWorldPosition vec)
        {
            super(vec, Blocks.chest);
        }

        @Override
        protected void onBlockPlaced()
        {
            TileEntity tile = getTileEntity();
            if (tile instanceof TileEntityChest)
            {
                ChestGenHooks info = ChestGenHooks.getInfo(MINESHAFT_CORRIDOR);
                WeightedRandomChestContent.generateChestContents(oldWorld().rand, info.getItems(oldWorld().rand), (TileEntityChest) tile, info.getCount(oldWorld().rand));
            }
        }
    }
}
