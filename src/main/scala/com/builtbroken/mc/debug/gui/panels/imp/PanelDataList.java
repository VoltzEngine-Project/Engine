package com.builtbroken.mc.debug.gui.panels.imp;

import com.builtbroken.mc.debug.component.DebugDataCellRenderer;
import com.builtbroken.mc.debug.data.IJsonDebugData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public abstract class PanelDataList<D extends Object> extends JPanel
{
    protected final DefaultListModel<IJsonDebugData> dataModel = new DefaultListModel();
    protected final List<D> data = new ArrayList();

    public PanelDataList()
    {
        setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));

        //Reload button
        Button button = new Button("Reload");
        button.addActionListener(e -> {
            dataModel.clear();
            reload(null);
        });
        menuPanel.add(button);

        //Search box
        JTextField searchBox = new JTextField();
        searchBox.setMinimumSize(new Dimension(200, -1));
        searchBox.setPreferredSize(new Dimension(200, 30));
        searchBox.setToolTipText("Search filter for current open tab");
        menuPanel.add(searchBox);

        //Search button
        button = new Button("Search");
        button.addActionListener(e -> {
            dataModel.clear();
            reload(searchBox.getText().trim());
        });
        menuPanel.add(button);

        add(menuPanel, BorderLayout.NORTH);

        //Create list
        JList<IJsonDebugData> dataLogList = new JList(dataModel);
        dataLogList.setLayoutOrientation(JList.VERTICAL);
        dataLogList.setCellRenderer(new DebugDataCellRenderer());

        //Click handler
        dataLogList.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent me)
            {
                JList table = (JList) me.getSource();
                Point p = me.getPoint();
                int row = table.locationToIndex(p);
                if (row != -1 && me.getClickCount() == 2)
                {
                    IJsonDebugData data = dataLogList.getModel().getElementAt(row);
                    if (data != null)
                    {
                        data.onDoubleClicked();
                    }
                }
            }
        });

        //Create scroll panel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(dataLogList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
        scrollPane.setMinimumSize(new Dimension(getWidth() - 100, getHeight() - 100));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Called to reload data being displayed
     *
     * @param filter - filter being used
     */
    protected void reload(String filter)
    {
        buildData();
        for (D object : data)
        {
            if (object != null)
            {
                if (shouldDisplay(object, filter))
                {
                    IJsonDebugData debugData = getDataEntryFor(object);
                    if (debugData != null)
                    {
                        dataModel.addElement(debugData);
                    }
                }
            }
        }
    }

    /**
     * Called to check if the object should be displayed given the filter
     *
     * @param object - object to display
     * @param filter - filter being used
     * @return true if should display
     */
    protected boolean shouldDisplay(D object, String filter)
    {
        return filter == null || filter.isEmpty() || object.toString().contains(filter);
    }

    /**
     * Converts an object to display data
     *
     * @param object
     * @return
     */
    protected abstract IJsonDebugData getDataEntryFor(D object);

    /**
     * Called to build display data
     */
    protected abstract void buildData();
}
