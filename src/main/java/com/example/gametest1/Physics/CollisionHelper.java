package com.example.gametest1.Physics;

import com.example.gametest1.GameObjects.BallsPair;
import com.example.gametest1.GameObjects.SimBall;
import com.example.gametest1.GameObjects.VelocityPair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CollisionHelper {

    public static boolean areCirclesColliding(BallsPair pair){
        return areCirclesColliding(pair.a().getPosition(), pair.a().getRadius(), pair.b().getPosition(), pair.b().getRadius());
    }

    public static boolean areCirclesColliding(Vec2 c1, double r1, Vec2 c2, double r2){
        return Math.sqrt(Math.abs(
                Math.pow(c1.getX() - c2.getX(), 2) +
                Math.pow(c1.getY() - c2.getY(), 2)
                )) <= r1 + r2;
    }

    public static VelocityPair calculateNewVelocities(Vec2 c1, Vec2 v1, double m1,
                                            Vec2 c2, Vec2 v2, double m2){

        Vec2 relVel = v1.substract(v2);
        Vec2 centVec = c1.substract(c2);
        double dotProd = Vec2.dot(relVel, centVec);
        double lengthSqrt = centVec.lengthSquared();
        double massTerm = 2*m2 / (m1 + m2);

        double scalar = (massTerm * dotProd / lengthSqrt);
        Vec2 correctionVec = centVec.scale(scalar);

        double massTerm2 = 2*m1 / (m1 + m2);
        double scalar2 = (massTerm2 * dotProd / lengthSqrt);
        Vec2 correctionVec2 = centVec.scale(scalar2 * -1);

        Vec2 newV1 = v1.substract(correctionVec.scale(0.86));
        Vec2 newV2 = v2.substract(correctionVec2.scale(0.86));

        return new VelocityPair(newV1, newV2);
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

    public static List<BallsPair> findPossibleCollisions(List<SimBall> balls) {
        List<BallsPair> result = new ArrayList<>();

        findPossibleCollisions(balls, 0, result);

        return result;
    }

    private static void findPossibleCollisions(List<SimBall> balls, int depth, List<BallsPair> result) {
        if(depth == -1){
            result.addAll(generatePairs(balls));
            return;
        }

        if(balls.size() <= 1)
            return;

        int currentCount = balls.size();

        boolean splitOnX = depth % 2 == 0;

        balls.sort((a, b) -> splitOnX ?
            Double.compare(a.getPosition().getX(), b.getPosition().getX()) :
            Double.compare(a.getPosition().getY(), b.getPosition().getY()));

        SimBall mid = balls.get(balls.size() / 2);
        double median = splitOnX ?
                mid.getPosition().getX() :
                mid.getPosition().getY();

        List<SimBall> left = new ArrayList<SimBall>();
        List<SimBall> right = new ArrayList<SimBall>();

        for(SimBall ball : balls){
            double ballMin = splitOnX ?
                    ball.getPosition().getX() - ball.getRadius() :
                    ball.getPosition().getY() - ball.getRadius();

            double ballMax = splitOnX ?
                    ball.getPosition().getX() + ball.getRadius() :
                    ball.getPosition().getY() + ball.getRadius();

            if (ballMin <= median) left.add(ball);
            if (ballMax >= median) right.add(ball);
        }

        if(left.size() == currentCount)
            findPossibleCollisions(left, -1, result);
        else
            findPossibleCollisions(left, depth + 1, result);

        if(right.size() == currentCount)
            findPossibleCollisions(right, -1, result);
        else
            findPossibleCollisions(right, depth + 1, result);

    }

    private static List<BallsPair> generatePairs(List<SimBall> balls) {
        List<BallsPair> res = new ArrayList<>();

        for(int i = 0; i < balls.size(); i++){
            for(int j = i+1; j < balls.size(); j++){
                res.add(new BallsPair(balls.get(i), balls.get(j)));
            }
        }

        return res;
    }
}
