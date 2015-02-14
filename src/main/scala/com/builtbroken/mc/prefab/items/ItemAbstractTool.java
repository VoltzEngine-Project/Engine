package com.builtbroken.mc.prefab.items;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Set;

/** Clone of MC/Forge's ItemTool class with minor additions
 * for better reuse.
 *
 * Created by robert on 2/14/2015.
 */
public class ItemAbstractTool extends Item
{
    private Set toolSet;

    protected float efficiencyOnProperMaterial = 4.0F;
    protected float damageVsEntity = 2.0F;

    /**
     * Called to damage the tool
     * @param stack -  stack to be damage
     * @param damage - amount of damage to do
     * @param entity - entity who used the tool, may be null
     */
    protected void damageTool(ItemStack stack, int damage, EntityLivingBase entity)
    {
        stack.damageItem(2, null);
    }

    /**
     * Gets the set of materials/blocks this tool
     * is effective against. Make sure to cache
     * this set so not to cause extra memory usage
     */
    protected Set getEffectiveBlockSet(ItemStack stack)
    {
        return toolSet;
    }

    protected float getEfficiencyVsBlock(ItemStack stack, Block block)
    {
        return efficiencyOnProperMaterial;
    }

    protected float getEfficiencyVsEntity(ItemStack stack)
    {
        return damageVsEntity;
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block)
    {
        return getEffectiveBlockSet(stack).contains(block) || getEffectiveBlockSet(stack).contains(block.getMaterial()) ? getEfficiencyVsBlock(stack, block) : 1.0F;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase hit, EntityLivingBase entity)
    {
        damageTool(stack, 2, entity);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
    {
        if ((double)block.getBlockHardness(world, x, y, z) != 0.0D)
        {
            damageTool(stack, 1, entity);
        }
        return true;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @Override @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    public int getItemEnchantability()
    {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
    {
        return false;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
        Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", (double)this.getEfficiencyVsEntity(stack),0));
        return multimap;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
        if (ForgeHooks.isToolEffective(stack, block, meta))
        {
            return efficiencyOnProperMaterial;
        }
        return super.getDigSpeed(stack, block, meta);
    }
}
