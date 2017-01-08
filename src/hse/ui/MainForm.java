package hse.ui;

import hse.Setings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yura on 25.12.2016.
 */
public class MainForm extends JFrame {



    public PicturePanel picturePanel;

    public MainForm() {
        super();


        this.setSize(new Dimension(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT));


        picturePanel = new PicturePanel();
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
