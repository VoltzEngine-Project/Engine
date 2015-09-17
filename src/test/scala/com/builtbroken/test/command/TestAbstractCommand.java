package com.builtbroken.test.command;

import com.builtbroken.mc.prefab.commands.AbstractCommand;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * JUnit test for {@link com.builtbroken.mc.prefab.commands.AbstractCommand}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/17/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestAbstractCommand extends AbstractTest
{
    @Test
    public void testInit()
    {
        AbstractCommand command = new AbstractCommand("command");
        assertTrue(command.getCommandName().equals("command"));
    }

    @Test
    public void testCombineArgs()
    {
        AbstractCommand command = new AbstractCommand("command");
        String[] args = new String[]{"Hi", "we", "love", "testing", ":p"};
        String combined = command.combine(args);
        assertTrue(combined.equals("Hi we love testing :p"));
        combined = command.combine(args, 0, 2);
        assertTrue(combined.equals("Hi we"));
        combined = command.combine(args, 2, 4);
        assertTrue(combined.equals("love testing"));
    }
}
