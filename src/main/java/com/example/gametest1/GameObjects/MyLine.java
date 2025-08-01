package com.example.gametest1.GameObjects;

import com.example.gametest1.Physics.Vec2;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MyLine {

    private Line node;
    private Vec2 anchor;
    private Vec2 destination;

    public MyLine(Vec2 anchor, Vec2 destination, Color color) {
        node = new Line();
        node.setStroke(color);
        node.setStrokeWidth(1);

        setAnchor(anchor);
        setDestination(destination);
    }

    private void setAnchor(Vec2 anchor) {
        this.anchor = anchor;
        node.setStartX(anchor.getX());
        node.setStartY(anchor.getY());
    }

    public void setDestination(Vec2 position) {
        this.destination = position;
        node.setEndX(position.getX());
        node.setEndY(position.getY());
    }

    public Line getNode(){
        return node;
    }
}
