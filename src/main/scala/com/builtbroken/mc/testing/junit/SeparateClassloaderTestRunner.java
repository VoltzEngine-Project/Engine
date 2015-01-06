package com.builtbroken.mc.testing.junit;

import com.builtbroken.mc.lib.helper.ReflectionUtility;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import javafx.scene.effect.Reflection;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class SeparateClassloaderTestRunner extends BlockJUnit4ClassRunner
{
    public SeparateClassloaderTestRunner(Class<?> clazz) throws InitializationError
    {
        super(getFromTestClassloader(clazz));
    }

    private static Class<?> getFromTestClassloader(Class<?> clazz) throws InitializationError {
        try {
            return Class.forName(clazz.getName(), true, new TestClassLoader());
        }
        catch (Exception e) {
            throw new InitializationError(e);
        }
    }

    public static class TestClassLoader extends LaunchClassLoader {
        public TestClassLoader() {
            super(((URLClassLoader)getSystemClassLoader()).getURLs());
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (name.startsWith("net.minecraft")) {
                return super.findClass(name);
            }
            return super.loadClass(name);
        }
    }
}