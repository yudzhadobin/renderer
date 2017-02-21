package hse.ui;

import hse.Setings;

import hse.Stage;
import hse.objects.Object3D;
import hse.objects.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yura on 25.12.2016.
 */
public class PicturePanel extends JPanel {
    SwapChain swapChain;



    public PicturePanel() {
        super();
        this.swapChain = SwapChain.getInstance();

//        BufferedImage bufferedImage = new BufferedImage(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.setDoubleBuffered(false);
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
                for (Object3D drawedObject : Stage.getInstance().getDisplayedObjects()) {
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

    boolean isMyUpdate = false;


    public void forceUpdate() {
        isMyUpdate = true;
        repaint();
    }

    @Override
    public void repaint() {
        if (!isMyUpdate) {
            return;
        }


        if(swapChain != null) {
            swapChain.swap();
        }
        super.repaint();
        isMyUpdate = false;
    }
    long lastUpdate = 0;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        long time = System.currentTimeMillis();

        g.drawImage(swapChain.getVisible(), 0, 0, this);

        System.out.println(time - lastUpdate);
        lastUpdate = time;
//        g.drawImage(ZBuffer.getBuffer().toBufferedImage(), 0, 0, this);
 //       g.drawImage(swapChain.getDiff(), 0, 0, this);
    }
}

