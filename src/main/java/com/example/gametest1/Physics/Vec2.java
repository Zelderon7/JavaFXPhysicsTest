package com.example.gametest1.Physics;

public class Vec2 {
    public static double dot(Vec2 v1, Vec2 v2) {
        return v1.x * v2.x +
                v1.y * v2.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double x;
    private double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getMagnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 scale(double scalar){
        return new Vec2(x * scalar, y * scalar);
    }

    public Vec2 add(Vec2 addend){
        return new Vec2(x + addend.x, y + addend.y);
    }

    public Vec2 multiply(Vec2 addend){
        return new Vec2(x * addend.x, y * addend.y);
    }

    public Vec2 getNormalized(){
        double mag = getMagnitude();
        return mag == 0 ? new Vec2(0, 0) : new Vec2(x / mag, y / mag);
    }

    public static Vec2 Zero(){
        return  new Vec2(0, 0);
    }

    public Vec2 subtract(Vec2 v2) {
        return new Vec2(x - v2.getX(), y - v2.getY());
    }

    public double lengthSquared(){
        return x * x + y * y;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vec2){
            return x == ((Vec2)obj).x && y == ((Vec2)obj).y;
        }
        return false;
    }
}
