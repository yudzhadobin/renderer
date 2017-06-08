package hse.ui;

import hse.TGALoader;
import hse.controllers.ChangeController;
import hse.controllers.change.Change;
import hse.controllers.change.ChangeType;
import hse.objects.Object3D;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Created by yuriy on 02.06.17.
 */
public class AddObjectPanel extends JFrame {

    ChangeController controller;

    public AddObjectPanel(ChangeController controller) {
        super();
        this.controller = controller;
        this.setMinimumSize(new Dimension(400,200));
        this.setVisible(true);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridLayout(3,2));
        final JLabel obj = new JLabel("Путь к модели: ");
        final JLabel text = new JLabel("Путь к тектсуре: ");

        final JTextField objectPath = new JTextField();
        final JTextField texturePath = new JTextField();
        objectPath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().endsWith(".obj");
                    }

                    @Override
                    public String getDescription() {
                        return "obj";
                    }
                });
                chooser.setCurrentDirectory(new File("./"));
                chooser.showOpenDialog(AddObjectPanel.this);
                File object = chooser.getSelectedFile();
                if (object != null) {
                    objectPath.setText(object.getAbsolutePath());
                }
            }
        });

        texturePath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser1 = new JFileChooser();
//

                chooser1.setCurrentDirectory(new File("./"));
                chooser1.showOpenDialog(AddObjectPanel.this);

                File object = chooser1.getSelectedFile();
                if(object != null) {
                    texturePath.setText(object.getAbsolutePath());
                }
            }
        });
        JButton saveButton = new JButton("Добавить");
        JButton exitButton = new JButton("Закрыть");

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage image = null;
                if(texturePath.getText() != null && !texturePath.getText().isEmpty()) {
                    image = TGALoader.getImage(texturePath.getText());
                }
                if(objectPath.getText() != null &&  !objectPath.getText().isEmpty()) {
                    Object3D object3D = Object3D.createFromFile(Paths.get(objectPath.getText()), image);
                    object3D.id = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
                    controller.performChange(new Change(
                            ChangeType.ADD_OBJECT,
                            object3D
                    ));
                    exitButton.doClick();
                }

            }
        });

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddObjectPanel.this.dispose();
            }
        });

        contentPane.add(obj);
        contentPane.add(objectPath);
        contentPane.add(text);
        contentPane.add(texturePath);

        contentPane.add(saveButton);
        contentPane.add(exitButton);

    }
}
