package com.example;

import processing.core.PVector;

class Population {
    Dot[] dots;

    float fitnessSum;
    int gen = 1;
    int bestDot = 0;
    int minStep = 400;

    Population(int size) {
        dots = new Dot[size];
        for (int i = 0; i < size; i++) {
            dots[i] = new Dot();
        }
    }

    public void show() {
        for (int i = 1; i < dots.length; i++)
            dots[i].show();
        dots[0].show();
    }

    public void update() {
        for (int i = 0; i < dots.length; i++)
            if (dots[i].brain.step > minStep) {
                dots[i].dead = true;
            } else {
                dots[i].update();
            }
    }

    public void calculateFitness() {
        for (int i = 0; i < dots.length; i++)
            dots[i].calculateFitness();
    }

    public boolean allDotsDead() {
        for (int i = 0; i < dots.length; i++)
            if (!dots[i].dead && !dots[i].reachedGoal)
                return false;
        return true;
    }

    public void naturalSelection() {
        Dot[] newDots = new Dot[dots.length];
        setBestDot();
        calculateFitnessSum();

        newDots[0] = dots[bestDot].givemeBaby();
        newDots[0].isBest = true;


        for (int i = 1; i < newDots.length; i++) {
            //select parent based on fitness
            Dot parent = selectParent();
            //get the BABY for theme
            newDots[i] = parent.givemeBaby();
        }
        dots = newDots.clone();
        gen++;
    }

    public void calculateFitnessSum() {
        fitnessSum = 0;
        for (int i = 0; i < dots.length; i++)
            fitnessSum += dots[i].fitness;
    }

    public Dot selectParent() {
        float rand = Main.p.random(fitnessSum);

        float runningSum = 0;
        for (int i = 0; i < dots.length; i++) {
            runningSum += dots[i].fitness;
            if (runningSum > rand) {
                return dots[i];
            }
        }
        System.out.print("HOW! How did you get here?");
        return null;
    }

    public void mutateBabies() {
        for (int i = 1; i < dots.length; i++)
            dots[i].brain.mutate();
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

        if (dots[bestDot].reachedGoal) {
            minStep = dots[bestDot].brain.step;
//            System.out.println("steps:" + minStep);
        }
    }
}
