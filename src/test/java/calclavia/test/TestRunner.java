package calclavia.test;

import calclavia.test.utility.TestLanguageUtility;
import calclavia.test.utility.TestMathUtility;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Unit testing runs through here
 *
 * This is just a placeholder, and an Example. Having code unit testable is Very beneficial to us
 * in the long run. using this new test suite i will implement will lead to Easy code hunting, Amazingly better Stability
 * Catching bugs, crashes, failures easily.
 *
 * A Common agreement about Unit testing is have almost every method unit tested. this is ofcourse not a option with our mods
 * Both because the codebase is just too big already, and also Minecraft is very hard to undergo unit testing, as it is not unit testable itself
 * This is marked for 1.7 to Unit test a lot more of our code. Including all initialization levels, commonly called methods
 * And with using Maxwolf's headless Server simulator, We can even test ingame some stuff.
 *
 * @since 14/03/14
 * @author tgame14
 */
public class TestRunner
{
    public static void main(String[] args)
    {
        Result result = JUnitCore.runClasses(TestMathUtility.class, TestLanguageUtility.class);
        System.err.println("Test Runner has had " + result.getFailureCount() + " fails out of " + result.getRunCount() + " Within " + result.getRunTime());
        System.err.println("All Failures Caught");
        for (Failure fail : result.getFailures())
        {
            System.err.println(fail);
        }
    }
}
