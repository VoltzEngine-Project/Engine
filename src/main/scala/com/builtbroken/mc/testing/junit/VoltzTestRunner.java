package com.builtbroken.mc.testing.junit;

import com.builtbroken.mc.lib.helper.ReflectionUtility;
import com.google.common.io.Files;
import javafx.scene.effect.Reflection;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import sun.security.krb5.internal.crypto.Des;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

public class VoltzTestRunner extends Runner
{
    protected Description desc;
    protected Class<? extends AbstractTest> clazz;

    public VoltzTestRunner(Class<? extends AbstractTest> clazz) throws InitializationError, ClassNotFoundException
    {
        desc = Description.createTestDescription(clazz, "VoltzTest");
        this.clazz = clazz;
    }

    @Override
    public Description getDescription()
    {
        return desc;
    }

    @Override
    public void run(RunNotifier notifier)
    {
        try
        {
            // Setup data
            Object[] data = new Object[]{"", "", "", "", "1.7.10", "", Files.createTempDir(), Collections.EMPTY_LIST};
            URL[] urLs = ((URLClassLoader) Launch.class.getClassLoader()).getURLs();
            LaunchClassLoader loader = new LaunchClassLoader(urLs);

            Class<?> itemz = loader.loadClass("cpw.mods.fml.relauncher.FMLRelaunchLog");
            Field mz = itemz.getDeclaredField("side");
            mz.setAccessible(true);

            mz.set(itemz, Enum.valueOf((Class<Enum>) mz.getType(), "CLIENT"));

            Class<?> item1 = loader.loadClass("cpw.mods.fml.common.Loader");
            Method m1 = item1.getMethod("injectData", Object[].class);
            m1.invoke(null, new Object[]{data});

            Class<?> test_class = loader.loadClass(clazz.getName());
            Object test = test_class.newInstance();

            Method setUpClass = ReflectionUtility.getMethod(test_class, "setUpForEntireClass");
            Method setUp = ReflectionUtility.getMethod(test_class, "setUpForTest", String.class);
            Method tearDownClass = ReflectionUtility.getMethod(test_class, "tearDownForEntireClass");
            Method tearDown = ReflectionUtility.getMethod(test_class, "tearDownForTest", String.class);

            //Setup data for the entire test class, making it a test suit by default

            setUpClass.invoke(test);

            for(Method method: test_class.getMethods())
            {
                String name = method.getName();
                Annotation an = method.getAnnotation(Test.class);
                if(an != null || name.startsWith("test"))
                {
                    Description description = Description.createTestDescription(test_class, method.getName());
                    notifier.fireTestStarted(description);
                    try
                    {
                        setUp.invoke(test, name);
                        method.invoke(test);
                        tearDown.invoke(test, name);
                    }
                    catch(Exception e)
                    {
                        Throwable cause = e;
                        if(e instanceof InvocationTargetException)
                        {
                            cause = e.getCause();
                        }
                        Failure failure = new Failure(description, cause);
                        notifier.fireTestFailure(failure);
                    }
                    notifier.fireTestFinished(description);
                }
            }

            //Clean up data for the entire test class
            tearDownClass.invoke(test);

        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}