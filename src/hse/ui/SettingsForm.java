package hse.ui;

import hse.objects.Object3D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Yura on 22.01.2017.
 */
public class SettingsForm extends JFrame {
    Object3D object3D;

    JSlider angleX = new JSlider();
    JSlider angleY = new JSlider();
    JSlider angleZ = new JSlider();


    public SettingsForm(Object3D object3D) {
        this.object3D = object3D;

        initSliders();

        Container contentPane = this.getContentPane();
        this.setMinimumSize(new Dimension(400,400));
        contentPane.setLayout(new FlowLayout());
        contentPane.add(angleX);
        contentPane.add(angleY);
        contentPane.add(angleZ);

    }


    private void initSliders() {
        initSlider(angleX);
        initSlider(angleY);
        initSlider(angleZ);

        angleX.addChangeListener(e -> object3D.setXRotation(angleX.getValue()));
        angleY.addChangeListener(e -> object3D.setYRotation(angleY.getValue()));
        angleZ.addChangeListener(e -> object3D.setZRotation(angleZ.getValue()));


    }


    private void initSlider(JSlider slider) {
        slider.setValue(0);
        slider.setMinimum(-180);
        slider.setMaximum(+180);
        slider.setMinorTickSpacing(10);
        slider.setMajorTickSpacing(90);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }
}

