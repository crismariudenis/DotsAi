package com.example;

import processing.core.PVector;

import java.util.ArrayList;

public class SmartPopulation {
    SmartDot[] dots;
    float fitnessSum;
    int gen = 1;
    int bestDot = 0;
    int minStep = 400;

    SmartPopulation(int size) {
        dots = new SmartDot[size];
        for (int i = 0; i < size; i++)
            dots[i] = new SmartDot();
    }

    public void show() {
        for (int i = 1; i < dots.length; i++)
            dots[i].show();
        dots[0].show();
        DisplayNetwork d=new DisplayNetwork(50,50,10,dots[0].nn);
        d.show();
    }

    public void calculateFitness() {
        for (SmartDot x : dots) {
            x.caculateFitness();
        }
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
        SmartDot[] newDots = new SmartDot[dots.length];
        setBestDot();
        calculateFitnessSum();

        newDots[0] = dots[bestDot].givemeBaby(selectParent());
        //HEre we don't ise the parent
//        System.out.println(dots[bestDot].fitness);
        newDots[0].isBest = true;
//        for(Matrix m :dots[bestDot].nn.weights)
//            m.print();
//        System.out.println("V-bias-V");
//        for(ArrayList<Float> bs:dots[bestDot].nn.bias) {
//            for (float x : bs)
//                System.out.print(x);
//            System.out.println();
//
//        }
//        System.out.println("-------------------");


        for (int i = 1; i < newDots.length; i++) {
            //select parent based on fitness
            SmartDot parent = selectParent();
            //get the BABY for theme

            newDots[i] = parent.givemeBaby(selectParent());
        }
        dots = newDots.clone();
        gen++;
    }

    public void calculateFitnessSum() {
        fitnessSum = 0;
        for (SmartDot x : dots)
            fitnessSum += x.fitness;
    }

    public  SmartDot selectParent() {
        float rand = Main.p.random(fitnessSum);

        float runningSum = 0;


        for (int i = 0; i < dots.length; i++) {
            runningSum += dots[i].fitness;
            if (runningSum > rand) {
                return dots[i];
            }
        }
        System.out.println("HOW! DID YOU GET HERE?!");
        return null;
    }

    public void mutateBabies() {
        for (int i = 0; i < dots.length; i++)
            dots[i].nn.mutate();
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
            minStep = dots[bestDot].nn.step;
//            System.out.println("step:" + minStep);
        }
    }

}
