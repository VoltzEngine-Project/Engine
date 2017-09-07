package com.builtbroken.mc.framework.json.debug.gui;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.jlib.debug.IDebugPrintListener;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.debug.component.DebugDataCellRenderer;
import com.builtbroken.mc.framework.json.debug.data.DebugData;
import com.builtbroken.mc.framework.json.debug.gui.json.JsonDataPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/16/2017.
 */
public class GuiJsonDebug extends JFrame
{
    JList dataLogList;

    List<DebugData> debugData = new ArrayList();

    DefaultListModel<DebugData> debugDataListModel = new DefaultListModel();


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

        JComponent panel2 = new JsonDataPanel();
        tabbedPane.addTab("Data", icon, panel2,
                "Shows list of loaded JSON data");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        add(tabbedPane, BorderLayout.CENTER);

        pack();
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
