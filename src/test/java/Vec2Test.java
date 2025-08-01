import com.example.gametest1.Physics.Vec2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vec2Test {

    @Test
    void addition(){
        Vec2 v1 = new Vec2(1, 2);
        Vec2 v2 = new Vec2(3, -1);
        Vec2 expected = new Vec2(4, 1);
        Vec2 result = v1.add(v2);
        assertEquals(expected, result);
    }

    @Test
    void subtraction(){
        Vec2 v1 = new Vec2(1, 2);
        Vec2 v2 = new Vec2(3, -1);
        Vec2 expected = new Vec2(-2, 3);
        Vec2 result = v1.substract(v2);
        assertEquals(expected, result);
    }

    @Test
    void multiplication(){
        Vec2 v1 = new Vec2(1, 2);
        Vec2 v2 = new Vec2(3, -1);
        Vec2 expected = new Vec2(3, -2);
        Vec2 result = v1.multiply(v2);
        assertEquals(expected, result);
    }

    @Test
    void dotProduction(){
        Vec2 v1 = new Vec2(1, 2);
        Vec2 v2 = new Vec2(3, -1);
        double expected = 1;
        double result = Vec2.dot(v1, v2);
        assertEquals(expected, result);
    }
}
