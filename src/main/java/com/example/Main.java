
package com.example;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet {

    public static PApplet p;

    SmartPopulation smartPop;
    public static int[] nnShape = new int[]{8,12,10,10,4};
    public static Goal goal;
    public static final float weightsMutationRate = 0.001f;
    public static final float biasMutationRate = 0.01f;

    public static boolean mouseMode=false;

    public void setup() {
        p = this;
        frameRate(120);
        generateNetworkShape(8,4);
        smartPop = new SmartPopulation(2000);
        goal=new Goal();
    }

    public void menu() {
        textSize(30);
        text("Generations: " + smartPop.gen, 5, height - 70);
        StringBuilder s = new StringBuilder("NN shape: ");
        for (int x : nnShape)
            s.append(x).append(" ");
        String S = String.valueOf(s);
        text(S, 5, height - 20);
        text("Best fitness: " + SmartPopulation.maxFitness, 5, height - 120);
    }

    public void draw() {
        //Todo: Save the best of the generation in a file + add loading method from file
        //Todo: Make multiple movements like curvature and mouse / GENERATE THE SHAPE AT THE BEGINNING
        background(200);

        goal.update();
//        fill(0, 0, 255,100);
//        ellipse(goal.x, goal.y, 100, 100);
        menu();
        if (smartPop.allDotsDead()) {
            smartPop.calculateFitness();
            smartPop.naturalSelection();
            smartPop.mutateBabies();
        } else {
            smartPop.update();
            smartPop.show();
        }
    }
    public void keyPressed(){
        if (key == 's') {
            mouseMode = !mouseMode;
        }
    }
    public void settings() {
        size(900, 900);
    }


    private void generateNetworkShape(int input, int output) {
        //////////////////
        int maxNrLayers = 7;
        int minNrLayers = 2;

        int minNrNodes = 4;
        int maxNrNodes = 12;
        //////////////////
        int nrLayers = (int) p.random(minNrLayers, maxNrLayers);
        nnShape = new int[nrLayers + 2];
        nnShape[0] = input;
        nnShape[nnShape.length - 1] = output;
        for (int i = 1; i < nnShape.length - 1; i++)
            nnShape[i] = ceil(p.random(minNrNodes, maxNrNodes));
    }

    public static void main(String[] args) {
        PApplet.main("com.example.Main", args);
    }
}
