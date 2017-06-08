package hse.ui;

import hse.Setings;
import hse.controllers.ChangeController;
import hse.controllers.change.Change;
import hse.controllers.change.ChangeType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Yura on 25.12.2016.
 */
public class MainForm extends JFrame {


    public PicturePanel picturePanel;

    public ChangeController controller;

    public MainForm(boolean isAutoDraw) {
        super();

        this.setSize(new Dimension(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT));
        this.setIgnoreRepaint(true);
        this.setResizable(false);


        JToolBar toolBar = new JToolBar();
        Button saveButton = new Button("Добавить объект");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                EventQueue.invokeLater(() -> {
                    AddObjectPanel objectPanel = new AddObjectPanel(controller);
                    objectPanel.setVisible(true);
                });

        }});

        Button deleteButton = new Button("Удалить объект");
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainForm.this.picturePanel.delete = true;
            }
        });
        Button changeMode = new Button("Сменить режим");
        changeMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.performChange(new Change(
                        "",
                        ChangeType.CHANGE_MODE

                ));
            }
        });
        toolBar.add(saveButton);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(deleteButton);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(changeMode);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);

    }

    public void initPicturePanel(ChangeController controller){
        this.controller = controller;

        picturePanel = new PicturePanel(controller);
        picturePanel.validate();
        picturePanel.setVisible(true);


        this.getContentPane().add(picturePanel, BorderLayout.CENTER);
    }


    @Override
    public void repaint() {
        super.repaint();
        picturePanel.repaint();
    }



}
