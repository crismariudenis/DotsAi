
package com.example;

import processing.core.PApplet;

/**
 * @author Denis Crismariu
 */
import static com.example.Parameters.*;

public class Main extends PApplet {

    public static PApplet p;
    SmartPopulation smartPop;
    public static Goal goal;

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
        for (int x : Parameters.nnShape)
            s.append(x).append(" ");
        String S = String.valueOf(s);
        text(S, 5, height - 20);
        text("Best fitness: " + SmartPopulation.maxFitness, 5, height - 120);
    }

    /**
     * Draws the scene every frame
     */
    public void draw() {
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

        int nrLayers = (int) p.random(Parameters.minNrLayers, Parameters.maxNrLayers);
        nnShape = new int[nrLayers + 2];
        nnShape[0] = input;
        nnShape[nnShape.length - 1] = output;
        for (int i = 1; i < nnShape.length - 1; i++)
            nnShape[i] = ceil(p.random(Parameters.minNrNodes, Parameters.maxNrNodes));
    }

    /**
     * The main function
     * @param args
     */
    public static void main(String[] args) {PApplet.main("com.example.Main", args);}
}
