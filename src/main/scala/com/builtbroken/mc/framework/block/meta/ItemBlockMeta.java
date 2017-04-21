package com.builtbroken.mc.framework.block.meta;

import com.builtbroken.mc.framework.block.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class ItemBlockMeta extends ItemBlockBase
{
    /** Quick cache of localization by meta data to provide a small speed bonus */
    public String[] localizationCache = new String[16];

    public ItemBlockMeta(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int damage = itemstack.getItemDamage();
        if (damage >= 0 && damage < 16)
        {
            if (localizationCache[damage] != null)
            {
                return localizationCache[damage];
            }

            //Assemble lang key, only called once per run
            String lang = getUnlocalizedName() + ".name";
            lang = lang.replace("${meta}", "" + damage);
            if (getBlockJson().meta[damage] != null)
            {
                lang = lang.replace("${metaLocalization}", getBlockJson().meta[damage].localization);
            }

            //Cache and return
            localizationCache[damage] = lang;
            return lang;
        }
        return getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName()
    {
        return getBlockJson().data.localization;
    }

    public BlockMeta getBlockJson()
    {
        return (BlockMeta) this.field_150939_a;
    }

    @Override
    public String toString()
    {
        return "ItemBlock[" + getBlockJson() + "]";
    }
}
