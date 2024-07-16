package org.by1337.blib.geom;

import junit.framework.TestCase;
import org.by1337.blib.util.Direction;
import org.junit.Assert;

public class AABBTest extends TestCase {

    public void testExpand() {
        AABB aabb = new AABB(-10, -10, -10, 10, 10, 10);
        AABB aabb1 = new AABB(-10, -15, -10, 10, 10, 10);

        aabb.expand(Direction.DOWN, 5);
        Assert.assertEquals(aabb, aabb1);

        aabb.expand(Direction.UP, 15);
        Assert.assertEquals(aabb, new AABB(-10, -15, -10, 10, 25, 10));

        aabb.expand(Direction.UP, -25);
        Assert.assertEquals(aabb, new AABB(-10, -15, -10, 10, 0, 10));
    }

    public void testGetAllPointsInAABB() {
        IntAABB aabb = new IntAABB(-10, -10, -10, -1, -1, -1);
        Assert.assertEquals(aabb.getAllPointsInAABB().size(), (aabb.getMaxX() - aabb.getMinX() + 1) * (aabb.getMaxY() - aabb.getMinY() + 1) * (aabb.getMaxZ() - aabb.getMinZ() + 1));
    }

    public void testIntersect() {
        AABB aabb = new AABB(50, 10, 50, 100, 100, 100);
        AABB aabb1 = new AABB(101, 10, 101, 150, 100, 150);
        Assert.assertFalse(aabb.intersect(aabb1));

        aabb1.expand(Direction.NORTH, 10);
        aabb1.expand(Direction.WEST, 10);
        System.out.println(aabb);
        System.out.println(aabb1);
        Assert.assertTrue(aabb.intersect(aabb1));
    }
}