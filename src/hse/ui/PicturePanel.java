package hse.ui;

import hse.Setings;
import hse.Task;
import hse.ZBuffer;
import hse.matrixes.Matrix;
import hse.matrixes.conversations.RotationX;
import hse.matrixes.conversations.RotationY;
import hse.matrixes.conversations.Scale;
import hse.objects.Object3D;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

/**
 * Created by Yura on 25.12.2016.
 */
public class PicturePanel extends JPanel {
    SwapChain swapChain;

    public PicturePanel() {
        super();
        this.swapChain = SwapChain.getInstance();

        this.setSize(new Dimension(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT));

        this.setVisible(true);
    }


    @Override
    public void repaint() {
        if(swapChain != null) {
            swapChain.swap();
            System.out.println("Visisble = " + swapChain.getVisible().hashCode() +
                    "  drawing " + swapChain.getDrawing().hashCode());
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

