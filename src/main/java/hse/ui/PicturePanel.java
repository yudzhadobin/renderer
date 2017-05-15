package hse.ui;

import hse.Setings;
import hse.Stage;
import hse.controllers.ChangeController;
import hse.objects.Object3D;
import hse.objects.Point3DInteger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by Yura on 25.12.2016.
 */
public class PicturePanel extends JPanel {
    SwapChain swapChain;

    ChangeController controller;

    public PicturePanel(ChangeController controller) {
        super();
        this.swapChain = SwapChain.getInstance();
        this.controller = controller;
//        BufferedImage bufferedImage = new BufferedImage(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.setDoubleBuffered(false);
        this.setSize(new Dimension(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT));

        this.setVisible(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point3DInteger point3DDouble = new Point3DInteger(
                        e.getX(),
                        e.getY(),
                        0
                );
                for (Object3D drawedObject : Stage.getInstance().getDisplayedObjects()) {
                    if(drawedObject.getBox().isIn(point3DDouble)) {
                        EventQueue.invokeLater(() -> {
                            SettingsForm form = new SettingsForm(drawedObject, controller);
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



        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -swapChain.getVisible().getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage filter = op.filter(swapChain.getVisible(), null);


        g.drawImage(filter, 0, 0, this);

//        System.out.println(time - lastUpdate);
        lastUpdate = time;
//        g.drawImage(ZBuffer.getBuffer().toBufferedImage(), 0, 0, this);
 //       g.drawImage(swapChain.getDiff(), 0, 0, this);
    }
}

