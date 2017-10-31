package com.builtbroken.mc.core.content.entity;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.core.content.entity.bat.EntityExBat;
import com.builtbroken.mc.core.content.entity.creeper.EntityExCreeper;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.lib.data.item.ItemStackWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/31/2017.
 */
public class ItemExEntitySpawnEgg extends Item
{
    public ItemExEntitySpawnEgg()
    {
        this.setUnlocalizedName("exSpawnEgg");
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double d0 = 0.0D;

            if (side == 1 && block.getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(world, stack.getItemDamage(), (double) x + 0.5D, (double) y + d0, (double) z + 0.5D);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                {
                    ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }
            }

            return true;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return stack;
        }
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

            if (movingobjectposition == null)
            {
                return stack;
            }
            else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!world.canMineBlock(player, i, j, k))
                    {
                        return stack;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack))
                    {
                        return stack;
                    }

                    if (world.getBlock(i, j, k) instanceof BlockLiquid)
                    {
                        Entity entity = spawnCreature(world, stack.getItemDamage(), (double) i, (double) j, (double) k);

                        if (entity != null)
                        {
                            if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                            {
                                ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                            }

                            if (!player.capabilities.isCreativeMode)
                            {
                                --stack.stackSize;
                            }
                        }
                    }
                }

                return stack;
            }
        }
    }

    public Entity spawnCreature(World world, int entityID, double x, double y, double z)
    {
        Entity entity = null;
        if (entityID == 0)
        {
            entity = new EntityExCreeper(world);
        }
        else if (entityID == 1)
        {
            entity = new EntityExBat(world);
        }

        if (entity != null && entity instanceof EntityLivingBase)
        {
            EntityLiving entityliving = (EntityLiving) entity;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            entityliving.onSpawnWithEgg((IEntityLivingData) null);
            world.spawnEntityInWorld(entity);
            entityliving.playLivingSound();
        }

        return entity;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = null;
        if (stack.getItemDamage() == 0)
        {
            s = "ExCreeper";
        }
        else if (stack.getItemDamage() == 1)
        {
            s = "ExBat";
        }

        if (s1 != null)
        {
            String entityTranslation = null;

            //Attempt to get custom name from explosive
            if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("ex"))
            {
                ItemStack exStack = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("ex"));
                if(exStack != null)
                {
                    IExplosiveHandler handler = ExplosiveRegistry.get(exStack);
                    if(handler != null)
                    {
                        entityTranslation = StatCollector.translateToLocal("entity." + s1 + "." + handler.getID() + ".name");
                    }
                }
            }

            //If custom name fails use default name
            if (entityTranslation == null || entityTranslation.isEmpty())
            {
                entityTranslation = StatCollector.translateToLocal("entity." + s1 + ".name");
            }

            s = entityTranslation;
        }

        return s;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        //TODO implement
        return 16777215;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass)
    {
        return Items.spawn_egg.getIconFromDamageForRenderPass(meta, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List items)
    {
        //Loop explosives
        for (IExplosiveHandler ex : ExplosiveRegistry.getExplosives())
        {
            //Get possible combinations
            List<ItemStackWrapper> explosiveItems = ExplosiveRegistry.getItems(ex);
            if (explosiveItems != null)
            {
                //Loop explosives items
                for (ItemStackWrapper wrapper : explosiveItems)
                {
                    //Loop creature types
                    for (int i = 0; i < 2; i++)
                    {
                        ItemStack stack = new ItemStack(item, 1, i);
                        stack.setTagCompound(new NBTTagCompound());
                        stack.getTagCompound().setTag("ex", wrapper.itemStack.writeToNBT(new NBTTagCompound()));
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        //Piggy back ItemMonsterEgg
    }
}
