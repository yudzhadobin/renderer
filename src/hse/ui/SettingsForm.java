package hse.ui;

import com.sun.scenario.Settings;
import hse.Setings;
import hse.matrixes.Projections;
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
    JSpinner scale = new JSpinner();
    JSpinner moveX = new JSpinner();
    JSpinner moveY = new JSpinner();
    JSpinner moveZ = new JSpinner();
    JComboBox projection = new JComboBox();
    JCheckBox light = new JCheckBox();


    public SettingsForm(Object3D object3D) {
        this.object3D = object3D;

        initSliders();
        initScale();
        Container contentPane = this.getContentPane();
        this.setMinimumSize(new Dimension(400,400));
        projection.addItem("Перспективная");
        projection.addItem("Ортогональная");
        projection.setSelectedIndex(0);

        projection.addItemListener(e -> {
            if(e.getItem().equals("Перспективная")) {
                Setings.projection = Projections.PERSPECTIVE;
            } else {
                Setings.projection = Projections.ORTHOGONAL;
            }
        });

        light.addChangeListener(
                e -> {
                    Setings.light_on = !light.isSelected();
                }
        );

        contentPane.setLayout(new FlowLayout());
        contentPane.add(angleX);
        contentPane.add(angleY);
        contentPane.add(angleZ);
        contentPane.add(scale);
        contentPane.add(moveX);
        contentPane.add(moveY);
        contentPane.add(moveZ);
        contentPane.add(projection);
        contentPane.add(light);

    }

    private void initScale() {
        SpinnerModel sm = new SpinnerNumberModel(100, 0, 500, 10); //default value,lower bound,upper bound,increment by
        scale.setModel(sm);
        scale.addChangeListener(
                e -> {
                    object3D.setScale((Integer) scale.getValue());
                }
        );
//        SpinnerModel moveModel1 = new SpinnerNumberModel(0, -100, 100, 1);
        moveX.setModel(new SpinnerNumberModel(0, -400, 400, 1));
        moveY.setModel(new SpinnerNumberModel(0, -400, 400, 1));
        moveZ.setModel(new SpinnerNumberModel(0, -400, 400, 1));

        moveX.addChangeListener(e -> {
            Setings.offset_X = (int) moveX.getValue();
        });
        moveY.addChangeListener(e -> {
            Setings.offset_Y = (int) moveY.getValue();
        });
        moveZ.addChangeListener(e -> {
            Setings.offset_Z = (int) moveZ.getValue();
        });
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

