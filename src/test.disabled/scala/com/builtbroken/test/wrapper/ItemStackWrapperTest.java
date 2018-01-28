package com.builtbroken.test.wrapper;

import com.builtbroken.mc.lib.helper.MathUtility;
import com.builtbroken.mc.lib.data.item.ItemStackWrapper;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
@RunWith(VoltzTestRunner.class)
public class ItemStackWrapperTest extends AbstractTest
{
    @Override
    public void setUpForEntireClass()
    {
        super.setUpForEntireClass();
    }

    //Reason 1 why we use the ItemStack wrapper
    public void testFailEquals()
    {
        ItemStack a = new ItemStack(Items.apple);
        ItemStack b = new ItemStack(Items.apple);
        assertTrue("Both items should be the same", a.isItemEqual(b));
        //If this ever returns true check if ItemStack has an equals method that works
        assertFalse("Maybe MC added an Equals method to ItemStack?", a == b);
    }


    //Reason 2 why we use the ItemStack wrapper
    public void testFailListGet()
    {
        ItemStack a = new ItemStack(Items.apple);
        ItemStack b = new ItemStack(Items.apple);
        assertTrue(a.isItemEqual(b));
        List<ItemStack> list = new ArrayList();
        list.add(a);
        //If this ever returns true check if ItemStack has an equals method that works
        assertFalse(list.contains(b));
    }

    //Reason 2 why we use the ItemStack wrapper
    public void testFailMapGet()
    {
        ItemStack a = new ItemStack(Items.apple);
        ItemStack b = new ItemStack(Items.apple);
        assertTrue(a.isItemEqual(b));
        HashMap<ItemStack, String> list = new HashMap();
        list.put(a, "test");
        //If this ever returns true check if ItemStack has an equals method that works
        assertFalse(list.containsKey(b));
    }

    public void testListContains()
    {
        ItemStackWrapper a = new ItemStackWrapper(new ItemStack(Items.apple));
        ItemStackWrapper b = new ItemStackWrapper(new ItemStack(Items.apple));
        assertTrue("Wrappers failed to equal each other", a.equals(b));
        List<ItemStackWrapper> list = new ArrayList();
        list.add(a);
        assertTrue("List.contains(ItemStackWrapper) failed", list.contains(b));
    }

    public void testMapContains()
    {
        ItemStackWrapper a = new ItemStackWrapper(new ItemStack(Items.apple));
        ItemStackWrapper b = new ItemStackWrapper(new ItemStack(Items.apple));
        assertTrue("Wrappers failed to equal each other", a.equals(b));
        HashMap<ItemStackWrapper, String> list = new HashMap();
        list.put(a, "string");
        assertTrue("List.contains(ItemStackWrapper) failed", list.containsKey(b));


        a = new ItemStackWrapper(new ItemStack(Items.coal, 10));
        b = new ItemStackWrapper(new ItemStack(Items.coal));
        assertTrue("Wrappers failed to equal each other", a.equals(b));
        list = new HashMap();
        list.put(a, "string");
        assertTrue("List.contains(ItemStackWrapper) failed", list.containsKey(b));
    }

    //Check if Wrapper equals items
    public void testCompareItem()
    {
        Item item = Items.arrow;
        ItemStack itemStack = new ItemStack(Items.arrow);
        ItemStackWrapper wrapper = new ItemStackWrapper(itemStack);
        wrapper.meta_compare = false;
        wrapper.nbt_compare = false;
        assertEquals("MC compare check for items equal", item, itemStack.getItem());
        assertTrue("Compare failed for item equals", wrapper.equals(item));
    }

    //Check if Wrapper equals block
    public void testCompareBlock()
    {
        Block block = Blocks.diamond_block;
        ItemStack itemStack = new ItemStack(Blocks.diamond_block);
        ItemStackWrapper wrapper = new ItemStackWrapper(itemStack);
        wrapper.meta_compare = false;
        wrapper.nbt_compare = false;
        assertEquals("MC compare check for items equal", Item.getItemFromBlock(block), itemStack.getItem());
        assertTrue("Compare failed for block equals", wrapper.equals(block));
    }

    //Check if Wrapper equals stack
    public void testCompareItemStack()
    {
        ItemStack stack = new ItemStack(Blocks.diamond_block);
        ItemStack itemStack = new ItemStack(Blocks.diamond_block);
        ItemStackWrapper wrapper = new ItemStackWrapper(itemStack);
        assertTrue("MC compare check for items equal", stack.isItemEqual(itemStack));
        assertTrue("Compare failed for block equals", wrapper.equals(stack));
    }

    public void testCheckMetadataCompare()
    {
        ItemStackWrapper a = new ItemStackWrapper(new ItemStack(Items.coal, 1, 0));
        ItemStackWrapper b = new ItemStackWrapper(new ItemStack(Items.coal, 1, 1));
        assertFalse(a.equals(b));
        b = new ItemStackWrapper(new ItemStack(Items.coal, 1, 0));
        assertTrue(a.equals(b));
    }

    public void testCheckNBTCompare()
    {
        ItemStackWrapper a = new ItemStackWrapper(new ItemStack(Items.apple));
        a.nbt_compare = true;
        ItemStackWrapper b = new ItemStackWrapper(new ItemStack(Items.apple));
        b.nbt_compare = true;

        assertTrue("Failed Null equals Null NBT", a.equals(b));

        //Compare Not null to Null
        a.itemStack.setTagCompound(new NBTTagCompound());
        assertTrue("Failed Not Null equals Null NBT",a.equals(b));

        //Compare Empty NBT
        a.itemStack.setTagCompound(new NBTTagCompound());
        b.itemStack.setTagCompound(new NBTTagCompound());
        assertTrue("Failed Empty NBT", a.equals(b));

        //Compare Null to  Not Null
        a.itemStack.setTagCompound(null);
        assertTrue("Failed Null equals Not Null NBT", a.equals(b));

        //Compare same NBT with data
        a.itemStack.setTagCompound(new NBTTagCompound());
        a.itemStack.getTagCompound().setBoolean("a", true);
        b.itemStack.setTagCompound(new NBTTagCompound());
        b.itemStack.getTagCompound().setBoolean("a", true);
        assertTrue("Failed same NBT compare", a.equals(b));

        //Compare NBT with different data
        a.itemStack.setTagCompound(new NBTTagCompound());
        a.itemStack.getTagCompound().setBoolean("a", true);
        b.itemStack.setTagCompound(new NBTTagCompound());
        b.itemStack.getTagCompound().setBoolean("b", true);
        assertFalse("Failed same NBT compare", a.equals(b));
    }

    public void testCheckStackSizeCompare()
    {
        ItemStackWrapper a = new ItemStackWrapper(new ItemStack(Items.apple));
        a.stack_size = true;
        a.itemStack.stackSize = 10;
        ItemStackWrapper b = new ItemStackWrapper(new ItemStack(Items.apple));

        assertFalse(a.equals(b));
        assertTrue(b.equals(a));

        HashMap<ItemStackWrapper, Boolean> map = new HashMap();
        map.put(b, true);
        assertTrue(map.containsKey(a.setStackCompare(false)));
    }

    //Check if Wrapper equals stack
    public void testForNotEquals()
    {
        ItemStack itemStack = new ItemStack(Blocks.diamond_block);
        ItemStackWrapper wrapper = new ItemStackWrapper(itemStack);

        //Random blocks to compare to for the hell of it
        for(int i =0; i < 10; i++)
        {
            Block block = Block.getBlockById(MathUtility.rand.nextInt(255));
            if(block != null && Item.getItemFromBlock(block) != null && block != Blocks.diamond_block)
            {
                ItemStack stack = new ItemStack(block);

                assertFalse("Should be false as the blocks are not the same", stack.isItemEqual(itemStack));
                assertFalse("Should be false as the blocks are not the same", wrapper.equals(stack));
            }
        }
    }

    public void testHashCodeEquals()
    {
        ItemStackWrapper a = new ItemStackWrapper(new ItemStack(Items.apple));
        ItemStackWrapper b = new ItemStackWrapper(new ItemStack(Items.apple));
        assertTrue("Wrappers failed to equal each other", a.equals(b));
        assertTrue("Hash codes don't equal", a.hashCode() == b.hashCode());

        a = new ItemStackWrapper(new ItemStack(Items.coal));
        b = new ItemStackWrapper(new ItemStack(Items.coal, 10));
        assertTrue("Wrappers failed to equal each other", a.equals(b));
        assertTrue("Hash codes don't equal", a.hashCode() == b.hashCode());
    }

    public void testHashCodeNotEquals()
    {
        //Check for item compare
        ItemStackWrapper a = new ItemStackWrapper(new ItemStack(Items.apple));
        ItemStackWrapper b = new ItemStackWrapper(new ItemStack(Items.stone_axe));
        assertFalse("Hash codes should not equal", a.hashCode() == b.hashCode());

        a = new ItemStackWrapper(new ItemStack(Items.apple));
        b = new ItemStackWrapper(new ItemStack(Items.apple));
        assertTrue("Hash codes should equal", a.hashCode() == b.hashCode());

        //Check for stack compare when turned off, which is default
        a = new ItemStackWrapper(new ItemStack(Items.apple, 2));
        b = new ItemStackWrapper(new ItemStack(Items.apple, 1));
        assertTrue("Hash codes should equal as stack size is not compared", a.hashCode() == b.hashCode());

        //Check for stack compare when turned on
        a = new ItemStackWrapper(new ItemStack(Items.apple, 2)).setStackCompare(true);
        b = new ItemStackWrapper(new ItemStack(Items.apple, 1)).setStackCompare(true);
        assertFalse("Hash codes should not equal", a.hashCode() == b.hashCode());

        //Check for meta compare
        a = new ItemStackWrapper(new ItemStack(Items.apple, 1, 0)).setMetaCompare(true);
        b = new ItemStackWrapper(new ItemStack(Items.apple, 1, 1)).setMetaCompare(true);
        assertFalse("Hash codes should not equal", a.hashCode() == b.hashCode());
    }
}
