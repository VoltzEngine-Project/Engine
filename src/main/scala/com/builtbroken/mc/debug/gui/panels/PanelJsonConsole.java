package com.builtbroken.mc.debug.gui.panels;

import com.builtbroken.jlib.debug.IDebugPrintListener;
import com.builtbroken.mc.debug.component.DebugDataCellRenderer;
import com.builtbroken.mc.debug.data.DebugData;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class PanelJsonConsole extends JPanel
{
    JList dataLogList;

    List<DebugData> debugData = new ArrayList();

    DefaultListModel<DebugData> debugDataListModel = new DefaultListModel();

    public void reloadDebugData(String filter)
    {
        debugDataListModel.removeAllElements();
        for (DebugData data : debugData)
        {
            if (filter == null || filter.isEmpty() || data.msg.contains(filter)) //TODO add regex support
            {
                debugDataListModel.addElement(data);
            }
        }
    }

    public void addDebug(String msg, String... lines)
    {
        DebugData data = new DebugData();
        data.msg = msg;
        data.lines = lines;
        debugDataListModel.addElement(data);
        this.debugData.add(data);
    }

    public static class DebugListener implements IDebugPrintListener
    {
        PanelJsonConsole window;

        public DebugListener(PanelJsonConsole window)
        {
            this.window = window;
        }

        @Override
        public void onMessage(String msg, String prefix, String spacer, boolean error)
        {
            StringBuilder builder = new StringBuilder();
            if (error)
            {
                builder.append("[Error]");
            }
            else
            {
                builder.append("[Info]");
            }

            builder.append(spacer);
            builder.append(prefix);
            builder.append(msg);

            window.addDebug(builder.toString());
        }

        @Override
        public void onMessageWithError(String msg, String prefix, String spacer, Throwable e)
        {
            onMessage(msg, prefix, spacer, true);
            String error = toString(e);
            String[] array = error.split("\n");
            window.addDebug(e.toString(), Arrays.copyOfRange(array, 1, array.length));
        }

        public String toString(Throwable t)
        {
            StringWriter writer = new StringWriter();
            t.printStackTrace(new PrintWriter(writer));
            return writer.toString();
        }

    }

    public JPanel buildConsoleTab()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        //Init data for testing
        addDebug("======================================");
        addDebug("=========JSON debug list==============");
        addDebug("======================================");


        //Create list
        dataLogList = new JList(debugDataListModel);
        dataLogList.setLayoutOrientation(JList.VERTICAL);
        dataLogList.setCellRenderer(new DebugDataCellRenderer());


        //Create scroll panel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(dataLogList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
        scrollPane.setMinimumSize(new Dimension(getWidth() - 100, getHeight() - 100));
        panel.add(scrollPane, BorderLayout.CENTER);

        //Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));
        Button button = new Button("Reload");
        button.addActionListener(e -> reloadDebugData(null));
        menuPanel.add(button);

        JTextField searchBox = new JTextField();
        searchBox.setMinimumSize(new Dimension(200, -1));
        searchBox.setPreferredSize(new Dimension(200, 30));
        searchBox.setToolTipText("Search filter");
        menuPanel.add(searchBox);

        button = new Button("Search");
        button.addActionListener(e -> reloadDebugData(searchBox.getText().trim()));
        menuPanel.add(button);


        panel.add(menuPanel, BorderLayout.NORTH);

        return panel;
    }
}
