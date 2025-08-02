import com.example.gametest1.GameObjects.SimBall;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



import com.example.gametest1.Physics.Vec2;
import com.example.gametest1.Physics.CollisionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CollisionHelperTest {

    @Test
    void testTrueBaseCollisionDetection(){
        Vec2 c1 = new Vec2(1, 2);
        double r1 = 2;

        Vec2 c2 = new Vec2(1, 1.56);
        double r2 = .867;

        assertTrue(CollisionHelper.areCirclesColliding(c1, r1, c2, r2));
    }

    @Test
    void testFalseBaseCollisionDetection(){
        Vec2 c1 = new Vec2(1, 1);
        double r1 = 2;

        Vec2 c2 = new Vec2(5, 7.56);
        double r2 = .867;

        assertFalse(CollisionHelper.areCirclesColliding(c1, r1, c2, r2));
    }

    @Test
    void testTrueBaseCollisionRightOnTheEdge(){
        Vec2 c1 = new Vec2(1, 1);
        double r1 = 1;

        Vec2 c2 = new Vec2(3, 1);
        double r2 = 1;

        assertTrue(CollisionHelper.areCirclesColliding(c1, r1, c2, r2));
    }

    @Test
    void smth(){

        assertTrue(true);
    }

    @Test
    void calculateNewVelocityVectorAfterBoundaryBounce() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Vec2 center = new Vec2(15, 10);
        Vec2 pos = new Vec2(5, 3);
        Vec2 v = new Vec2(-1, -2);
        SimBall ball = new SimBall(pos, 1);
        ball.setVelocity(v);

        Method method = CollisionHelper.class.getDeclaredMethod(
                "calculateReflectionVelocityVector",
                SimBall.class,
                Vec2.class
        );

        method.setAccessible(true);

        Vec2 res = (Vec2) method.invoke(null, ball, center);
        Vec2 expected = new Vec2(2.2215765100671, 0.255033557047);

        assertTrue(expected.subtract(res).getMagnitude() < .1, "Expected: " + expected.toString() + "\nActual: " + res.toString());
    }

    @Test
    void calculateBallsBounceTest(){
        Vec2 c1 = new Vec2(4, 3);
        Vec2 v1 = new Vec2(1, 2);
        double m1 = 1;
        Vec2 c2 = new Vec2(7, 5);
        Vec2 v2 = new Vec2(-1, -1);
        double m2 = 2;

        Vec2 nv1 = CollisionHelper.calculateNewVelocity(c1, v1, m1, c2, v2, m2);

        Vec2 expected = new Vec2(-2.69, -.45);

        assertTrue(nv1.subtract(expected).getMagnitude() < .3, "Expected: " + expected.toString() + "\nActual: " + nv1.toString());
    }

    @Test
    void seperateBallsTest(){
        SimBall a = new SimBall(new Vec2(0, 0), 1, 1);
        SimBall b = new SimBall(new Vec2(1.5, 1.5), 1.5, 2);

        Vec2 aExp = new Vec2(-0.08925565098878965, -0.08925565098878965);
        Vec2 bExp = new Vec2(1.6785113019775793, 1.6785113019775793);
        CollisionHelper.seperateBalls(a, b);
        assertTrue(a.getPosition().subtract(aExp).getMagnitude() < .1 &&
                b.getPosition().subtract(bExp).getMagnitude() < .1);
    }
}
