package com.example.gametest1.GameObjects;

import com.example.gametest1.Physics.CollisionHelper;
import com.example.gametest1.Physics.Vec2;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ArenaCircle implements IArena {
    private static final double WALL_RESTITUTION = .2;
    private Vec2 center;
    private double radius;

    public ArenaCircle(Vec2 center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void draw(Pane root) {
        Circle boundary = new Circle(center.getX(), center.getY(), radius);
        boundary.setStroke(Color.GRAY);
        boundary.setFill(Color.TRANSPARENT);
        root.getChildren().add(boundary);
    }

    @Override
    public void handleCollisionWithBall(SimBall ball) {

        if(ball.getPosition().subtract(center).getMagnitude() < radius - ball.getRadius())
            return;

        moveBallBackToArena(ball);

        Vec2 newVec = calculateReflectionVelocityVector(ball);
        ball.setVelocity(newVec);
    }


    private void moveBallBackToArena(SimBall ball) {
        Vec2 dir = ball.getPosition().subtract(center);
        double scalar = radius - ball.getRadius();
        Vec2 newPos = center.add(dir.getNormalized().scale(scalar));
        ball.setPosition(newPos);
    }

    private Vec2 calculateReflectionVelocityVector(SimBall ball) {
        Vec2 v = ball.getVelocity();
        Vec2 normal = ball.getPosition().subtract(center).getNormalized();//A vector from Center to Ball
        double vDotN = Vec2.dot(v, normal);//the amount of the velocity aligned with the normal (directly towards the wall)
        Vec2 velocityByNormal = normal.scale(vDotN);//That part of the velocity
        Vec2 directionalVelocity = v.subtract(velocityByNormal);//The remaining of the velocity, perpendicular to the normal
        Vec2 newVec = velocityByNormal.scale(-1*WALL_RESTITUTION).add(directionalVelocity);//Flip the velocity to go towards the center, preserving sideways direction
        return newVec;
    }

    public Vec2 getCenter() {
        return center;
    }

    public void setCenter(Vec2 center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
