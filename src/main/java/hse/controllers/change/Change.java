package hse.controllers.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hse.objects.Object3D;

/**
 * Created by yuriy on 04.03.17.
 */
public class Change {
    String objectID;

    ChangeType change;

    Direction direction;

    Number value;
    @JsonIgnore
    Object3D object3D;

    int start = -1;
    int end = -1;

    public Change() {
    }

    public Change(String objectID, ChangeType change) {
        this.objectID = objectID;
        this.change = change;
    }

    public Change(String objectID, ChangeType change, Direction direction, Number value) {
        this.objectID = objectID;
        this.change = change;
        this.direction = direction;
        this.value = value;
    }

    public Change(String objectID, ChangeType change, Number value) {
        this.objectID = objectID;
        this.change = change;
        this.value = value;
    }


    public Change(ChangeType change, Direction direction, Number value) {
        this.change = change;
        this.direction = direction;
        this.value = value;
    }

    public Change(ChangeType change, Object3D object3D) {
       this.change = change;
       this.object3D = object3D;
    }

    public String getObjectID() {
        return objectID;
    }

    public ChangeType getChange() {
        return change;
    }

    public Number getValue() {
        return value;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public void setChange(ChangeType change) {
        this.change = change;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isIn(int i) {
        return start == -1 || (start <= i && i <= end);
    }

    public Object3D getObject3D() {
        return object3D;
    }

    public void setObject3D(Object3D object3D) {
        this.object3D = object3D;
    }
}
