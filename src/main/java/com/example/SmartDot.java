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
            fitness = 1.0f / 16.0f + 10000.0f / (float) (nn.step*nn.step);
        } else {
            float distanceToGoal = dist(pos.x, pos.y, Main.goal.x, Main.goal.y);
            //Todo : use the time spent around the target as an important factor
            fitness = 50.0f / (distanceToGoal * distanceToGoal);
        }
    }

    public void update() {
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

    public SmartDot givemeBaby(SmartDot p2) {
        ///-----------------Can merge multiple parent for more complex projects-----------------
        SmartDot baby = new SmartDot();
        if(!this.isBest) {
            baby.nn = nn.merge(p2.nn);
        }
        else
            baby.nn=nn.clone();
        return baby;
    }

    public void move() {
        PVector acc = new PVector(0, 0);
        if (nn.maxNrStep > nn.step) {
            ArrayList<Float> ans = nn.process(pos, vel, Main.goal);
            float up = ans.get(0);
            float down = ans.get(1);
            float right = ans.get(2);
            float left = ans.get(3);
            nn.step++;
            int x, y;
            if (up > down) x = -1;
            else x = 1;
            if (right > left) y = 1;
            else y = -1;
            if (up == down) y = 0;
            if (right == left) x = 0;
            acc = new PVector(x, y);
        } else
            dead = true;
        vel.add(acc);
        vel.limit(5);
        pos.add(vel);
//        System.out.println(acc);
    }
}
