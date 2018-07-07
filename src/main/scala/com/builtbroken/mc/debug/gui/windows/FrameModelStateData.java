package com.builtbroken.mc.debug.gui.windows;

import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;

import javax.swing.*;
import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class FrameModelStateData extends JFrame
{
    public final ModelState modelState;

    private Label labelModelID;

    private TextField renderOffsetX;
    private TextField renderOffsetY;
    private TextField renderOffsetZ;

    private TextField renderScaleX;
    private TextField renderScaleY;
    private TextField renderScaleZ;

    private TextField renderRotationPointX;
    private TextField renderRotationPointY;
    private TextField renderRotationPointZ;

    private TextField renderRotationX;
    private TextField renderRotationY;
    private TextField renderRotationZ;

    private Button reloadButton;
    private Button applyButton;

    public boolean dataNeedsPulled = false;

    public FrameModelStateData(ModelState modelState)
    {
        this.modelState = modelState;
        setSize(new Dimension(600, 600));
        setResizable(false);
        setTitle("JSON ModelState debug window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //---------------------------------
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(0, 2));

        topPanel.add(new Label("ID:"));
        topPanel.add(new Label(modelState.id));

        add(topPanel, BorderLayout.NORTH);

        //---------------------------------
        JPanel centerDataPanel = new JPanel();
        centerDataPanel.setLayout(new GridLayout(0, 2));

        centerDataPanel.add(new Label("Model ID:"));
        centerDataPanel.add(labelModelID = new Label("---"));

        //Spacer
        centerDataPanel.add(new JPanel());
        centerDataPanel.add(new JPanel());

        //-------------------------------
        //Render Offset
        centerDataPanel.add(new Label("Offset:"));
        centerDataPanel.add(new JPanel());

        centerDataPanel.add(new Label("X:"));
        centerDataPanel.add(renderOffsetX = new TextField(10));

        centerDataPanel.add(new Label("Y:"));
        centerDataPanel.add(renderOffsetY = new TextField(10));

        centerDataPanel.add(new Label("Z:"));
        centerDataPanel.add(renderOffsetZ = new TextField(10));

        //Spacer
        centerDataPanel.add(new JPanel());
        centerDataPanel.add(new JPanel());

        //-------------------------------
        //Render Scale
        centerDataPanel.add(new Label("Scale:"));
        centerDataPanel.add(new JPanel());

        centerDataPanel.add(new Label("X:"));
        centerDataPanel.add(renderScaleX = new TextField(10));

        centerDataPanel.add(new Label("Y:"));
        centerDataPanel.add(renderScaleY = new TextField(10));

        centerDataPanel.add(new Label("Z:"));
        centerDataPanel.add(renderScaleZ = new TextField(10));

        //Spacer
        centerDataPanel.add(new JPanel());
        centerDataPanel.add(new JPanel());

        //-------------------------------
        //Render Scale
        centerDataPanel.add(new Label("Rotation Point:"));
        centerDataPanel.add(new JPanel());

        centerDataPanel.add(new Label("X:"));
        centerDataPanel.add(renderRotationPointX = new TextField(10));

        centerDataPanel.add(new Label("Y:"));
        centerDataPanel.add(renderRotationPointY = new TextField(10));

        centerDataPanel.add(new Label("Z:"));
        centerDataPanel.add(renderRotationPointZ = new TextField(10));

        //Spacer
        centerDataPanel.add(new JPanel());
        centerDataPanel.add(new JPanel());

        //-------------------------------
        //Render Scale
        centerDataPanel.add(new Label("Rotation:"));
        centerDataPanel.add(new JPanel());

        centerDataPanel.add(new Label("Yaw:"));
        centerDataPanel.add(renderRotationX = new TextField(10));

        centerDataPanel.add(new Label("Pitch:"));
        centerDataPanel.add(renderRotationY = new TextField(10));

        centerDataPanel.add(new Label("Roll:"));
        centerDataPanel.add(renderRotationZ = new TextField(10));

        //Spacer
        centerDataPanel.add(new JPanel());
        centerDataPanel.add(new JPanel());


        //-----------------------------
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(centerDataPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
        scrollPane.setMinimumSize(new Dimension(getWidth() - 100, getHeight() - 100));
        add(scrollPane, BorderLayout.CENTER);


        //-----------------------------
        JPanel buttonButton = new JPanel();
        buttonButton.setLayout(new GridLayout(0, 2));

        centerDataPanel.add(reloadButton = new Button("Reload"));
        centerDataPanel.add(applyButton = new Button("Apply"));

        reloadButton.addActionListener(e -> reloadData());
        applyButton.addActionListener(e -> {
            dataNeedsPulled = true;
            applyButton.setEnabled(false);
        });

        add(buttonButton, BorderLayout.SOUTH);


        //-----------------------------
        reloadData();
    }

    protected void reloadData()
    {
        labelModelID.setText(modelState.modelID);

        renderOffsetX.setText("" + (modelState.offset != null ? modelState.offset.x() : 0));
        renderOffsetY.setText("" + (modelState.offset != null ? modelState.offset.y() : 0));
        renderOffsetZ.setText("" + (modelState.offset != null ? modelState.offset.z() : 0));

        renderScaleX.setText("" + (modelState.scale != null ? modelState.scale.x() : 0));
        renderScaleY.setText("" + (modelState.scale != null ? modelState.scale.y() : 0));
        renderScaleZ.setText("" + (modelState.scale != null ? modelState.scale.z() : 0));

        renderRotationPointX.setText("" + (modelState.rotationPoint != null ? modelState.rotationPoint.x() : 0));
        renderRotationPointY.setText("" + (modelState.rotationPoint != null ? modelState.rotationPoint.y() : 0));
        renderRotationPointZ.setText("" + (modelState.rotationPoint != null ? modelState.rotationPoint.z() : 0));

        renderRotationX.setText("" + (modelState.rotation != null ? modelState.rotation.yaw() : 0));
        renderRotationY.setText("" + (modelState.rotation != null ? modelState.rotation.pitch() : 0));
        renderRotationZ.setText("" + (modelState.rotation != null ? modelState.rotation.roll() : 0));
    }

    /**
     * Called from model state object to apply data.
     * Do not call outside of render thread. As this
     * can break model from rendering.
     */
    public void applyData()
    {
        //Reset data
        dataNeedsPulled = false;

        //Apply data
        try
        {
            modelState.offset = new Pos(
                    Double.parseDouble(renderOffsetX.getText().trim()),
                    Double.parseDouble(renderOffsetY.getText().trim()),
                    Double.parseDouble(renderOffsetZ.getText().trim())
            );

            modelState.scale = new Pos(
                    Double.parseDouble(renderScaleX.getText().trim()),
                    Double.parseDouble(renderScaleY.getText().trim()),
                    Double.parseDouble(renderScaleZ.getText().trim())
            );

            modelState.rotationPoint = new Pos(
                    Double.parseDouble(renderRotationPointX.getText().trim()),
                    Double.parseDouble(renderRotationPointY.getText().trim()),
                    Double.parseDouble(renderRotationPointZ.getText().trim())
            );

            modelState.rotation = new EulerAngle(
                    Double.parseDouble(renderRotationX.getText().trim()),
                    Double.parseDouble(renderRotationY.getText().trim()),
                    Double.parseDouble(renderRotationZ.getText().trim())
            );
            modelState._cachedRotation = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error apply changes", JOptionPane.ERROR_MESSAGE);
        }

        //Enable button
        applyButton.setEnabled(true);
    }
}
