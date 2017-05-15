package hse.json;

/**
 * Created by yuriy on 15.05.17.
 */
public class Model {
    String id;
    String pathToFile;
    String pathToTexture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String getPathToTexture() {
        return pathToTexture;
    }

    public void setPathToTexture(String pathToTexture) {
        this.pathToTexture = pathToTexture;
    }
}
