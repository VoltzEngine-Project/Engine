package com.builtbroken.test.access;

import com.builtbroken.mc.framework.access.AccessGroup;
import com.builtbroken.mc.framework.access.AccessProfile;
import com.builtbroken.mc.framework.access.AccessUser;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/14/2018.
 */
@RunWith(VoltzTestRunner.class)
public class TestAccessProfile extends AbstractTest
{
    /** MC server instance for the entire class file to use */
    protected MinecraftServer server;
    /** World server to build tests inside, make sure to clean up as its used over all tests in this class */
    protected FakeWorldServer world;
    /** Tester that has not choice in what to test, test between tests but not deleted. Make sure to cleanup non-vanilla data between tests */
    protected TestPlayer player;

    @Override
    public void setUpForEntireClass()
    {
        super.setUpForEntireClass();
        server = new FakeDedicatedServer(new File(FakeWorldServer.baseFolder, "TestAbstractLocation"));
        world = FakeWorldServer.newWorld(server, "TestAbstractLocation");
        player = new TestPlayer(server, world, new GameProfile(null, "TileTester"));
    }

    @Test
    public void testContainsUsername()
    {
        AccessProfile profile = new AccessProfile();
        AccessGroup group = new AccessGroup("g1", new AccessUser("name"), new AccessUser("name2"), new AccessUser("name3"));

        assertFalse(profile.containsUser("name"));
        assertFalse(profile.containsUser("name2"));
        assertFalse(profile.containsUser("name3"));

        profile.addGroup(group);
        assertEquals(group, profile.getGroup("g1"));

        assertTrue(profile.containsUser("name"));
        assertTrue(profile.containsUser("name2"));
        assertTrue(profile.containsUser("name3"));
    }

    @Test
    public void testContainsPlayer()
    {
        AccessProfile profile = new AccessProfile();
        profile.addGroup(new AccessGroup("g1", new AccessUser(player)));

        assertTrue(profile.containsUser(player));
    }
}
