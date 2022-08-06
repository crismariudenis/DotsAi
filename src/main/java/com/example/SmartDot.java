package com.example;

import processing.core.PVector;

import java.util.ArrayList;

import static com.example.Main.*;
import static com.example.Main.p;

class SmartDot {
    PVector pos;
    PVector vel;
    PVector acc;
    NeuralNetwork nn;
    float closeToGoalPoints = 0.0f;
    float timesCloseToGoal = 0;

    int diameter = 4;
    boolean dead = false;
    float fitness = 0.0f;
    boolean reachedGoal = false;
    boolean isBest = false;

    SmartDot() {
        pos = new PVector(p.width / 2, p.height - 10);
        vel = new PVector(0, 0);
        acc = new PVector(0, 0);
        nn = new NeuralNetwork(Main.nnShape);
    }

    public void show() {
        if (dead || reachedGoal)
            return;
        if (isBest) {
            p.fill(0, 255, 0);
            p.ellipse(pos.x, pos.y, 2 * diameter, 2 * diameter);
        } else {
            p.fill(0);
            p.ellipse(pos.x, pos.y, diameter, diameter);
        }
    }

    public void calculateFitness() {
        if (reachedGoal) {
            fitness = 5+closeToGoalPoints/100;
            SmartPopulation.dotsReachedGoals++;
        } else
            fitness = 0.1f + closeToGoalPoints;
    }

    public void update() {
        if (dist(pos.x, pos.y, goal.pos.x, goal.pos.y) <= 100 && !dead) {
            timesCloseToGoal += 1f;
            closeToGoalPoints+=timesCloseToGoal;
        } else
            timesCloseToGoal = 0;

        if (!dead && !reachedGoal) {
            move();
            //hits wall
            if (min(pos.x, pos.y) < diameter / 2 || pos.x > p.width - diameter / 2 || pos.y > p.height - diameter / 2) {
                dead = true;
                return;
            }

            //hits goal
            if (dist(pos.x, pos.y, goal.pos.x, goal.pos.y) < 5)
                reachedGoal = true;
        }
    }

    public SmartDot giveBaby(SmartDot p2, boolean isBest) {
        SmartDot baby = new SmartDot();

        //if isn't best merge two parents
        if (!isBest)
            baby.nn = merge(nn, p2.nn);
        else
            baby.nn = nn.clone();
        return baby;
    }

    public void move() {
        PVector acc = new PVector(0, 0);
        if (nn.maxNrStep > nn.step) {


            ArrayList<Float> ans = nn.process(pos, vel);
            nn.step++;

            //get output nodes
            float up = ans.get(0);
            float down = ans.get(1);
            float right = ans.get(2);
            float left = ans.get(3);

            //interpret output in range with {-1,0,1}
            int x = Float.compare(down, up);
            int y = Float.compare(right, left);

            acc = new PVector(x, y);
        } else
            dead = true;
        vel.add(acc);
        vel.limit(5);
        pos.add(vel);
    }

    //dummy function to make the code look cleaner
    private NeuralNetwork merge(NeuralNetwork a, NeuralNetwork b) {
        return a.merge(b);
    }
}
