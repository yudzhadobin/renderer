package hse.controllers;

import hse.Stage;
import hse.controllers.change.Change;
import hse.objects.Camera;
import hse.objects.Object3D;
import hse.ui.MainForm;

/**
 * Created by yuriy on 04.03.17.
 */
public class ChangeController {

    Stage stage = Stage.getInstance();

    Updater updater;

    MainForm form;
    boolean isAutoDraw;

    public static ChangeController createController(boolean isAutoDraw, MainForm form) {
        ChangeController controller = new ChangeController();

        controller.form = form;
        controller.isAutoDraw = isAutoDraw;
        return controller;
    }

    public void start() {
        updater = isAutoDraw ? new Master(form) : new EventMaster(form);
    }

    public void performChange(Change... changes) {
        for (Change change : changes) {


            Object3D obj = stage.getObject(change.getObjectID());
            switch (change.getChange()) {
                case CAMERA_EYE_MOVE:
                    Camera.getInstance().setEye(change.getDirection(), (Double) change.getValue());
                    break;
                case CAMERA_CENTER_MOVE:
                    Camera.getInstance().setC(change.getDirection(), (Double) change.getValue());
                    break;
                case MODEL_MOVE:
                    obj.setMove(change.getDirection(), (Integer) change.getValue());
                    break;
                case ROTATION:
                    obj.setRotation(change.getDirection(), ((int) change.getValue()));
                    break;
                case SCALE_CHANGE:
                    obj.setScale((Integer) change.getValue());
                    break;
            }
        }
        if(updater != null) {
            updater.redraw();
        }
    }
}
