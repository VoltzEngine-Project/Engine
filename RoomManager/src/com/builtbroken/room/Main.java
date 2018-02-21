package com.builtbroken.room;

import com.builtbroken.room.data.DataHandler;
import com.builtbroken.room.gui.UserInteraction;
import com.builtbroken.room.logic.Logic;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/21/2018.
 */
public class Main
{
    public static DataHandler data;
    public static Logic logic;
    public static UserInteraction userInteraction;

    //Main
    public static void main(String... args)
    {
        System.out.println("Start");
        loadData();
        initLogic();
        initInterface();
        runLoop();
        System.out.println("Stop");
    }

    public static void loadData()
    {
        data = new DataHandler();
        data.loadData();
        System.out.println("Loaded data");
    }

    public static void initLogic()
    {
        logic = new Logic(data);
        System.out.println("Loaded Logic");
    }


    public static void initInterface()
    {
        userInteraction = new UserInteraction(logic);
        System.out.println("Load interface");
    }

    public static void runLoop()
    {
        boolean run = true;
        while(run)
        {
            run = userInteraction.run();
        }
    }
}
