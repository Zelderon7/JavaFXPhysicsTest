package com.example.gametest1;

import com.example.gametest1.GameObjects.MyLine;
import com.example.gametest1.GameObjects.SimBall;
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
    private static final int BALL_COUNT = 10;
    private static final double ARENA_RADIUS = 350;
    private static final Vec2 CENTER = new Vec2(WIDTH / 2, HEIGHT / 2);
    private static Random random = new Random();

    // JavaFX root node
    private Pane root;

    // Ball data structure (custom class you’ll define)
    private List<SimBall> balls = new ArrayList<>();

    long lastTime = 0;

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

    private void initBalls() {
        for (int i = 0; i < BALL_COUNT; i++) {
            // You’ll define SimBall class to manage position, velocity, and JavaFX node
            double gap = (double) WIDTH / (BALL_COUNT + 1);
            SimBall ball = new SimBall((i+1)* gap, HEIGHT / 2.0, 10);
            ball.setColor(getRandomPredefinedColor());
            ball.setVelocity(new Vec2(random.nextDouble() - .5, random.nextDouble() -.5).scale(300));

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

                moveBalls(deltaTime);
                updatePhysics();
                updateVisuals();
            }
        };
        timer.start();
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
        for(int i = 0; i < balls.size(); i++){
            SimBall ballI = balls.get(i);
            //Check if hits container
            if(ballI.getPosition().substract(CENTER).getMagnitude() >= ARENA_RADIUS - ballI.getRadius()){
                createLine(ballI);
                CollisionHelper.resolveCollisionWithArena(ballI, CENTER, ARENA_RADIUS);
            }

            for(int j = i+1;  j < balls.size(); j++){
                SimBall ballJ = balls.get(j);


                if(CollisionHelper.areCirclesColliding(ballI.getPosition(), ballI.getRadius(), ballJ.getPosition(), ballJ.getRadius())){
                    //Seperate balls
                    CollisionHelper.seperateBalls(ballI, ballJ);

                    //calculate new velocities
                    Vec2 newVelI = CollisionHelper.calculateNewVelocity(ballI.getPosition(), ballI.getVelocity(), ballI.getMass(), ballJ.getPosition(), ballJ.getVelocity(), ballJ.getMass());
                    Vec2 newVelJ = CollisionHelper.calculateNewVelocity(ballJ.getPosition(), ballJ.getVelocity(), ballJ.getMass(), ballI.getPosition(), ballI.getVelocity(), ballI.getMass());

                    //apply them
                    ballI.setVelocity(newVelI);
                    ballJ.setVelocity(newVelJ);
                }
            }
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