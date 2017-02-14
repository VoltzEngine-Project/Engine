package com.builtbroken.mc.lib.mod.compat.rf;

import cofh.api.energy.IEnergyHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.TileEnt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Demo battery for testing RF support in the development environment. Should not be extended, implemented, or used outside of the development environment.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/16/2016.
 */
public class TileRFBattery extends TileEnt implements IEnergyHandler
{
    //Settings
    private static int maxEnergy = 1000000;
    private static int iconsArrayLength = 16;

    @SideOnly(Side.CLIENT)
    private static IIcon[] icons;
    private static IIcon topBotIcon;

    //TODO move to testing repo
    private int energy = 0;
    private int cachedMeta = -1;
    private boolean energyHadChanged = true;

    private boolean infinite = false;


    /** Bitmask use to check if a wire can connect on a side **/
    private byte canConnectSide = 0;

    public TileRFBattery()
    {
        super("DemoRFBattery", Material.iron);
        this.creativeTab = CreativeTabs.tabRedstone;
        this.resistance = 10;
        this.hardness = 10;
    }

    @Override
    public TileRFBattery newTile()
    {
        return new TileRFBattery();
    }

    @Override
    public void update()
    {
        super.update();
        if (isServer())
        {
            if (infinite)
            {
                energy = maxEnergy;
            }
            //Updates the block meta if the energy value has changed
            if (energyHadChanged)
            {
                energyHadChanged = false;

                //Find out what meta value the energy converts into
                float percent = (float) energy / (float) maxEnergy;
                int meta = (int) (percent * iconsArrayLength);

                //Only trigger a block update if needed
                if (cachedMeta == -1 || cachedMeta != meta)
                {
                    world().setBlockMetadataWithNotify(xi(), yi(), zi(), meta, 3);
                    cachedMeta = world().getBlockMetadata(xi(), yi(), zi());
                }
            }
            //If we have energy attempt to give it away
            if (energy > 0)
            {
                int prev = energy;
                energy = RFEnergyHandler.exportEnergyAllSides(world(), xi(), yi(), zi(), this, energy);
                //Trigger update on next tick if energy value changed
                if (prev != energy)
                {
                    energyHadChanged = true;
                }
            }
        }
    }

    @Override
    protected boolean onPlayerRightClick(EntityPlayer player, int side, Pos hit)
    {
        //Ignore clicks with block to allow easy building
        if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlock))
        {
            if (isServer())
            {
                if (Engine.runningAsDev && player.getHeldItem() != null)
                {
                    if (player.getHeldItem().getItem() == Items.redstone)
                    {
                        energy = maxEnergy;
                        player.addChatComponentMessage(new ChatComponentText("Energy has been restored to max"));
                    }
                    else if (player.getHeldItem().getItem() == Items.glowstone_dust)
                    {
                        infinite = !infinite;
                        player.addChatComponentMessage(new ChatComponentText("Power set to infinite: " + infinite));
                    }
                }
                player.addChatComponentMessage(new ChatComponentText(LanguageUtility.getLocal("text.rf.power.amount").replace("%1", "" + energy).replace("%2", "" + maxEnergy)));
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean onPlayerRightClickWrench(EntityPlayer player, int side, Pos hit)
    {
        if (isServer())
        {
            toggleRenderSide(ForgeDirection.getOrientation(side));
            player.addChatComponentMessage(new ChatComponentText("Side set to connect: " + canConnectEnergy(ForgeDirection.getOrientation(side))));
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 0 || side == 1 ? topBotIcon : icons[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        topBotIcon = iconRegister.registerIcon(References.PREFIX + "metal_diamond");
        icons = new IIcon[iconsArrayLength];
        icons[0] = iconRegister.registerIcon(References.PREFIX + "battery/battery");
        for (int i = 1; i < iconsArrayLength; i++)
        {
            icons[i] = iconRegister.registerIcon(References.PREFIX + "battery/battery" + i);
        }
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        int room = maxEnergy - energy;
        int energyToStore = room > maxReceive ? maxReceive : room;
        if (energyToStore > 0 && !simulate)
        {
            energy += energyToStore;
            energyHadChanged = true;
        }
        return energyToStore;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        int energyToDrain = maxExtract < energy ? maxExtract : energy;
        if (energyToDrain > 0 && !simulate)
        {
            energy -= energyToDrain;
            energyHadChanged = true;
        }
        return energyToDrain;
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return energy;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return maxEnergy;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return (canConnectSide & (1 << from.ordinal())) != 0;
    }

    public void setConnectSide(ForgeDirection direction, boolean isClear)
    {
        if (isClear)
        {
            canConnectSide = (byte) (canConnectSide | (1 << direction.ordinal()));
        }
        else
        {
            canConnectSide = (byte) (canConnectSide & ~(1 << direction.ordinal()));

        }
        markDirty();
    }

    public void toggleRenderSide(ForgeDirection direction)
    {
        this.setConnectSide(direction, !canConnectEnergy(direction));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        energy = nbt.getInteger("rf");
        infinite = nbt.getBoolean("infiniteEnergy");
        canConnectSide = nbt.getByte("connections");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("rf", energy);
        nbt.setBoolean("infiniteEnergy", infinite);
        nbt.setByte("connections", canConnectSide);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        for (int i = 0; i < 15; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onPlaced(entityLiving, itemStack);
        TileEntity tile = world().getTileEntity(xi(), yi(), zi());
        if (itemStack.getItemDamage() > 0 && tile instanceof TileRFBattery)
        {
            ((TileRFBattery) tile).energy = (int) (maxEnergy * (itemStack.getItemDamage() / 14f));
        }
    }
}
