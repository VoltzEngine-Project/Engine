package com.builtbroken.mc.framework.json.debug.component;

import com.builtbroken.mc.framework.json.debug.data.IJsonDebugData;

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