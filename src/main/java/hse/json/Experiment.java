package hse.json;

import hse.Stage;
import hse.TGALoader;
import hse.objects.Object3D;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by yuriy on 15.05.17.
 */
public class Experiment {
    List<Model> models;
    int duration;
    Environment environment;

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

    public void init() {
        models.forEach(model -> {
            BufferedImage image = null;
            if(model.pathToTexture != null) {
                image = TGALoader.getImage("./models/head.tga"); // TODO: 07.01.2017 rewrite;
            }
            Object3D object3D = Object3D.createFromFile(Paths.get(model.getPathToFile()), image);
            object3D.id = model.id;
            Stage.getInstance().addObject(object3D);
        });

        environment.init();
    }
}
