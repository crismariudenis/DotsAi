
package com.example;

import processing.core.PApplet;

/**
 * @author Denis Crismariu
 */
public class Main extends PApplet {

    public static PApplet p;
    SmartPopulation smartPop;
    public static int[] nnShape = new int[]{4,8,4};
    public static Goal goal;
    public static final float weightsMutationRate = 0.001f;
    public static final float biasMutationRate = 0.01f;

    public static boolean mouseMode=false;

    /**
     * Function that is called by processing one time
     * at the start of the program
     * It is called after the main
     */
    public void setup() {
        p = this;
        frameRate(120);
//        generateNetworkShape(12,4);
        smartPop = new SmartPopulation(2000);
        goal=new Goal();
    }

    /**
     * Draws the menu screen
     */
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

    /**
     * Functions that is called every frame and call all the other functions
     */
    public void draw() {
        //Todo: Save the best of the generation in a file + add loading method from file
        //Todo: Make multiple movements like curvature and mouse / GENERATE THE SHAPE AT THE BEGINNING
        //Todo: App/ Phone app to select all the values like, shape , inputs, fitness function
        background(200);

        goal.update();

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

    /**
     * Inverts the mouse mode if the key 'S' is pressed
     */
    public void keyPressed(){
        if (key == 's') {
            mouseMode = !mouseMode;
        }
    }

    /**
     * Initializing the screen size
     */
    public void settings() {
        size(900, 900);
    }

    /**
     * Generates a random shape for the network
     * @param input the number of nodes in the input layer
     * @param output the number of nodes in the output layer
     */
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

    /**
     * The main function
     * @param args
     */
    public static void main(String[] args) {PApplet.main("com.example.Main", args);}
}
