package hse.json;

import hse.OculusCulling;
import hse.Stage;
import hse.TGALoader;
import hse.controllers.ChangeController;
import hse.controllers.change.Change;
import hse.objects.Object3D;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yuriy on 15.05.17.
 */
public class Experiment {
    List<Model> models;
    int duration;
    Environment environment;
    List<Change> changes;
    List<Change> initChanges;

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    public List<Change> getInitChanges() {
        return initChanges;
    }

    public void setInitChanges(List<Change> initChanges) {
        this.initChanges = initChanges;
    }

    public void init(ChangeController controller, OculusCulling oculusCulling) {
        models.forEach(model -> {
            BufferedImage image = null;
            if(model.pathToTexture != null) {
                image = TGALoader.getImage( model.pathToTexture); // TODO: 07.01.2017 rewrite;
            }
            Object3D object3D = Object3D.createFromFile(Paths.get(model.getPathToFile()), image);
            object3D.id = model.id;
            Stage.getInstance().addObject(object3D);
        });
        environment.setOculusCulling(oculusCulling);
        environment.init();

        controller.performChange(initChanges.toArray(new Change[initChanges.size()]));
    }

    public void start(ChangeController controller) {
        for (int i = 0; i < duration; i++) {
            final int copy = i;
            List<Change> changes = this.changes.stream().filter(change -> change.isIn(copy)).collect(Collectors.toList());

            controller.performChange(
                changes.toArray(new Change[changes.size()])
            );

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
