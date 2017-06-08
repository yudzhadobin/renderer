package hse.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hse.DrawingMode;
import hse.OculusCulling;
import hse.Setings;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by yuriy on 15.05.17.
 */
public class Environment {

    DrawingMode drawingMode;
    @JsonIgnore
    OculusCulling oculusCulling;

    String outputDirectory;

    public DrawingMode getDrawingMode() {
        return drawingMode;
    }

    public void setDrawingMode(DrawingMode drawingMode) {
        this.drawingMode = drawingMode;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public OculusCulling getOculusCulling() {
        return oculusCulling;
    }

    public void setOculusCulling(OculusCulling oculusCulling) {
        this.oculusCulling = oculusCulling;
    }

    public void init() {
        Setings.drawingMode = drawingMode;
        Setings.oculusCullingMode = OculusCulling.Z_BUFFER;
        Path path = Paths.get(outputDirectory+ Setings.oculusCullingMode, "experiment "
                + Setings.curentExperimentIndex);
        try {
            Files.createDirectories(path.getParent());
            System.setOut(new PrintStream(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
