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
                final String[] parts = s.replaceAll("\\s+", " ").split(" ");

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


                            pointInfos.add(new PointInfo(
                                    result.transformedPoints.get(
                                            result.localPoints.get(Integer.parseInt(infos[0]) - 1)
                                    ).getValue(),
                                    uvCoordinates.get(Integer.parseInt(infos[1]) - 1),
                                    normals.get(Integer.parseInt(infos[2]) - 1),
                                    Integer.parseInt(infos[0]) - 1
                            ));
                        }
                        result.sides.add(new Side(pointInfos));
//                        //заполняем грани
//                        result.sides.addAll(Side.create(
//                                new ArrayList<Integer>() {
//                                    {
//                                        for (int i = 1; i < parts.length; i++) {
//                                            add(Integer.parseInt(parts[i].split("/")[0]) - 1);
//                                        }
//                                    }
//                                }
//                        ));
//
//                        UvCoordinate was = null;
//                        int index = -1;
//                        for (int i = 1; i < parts.length; i++) {
//                            if(result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).getUvCoordinate() != null) {
//                                 was = result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).getUvCoordinate();
//                                index = i;
//                            }
//                            result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).
//                                    setNormal(normals.get(Integer.parseInt(parts[i].split("/")[1])-1));
//                        }
//
//                        for (int i = 1; i < parts.length; i++) {
//                            result.localPoints.get(Integer.parseInt(parts[i].split("/")[0]) - 1).
//                                    setUvCoordinate(uvCoordinates.get(Integer.parseInt(parts[i].split("/")[1])-1));
//                        }
//                        if(index != -1) {
//                            if (!was.equals(result.localPoints.get(Integer.parseInt(parts[index].split("/")[0]) - 1).getUvCoordinate())) {
//                                int j = 5;
//                            }
//                        }

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

    @Deprecated()
    public void draw() {
        Matrix scale = new Scale();

        List<Point3D<Double>> transformedPoints = new ArrayList<>();

        for (int i = 0; i < localPoints.size(); i++) {
            transformedPoints.add(scale.multiple(localPoints.get(i)));
        }

        for (int i = 0; i < sides.size(); i++) {
            Side side = sides.get(i);
            side.drawContour(SwapChain.getInstance(), Projections.ORTHOGONAL);
        }
    //    SwapChain.getInstance().swap();
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
    }
}
