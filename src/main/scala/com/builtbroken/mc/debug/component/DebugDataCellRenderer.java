package com.builtbroken.mc.debug.component;

import com.builtbroken.mc.debug.data.IJsonDebugData;

import javax.swing.*;
import java.awt.*;

public class DebugDataCellRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList list, Object o, int index, boolean isSelected, boolean cellHasFocus)
    {
        IJsonDebugData data = (IJsonDebugData) o;
        return super.getListCellRendererComponent(list, data.buildDebugLineDisplay(), index, isSelected, cellHasFocus);
    }
}