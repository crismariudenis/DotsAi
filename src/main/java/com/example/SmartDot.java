package com.example;

import processing.core.PVector;

import java.util.ArrayList;

import static com.example.Main.*;
import static com.example.Main.p;

/**
 * @author Denis Crismariu
 */
public class SmartDot {
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

    /**
     * Constructor initializing values for
     * pos: the position
     * vel: the velocity
     * acc: the acceleration
     * nn: the neural network
     */
    SmartDot() {
        pos = new PVector(p.width / 2, p.height - 10);
        vel = new PVector(0, 0);
        acc = new PVector(0, 0);
        nn = new NeuralNetwork(Main.nnShape);
    }

    /**
     * Draws the dot if it is alive
     * Dot is green if it's the best from the previous generation
     */
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

    /**
     * Calculates the fitness of every dot
     */
    public void calculateFitness() {
        if (reachedGoal) {
            fitness = 5+closeToGoalPoints/100;
            SmartPopulation.dotsReachedGoals++;
        } else
            fitness = 0.1f + closeToGoalPoints;
    }

    /**
     * closeToGoalPoints becomes bigger with the time spent close to the goal
     * Calculates if the dot is dead, or if it reached the goal
     */
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

    /**
     * @param p2 the second parent
     * @return a combination of the two parents
     */
    public SmartDot giveBaby(SmartDot p2) {
        SmartDot baby = new SmartDot();
        baby.nn = merge(this.nn, p2.nn);
        return baby;
    }

    /**
     * This function is only called for the best dot from the previous generation
     * @return a clone of the parent
     */
    public SmartDot giveBaby(){
        SmartDot baby = new SmartDot();
        baby.nn=nn.clone();
        return baby;
    }

    /**
     * Moves the player based on the value calculated by the neural network
     */
    public void move() {
        PVector acc = new PVector(0, 0);
        if (nn.maxNrSteps > nn.step) {
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

    /**
     * Dummy function to make the code look cleaner
     * @param a network 1
     * @param b network 2
     * @return a new network with 50/50 values from a and b networks
     */

    private NeuralNetwork merge(NeuralNetwork a, NeuralNetwork b) {
        return a.merge(b);
    }
}
