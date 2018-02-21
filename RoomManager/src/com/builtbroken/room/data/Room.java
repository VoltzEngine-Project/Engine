package com.builtbroken.room.data;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/21/2018.
 */
public class Room
{
    public String name;
    public boolean isTaken;

    public Room(String name, boolean isTaken)
    {
        this.name = name;
        this.isTaken = isTaken;
    }
}
