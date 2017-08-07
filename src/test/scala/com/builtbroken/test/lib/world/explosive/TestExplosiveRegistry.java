package com.builtbroken.test.lib.world.explosive;

import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import com.builtbroken.mc.prefab.explosive.handler.ExplosiveHandlerTNT;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import com.builtbroken.mc.testing.junit.AbstractTest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/4/2016.
 */
public class TestExplosiveRegistry extends AbstractTest
{
    /** Tests {@link ExplosiveRegistry#get(String) */
    public void testGetString()
    {
        ExplosiveHandlerTNT t = new ExplosiveHandlerTNT();
        ExplosiveRegistry.getExplosiveMap().put("yolo", t );
        assertSame(t, ExplosiveRegistry.get("yolo"));
    }

    /** Tests {@link ExplosiveRegistry#get(ItemStack) */
    public void testGetItemStack()
    {
    }

    /** Tests {@link ExplosiveRegistry#isRegistered(IExplosiveHandler) */
    public void testIsRegistered()
    {
    }

    /** Tests {@link ExplosiveRegistry#getExplosives(String) */
    public void testGetExplosivesString()
    {
    }

    /** Tests {@link ExplosiveRegistry#getExplosives() */
    public void testGetExplosives()
    {
    }

    /** Tests {@link ExplosiveRegistry#getItems(IExplosiveHandler) */
    public void testGetItems()
    {
    }

    /** Tests {@link ExplosiveRegistry#getMods() */
    public void testGetMods()
    {
    }

    /** Tests {@link ExplosiveRegistry#getExplosiveSize(ItemStackWrapper) */
    public void testGetExplosiveSizeItemStackWrapper()
    {
    }

    /** Tests {@link ExplosiveRegistry#getExplosiveSize(double, double) */
    public void testGetExplosiveSizeDD()
    {
       // double r1 = ExplosiveRegistry.getExplosiveSize(5, 1);
       // double r2 = ExplosiveRegistry.getExplosiveSize(5, 2);
        //double r3 = ExplosiveRegistry.getExplosiveSize(5, 3);

        //assertEquals(5.0, r1);
        //assertEquals(6.299, (int)(r2 * 1000) / 1000.0);
        //assertEquals(7.211, (int)(r3 * 1000) / 1000.0);
    }

    /** Tests {@link ExplosiveRegistry#getExplosiveSize(ItemStack) */
    public void testGetExplosiveSizeItemStack()
    {
    }

    /** Tests {@link ExplosiveRegistry#getExplosiveMap() */
    public void testGetExplosiveMap()
    {
    }

    /** Tests {@link ExplosiveRegistry#registerExplosiveItem(ItemStack, IExplosiveHandler, double) */
    public void testRegisterExplosiveItemIED()
    {
    }

    /** Tests {@link ExplosiveRegistry#registerExplosiveItem(ItemStack, IExplosiveHandler) */
    public void testRegisterExplosiveItemIE()
    {
    }

    /** Tests {@link ExplosiveRegistry#registerExplosiveItem(ItemStack) */
    public void testRegisterExplosiveItemI()
    {
    }

    /** Tests {@link ExplosiveRegistry#registerOrGetExplosive(String, String, IExplosiveHandler) */
    public void testRegisterOrGetExplosiveSSE()
    {
    }

    /** Tests {@link ExplosiveRegistry#registerExplosive(String, String, IExplosiveHandler) */
    public void testRegisterExplosive()
    {
    }

    /** Tests {@link ExplosiveRegistry#unregisterExplosiveItem(ItemStack) */
    public void testUnregisterExplosiveItemI()
    {
    }

    /** Tests {@link ExplosiveRegistry#triggerExplosive(Location, IExplosiveHandler, TriggerCause, double, NBTTagCompound) */
    public void testTriggerExplosiveLocation()
    {
    }

    /** Tests {@link ExplosiveRegistry#triggerExplosive(World, double, double, double, IExplosiveHandler, TriggerCause, double, NBTTagCompound) */
    public void testTriggerExplosive()
    {
    }
}
