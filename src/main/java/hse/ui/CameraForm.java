package hse.ui;

import hse.controllers.ChangeController;
import hse.controllers.change.Change;
import hse.controllers.change.ChangeType;
import hse.controllers.change.Direction;
import hse.objects.Camera;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuriy on 27.02.17.
 */
public class CameraForm extends JFrame {

    ChangeController controller;


    Camera camera = Camera.getInstance();

    JSpinner eyeX = new JSpinner();
    JSpinner eyeY = new JSpinner();
    JSpinner eyeZ = new JSpinner();

    JSpinner centerX = new JSpinner();
    JSpinner centerY = new JSpinner();
    JSpinner centerZ = new JSpinner();


    public CameraForm(ChangeController controller) {
        this.controller = controller;

        initSpinner();

        Container contentPane = this.getContentPane();
        this.setMinimumSize(new Dimension(400,400));
        contentPane.setLayout(new FlowLayout());
        contentPane.add(eyeX);
        contentPane.add(eyeY);
        contentPane.add(eyeZ);

        contentPane.add(centerX);
        contentPane.add(centerY);
        contentPane.add(centerZ);

    }

    private void initSpinner() {
//        SpinnerModel moveModel1 = new SpinnerNumberModel(0, -100, 100, 1);
        eyeX.setModel(new SpinnerNumberModel(camera.getEye(Direction.X), -800, 800, 0.1));
        eyeY.setModel(new SpinnerNumberModel(camera.getEye(Direction.Y), -800, 800, 0.1));
        eyeZ.setModel(new SpinnerNumberModel(camera.getEye(Direction.Z), -800, 800, 0.1));

        centerX.setModel(new SpinnerNumberModel(camera.getC(Direction.X), -800, 800, 0.1));
        centerY.setModel(new SpinnerNumberModel(camera.getC(Direction.Y), -800, 800, 0.1));
        centerZ.setModel(new SpinnerNumberModel(camera.getC(Direction.Z), -800, 800, 0.1));


        eyeX.addChangeListener(e -> {
            controller.performChange(new Change(ChangeType.CAMERA_EYE_MOVE, Direction.X, (Number) eyeX.getValue()));
        });
        eyeY.addChangeListener(e -> {
            controller.performChange(new Change(ChangeType.CAMERA_EYE_MOVE, Direction.Y, (Number) eyeY.getValue()));
        });
        eyeZ.addChangeListener(e -> {
            controller.performChange(new Change(ChangeType.CAMERA_EYE_MOVE, Direction.Z, (Number) eyeZ.getValue()));
        });

        centerX.addChangeListener(e -> {
            controller.performChange(new Change(ChangeType.CAMERA_CENTER_MOVE, Direction.X, (Number) centerX.getValue()));
        });
        centerY.addChangeListener(e -> {
            controller.performChange(new Change(ChangeType.CAMERA_CENTER_MOVE, Direction.Y, (Number) centerY.getValue()));
        });
        centerZ.addChangeListener(e -> {
            controller.performChange(new Change(ChangeType.CAMERA_CENTER_MOVE, Direction.Z, (Number) centerZ.getValue()));
        });
    }
}
