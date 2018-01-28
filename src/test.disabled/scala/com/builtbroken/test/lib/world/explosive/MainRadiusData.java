package com.builtbroken.test.lib.world.explosive;

import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;

import java.util.Scanner;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2016.
 */
public class MainRadiusData
{
    public static void main(String... args)
    {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a number: ");
        int radius = reader.nextInt();

        System.out.println("Radius: " + radius);
        for (int scale = 1; scale < 128; scale++)
        {
            double newRadius = ExplosiveRegistry.getExplosiveSize(radius, scale);
            System.out.println("\tScale[" + scale + "] = " + newRadius);
            if (scale % 5 == 0)
            {
                System.out.println();
            }
        }
        System.out.println();

        System.out.println("Press any key to exit... ");
        reader.next();
    }
}
