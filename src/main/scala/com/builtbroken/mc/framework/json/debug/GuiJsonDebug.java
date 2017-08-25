package com.builtbroken.mc.framework.json.debug;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.jlib.debug.IDebugPrintListener;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/16/2017.
 */
public class GuiJsonDebug extends JFrame
{
    JList dataLogList;

    List<DebugData> debugData = new ArrayList();

    DefaultListModel<DebugData> debugDataListModel = new DefaultListModel();

    JTabbedPane jsonDataPanel;

    public void init()
    {
        //Setup this
        setSize(new Dimension(1000, 800));
        setResizable(false);
        setTitle("JSON debug window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("images/middle.gif");

        JComponent panel1 = buildConsoleTab();
        tabbedPane.addTab("Debug", icon, panel1,
                "Shows debug output console");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = buildDataPanel();
        tabbedPane.addTab("Data", icon, panel2,
                "Shows list of loaded JSON data");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        add(tabbedPane, BorderLayout.CENTER);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path)
    {
        URL imgURL = GuiJsonDebug.class.getResource(path);
        if (imgURL != null)
        {
            return new ImageIcon(imgURL);
        }
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
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
        dataLogList.setCellRenderer(new CellRenderer());


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
        button.addActionListener(e -> {
            reloadDebugData();
        });
        menuPanel.add(button);
        menuPanel.add(new Button("TWO"));
        menuPanel.add(new Button("THREE"));
        panel.add(menuPanel, BorderLayout.NORTH);

        return panel;
    }

    public JPanel buildDataPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //Create list
        dataLogList = new JList(debugDataListModel);
        dataLogList.setLayoutOrientation(JList.VERTICAL);
        dataLogList.setCellRenderer(new CellRenderer());


        jsonDataPanel = new JTabbedPane();

        JPanel panel1 = new JPanel();
        Button initButton = new Button("Load");
        initButton.addActionListener(e -> reloadJsonData());
        panel1.add(initButton);
        jsonDataPanel.addTab("Init", null, panel1, "The only panel until you hit load");

        panel.add(jsonDataPanel, BorderLayout.CENTER);

        //Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));
        Button button = new Button("Reload Data");
        button.addActionListener(e -> reloadJsonData());
        menuPanel.add(button);
        menuPanel.add(new Button("..."));
        menuPanel.add(new Button("..."));
        panel.add(menuPanel, BorderLayout.NORTH);

        return  panel;
    }

    public void reloadDebugData()
    {
        debugDataListModel.removeAllElements();
        for (DebugData data : debugData)
        {
            debugDataListModel.addElement(data);
        }
    }

    public void reloadJsonData()
    {
        jsonDataPanel.removeAll();
        for(Map.Entry<String, List<IJsonGenObject>> entry : JsonContentLoader.INSTANCE.generatedObjects.entrySet())
        {
            createJsonDataTab(entry.getKey(), entry.getValue());
        }
    }

    protected void createJsonDataTab(String key, List<IJsonGenObject> objects)
    {
        JPanel panel = new JPanel();

        //Generate data
        DefaultListModel<DebugData> model = new DefaultListModel();

        for(IJsonGenObject object : objects)
        {
            model.addElement(new DebugData("MOD: " + object.getMod() + "  ID: " + object.getContentID()));
        }

        //Create list
        JList<DebugData> dataLogList = new JList(model);
        dataLogList.setLayoutOrientation(JList.VERTICAL);
        dataLogList.setCellRenderer(new CellRenderer());

        //Create scroll panel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(dataLogList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
        scrollPane.setMinimumSize(new Dimension(getWidth() - 100, getHeight() - 100));
        panel.add(scrollPane, BorderLayout.CENTER);

        //Create tab
        jsonDataPanel.addTab(key, null, panel); //TODO add icon
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
        GuiJsonDebug window;

        public DebugListener(GuiJsonDebug window)
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

    protected class CellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object o, int index, boolean isSelected, boolean cellHasFocus)
        {
            DebugData data = (DebugData) o;
            if (data.lines != null && data.lines.length > 0)
            {
                String text = "<html>";
                text += "<h3>" + data.msg + "</h3>";
                for (String s : data.lines)
                {
                    text += "<p>" + s.replace("\t", "    ") + "</p>";
                }
                text += "</html>";
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
            return super.getListCellRendererComponent(list, data.msg.replace("\t", "    "), index, isSelected, cellHasFocus);
        }
    }

    //Used to test GUI
    public static void main(String... args) throws InterruptedException
    {
        GuiJsonDebug window = new GuiJsonDebug();
        window.init();
        window.setVisible(true);

        DebugPrinter printer = new DebugPrinter(Engine.logger());
        printer.add(new DebugListener(window));

        printer.log("Test 1");
        printer.log("Test 2");
        printer.log("Test 3");

        printer.start("Starting section 1");
        printer.start("Starting section 2");
        printer.start("Starting section 3");
        printer.log("Test 1");
        printer.log("Test 2");
        printer.log("Test 3");
        printer.log("Test 1");
        printer.log("Test 2");
        printer.log("Test 3");
        printer.log("Test 1");
        printer.log("Test 2");
        printer.log("Test 3");
        printer.end("Done...");
        printer.end("Done...");
        printer.end("Done...");

        printer.error("Some error");
        try
        {
            simErrorChain();
        }
        catch (Exception e)
        {
            printer.error("Some error 2", e);
        }

        for (int i = 0; i < 1000; i++)
        {
            printer.log("--i: " + i);
        }

        while (window.isVisible())
        {
            Thread.sleep(1000);
        }
        System.out.println("Done");
    }

    protected static void simErrorChain()
    {
        methodA();
    }

    private static void methodA()
    {
        methodB();
    }

    private static void methodB()
    {
        methodC();
    }

    private static void methodC()
    {
        methodD();
    }

    private static void methodD()
    {
        throw new RuntimeException();
    }
}
