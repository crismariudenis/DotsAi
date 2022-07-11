
package com.example;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet {

    public static PApplet p;

    SmartPopulation test;
    public static Obstacle[] walls;
    public static int[] nnShape = new int[]{8, 8, 8, 4};
    //Todo: Search for a better shape

    //Todo:Check if the dots move as intended

    //    public static int[] nnShape = new int[]{3,1};
//    public static PVector goal =
    public static PVector goal = new PVector(400, 100);
    public static PVector goalMove = new PVector(1, -1);

    public static final int nrObstacles = 0;

    public void setup() {
        p = this;
        /* size commented out by preprocessor */
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
        String S= String.valueOf(s);
        text(S, 5, height-20);
    }

    public void draw() {

//        goal.x=mouseX;
//        goal.y=mouseY;

        if (goal.x + goalMove.x > width - 10 || goal.x + goalMove.x < 10)
            goalMove.x *= -1;
        if (goal.y + goalMove.y > height - 10 || goal.y + goalMove.x < 10)
            goalMove.y *= -1;
        goal.add(goalMove);
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
        size(750, 750);
    }

    static public void main(String[] args) {
        PApplet.main("com.example.Main", args);
    }
}