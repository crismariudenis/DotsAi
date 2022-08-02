package com.example;

import processing.core.PVector;

import java.util.Objects;

import static com.example.Main.p;
import static java.lang.Math.max;

public class SmartPopulation {
    SmartDot[] dots;
    DisplayNetwork dn;
    float fitnessSum;
    int gen = 1;
    int bestDot = 0;
    int minStep = 400;
    static int maxFitness = 0;

    SmartPopulation(int size) {
        dots = new SmartDot[size];
        for (int i = 0; i < size; i++)
            dots[i] = new SmartDot();
        dn = new DisplayNetwork(50, 50, 10, dots[0].nn);

    }

    public void show() {
        for (int i = 1; i < dots.length; i++)
            dots[i].show();
        dots[0].show();
        dn.show();
    }

    public void calculateFitness() {
        for (SmartDot x : dots)
            x.calculateFitness();
    }

    public void update() {
        for (SmartDot x : dots)
            if (x.nn.step > x.nn.maxNrStep)
                x.dead = true;
            else
                x.update();
    }

    public boolean allDotsDead() {
        for (SmartDot x : dots)
            if (!x.dead && !x.reachedGoal)
                return false;
        return true;
    }

    public void naturalSelection() {
        Main.goal = new PVector(p.random(100, p.width - 100), 100);


        //generate goal velocity
        float y = p.random(-2, 2);
        float x = p.random(-2, 2);

        Main.goalVel = new PVector(x, -y);

        SmartDot[] newDots = new SmartDot[dots.length];
        setBestDot();
        calculateFitnessSum();
        SmartDot parent1 = selectParent();
        newDots[0] = dots[bestDot].giveBaby(parent1, true);

        newDots[0].isBest = true;
        maxFitness = max(maxFitness, (int) dots[bestDot].fitness);
        //change the displayed network
        dn.changeNetwork(dots[bestDot].nn);

        for (int i = 1; i < newDots.length; i++) {
            //select parent based on fitness
            SmartDot p1 = selectParent();
            SmartDot p2 = selectParent();
            //get the BABY for the 2 parents
            newDots[i] = p1.giveBaby(p2, false);
        }
        dots = newDots.clone();
        gen++;
    }

    public void calculateFitnessSum() {
        fitnessSum = 0;
        for (SmartDot x : dots)
            fitnessSum += x.fitness;
    }

    //select parent based on fitness
    public SmartDot selectParent() {
        float rand = p.random(fitnessSum);
        float runningSum = 0;
        for (SmartDot x : dots) {
            runningSum += x.fitness;
            if (runningSum > rand)
                return x;
        }
        System.out.println("HOW! DID YOU GET HERE?!");
        return null;
    }

    public void mutateBabies() {
        for (SmartDot x : dots) x.nn.mutate();
    }

    public void setBestDot() {
        float maxi = 0;
        int maxIndex = 0;
        for (int i = 0; i < dots.length; i++)
            if (dots[i].fitness > maxi) {
                maxi = dots[i].fitness;
                maxIndex = i;
            }
        bestDot = maxIndex;
        if (dots[bestDot].reachedGoal)
            minStep = dots[bestDot].nn.step;
    }

    //compare the weights and biases of th best and previous best player
    public String compare() {
        setBestDot();
        SmartDot a = dots[0];
        SmartDot b = dots[bestDot];
        int weightsChanges = 0, biasChange = 0;
        NeuralNetwork na = a.nn;
        NeuralNetwork nb = b.nn;
        //add the number of different weights
        for (int i = 0; i < na.weights.length; i++) {
            weightsChanges += na.weights[0].compare(nb.weights[0]);
        }
        //ad the number of different bias
        for (int i = 0; i < na.bias.length; i++)
            for (int j = 0; j < na.bias[i].size(); j++)
                if (!Objects.equals(na.bias[i].get(j), nb.bias[i].get(j))) {
                    biasChange++;
                }
        return "Changes: " + "weights=" + weightsChanges + " biases=" + biasChange;
    }


}
