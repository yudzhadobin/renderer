package hse.ui;

import hse.objects.Camera;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuriy on 27.02.17.
 */
public class CameraForm extends JFrame {

    Camera camera = Camera.getInstance();

    JSpinner eyeX = new JSpinner();
    JSpinner eyeY = new JSpinner();
    JSpinner eyeZ = new JSpinner();

    JSpinner centerX = new JSpinner();
    JSpinner centerY = new JSpinner();
    JSpinner centerZ = new JSpinner();


    public CameraForm() {
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
        eyeX.setModel(new SpinnerNumberModel(0, -400, 400, 1));
        eyeY.setModel(new SpinnerNumberModel(0, -400, 400, 1));
        eyeZ.setModel(new SpinnerNumberModel(0, -400, 400, 1));

        centerX.setModel(new SpinnerNumberModel(0, -400, 400, 0.01));
        centerY.setModel(new SpinnerNumberModel(0, -400, 400, 0.01));
        centerZ.setModel(new SpinnerNumberModel(0, -400, 400, 0.01));


        eyeX.addChangeListener(e -> {
            camera.setEyeX((int)eyeX.getValue());
        });
        eyeY.addChangeListener(e -> {
            camera.setEyeY((int)eyeY.getValue());
        });
        eyeZ.addChangeListener(e -> {
            camera.setEyeZ((int)eyeZ.getValue());
        });

        centerX.addChangeListener(e -> {
            camera.setCenterX((int)eyeX.getValue());
        });
        centerY.addChangeListener(e -> {
            camera.setCenterY((int)eyeY.getValue());
        });
        centerZ.addChangeListener(e -> {
            camera.setCenterZ((int)eyeZ.getValue());
        });
    }
}
