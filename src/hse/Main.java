package hse;

import java.nio.file.Paths;

import hse.controllers.ChangeController;
import hse.controllers.EventMaster;
import hse.controllers.Master;
import hse.controllers.change.Change;
import hse.controllers.change.ChangeType;
import hse.objects.Camera;
import hse.objects.Object3D;
import hse.ui.CameraForm;
import hse.ui.MainForm;

import java.awt.*;

/**
 * Created by Yura on 25.12.2016.
 */
public class Main {
    static MainForm form;
    static ChangeController controller;


    public static void main(String[] args) throws InterruptedException {

//        Object3D fromFile = Object3D.createFromFile(Paths.get("./models/head.obj"));

        EventQueue.invokeLater(() -> {
            form = new MainForm(false);
            controller = ChangeController.createController(false, form);
            form.initPicturePanel(controller);

            form.setVisible(true);
        });

        Thread.currentThread().sleep(4000);
        Stage.getInstance().addObject(Object3D.createFromFile(Paths.get("./models/head.obj")));
        Stage.getInstance().getObject(0).id = "head";

        controller.start();

        controller.performChange(new Change(
                "",
                ChangeType.INIT
        ));
        //        Stage.getInstance().addObject(Object3D.createFromFile(Paths.get("./models/cube.obj")));
//        Stage.getInstance().addObject(Object3D.createFromFile(Paths.get("./models/cube.obj")));
//        Stage.getInstance().addObject(Object3D.createFromFile(Paths.get("./models/cube.obj")));
//        Stage.getInstance().addObject(Object3D.createFromFile(Paths.get("./models/cube.obj")));
//        Master master = new Master(form);

        new CameraForm(controller).setVisible(true);
    }
}
