package hse.ui;

import com.sun.scenario.Settings;
import hse.DrawingMode;
import hse.Setings;
import hse.controllers.ChangeController;
import hse.controllers.change.Change;
import hse.controllers.change.ChangeType;
import hse.controllers.change.Direction;
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

    ChangeController controller;

    JSlider angleX = new JSlider();
    JSlider angleY = new JSlider();
    JSlider angleZ = new JSlider();
    JSpinner scale = new JSpinner();
    JSpinner moveX = new JSpinner();
    JSpinner moveY = new JSpinner();
    JSpinner moveZ = new JSpinner();
    JComboBox projection = new JComboBox();
    JComboBox drawMode = new JComboBox();

    JCheckBox light = new JCheckBox();


    public SettingsForm(Object3D object3D, ChangeController changeController) {
        this.object3D = object3D;
        this.controller = changeController;

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
            controller.performChange(new Change(
                    object3D.id,
                    ChangeType.PROJECTION_CHANGE
            ));
        });

        drawMode.addItem("Контур");
        drawMode.addItem("Модель");
        drawMode.addItem("Текстуры");

        drawMode.addItemListener(e -> {
            if(e.getItem().equals("Контур")) {
                Setings.drawingMode = DrawingMode.CONTOUR;
            }
            if(e.getItem().equals("Модель")) {
                Setings.drawingMode = DrawingMode.MODEL;
            }
            if(e.getItem().equals("Текстуры")) {
                Setings.drawingMode = DrawingMode.TEXTURED;
            }

            controller.performChange(new Change(
                    object3D.id,
                    ChangeType.TEXTURE_CHANGE
            ));
        });


        light.addChangeListener(
                e -> {
                    Setings.light_on = !light.isSelected();

                    controller.performChange(new Change(
                            object3D.id,
                            ChangeType.LIGHT_CHANGE
                    ));
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
        contentPane.add(drawMode);

    }

    private void initScale() {
        SpinnerModel sm = new SpinnerNumberModel(1, 0, 500, 1); //default value,lower bound,upper bound,increment by
        scale.setModel(sm);
        scale.addChangeListener(
                e -> {
                    controller.performChange(new Change(
                            object3D.id,
                            ChangeType.SCALE_CHANGE,
                            (Number) scale.getValue()
                    ));
                }
        );
//        SpinnerModel moveModel1 = new SpinnerNumberModel(0, -100, 100, 1);
        moveX.setModel(new SpinnerNumberModel(0, -400, 400, 1));
        moveY.setModel(new SpinnerNumberModel(0, -400, 400, 1));
        moveZ.setModel(new SpinnerNumberModel(0, -400, 400, 1));

        moveX.addChangeListener(e -> {
            controller.performChange(new Change(
                    object3D.id,
                    ChangeType.MODEL_MOVE,
                    Direction.X,
                    (Double) moveX.getValue()
            ));
        });
        moveY.addChangeListener(e -> {
            controller.performChange(new Change(
                    object3D.id,
                    ChangeType.MODEL_MOVE,
                    Direction.Y,
                    (Double) moveY.getValue()
            ));
        });
        moveZ.addChangeListener(e -> {
            controller.performChange(new Change(
                    object3D.id,
                    ChangeType.MODEL_MOVE,
                    Direction.Z,
                    (Double) moveZ.getValue()
            ));
        });
    }

    private void initSliders() {
        initSlider(angleX);
        initSlider(angleY);
        initSlider(angleZ);

        angleX.addChangeListener(e -> controller.performChange(new Change(
                object3D.id,
                ChangeType.ROTATION,
                Direction.X,
                angleX.getValue()
        )));

        angleY.addChangeListener(e -> controller.performChange(new Change(
                object3D.id,
                ChangeType.ROTATION,
                Direction.Y,
                angleY.getValue()
        )));

        angleZ.addChangeListener(e -> controller.performChange(new Change(
                object3D.id,
                ChangeType.ROTATION,
                Direction.Z,
                angleZ.getValue()
        )));


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

