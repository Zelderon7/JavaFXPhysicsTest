package com.example.gametest1.GameObjects;

import com.example.gametest1.Physics.Vec2;
import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class SimBall {
    private Circle node;
    private double mass;
    private Color color;
    private double radius;
    private Vec2 position;
    private Vec2 velocity;

    private ArrayList<MyLine> lines = new ArrayList<MyLine>();

    public SimBall(Vec2 position, double r){
        this(position, r, 1);
    }

    public SimBall(Vec2 position, double r, double mass){
        this(position.getX(), position.getY(), r, mass);
    }
    public  SimBall(double x, double y, double r){
        this(x, y, r,1);
    }

    public SimBall(double x, double y, double r, double mass){
        this(x, y, r, Color.WHITE, mass);
    }

    public SimBall(double x, double y, double r, Color color, double mass) {

        node = new Circle(10, Color.BLUE); // or random size/color
        position = new Vec2(x, y);
        velocity = Vec2.Zero();
        setRadius(r);
        setColor(color);
        setMass(mass);
        updateNodePosition();
    }

    public void applyVelocity(double dt) {
        // Apply velocity to position based on deltaTime(dt)
        position = position.add(velocity.scale(dt));
        // Later: check boundary, collisions, etc.
    }

    public void updateNodePosition() {
        node.setLayoutX(position.getX());
        node.setLayoutY(position.getY());

        updateLines();
    }

    private void updateLines() {
        for (MyLine line : lines) {
            line.setDestination(getPosition());
        }
    }

    public Circle getNode() {
        return node;
    }

    // Add getters, setters, or collision helpers as needed

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.node.setFill(color);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.node.setRadius(radius);
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public void addLine(MyLine line) {
        for (int i = 0; i < lines.size(); i++){
            MyLine l = lines.get(i);
            // Reduce opacity by 10%
            double newOpacity = l.getNode().getOpacity() - 0.10; // Clamp to 0
            if(newOpacity <= 0){
                lines.remove(l);
                continue;
            }

            l.getNode().setOpacity(newOpacity);
        }
        lines.add(line);
    }
}

