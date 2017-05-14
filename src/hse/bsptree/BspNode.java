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
import java.util.List;

import static hse.Setings.drawingMode;

/**
 * Created by yuriy on 03.05.17.
 */
public class BspNode {

    List<Side> on;
    BspTree in;
    BspTree out;

    Plane3D plane;


    public BspNode(Plane3D plane) {
        this.plane = plane;

        this.on = new ArrayList<>();
        this.in = new BspTree();
        this.out = new BspTree();
    }

    public BspNode(Side side) {
        this.plane = side.getPlane();

        this.on = new ArrayList<>();
//        this.on.add(side);
        this.in = new BspTree();
        this.out = new BspTree();
    }

    public void insert(List<Side> sides, SideLocation keep) {
        if(sides.size() < 3) {
            on.addAll(sides);
            return;
        }

        List<Side> in = new ArrayList<>();
        List<Side> out = new ArrayList<>();

        for (Side side : sides) {
            Pair<SideLocation, Pair<List<Side>, List<Side>>> split = plane.split(side);

            if(split.getKey() == SideLocation.ON) {
                on.add(side);
            } else {
                if(split.getKey() == SideLocation.IN || split.getKey() == SideLocation.SPANNING) {
                    in.addAll(split.getValue().getKey());
                }
                if(split.getKey() == SideLocation.OUT || split.getKey() == SideLocation.SPANNING) {
                    out.addAll(split.getValue().getValue());
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


    private void drawOnSides() {

        // TODO: 14.05.17  
        on.stream().forEach(side -> {
            Object3D object3D = Stage.getInstance().getObject(0);
            switch (drawingMode) {
                case CONTOUR: {
                    side.drawContour(SwapChain.getInstance(),  null, Setings.projection, object3D);
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
    }



}
