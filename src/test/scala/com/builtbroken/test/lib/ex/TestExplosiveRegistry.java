package com.builtbroken.test.lib.ex;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import com.builtbroken.mc.prefab.explosive.ExplosiveHandler;
import com.builtbroken.mc.prefab.explosive.ItemNBTExplosive;
import com.builtbroken.mc.prefab.explosive.blast.BlastBasic;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/28/2016.
 */
@RunWith(VoltzTestRunner.class)
public class TestExplosiveRegistry extends AbstractTest
{
    private static ItemNBTExplosive explosive;

    @Override
    public void setUpForEntireClass()
    {
        if(explosive == null)
        {
            explosive = new ItemNBTExplosive();
            GameRegistry.registerItem(explosive, "testExplosiveRegExItem");
        }
    }

    @Test
    public void testRegisterExplosiveItem()
    {
        setUpForEntireClass();
        IExplosiveHandler TNT = new ExplosiveHandler("tnt", BlastBasic.class, 1);
        ExplosiveRegistry.registerOrGetExplosive(References.DOMAIN, "TNT", TNT);
        ExplosiveRegistry.registerExplosiveItem(new ItemStack(Blocks.tnt), ExplosiveRegistry.get("TNT"), 2);

        assertTrue(ExplosiveRegistry.get("TNT") == TNT);
        assertTrue(ExplosiveRegistry.get(new ItemStack(Blocks.tnt)) == TNT);
        assertTrue(ExplosiveRegistry.getExplosiveSize(new ItemStack(Blocks.tnt)) == 2);
        assertTrue(ExplosiveRegistry.getItems(TNT).contains(new ItemStackWrapper(new ItemStack(Blocks.tnt))));

        ItemStack stack = new ItemStack(explosive);
        explosive.setExplosive(stack, TNT, 3, null);
        ExplosiveRegistry.registerExplosiveItem(stack);

        assertTrue(ExplosiveRegistry.get(stack) == TNT);
        assertTrue(ExplosiveRegistry.getExplosiveSize(stack) == 3);
        assertTrue(ExplosiveRegistry.getItems(TNT).contains(new ItemStackWrapper(stack)));
    }
}
