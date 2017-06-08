package hse.bsptree;


import hse.objects.Object3D;
import hse.objects.Side;

import java.util.List;
import java.util.Map;

/**
 * Created by yuriy on 03.05.17.
 */
public class BspTree {

    public static BspTree bspTree = new BspTree();

    public static BspTree getInstance() {
        return bspTree;
    }

    BspNode node;


    public void insert(Map<Object3D, List<Side>> sides, SideLocation keep, SideLocation current) {
        if (sides.isEmpty()) {
            return;
        }

        if(node != null) {
            node.insert(sides, keep);
        } else {
            if(current == keep || keep == SideLocation.SPANNING) {
                node = new BspNode();
                node.insert(sides, keep);
            }
        }
    }


    public int getSide(Map<Object3D, List<Side>> sides) {
        return (int)(Math.random() * sides.size());
    }

    public void draw() {
        if(node != null) {
            node.draw();
        }
    }

    public void clear() {
        node = null;
        System.out.println("BSP_TREE clear");
    }
}
