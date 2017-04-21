package hse.ui;

import hse.Setings;
import hse.controllers.ChangeController;
import hse.objects.Object3D;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yura on 25.12.2016.
 */
public class MainForm extends JFrame {


    public PicturePanel picturePanel;

    public MainForm(boolean isAutoDraw) {
        super();

        this.setSize(new Dimension(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT));
        this.setIgnoreRepaint(true);
        this.setResizable(false);

    }

    public void initPicturePanel(ChangeController controller){
        picturePanel = new PicturePanel(controller);
        picturePanel.validate();
        picturePanel.setVisible(true);

        this.getContentPane().add(picturePanel);
    }


    @Override
    public void repaint() {
        super.repaint();
        picturePanel.repaint();
    }



}
