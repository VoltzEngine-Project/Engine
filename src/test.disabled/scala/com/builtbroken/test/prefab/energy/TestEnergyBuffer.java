package com.builtbroken.test.prefab.energy;

import com.builtbroken.mc.framework.energy.data.EnergyBuffer;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * Created by Dark on 8/15/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestEnergyBuffer extends AbstractTest
{
    public void testFill()
    {
        EnergyBuffer buffer = new EnergyBuffer(100);
        Assert.assertTrue("Buffer should be empty", buffer.getEnergyStored() == 0);
        Assert.assertTrue("Buffer should have a max of 100", buffer.getMaxBufferSize() == 100);

        int filled = buffer.addEnergyToStorage(10, false);
        Assert.assertTrue("Buffer should have returned 10", filled == 10);
        Assert.assertTrue("Buffer should still be empty as we faked energy transfer", buffer.getEnergyStored() == 0);

        filled = buffer.addEnergyToStorage(10, true);
        Assert.assertTrue("Buffer should have returned 10", filled == 10);
        Assert.assertTrue("Buffer should have 10 energy units stored", buffer.getEnergyStored() == 10);

        filled = buffer.addEnergyToStorage(-10, true);
        Assert.assertTrue("Buffer should have returned 0", filled == 0);
        Assert.assertTrue("Buffer should have 10 energy units stored", buffer.getEnergyStored() == 10);
    }

    public void testMaxFill()
    {
        EnergyBuffer buffer = new EnergyBuffer(100);

        int filled = buffer.addEnergyToStorage(200, false);
        Assert.assertTrue("Buffer should have returned 100", filled == 100);
        Assert.assertTrue("Buffer should still be empty as we faked energy transfer", buffer.getEnergyStored() == 0);

        filled = buffer.addEnergyToStorage(200, true);
        Assert.assertTrue("Buffer should have returned 100", filled == 100);
        Assert.assertTrue("Buffer should have 10 energy units stored", buffer.getEnergyStored() == 100);
    }

    public void testDrain()
    {
        EnergyBuffer buffer = new EnergyBuffer(100);
        buffer.addEnergyToStorage(100, true);

        int drained = buffer.removeEnergyFromStorage(10, false);
        Assert.assertTrue("Buffer should have returned 10", drained == 10);
        Assert.assertTrue("Buffer should still be full as we faked energy transfer", buffer.getEnergyStored() == 100);

        drained = buffer.removeEnergyFromStorage(10, true);
        Assert.assertTrue("Buffer should have returned 10", drained == 10);
        Assert.assertTrue("Buffer should have 90 energy units stored", buffer.getEnergyStored() == 90);

        drained = buffer.addEnergyToStorage(-10, true);
        Assert.assertTrue("Buffer should have returned 0", drained == 0);
        Assert.assertTrue("Buffer should have 10 energy units stored", buffer.getEnergyStored() == 90);
    }

    public void testMaxDrain()
    {
        EnergyBuffer buffer = new EnergyBuffer(100);
        buffer.addEnergyToStorage(100, true);

        int drained = buffer.removeEnergyFromStorage(200, false);
        Assert.assertTrue("Buffer should have returned 10", drained == 100);
        Assert.assertTrue("Buffer should still be full as we faked energy transfer", buffer.getEnergyStored() == 100);

        drained = buffer.removeEnergyFromStorage(200, true);
        Assert.assertTrue("Buffer should have returned 10", drained == 100);
        Assert.assertTrue("Buffer should have be empty", buffer.getEnergyStored() == 0);
    }
}
