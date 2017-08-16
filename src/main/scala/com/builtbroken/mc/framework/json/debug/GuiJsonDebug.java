package com.builtbroken.mc.framework.json.debug;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.jlib.debug.IDebugPrintListener;
import com.builtbroken.mc.core.Engine;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    List<DebugData> data = new ArrayList();
    DefaultListModel<DebugData> model = new DefaultListModel();

    public void init()
    {
        //Setup this
        setLayout(new BorderLayout());
        setSize(new Dimension(1000, 800));
        setResizable(false);
        setTitle("JSON debug window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        //Init data for testing
        addData("======================================");
        addData("=========JSON debug list==============");
        addData("======================================");


        //Create list
        dataLogList = new JList(model);
        dataLogList.setLayoutOrientation(JList.VERTICAL);
        dataLogList.setCellRenderer(new CellRenderer());
        dataLogList.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
        dataLogList.setMinimumSize(new Dimension(getWidth() - 100, getHeight() - 100));

        //Create scroll panel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(dataLogList);
        add(scrollPane, BorderLayout.CENTER);

        //Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));
        menuPanel.add(new Button("ONE"));
        menuPanel.add(new Button("TWO"));
        menuPanel.add(new Button("THREE"));
        add(menuPanel, BorderLayout.NORTH);
    }

    public void addData(String msg, String... lines)
    {
        DebugData data = new DebugData();
        data.msg = msg;
        data.lines = lines;
        model.addElement(data);
        this.data.add(data);
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

            window.addData(builder.toString(), null);
        }

        @Override
        public void onMessageWithError(String msg, String prefix, String spacer, Throwable e)
        {
            onMessage(msg, prefix, spacer, true);
            String error = toString(e);
            String[] array = error.split("\n");
            window.addData(e.toString(), Arrays.copyOfRange(array, 1, array.length));
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

    public static class DebugData
    {
        String msg;
        String[] lines;
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
