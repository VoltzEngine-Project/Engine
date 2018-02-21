package com.builtbroken.room.gui;

import com.builtbroken.room.data.Room;
import com.builtbroken.room.logic.Logic;

import javax.swing.*;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/21/2018.
 */
public class UserInteraction
{
    Logic logic;

    public UserInteraction(Logic logic)
    {
        this.logic = logic;
    }

    public boolean run()
    {
        System.out.println("Running interface");
        int option = JOptionPane.showConfirmDialog(null, "Get empty rooms? Cancel to exit..", "Empty Room Check", JOptionPane.YES_NO_CANCEL_OPTION);
        if(option == 0)
        {
            List<Room> rooms = logic.getEmptyRooms();
            String display = "Empty Rooms: \n";
            if(!rooms.isEmpty())
            {
                for (Room room : rooms)
                {
                    display += "   " + room.name + "\n";
                }
            }
            else
            {
                display = "No empty rooms exist";
            }
            JOptionPane.showConfirmDialog(null, display, "Empty Room Check", JOptionPane.DEFAULT_OPTION );
        }
        return option != 2;
    }
}
