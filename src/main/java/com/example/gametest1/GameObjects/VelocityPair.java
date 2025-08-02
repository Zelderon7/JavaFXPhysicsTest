package com.example.gametest1.GameObjects;

import com.example.gametest1.Physics.Vec2;

public class VelocityPair {
    public final Vec2 v1New;
    public final Vec2 v2New;

    public VelocityPair(Vec2 v1New, Vec2 v2New) {
        this.v1New = v1New;
        this.v2New = v2New;
    }
}
