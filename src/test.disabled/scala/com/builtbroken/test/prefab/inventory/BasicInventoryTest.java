package com.builtbroken.test.prefab.inventory;

import com.builtbroken.mc.abstraction.EngineLoader;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.prefab.inventory.BasicInventory;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/19/2015.
 */
@RunWith(VoltzTestRunner.class)
public class BasicInventoryTest extends AbstractTest
{
    @Test
    public void testInit()
    {
        BasicInventory inv = new BasicInventory(10);
        assertSame(inv.getSizeInventory(), 10);
        assertSame(inv.getContainedItems().size(), 0);
    }

    @Test
    public void testSetAndGetSlots()
    {
        BasicInventory inv = new BasicInventory(10);
        EngineLoader engine = Engine.loaderInstance;
        Engine.loaderInstance = null;
        try
        {
            inv.setInventorySlotContents(-1, new ItemStack(Items.record_11));
            if (Engine.loaderInstance != null)
            {
                System.out.println("Engine instance should have been null");
            }
            fail("Didn't throw error");
        }
        catch (RuntimeException e)
        {
        }
        try
        {
            inv.setInventorySlotContents(10, new ItemStack(Items.record_11));
            fail("Didn't throw error");
        }
        catch (RuntimeException e)
        {
        }
        //Test set when slot is null
        for (int i = 0; i < 10; i++)
        {
            inv.setInventorySlotContents(i, new ItemStack(Items.apple));
            assertTrue(inv.getStackInSlot(i) != null);
            assertTrue(InventoryUtility.stacksMatchExact(inv.getStackInSlot(i), new ItemStack(Items.apple)));
        }

        //Test set when slot is not null
        for (int i = 0; i < 10; i++)
        {
            inv.setInventorySlotContents(i, new ItemStack(Items.stone_axe));
            assertTrue(inv.getStackInSlot(i) != null);
            assertTrue(InventoryUtility.stacksMatchExact(inv.getStackInSlot(i), new ItemStack(Items.stone_axe)));
        }

        //Test set null, not null
        for (int i = 0; i < 10; i++)
        {
            inv.setInventorySlotContents(i, null);
            assertTrue(inv.getStackInSlot(i) == null);
        }

        inv = new BasicInventory(10)
        {
            @Override
            public void markDirty()
            {
                throw new RuntimeException();
            }
        };

        try
        {
            inv.setInventorySlotContents(0, new ItemStack(Items.record_11));
            fail("Should have thrown an error");
        }
        catch (RuntimeException e)
        {

        }

        try
        {
            inv.setInventorySlotContents(0, new ItemStack(Items.record_13));
            fail("Should have thrown an error");
        }
        catch (RuntimeException e)
        {

        }
        Engine.loaderInstance = engine;
    }

    @Test
    public void testGetStackInSlotOnClosing()
    {
        BasicInventory inv = new BasicInventory(10);
        //Test set when slot is null
        for (int i = 0; i < 10; i++)
        {
            inv.setInventorySlotContents(i, new ItemStack(Items.apple));
            assertTrue(inv.getStackInSlot(i) != null);
            assertTrue(InventoryUtility.stacksMatchExact(inv.getStackInSlot(i), new ItemStack(Items.apple)));
            ItemStack stack = inv.getStackInSlotOnClosing(i);
            assertTrue(inv.getStackInSlot(i) == null);
            assertTrue(InventoryUtility.stacksMatchExact(stack, new ItemStack(Items.apple)));
        }
    }

    @Test
    public void testSave()
    {
        BasicInventory inv = new BasicInventory(10);
        //Test set when slot is null
        for (int i = 0; i < 10; i++)
        {
            inv.setInventorySlotContents(i, new ItemStack(Items.apple));
        }
        NBTTagCompound tag = new NBTTagCompound();
        inv.save(tag);
        inv = new BasicInventory(10);
        inv.load(tag);
        for (int i = 0; i < 10; i++)
        {
            assertTrue(InventoryUtility.stacksMatchExact(inv.getStackInSlot(i), new ItemStack(Items.apple)));
        }
    }

    @Test
    public void testInventoryUpdate()
    {
        BasicInventoryObj inv = new BasicInventoryObj(10);
        inv.setInventorySlotContents(0, new ItemStack(Items.apple, 1, 0));
        assertEquals(inv.slot, 0);
        assertNull(inv.prev);
        assertEquals(inv.item.getItem(), Items.apple);
        inv.reset();

        inv.setInventorySlotContents(0, new ItemStack(Items.apple, 1, 0));
        assertEquals(inv.slot, -1);

        inv.setInventorySlotContents(0, new ItemStack(Items.apple, 2, 0));
        assertEquals(inv.slot, 0);
        assertEquals(inv.item.getItem(), Items.apple);
        assertEquals(inv.prev.getItem(), Items.apple);
        assertEquals(inv.prev.stackSize, 1);
        assertEquals(inv.item.stackSize, 2);
        inv.reset();

        inv.setInventorySlotContents(0, new ItemStack(Items.apple, 1, 0));
        assertEquals(inv.slot, 0);
        assertEquals(inv.item.getItem(), Items.apple);
        assertEquals(inv.prev.getItem(), Items.apple);
        assertEquals(inv.prev.stackSize, 2);
        assertEquals(inv.item.stackSize, 1);
        inv.reset();
    }

    /**
     * Test object for accessing internal data
     */
    public final class BasicInventoryObj extends BasicInventory
    {
        public int slot = -1;
        public ItemStack prev;
        public ItemStack item;

        public BasicInventoryObj(int slots)
        {
            super(slots);
        }

        @Override
        protected void onInventoryChanged(int slot, ItemStack prev, ItemStack item)
        {
            this.slot = slot;
            this.prev = prev;
            this.item = item;
        }

        public void reset()
        {
            slot = -1;
            prev = null;
            item = null;
        }
    }
}
