
package com.example;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet {

    public static PApplet p;

    SmartPopulation test;
    public static Obstacle[] walls;
    public static int[] nnShape = new int[]{4, 8, 4};
    //Todo: Generate a better shape
    public static PVector goal = new PVector(400, 100);
    public static PVector goalVel = new PVector(1, -1);

    public static final int nrObstacles = 0;

    public void setup() {
        p = this;
        frameRate(120);
        test = new SmartPopulation(2000);
        // walls =new Obstacle[2];
        //walls[0]=new Obstacle(400, 300, 600, 10);
        //walls[1]=new Obstacle(0, 450, 600, 10);
    }

    public void menu() {
        textSize(30);
        text("Generations: " + test.gen, 5, height - 70);
        StringBuilder s = new StringBuilder("NN shape: ");
        for (int x : nnShape)
            s.append(x).append(" ");
        String S = String.valueOf(s);
        text(S, 5, height - 20);
        text("Best fitness: " + SmartPopulation.maxFitness, 5, height - 120);
    }

    public void draw() {

        //Todo: Make multiple movements like curvature and mouse / GENERATE THE SHAPE AT THE BEGINNING

        if (goal.x + goalVel.x > width - 10 || goal.x + goalVel.x < 10)
            goalVel.x *= -1;
        if (goal.y + goalVel.y > height - 10 || goal.y + goalVel.x < 10)
            goalVel.y *= -1;
        goal.add(goalVel);
        background(200);
        fill(255, 0, 0);
        ellipse(goal.x, goal.y, 10, 10);
        menu();

        for (int i = 0; i < nrObstacles; i++)
            walls[i].show();

        if (test.allDotsDead()) {
            test.calculateFitness();
            test.naturalSelection();
            test.mutateBabies();
        } else {
            test.update();
            test.show();
        }
    }

    public void settings() {
        size(900, 900);
    }

    static public void main(String[] args) {
        PApplet.main("com.example.Main", args);
    }
}