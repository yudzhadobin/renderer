package hse.controllers;

import hse.*;
import hse.bsptree.Plane3D;
import hse.controllers.change.Change;
import hse.matrixes.PerspectiveProjection;
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
                    if (Setings.projection instanceof PerspectiveProjection) {
                        ((PerspectiveProjection)Setings.projection).update();
                    }
                    break;
                case CAMERA_CENTER_MOVE:
                    Camera.getInstance().setC(change.getDirection(), (Double) change.getValue());
                    break;
                case MODEL_MOVE:
                    obj.setMove(change.getDirection(), (Double) change.getValue());
                    Setings.bspRebuild = true;
                    break;
                case ROTATION:
                    obj.setRotation(change.getDirection(), ((int) change.getValue()));
                    Setings.bspRebuild = true;
                    break;
                case ROTATION_INC:
                    int angle = obj.getRotation(change.getDirection());
                    obj.setRotation(change.getDirection(), ( angle + (int) change.getValue()));
                    Setings.bspRebuild = true;
                    break;
                case MOVE_INC:
                    double move = obj.getMove(change.getDirection());
                    obj.setMove(change.getDirection(), move + (double) change.getValue());
                    Setings.bspRebuild = true;
                    break;
                case CAMERA_EYE_MOVE_INC:
                    Camera camera = Camera.getInstance();
                    double eye = camera.getEye(change.getDirection());
                    camera.setEye(change.getDirection(), eye + (Double) change.getValue());
                    if (Setings.projection instanceof PerspectiveProjection) {
                        ((PerspectiveProjection)Setings.projection).update();
                    }
                    break;
                case SCALE_CHANGE:
                    obj.setScale( (Double) change.getValue());
                    Setings.bspRebuild = true;
                    break;
                case ADD_OBJECT:
                    Stage.getInstance().addObject(change.getObject3D());
                    Setings.bspRebuild = true;
                    break;
                case DELETE_OBJECT:
                    Stage.getInstance().deleteObject(change.getObjectID());
                    Setings.bspRebuild = true;
                    break;
                case CHANGE_MODE:
                    int next = (Setings.oculusCullingMode.ordinal() + 1) % 2;
                    Setings.oculusCullingMode = OculusCulling.values()[next];
                    Setings.bspRebuild = true;
                    System.out.printf("Now mode is %s\n", Setings.oculusCullingMode.toString());
                    ZBuffer.getBuffer().clear();
                    break;
                case TEXTURE_CHANGE:
                    if(Setings.drawingMode == DrawingMode.CONTOUR) {
                        Plane3D.EPSILON = 0.002d;
                    } else {
                        Plane3D.EPSILON = 0.3d;
                    }
                    Setings.bspRebuild = true;
                    ZBuffer.getBuffer().clear();
                    break;
            }
        }
        if(updater != null) {
            updater.redraw();
        }
    }
}
