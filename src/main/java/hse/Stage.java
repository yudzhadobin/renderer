package hse;

import hse.objects.Camera;
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

    static public double diffuseReflectionCoef = .70;
    static public double lightIntensity = 1;
    static public int light_x = 0;
    static public int light_y = 0;
    static public int light_z = 100;

    private List<Object3D> displayedObjects = new ArrayList<>();

    public Stage() {
    }

    public void addObject(Object3D object) {
        displayedObjects.add(object);
    }

    public void deleteObject(String id) {
        for (int i = 0; i < displayedObjects.size(); i++) {
            Object3D object3D = displayedObjects.get(i);
            if(object3D.id.equals(id)) {
                displayedObjects.remove(object3D);
                return;
            }
        }
    }

    public int getObjectCount() {
        return displayedObjects.size();
    }

    public Object3D getObject(int index) {
        return displayedObjects.get(index);
    }

    public void clear() {
        displayedObjects.clear();
        Camera.getInstance().clear();
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
