package com.example.gametest1.GameObjects;

import javafx.scene.layout.Pane;

public interface IArena {
    public void draw(Pane root);
    public void handleCollisionWithBall(SimBall ball);
}
