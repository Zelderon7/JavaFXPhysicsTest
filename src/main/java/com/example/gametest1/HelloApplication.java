package com.example.gametest1;

import com.example.gametest1.GameObjects.BallsPair;
import com.example.gametest1.GameObjects.MyLine;
import com.example.gametest1.GameObjects.SimBall;
import com.example.gametest1.GameObjects.VelocityPair;
import com.example.gametest1.Physics.CollisionHelper;
import com.example.gametest1.Physics.Vec2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {


    // Constants for simulation
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int BALL_COUNT = 300;
    private static final double BALL_RADIUS_MIN = 2;
    private static final double BALL_RADIUS_MAX = 8;
    private static final double BALL_SPEED_MIN = 8;
    private static final double BALL_SPEED_MAX = 50;
    private static final double ARENA_RADIUS = 350;
    private static final double GRAVITY_FORCE = 10;
    private static final Vec2 CENTER = new Vec2(WIDTH / 2, HEIGHT / 2);
    private static Random random = new Random();
    private static boolean DRAW_LINES = false;

    // JavaFX root node
    private Pane root;

    // Ball data structure (custom class you’ll define)
    private List<SimBall> balls = new ArrayList<>();

    long lastTime = 0;
    private Vec2 gravity = new Vec2(0, 1).scale(GRAVITY_FORCE);

    @Override
    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);
        stage.setTitle("Ball Simulation");
        stage.setScene(scene);
        stage.show();

        // Set up arena boundary (optional, for visuals)
        drawArena();

        // Create balls and add to scene
        initBalls();
        //testInit();
        //testInit2();

        // Start simulation loop
        startSimulation();
    }

    private void drawArena() {
        Circle boundary = new Circle(WIDTH / 2.0, HEIGHT / 2.0, ARENA_RADIUS);
        boundary.setStroke(Color.GRAY);
        boundary.setFill(Color.TRANSPARENT);
        root.getChildren().add(boundary);
    }

    //<editor-fold desc="Testing Methods">
    private void testInit(){
        SimBall ball = new SimBall(400, 720, 20, Color.YELLOW, 1);
        ball.setVelocity(new Vec2(60, -2).scale(10));
        balls.add(ball);
        root.getChildren().add(ball.getNode());
    }

    private void testInit2(){
        SimBall ball = new SimBall(200, 400, 20, Color.WHITE, 1);
        ball.setVelocity(new Vec2(100, 0));
        SimBall ball2 = new SimBall(600, 400, 20, Color.WHITE, 1);
        ball2.setVelocity(new Vec2(-100, 0));

        balls.add(ball);
        balls.add(ball2);
        root.getChildren().add(ball.getNode());
        root.getChildren().add(ball2.getNode());
    }

    //</editor-fold>

    private void initBalls() {

        double gap = (double) WIDTH / (BALL_COUNT + 1);
        for (int i = 0; i < BALL_COUNT; i++) {
            double radius = random.nextDouble(BALL_RADIUS_MAX - BALL_RADIUS_MIN) + BALL_RADIUS_MIN;
            double speed = random.nextDouble(BALL_SPEED_MAX - BALL_SPEED_MIN) + BALL_SPEED_MIN;
            SimBall ball = new SimBall((i+1)* gap, HEIGHT / 2.0, radius, Color.YELLOW, radius);
            ball.setColor(getRandomPredefinedColor());
            ball.setVelocity(new Vec2(random.nextDouble() - .5, random.nextDouble() -.5).getNormalized().scale(speed));

            balls.add(ball);
            root.getChildren().add(ball.getNode());
        }
    }

    private Color getRandomPredefinedColor() {
        List<Color> colors = new ArrayList<>();
        for (Field field : Color.class.getFields()) {
            if (field.getType() == Color.class) {
                try {
                    colors.add((Color) field.get(null));
                } catch (IllegalAccessException ignored) {}
            }
        }

        return colors.get(new Random().nextInt(colors.size()));
    }

    private void startSimulation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                if(deltaTime < (double) 1 /60){//Max fps (60)
                    return;
                }

                lastTime = now;

                applyGravity(deltaTime);
                moveBalls(deltaTime);
                updatePhysics();
                updateVisuals();
            }
        };
        timer.start();
    }

    private void applyGravity(double deltaTime) {
        for (SimBall ball : balls){
            ball.setVelocity(ball.getVelocity().add(gravity.scale(deltaTime)));
        }
    }

    private void updateVisuals() {
        for (SimBall ball : balls){
            ball.updateNodePosition();
        }
    }

    private void moveBalls(double dt) {
        for(SimBall ball : balls){
            ball.applyVelocity(dt);
        }
    }

    private void updatePhysics() {

        keepBallsInsideOfArena();

        //Use spacial partitioning to find possible collisions
        List<BallsPair> possibleCollisions = CollisionHelper.findPossibleCollisions(balls);
        for(int i = 0; i < 5; i++){
            if(handleCollisions(possibleCollisions))
                break;
        }
    }

    private boolean handleCollisions(List<BallsPair> possibleCollisions) {
        boolean flag = true;
        for(BallsPair pair : possibleCollisions){
            if(CollisionHelper.areCirclesColliding(pair)){

                VelocityPair velocities = CollisionHelper.calculateNewVelocities(pair.a().getPosition(), pair.a().getVelocity(), pair.a().getMass(), pair.b().getPosition(), pair.b().getVelocity(), pair.b().getMass());
                pair.a().setVelocity(velocities.v1New);
                pair.b().setVelocity(velocities.v2New);

                CollisionHelper.seperateBalls(pair.a(), pair.b());
                flag = false;
            }
        }
        return flag;
    }

    private void keepBallsInsideOfArena() {
        for (SimBall ball : balls){
            if(ball.getPosition().substract(CENTER).getMagnitude() >= ARENA_RADIUS - ball.getRadius())
                CollisionHelper.resolveCollisionWithArena(ball, CENTER, ARENA_RADIUS);
        }
    }

    private void createLine(SimBall ballI) {
        Vec2 anc = ballI.getPosition().substract(CENTER).getNormalized().scale(ARENA_RADIUS).add(CENTER);
        MyLine line = new MyLine(anc, ballI.getPosition(), ballI.getColor());
        ballI.addLine(line);
        root.getChildren().add(line.getNode());
    }

    public static void main(String[] args) {
        launch();
    }
}