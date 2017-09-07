package com.builtbroken.mc.framework.json.debug.gui.json;

import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.debug.IJsonDebugDisplay;
import com.builtbroken.mc.framework.json.debug.component.DebugDataCellRenderer;
import com.builtbroken.mc.framework.json.debug.data.DebugData;
import com.builtbroken.mc.framework.json.debug.data.DebugJsonData;
import com.builtbroken.mc.framework.json.debug.data.IJsonDebugData;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class JsonDataPanel extends JPanel
{
    JTabbedPane jsonDataPanel;

    HashMap<String, JPanel> processorToPanel = new HashMap();
    HashMap<String, DefaultListModel<IJsonDebugData>> processorToDataModel = new HashMap();

    public JsonDataPanel()
    {
        setLayout(new BorderLayout());

        jsonDataPanel = new JTabbedPane();

        JPanel panel1 = new JPanel();

        //Init load button, removed when data is loaded
        Button initButton = new Button("Load");
        initButton.addActionListener(e -> reloadJsonData());
        panel1.add(initButton);
        jsonDataPanel.addTab("Init", null, panel1, "The only panel until you hit load");
        add(jsonDataPanel, BorderLayout.CENTER);

        //Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));

        //Reload button
        Button button = new Button("Rebuild JSON Data");
        button.addActionListener(e -> reloadJsonData());
        menuPanel.add(button);

        add(menuPanel, BorderLayout.NORTH);
    }


    public void reloadJsonData()
    {
        jsonDataPanel.removeAll();
        for (Map.Entry<String, java.util.List<IJsonGenObject>> entry : JsonContentLoader.INSTANCE.generatedObjects.entrySet())
        {
            createJsonDataTab(entry.getKey(), entry.getValue());
        }
    }

    protected void createJsonDataTab(String key, List<IJsonGenObject> objects)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));

        //Reload button
        Button button = new Button("Reload");
        button.addActionListener(e -> {
            DefaultListModel<IJsonDebugData> model = processorToDataModel.get(key);
            if (model != null)
            {
                model.clear();
                buildDataForProcessor(model, objects, null);
            }
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
            DefaultListModel<IJsonDebugData> model = processorToDataModel.get(key);
            if (model != null)
            {
                model.clear();
                buildDataForProcessor(model, objects, searchBox.getText().trim());
            }
        });
        menuPanel.add(button);

        panel.add(menuPanel, BorderLayout.NORTH);

        //Generate data
        DefaultListModel<IJsonDebugData> model = new DefaultListModel();
        buildDataForProcessor(model, objects, null);

        //Create list
        JList<IJsonDebugData> dataLogList = new JList(model);
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
        
        panel.add(scrollPane, BorderLayout.CENTER);

        //Create tab
        jsonDataPanel.addTab(key, null, panel); //TODO add icon
        processorToPanel.put(key, panel);
        processorToDataModel.put(key, model);
    }

    protected void buildDataForProcessor(DefaultListModel<IJsonDebugData> model, List<IJsonGenObject> objects, String filter)
    {
        for (IJsonGenObject object : objects)
        {
            if (filter == null || filter.isEmpty() || object.getContentID().contains(filter))
            {
                model.addElement(getDataEntryFor(object));
            }
        }
    }

    protected IJsonDebugData getDataEntryFor(IJsonGenObject object)
    {
        if (object instanceof IJsonDebugDisplay)
        {
            return new DebugJsonData((IJsonDebugDisplay) object);
        }
        return new DebugData("MOD: " + object.getMod() + "  ID: " + object.getContentID());
    }
}
