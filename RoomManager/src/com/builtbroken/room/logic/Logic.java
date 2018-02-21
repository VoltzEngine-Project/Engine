package com.builtbroken.room.logic;

import com.builtbroken.room.data.DataHandler;
import com.builtbroken.room.data.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/21/2018.
 */
public class Logic
{
    public DataHandler dataHandler;

    public Logic(DataHandler dataHandler)
    {
        this.dataHandler = dataHandler;
    }

    public List<Room> getEmptyRooms()
    {
        List<Room> rooms = new ArrayList();

        //Get all rooms
        for(Room room : dataHandler.rooms.values())
        {
            //Room is not taken
            if(!room.isTaken)
            {
                rooms.add(room);
            }
        }
        return rooms;
    }
}
