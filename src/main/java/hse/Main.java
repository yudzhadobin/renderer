package hse;

import com.fasterxml.jackson.databind.ObjectMapper;
import hse.controllers.ChangeController;
import hse.controllers.change.Change;
import hse.controllers.change.ChangeType;
import hse.controllers.change.Direction;
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
//        args = new String[] {"/Users/yuriy/IdeaProjects/renderer/experiments/demo.json", "bspd"};

        EventQueue.invokeLater(() -> {
            form = new MainForm(false);
            controller = ChangeController.createController(false, form);

            form.initPicturePanel(controller);

            form.setVisible(true);
        });
        Thread.currentThread().sleep(5000);
        controller.start();

        if(args.length > 0) {
            File file = new File(Paths.get(args[0]).toUri());
            ObjectMapper mapper = new ObjectMapper();
            Experiment experiment = mapper.readValue(file, Experiment.class);
            for (int i = 0; i < 1; i++) {
                OculusCulling cullingMode = OculusCulling.Z_BUFFER;

                if(args.length > 1) {
                    String mode = args[1].trim().toLowerCase();

                    if(mode.equals("bsp")){
                        cullingMode = OculusCulling.BSP_TREE;
                    } else {
                        cullingMode = OculusCulling.Z_BUFFER;
                    }
                }

                experiment.init(controller, cullingMode);
                experiment.start(controller);

                Stage.getInstance().clear();
                Setings.curentExperimentIndex++;
            }
            form.dispose();
            System.exit(0);

        } else {
            controller.performChange(new Change(ChangeType.INIT,
                    Direction.X, 0));
        }

        new CameraForm(controller).setVisible(true);
    }



}
