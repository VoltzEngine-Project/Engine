package com.builtbroken.room.data;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/21/2018.
 */
public class DataHandler
{
    public HashMap<Integer, Room> rooms;

    public DataHandler()
    {
        rooms = new HashMap();
    }

    public void loadData()
    {
        rooms.put(0, new Room("Funky Room", true));
        rooms.put(1, new Room("Party Room", true));
        rooms.put(2, new Room("Jazz Room", true));
        rooms.put(3, new Room("Norm Room", true));
    }
}
