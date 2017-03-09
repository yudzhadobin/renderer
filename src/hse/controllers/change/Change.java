package hse.controllers.change;

/**
 * Created by yuriy on 04.03.17.
 */
public class Change {
    String objectID;

    ChangeType change;

    Direction direction;

    Number value;

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
}
