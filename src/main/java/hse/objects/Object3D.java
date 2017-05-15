package hse.objects;

import hse.UvCoordinate;
import hse.controllers.change.Direction;
import javafx.util.Pair;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yura on 01.01.2017.
 */
public class Object3D {

    public String id;
    List<Point3DDouble> localPoints;
    List<Point3DDouble> extraPoints;

    Map<Point3DDouble,Pair<ChangeableSupplier<Boolean>, Point3DDouble>> transformedPoints;

    List<Side> sides;


    List<Integer> rotations = new ArrayList<Integer>() {
        {
            add(0);
            add(0);
            add(0);
        }
    };
    List<Double> move = new ArrayList<Double>() {
        {
            add(0.0d);
            add(0.0d);
            add(0.0d);
        }
    };

    
    BufferedImage texture;

    BoundingBox box = new BoundingBox();

    private double scale = 1;

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public BoundingBox getBox() {
        return box;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public Object3D() {
        localPoints = new ArrayList<>();
        sides = new ArrayList<>();
        transformedPoints = new HashMap<>();
    }

    public static Object3D createFromFile(Path path, BufferedImage texture) {
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

            Object3D result = new Object3D();
            result.texture = texture;
            String s;
            List<UvCoordinate> uvCoordinates = new ArrayList<>();
            List<Normal> normals = new ArrayList<>();
            while ((s = reader.readLine()) != null) {
                final String[] parts = s.replaceAll("\\s+", " ").trim().split(" ");

                switch (parts[0]) {
                    case "v":
                        Point3DDouble point3DDouble = new Point3DDouble(
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3])
                        );

                        result.localPoints.add(point3DDouble);
                        result.transformedPoints.put(
                                point3DDouble, new Pair<> (
                                        new ChangeableSupplier<Boolean>(false),
                                        new Point3DDouble(point3DDouble)
                                )
                        );
                        break;

                    case "vn":
                        normals.add(new Normal(
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3])
                        ));
                        break;

                    case "f":
                        List<PointInfo> pointInfos = new ArrayList<>();
                        for (int i = 1; i < parts.length; i++) {

                            String[] infos = parts[i].split("/");
                            UvCoordinate uvCoordinate;
                            try {
                                uvCoordinate = uvCoordinates.get(Integer.parseInt(infos[1]) - 1);
                            }catch (Exception e) {
                                uvCoordinate = new UvCoordinate(0,0,0);//// TODO: 07.02.2017 rewrite
                            }
                            pointInfos.add(new PointInfo(
                                    result.transformedPoints.get(
                                            result.localPoints.get(Integer.parseInt(infos[0]) - 1)
                                    ).getValue(),
                                    uvCoordinate,
                                    normals.get(Integer.parseInt(infos[2]) - 1),
                                    Integer.parseInt(infos[0]) - 1
                            ));
                        }
                        result.sides.add(new Side(pointInfos));
                        break;

                    case "vt":
                        if(result.texture != null) {
                            uvCoordinates.add(new UvCoordinate(
                                    (int) (result.texture.getWidth() * Double.parseDouble(parts[1])),
                                    (int) (result.texture.getHeight() * Double.parseDouble(parts[2])),
                                    0
                            ));
                        }
                        break;

                }

            }
                return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Point3DDouble> getLocalPoints() {
        return localPoints;
    }

    public Map<Point3DDouble, Pair<ChangeableSupplier<Boolean>, Point3DDouble>> getTransformedPoints() {
        return transformedPoints;
    }

    public List<Side> getSides() {
        return sides;
    }

    public void clear() {
        transformedPoints.forEach(
                (point3D, changeableSupplierPoint3DPair) -> {
                    transformedPoints.get(point3D).getKey().set(false);
                }
        );
        box.clear();
    }

    public void setMove(Direction direction, double value) {
        this.move.set(direction.ordinal(), value);
    }

    public void setRotation(Direction direction, int value) {
        this.rotations.set(direction.ordinal(), value);
    }

    public void setXRotation(int x) {
        this.rotations.set(0, x);
    }

    public void setYRotation(int y) {
        this.rotations.set(1, y);
    }

    public void setZRotation(int z) {
        this.rotations.set(2, z);
    }

    public int getXRotation() {
        return rotations.get(0);
    }

    public int getYRotation() {
        return rotations.get(1);
    }

    public int getZRotation() {
        return rotations.get(2);
    }

    public void setXMove(double x) {
        this.move.set(0, x);
    }

    public void setYMove(double y) {
        this.move.set(1, y);
    }

    public void setZMove(double z) {
        this.move.set(2, z);
    }

    public double getXMove() {
        return move.get(0);
    }

    public double getYMove() {
        return move.get(1);
    }

    public double getZMove() {
        return move.get(2);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }
}
