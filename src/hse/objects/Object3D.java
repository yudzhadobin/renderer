package hse.objects;

import hse.TGALoader;
import hse.UvCoordinate;
import hse.matrixes.Matrix;
import hse.matrixes.Projections;
import hse.matrixes.conversations.Scale;
import hse.ui.SwapChain;
import javafx.beans.binding.DoubleExpression;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yura on 01.01.2017.
 */
public class Object3D {
    List<Point3D<Double>> localPoints;
    List<Side> sides;

    Point3D position;
    BufferedImage texture;

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public Object3D() {
        localPoints = new ArrayList<>();
        sides = new ArrayList<>();


        try {
           texture = TGALoader.getImage("./models/head.tga"); // TODO: 07.01.2017 rewrite
        } catch (IOException e) {
            e.printStackTrace();
        }
        //// TODO: 03.01.2017 position init 
    }

    public static Object3D createFromFile(Path path) {
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

            Object3D result = new Object3D();

            String s;
            List<UvCoordinate> uvCoordinates = new ArrayList<>();
            List<Normal> normals = new ArrayList<>();
            while ((s = reader.readLine()) != null) {
                final String[] parts = s.replaceAll("\\s+", " ").split(" ");

                switch (parts[0]) {
                    case "v":
                        result.localPoints.add(new Point3D<>(
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3])
                        ));
                        break;

                    case "vn":
                        normals.add(new Normal(
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3])
                        ));
                        break;

                    case "f":


                        result.sides.addAll(Side.create(
                                new ArrayList<Integer>() {
                                    {
                                        for (int i = 1; i < parts.length; i++) {
                                            add(Integer.parseInt(parts[i].split("/")[0]) - 1);
                                        }
                                    }
                                }
                        ));

                        UvCoordinate was = null;
                        int index = -1;
                        for (int i = 1; i < parts.length; i++) {
                            if(result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).getUvCoordinate() != null) {
                                 was = result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).getUvCoordinate();
                                index = i;
                            }
                            result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).
                                    setNormal(normals.get(Integer.parseInt(parts[i].split("/")[1])-1));
                        }

                        for (int i = 1; i < parts.length; i++) {
                            result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).
                                    setUvCoordinate(uvCoordinates.get(Integer.parseInt(parts[i].split("/")[1])-1));
                        }
                        if(index != -1) {
                            if (!was.equals(result.localPoints.get(Integer.parseInt(parts[index].split("/")[0]) - 1).getUvCoordinate())) {
                                int j = 5;
                            }
                        }

                        break;

                    case "vt":
                        uvCoordinates.add(new UvCoordinate(
                                (int)(result.texture.getWidth() * Double.parseDouble(parts[1])),
                                (int)(result.texture.getHeight() * Double.parseDouble(parts[2])),
                                0
                        ));

                        break;

                }

            }
                return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void draw() {
        Matrix scale = new Scale();

        List<Point3D<Double>> transformedPoints = new ArrayList<>();

        for (int i = 0; i < localPoints.size(); i++) {
            transformedPoints.add(scale.multiple(localPoints.get(i)));
        }

        for (int i = 0; i < sides.size(); i++) {
            Side side = sides.get(i);
            side.drawContour(SwapChain.getInstance(), transformedPoints, Projections.ORTHOGONAL);
        }
    //    SwapChain.getInstance().swap();
    }

    public List<Point3D<Double>> getLocalPoints() {
        return localPoints;
    }

    public List<Side> getSides() {
        return sides;
    }
}
