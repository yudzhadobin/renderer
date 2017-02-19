package hse.objects;

import hse.TGALoader;
import hse.UvCoordinate;
import hse.matrixes.Matrix;
import hse.matrixes.Projections;
import hse.matrixes.conversations.Scale;
import hse.ui.SwapChain;
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
import java.util.function.Supplier;

/**
 * Created by Yura on 01.01.2017.
 */
public class Object3D {
    List<Point3D<Double>> localPoints;

    Map<Point3D<Double>,Pair<ChangeableSupplier<Boolean>, Point3D<Double>>> transformedPoints;

    List<Side> sides;


    List<Integer> rotations = new ArrayList<Integer>() {
        {
            add(0);
            add(0);
            add(0);
        }
    };
    List<Integer> move = new ArrayList<Integer>() {
        {
            add(0);
            add(0);
            add(0);
        }
    };

    
    Point3D position;
    BufferedImage texture;

    BoundingBox box = new BoundingBox();
    private int scale = 1;

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
                final String[] parts = s.replaceAll("\\s+", " ").trim().split(" ");

                switch (parts[0]) {
                    case "v":
                        Point3D<Double> point3D = new Point3D<>(
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3])
                        );

                        result.localPoints.add(point3D);
                        result.transformedPoints.put(
                                point3D, new Pair<> (
                                        new ChangeableSupplier<Boolean>(false),
                                        new Point3D<>(point3D)
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


    public List<Point3D<Double>> getLocalPoints() {
        return localPoints;
    }

    public Map<Point3D<Double>, Pair<ChangeableSupplier<Boolean>, Point3D<Double>>> getTransformedPoints() {
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


    public void setXMove(int x) {
        this.move.set(0, x);
    }

    public void setYMove(int y) {
        this.move.set(1, y);
    }

    public void setZMove(int z) {
        this.move.set(2, z);
    }

    public int getXMove() {
        return move.get(0);
    }

    public int getYMove() {
        return move.get(1);
    }

    public int getZMove() {
        return move.get(2);
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }
}
