package com.example;


import java.util.Objects;

import static com.example.Main.p;
import static java.lang.Math.max;

/**
 * @author Denis Crismariu
 * A population of SmartDots
 */
public class SmartPopulation {
    SmartDot[] dots;
    DisplayNetwork dn;
    float fitnessSum;
    int gen = 1;
    int bestDot = 0;
    static int maxFitness = 0;
    static int dotsReachedGoals = 0;

    /**
     * Construct that generates the array of dots and the DisplayNetwork for the best dot
     * @param size the number of players in the generation
     */
    SmartPopulation(int size) {
        dots = new SmartDot[size];
        for (int i = 0; i < size; i++)
            dots[i] = new SmartDot();
        dn = new DisplayNetwork(50, 50, 10, dots[0].nn);

    }

    /**
     * Shows all the dots and the DisplayNetwork
     */
    public void show() {
        for (int i = 1; i < dots.length; i++)
            dots[i].show();
        dots[0].show();
        dn.show();
    }

    /**
     * Calculates the fitness of very dot
     */
    public void calculateFitness() {
        for (SmartDot x : dots)
            x.calculateFitness();
    }

    /**
     * Updates the dots if they haven't overcome the maxNrSteps
     */
    public void update() {
        for (SmartDot x : dots)
            if (x.nn.step > x.nn.maxNrSteps)
                x.dead = true;
            else
                x.update();
    }

    /**
     *
     * @return if all dots are dead
     */
    public boolean allDotsDead() {
        for (SmartDot x : dots)
            if (!x.dead && !x.reachedGoal)
                return false;
        return true;
    }


    /**
     * Calculates the best dot which will get to the next generation without any changes
     * The other dots are made from merging 2 parents chose based on their fitness
     * with some mutation on top of that
     */
    public void naturalSelection() {
        //generate a new goal
        Main.goal=new Goal();

        SmartDot[] newDots = new SmartDot[dots.length];
        setBestDot();
        calculateFitnessSum();
        newDots[0] = dots[bestDot].giveBaby();

        newDots[0].isBest = true;
        maxFitness = max(maxFitness, (int) dots[bestDot].fitness);
        //change the displayed network
        dn.changeNetwork(dots[bestDot].nn);

        for (int i = 1; i < newDots.length; i++) {
            //select parent based on fitness
            SmartDot p1 = selectParent();
            SmartDot p2 = selectParent();
            //make sure the parents are different

            while (p1 == p2)
                p1 = selectParent();

            //get the BABY for the 2 parents
            newDots[i] = p1.giveBaby(p2);
        }
        dots = newDots.clone();
        gen++;
        dotsReachedGoals = 0;
    }

    public void calculateFitnessSum() {
        fitnessSum = 0;
        for (SmartDot x : dots)
            fitnessSum += x.fitness;
    }

    /**
     *
     * @return a random dot based on the fitness
     */
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

    /**
     * mutates every dot beside the best
     */
    public void mutateBabies() {
        for(int i=1;i<dots.length;i++)
            dots[i].nn.mutate();
    }

    /**
     * find the bestDot based on the fitness
     */
    public void setBestDot() {
        float maxi = 0;
        int maxIndex = 0;
        for (int i = 0; i < dots.length; i++)
            if (dots[i].fitness > maxi) {
                maxi = dots[i].fitness;
                maxIndex = i;
            }
        bestDot = maxIndex;

    }

    /**
     *
     * @return the number of differences from prev best to current best in weights and biases
     */
    public String compare() {
        setBestDot();
        SmartDot a = dots[0];
        SmartDot b = dots[bestDot];
        int weightsChanges = 0, biasesChange = 0;
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
                    biasesChange++;
                }
        return "Changes: " + "weights=" + weightsChanges + " biases=" + biasesChange;
    }


}
