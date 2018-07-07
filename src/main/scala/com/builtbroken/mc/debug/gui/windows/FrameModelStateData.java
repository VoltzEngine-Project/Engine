package com.builtbroken.mc.debug.gui.windows;

import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class FrameModelStateData extends JFrame implements ActionListener
{
    public static final String COMMAND_RELOAD = "reload";
    public static final String COMMAND_APPLY = "apply";


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

    public FrameModelStateData(ModelState modelState)
    {
        this.modelState = modelState;
        setSize(new Dimension(400, 400));
        setResizable(false);
        setTitle("JSON ModelState debug window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        add(new Label("Model ID:"));
        add(labelModelID = new Label("---"));

        //Spacer
        add(new JPanel());
        add(new JPanel());

        //-------------------------------
        //Render Offset
        add(new Label("Offset:"));
        add(new JPanel());

        add(new Label("X:"));
        add(renderOffsetX = new TextField(10));

        add(new Label("Y:"));
        add(renderOffsetY = new TextField(10));

        add(new Label("Z:"));
        add(renderOffsetZ = new TextField(10));

        //Spacer
        add(new JPanel());
        add(new JPanel());

        //-------------------------------
        //Render Scale
        add(new Label("Scale:"));
        add(new JPanel());

        add(new Label("X:"));
        add(renderScaleX = new TextField(10));

        add(new Label("Y:"));
        add(renderScaleY = new TextField(10));

        add(new Label("Z:"));
        add(renderScaleZ = new TextField(10));

        //Spacer
        add(new JPanel());
        add(new JPanel());

        //-------------------------------
        //Render Scale
        add(new Label("Rotation Point:"));
        add(new JPanel());

        add(new Label("X:"));
        add(renderRotationPointX = new TextField(10));

        add(new Label("Y:"));
        add(renderRotationPointY = new TextField(10));

        add(new Label("Z:"));
        add(renderRotationPointZ = new TextField(10));

        //Spacer
        add(new JPanel());
        add(new JPanel());

        //-------------------------------
        //Render Scale
        add(new Label("Rotation:"));
        add(new JPanel());

        add(new Label("Yaw:"));
        add(renderRotationX = new TextField(10));

        add(new Label("Pitch:"));
        add(renderRotationY = new TextField(10));

        add(new Label("Roll:"));
        add(renderRotationZ = new TextField(10));

        //Spacer
        add(new JPanel());
        add(new JPanel());

        add(reloadButton = new Button("Reload"));
        add(applyButton = new Button("Apply"));

        reloadButton.setActionCommand(COMMAND_RELOAD);
        reloadButton.addActionListener(this);

        applyButton.setActionCommand(COMMAND_APPLY);
        applyButton.addActionListener(this);

        reloadData();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equalsIgnoreCase(COMMAND_RELOAD))
        {
            reloadData();
        }
        else if (e.getActionCommand().equalsIgnoreCase(COMMAND_APPLY))
        {
            applyData();
        }
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

    protected void applyData()
    {
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
    }
}
