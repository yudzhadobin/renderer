package hse.bsptree;

import hse.Setings;
import hse.Stage;
import hse.objects.Camera;
import hse.objects.Normal;
import hse.objects.Object3D;
import hse.objects.Side;
import hse.ui.SwapChain;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hse.Setings.drawingMode;

/**
 * Created by yuriy on 03.05.17.
 */
public class BspNode {

    Map<Object3D, List<Side>> on;
    BspTree in;
    BspTree out;

    Plane3D plane;


    public BspNode() {

        this.on = new HashMap<>();
        Stage.getInstance().getDisplayedObjects().forEach(
                object3D -> on.put(object3D, new ArrayList<>())
        );
        this.in = new BspTree();
        this.out = new BspTree();
    }

    public void insert(Map<Object3D, List<Side>> sides, SideLocation keep) {
        List<Sub> allSides = new ArrayList<>();

        for (Object3D object3D : sides.keySet()) {
            sides.get(object3D).forEach(
                    side -> allSides.add(new Sub(side, object3D))
            );
        }

        if(!allSides.isEmpty()) {
            this.plane = allSides.get((int)(Math.random() * allSides.size())).side.getPlane();
        }
        if(allSides.size() < 1) {
            allSides.forEach(sub -> {
                on.get(sub.object3D).add(sub.side);
            });
            return;
        }



        Map<Object3D, List<Side>> in = new HashMap<>();
        Map<Object3D, List<Side>> out = new HashMap<>();

        Stage.getInstance().getDisplayedObjects().forEach(
                object3D -> in.put(object3D, new ArrayList<>())
        );
        Stage.getInstance().getDisplayedObjects().forEach(
                object3D -> out.put(object3D, new ArrayList<>())
        );
        for (Sub sub: allSides) {
            Pair<SideLocation, Pair<List<Side>, List<Side>>> split = plane.split(sub.side);

            if(split.getKey() == SideLocation.ON) {
                on.get(sub.object3D).add(sub.side);
            } else {
                if(split.getKey() == SideLocation.IN || split.getKey() == SideLocation.SPANNING) {
                    in.get(sub.object3D).addAll(split.getValue().getKey());
                }
                if(split.getKey() == SideLocation.OUT || split.getKey() == SideLocation.SPANNING) {
                    out.get(sub.object3D).addAll(split.getValue().getValue());
                }
            }
        }
        if(!in.isEmpty()) {
            this.in.insert(in, keep, SideLocation.IN);
        }
        if(!out.isEmpty()) {
            this.out.insert(out, keep, SideLocation.OUT);
        }
    }

    public void draw() {
        if(plane == null) {
            return;
        }
        Normal eye = Camera.getInstance().eye;
        double sgn = plane.multiple(eye);
        if(sgn < 0.0) {
            out.draw();
            drawOnSides();
            in.draw();
        } else {
            in.draw();
            drawOnSides();
            out.draw();
        }
    }

    private class Sub {
        Side side;
        Object3D object3D;

        public Sub(Side side, Object3D object3D) {
            this.side = side;
            this.object3D = object3D;
        }
    }
    private void drawOnSides() {

        on.forEach(((object3D, sides) -> {
            sides.forEach(side -> {
                switch (drawingMode) {
                    case CONTOUR: {
                        side.drawContour(SwapChain.getInstance(), null, Setings.projection, object3D);
                        break;
                    }
                    case MODEL: {
                        side.drawFill(SwapChain.getInstance(), null, Setings.projection, object3D, Setings.light_on);
                        break;
                    }
                    case TEXTURED: {
                        side.drawTextured(SwapChain.getInstance(), null, Setings.projection, object3D, Setings.fillType, Setings.light_on);
                        break;
                    }
                }
            });
        }));
    }



}
