package hse;

import com.fasterxml.jackson.databind.ObjectMapper;
import hse.controllers.ChangeController;
import hse.controllers.change.Change;
import hse.controllers.change.ChangeType;
import hse.json.Experiment;
import hse.ui.CameraForm;
import hse.ui.MainForm;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Yura on 25.12.2016.
 */
public class Main {
    static MainForm form;
    static ChangeController controller;


    public static void main(String[] args) throws InterruptedException, IOException {
        File file = new File(Paths.get("experiments/test.json").toUri());
        ObjectMapper mapper = new ObjectMapper();
        Experiment experiment = mapper.readValue(file, Experiment.class);

        EventQueue.invokeLater(() -> {
            form = new MainForm(false);
            controller = ChangeController.createController(false, form);
            form.initPicturePanel(controller);

            form.setVisible(true);
        });

        Thread.currentThread().sleep(1000);

        controller.start();
        experiment.init();
//        Stage.getInstance().addObject(Object3D.createFromFile(Paths.get("./models/head.obj")));
//        Stage.getInstance().getObject(0).id = "head";
//        Stage.getInstance().addObject(Object3D.createFromFile(Paths.get("./models/cube.obj")));
//        Stage.getInstance().getObject(1).id = "cube";

//
//
        controller.performChange(new Change(
                "",
                ChangeType.INIT
        ));

//        controller.performChange(new Change(
//                        "head",
//                        ChangeType.SCALE_CHANGE,
//                        0.3
//                )
//
//        );
//
//        controller.performChange(new Change(
//                "head",
//                ChangeType.ROTATION,
//                Direction.X,
//                90
//        ));
////
//        controller.performChange(new Change(
//                "head",
//                ChangeType.MODEL_MOVE,
//                Direction.X,
//                0.2
//        ));
        new CameraForm(controller).setVisible(true);
//        Object3D fromFile = Object3D.createFromFile(Paths.get("./models/head.obj"));

//        PointInfo a = new PointInfo(new Point3DDouble(0.0, 0.0 ,0.0), null, new Normal(0.0, 0.0 , 1.0), 0);
//        PointInfo b = new PointInfo(new Point3DDouble(1.0, 0.0 ,0.0), null, new Normal(0.0, 0.0 , 1.0), 0);
//        PointInfo c = new PointInfo(new Point3DDouble(1.0, 1.0 ,0.0), null, new Normal(0.0, 0.0 , 1.0), 0);
//
//        Side side = new Side(a, b, c);
//
//        Plane3D plane = side.getPlane();
//
//
//        PointInfo a1 = new PointInfo(new Point3DDouble(0.5, 0.0 ,0.0), null, new Normal(-1.0, 0.0 , 0.0), 0);
//        PointInfo b1 = new PointInfo(new Point3DDouble(0.5, 0.0 ,1.0), null, new Normal(-1.0, 0.0 , 0.0), 0);
//        PointInfo c1 = new PointInfo(new Point3DDouble(0.5, 1.0 ,1.0), null, new Normal(-1.0, 0.0 , 0.0), 0);
//
//        Side side1 = new Side(a1, b1, c1);
//

//        Pair<SideLocation, Pair<List<Side>, List<Side>>> split = side1.getPlane().split(side);
//        BspTree bspTree = new BspTree();
//        bspTree.insert(fromFile.getSides(), SideLocation.SPANNING, SideLocation.ON);
//
//
////        Pair<SideLocation, Pair<List<Side>, List<Side>>> split1 = fromFile.getSides().get(0).getPlane().split(fromFile.getSides().get(0));
//        int j =5;
//        Plane3D plane = fromFile.getSides().get(3).getPlane();

//        List<Plane3D.SideLocation> sideLocations = fromFile.getSides().stream().map(side -> {plane.split(side);}).collect(Collectors.toList());

//        fromFile.getSides().forEach(side -> {
//            plane.split(side);
//        });
    }



}
