package com.example;

import processing.core.PVector;

import java.util.ArrayList;

import static com.example.Main.*;
import static com.example.Main.p;
import static com.example.Parameters.*;


/**
 * @author Denis Crismariu
 */

public class DisplayNetwork {
    float x, y, scale;
    float diameter, spacingX, spacingY;
    ArrayList<PVector>[] nodesCords;
    NeuralNetwork nn;

    /**
     *
     * @param x the x coordinate of the top left corner of the network
     * @param y the y coordinate of the top left corner of the network
     * @param scale the scale with multiplies the default values of the network
     * @param nn the neural network of the dot
     */
    DisplayNetwork(float x, float y, float scale, NeuralNetwork nn) {
        this.x = x;
        this.y = y;
        this.nn = nn;
        this.scale = scale;
        this.diameter = 2 * scale;
        this.spacingX = 4 * scale;
        this.spacingY = scale;
        calculateNodesCords();
    }

    /**
     * Shows the weights line and the neurons
     */
    public void show() {
        p.translate(x, y);
        showWeights();
        showNeurons();
//        showNames();
    }

    /**
     * Calculates the location of the nodes of the neuron network on the screen
     */
    private void calculateNodesCords() {
        int maxi = -1;
        for (int x : nnShape)
            maxi = max(x, maxi);

        float height = maxi * diameter + (maxi - 1) * spacingY;
        nodesCords = (ArrayList<PVector>[]) new ArrayList[nnShape.length];
        for (int i = 0; i < nnShape.length; i++) {
            nodesCords[i] = new ArrayList<>();
            int layer = nnShape[i];
            float localHeight = layer * diameter + (layer - 1) * spacingY;
            float y = (height - localHeight) / 2 + diameter / 2;
            float x = i * (diameter + spacingX);
            for (int j = 0; j < nnShape[i]; j++) {
                p.ellipse(x, y, diameter, diameter);
                nodesCords[i].add(new PVector(x, y));
                y += diameter / 2 + spacingY + diameter / 2;
            }
        }
    }

    /**
     * Draws the neurons on the screen
     */
    private void showNeurons() {
        p.fill(255);
        for (ArrayList<PVector> nc : nodesCords) {
            for (PVector n : nc)
                p.ellipse(n.x, n.y, diameter, diameter);
        }
    }

    /**
     * Change the drawn nn with a new one
     * @param nn the new Neural Network
     */
    public void changeNetwork(NeuralNetwork nn) {
        this.nn = nn;
    }

    /**
     * Draws the weights lines on the screen
     */
    private void showWeights() {
        for (int LAYER = 0; LAYER < nnShape.length - 1; LAYER++) {
            for (int i = 0; i < nn.weights[LAYER].width; i++)
                for (int j = 0; j < nn.weights[LAYER].height; j++) {
                    float W = nn.weights[LAYER].get(j, i);
                    if (W > 0)
                        p.stroke(0, 0, 255,100);
                    else if (W < 0)
                        p.stroke(255, 0, 0,100);
                    else
                        p.stroke(0);
                    PVector p1 = nodesCords[LAYER].get(j);
                    PVector p2 = nodesCords[LAYER + 1].get(i);
                    p.line(p1.x, p1.y, p2.x, p2.y);
                }
        }
        p.stroke(0);
    }
}

