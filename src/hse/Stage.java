package hse;

import hse.objects.Object3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yura on 09.01.2017.
 */
public class Stage {
    static Stage instance = new Stage();

    public static Stage getInstance() {
        return instance;
    }

    static public double diffuseReflectionCoef = .50;
    static public double lightIntensity = 1;
    static public int light_x = 50;
    static public int light_y = 50;
    static public int light_z = 10;

    private List<Object3D> displayedObjects = new ArrayList<>();

    public Stage() {
    }

    public void addObject(Object3D object) {
        displayedObjects.add(object);
    }

    public int getObjectCount() {
        return displayedObjects.size();
    }

    public Object3D getObject(int index) {
        return displayedObjects.get(index);
    }


    public Object3D getObject(String id) {
        if(id == null) {
            return null;
        }

        for (Object3D displayedObject : displayedObjects) {

            if (displayedObject.id.equals(id)) {
                return displayedObject;
            }
        }
        return null;
    }

    public List<Object3D> getDisplayedObjects() {
        return displayedObjects;
    }
}
