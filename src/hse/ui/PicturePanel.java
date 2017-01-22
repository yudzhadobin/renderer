package hse.ui;

import hse.Setings;

import hse.objects.Object3D;
import hse.objects.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yura on 25.12.2016.
 */
public class PicturePanel extends JPanel {
    SwapChain swapChain;

    List<Object3D> drawedObjects =  new ArrayList<>();


    public PicturePanel() {
        super();
        this.swapChain = SwapChain.getInstance();

        this.setSize(new Dimension(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT));

        this.setVisible(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point3D<Integer> point3D = new Point3D<Integer>(
                        e.getX(),
                        e.getY(),
                        0
                );
                for (Object3D drawedObject : drawedObjects) {
                    if(drawedObject.getBox().isIn(point3D)) {
                        EventQueue.invokeLater(() -> {
                            SettingsForm form = new SettingsForm(drawedObject);
                            form.setVisible(true);
                        });
                    }
                }
            }
        });
    }


    @Override
    public void repaint() {
        if(swapChain != null) {
            swapChain.swap();
        }
        super.repaint();

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(swapChain.getVisible(), 0, 0, this);
//        g.drawImage(ZBuffer.getBuffer().toBufferedImage(), 0, 0, this);
 //       g.drawImage(swapChain.getDiff(), 0, 0, this);
    }
}

