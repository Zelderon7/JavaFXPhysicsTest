package com.example.gametest1.Physics;

import com.example.gametest1.GameObjects.SimBall;

import java.lang.reflect.Method;

public class CollisionHelper {
    public static boolean areCirclesColliding(Vec2 c1, double r1, Vec2 c2, double r2){
        return Math.sqrt(Math.abs(
                Math.pow(c1.getX() - c2.getX(), 2) +
                Math.pow(c1.getY() - c2.getY(), 2)
                )) <= r1 + r2;
    }

    public static Vec2 calculateNewVelocity(Vec2 c1, Vec2 v1, double m1,
                                            Vec2 c2, Vec2 v2, double m2){

        Vec2 relVel = v1.substract(v2);
        Vec2 centVec = c1.substract(c2);
        double dotProd = Vec2.dot(relVel, centVec);
        double lengthSqrt = centVec.lengthSquared();
        double massTerm = 2*m2 / (m1 + m2);

        double scalar = (massTerm * dotProd / lengthSqrt);
        Vec2 correctionVec = centVec.scale(scalar);

        Vec2 res = v1.substract(correctionVec);
        return res;
    }


    public static void resolveCollisionWithArena(SimBall ball, Vec2 center, double R) {

        moveBallBackToArena(ball, center, R);

        Vec2 newVec = calculateReflectionVelocityVector(ball, center);
        ball.setVelocity(newVec);
    }

    private static void moveBallBackToArena(SimBall ball, Vec2 center, double r) {
        Vec2 dir = ball.getPosition().substract(center);
        double scalar = r - ball.getRadius();
        Vec2 newPos = center.add(dir.getNormalized().scale(scalar));
        ball.setPosition(newPos);
    }

    private static Vec2 calculateReflectionVelocityVector(SimBall ball, Vec2 center) {
        Vec2 v = ball.getVelocity();
        Vec2 mirrorVector = center.substract(ball.getPosition());
        double scalar = 2 * Vec2.dot(v.scale(-1), mirrorVector) /
                mirrorVector.lengthSquared();
        Vec2 newVec = mirrorVector.scale(scalar).add(v);

        return newVec;
    }


    public static void seperateBalls(SimBall a, SimBall b) {
        Vec2 lineOfImpact = b.getPosition().substract(a.getPosition());
        lineOfImpact = lineOfImpact.getNormalized().scale(a.getRadius() + b.getRadius() - lineOfImpact.getMagnitude());
        double masses = a.getMass() + b.getMass();
        a.setPosition(a.getPosition().substract(lineOfImpact.scale(1/masses*a.getMass())));
        b.setPosition(b.getPosition().add(lineOfImpact.scale(1/masses*b.getMass())));
    }
}
