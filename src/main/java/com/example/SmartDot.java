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
    float closeToGoal = 0.0f;
    int timesCloseToGoal = 0;

    int d = 4;
    boolean dead = false;
    float fitness = 0.0f;
    boolean reachedGoal = false;
    boolean isBest = false;

    SmartDot() {
        pos = new PVector(Main.p.width / 2, Main.p.height - 10);
        vel = new PVector(0, 0);
        acc = new PVector(0, 0);
        nn = new NeuralNetwork(Main.nnShape);
    }

    public void show() {
        if (isBest) {
            Main.p.fill(0, 255, 0);
            Main.p.ellipse(pos.x, pos.y, 2 * d, 2 * d);
        } else {
            Main.p.fill(0);
            Main.p.ellipse(pos.x, pos.y, d, d);
        }
    }

    public void caculateFitness() {
        if (reachedGoal) {
            fitness = 1.1f*closeToGoal+5f;
        } else {
            fitness = 0.1f + 1f * closeToGoal;
        }
//        System.out.println(fitness );
    }

    public void update() {
        if (dist(pos.x, pos.y, goal.x, goal.y) <= 30 && !dead) {
            timesCloseToGoal++;
            closeToGoal += timesCloseToGoal;
        } else
            timesCloseToGoal = 0;

        if (!dead && !reachedGoal) {
            move();
            //hits wall
            if (min(pos.x, pos.y) < d / 2 || pos.x > Main.p.width - d / 2 || pos.y > Main.p.height - d / 2) {
                dead = true;
                return;
            }
            //hits obstacles
            for (int i = 0; i < Main.nrObstacles; i++)
                if (Main.walls[i].hit(this)) {
                    dead = true;
                    return;
                }
            //hits goal

            if (dist(pos.x, pos.y, Main.goal.x, Main.goal.y) < 5)
                reachedGoal = true;

        }
    }

    public SmartDot givemeBaby(SmartDot p2, boolean isBest) {
        ///-----------------Can merge multiple parent for more complex projects-----------------
        SmartDot baby = new SmartDot();


        if (!isBest)
            baby.nn = nn.merge(p2.nn);
//            baby.nn=nn.clone();
        else {
            baby.nn = nn.clone();
        }
        return baby;
    }

    public void move() {
        PVector acc = new PVector(0, 0);
        if (nn.maxNrStep > nn.step) {
            ArrayList<Float> ans = nn.process(pos, vel);
            nn.step++;
            //Interpret ans
            float up = ans.get(0);
            float down = ans.get(1);
            float right = ans.get(2);
            float left = ans.get(3);
            int x, y;
            x = up > down ? -1 : 1;
            y = right > left ? 1 : -1;
            if (up == down) y = 0;
            if (right == left) x = 0;
            acc = new PVector(x, y);
        } else
            dead = true;
        vel.add(acc);
        vel.limit(5);
        pos.add(vel);
    }
}
