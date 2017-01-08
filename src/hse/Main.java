package hse;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Paths;

import hse.matrixes.Matrix;
import hse.matrixes.OrthogonalProjection;
import hse.matrixes.PerspectiveProjection;
import hse.matrixes.conversations.RotationX;
import hse.matrixes.conversations.RotationY;
import hse.objects.Object3D;
import hse.objects.Point3D;
import hse.ui.MainForm;

import java.awt.*;

/**
 * Created by Yura on 25.12.2016.
 */
public class Main {
    static MainForm form;

    public static void main(String[] args) throws InterruptedException {


        EventQueue.invokeLater(() -> {
            form = new MainForm();
            form.setVisible(true);
        });

        Thread.currentThread().sleep(1000);
        Object3D fromFile = Object3D.createFromFile(Paths.get("./models/head.obj"));

        Master master = new Master(fromFile, form);

    }
}
