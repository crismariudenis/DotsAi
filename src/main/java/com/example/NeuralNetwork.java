package com.example;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

import static com.example.Main.*;
import static com.example.Main.p;
import static com.example.Parameters.*;


/**
 * @author Denis Crismariu
 */
public class NeuralNetwork {
    Matrix[] weights; // Matrix[i] is from i to i+1
    ArrayList<Float>[] biases;

    int step = 0;

    /**
     * generates the biases with random values
     * generates new weights
     *
     * @param shape the shape of the neural network
     */
    NeuralNetwork(int[] shape) {
        biases = (ArrayList<Float>[]) new ArrayList[shape.length];
        weights = new Matrix[shape.length - 1];
        for (int i = 0; i < shape.length; i++) {
            if (i != shape.length - 1)//last layer
                weights[i] = new Matrix(shape[i], shape[i + 1], minWeight, maxWeight);
            biases[i] = new ArrayList<>();
            initialize(biases[i], shape[i]);
        }
    }

     /**
     * Packs the input nodes values into and ArrayList
     *
     * @param pos the position of the player
     * @param vel the velocity of the player
     * @return an ArrayList with all the values in the input layer
     */
    private ArrayList<Float> getInput(PVector pos, PVector vel) {
        ArrayList<Float> input = new ArrayList<>();
        input.add(pos.x + vel.x - (goal.pos.x + goal.vel.x));
        input.add(pos.y + vel.y - (goal.pos.y + goal.vel.y));
        input.add(pos.x - goal.pos.x);
        input.add(pos.y - goal.pos.y);
//        input.add(pos.x+ vel.x);
//        input.add(pos.y+ vel.y);
//        input.add(p.width-(pos.x+vel.x));
//        input.add(p.height-(pos.y+ vel.y));
//        input.add(pos.x);
//        input.add(pos.y);
//        input.add(p.width-pos.x);
//        input.add(p.height-pos.y);
        return input;
    }

    /**
     * Computes the values from each layer to the next one using the sigmoid function for activation
     * The output values are clamped between -1 and 1
     *
     * @param pos the position of the player
     * @param vel the layer of the player
     * @return an ArrayList<Float>
     */
    ArrayList<Float> process(PVector pos, PVector vel) {
        ArrayList<Float> input = getInput(pos, vel);
        if (input.size() != nnShape[0]) {
            System.out.println("Number of inputs= " + input.size() + " doesn't match the NN shape which is= " + nnShape[0]);
            System.exit(0);
        } else {
            //loop through all the layers except the last
            for (int i = 0; i < nnShape.length - 1; i++) { //Multiply the weight matrix by the input
                //applying sigmoid to the whole input
                sigmoid(input);
                input = weights[i].compute(input, biases[i + 1]);
            }
        }
        //clamp the output between [-1,1]
        for (int i = 0; i < input.size(); i++)
            input.set(i, clamp(input.get(i), -1, 1));

        return input;
    }

    /**
     * @param bias the arraylist in which we need to add the random biases
     * @param size the size of the bias ArrayList
     */
    private void initialize(ArrayList<Float> bias, int size) {
        for (int i = 0; i < size; i++) {
            Random R = new Random();
            bias.add(minBias + R.nextFloat() * (maxBias - minBias));
        }
    }

    /**
     * Sigmoid activation function
     *
     * @param v the input ArrayLists
     */
    private void sigmoid(ArrayList<Float> v) {
        v.replaceAll(x -> 1 / (1 + exp(-x)));
    }

    /**
     * Custom cloning function for copying
     *
     * @return the clone of the NeuralNetwork
     */
    public NeuralNetwork clone() {
        NeuralNetwork clone = new NeuralNetwork(nnShape);
        //clone the bias by value
        clone.biases = (ArrayList<Float>[]) new ArrayList[this.biases.length];
        for (int i = 0; i < this.biases.length; i++) {
            clone.biases[i] = new ArrayList<>();
            for (int j = 0; j < this.biases[i].size(); j++)
                clone.biases[i].add(this.biases[i].get(j));
        }
        //clone the weights
        clone.weights = new Matrix[this.weights.length];
        for (int i = 0; i < this.weights.length + 1; i++)
            if (i != this.weights.length)//last layer
                clone.weights[i] = weights[i].copy();

        return clone;
    }

    /**
     * Merging two neural networks
     *
     * @param p2 the other neural network
     * @return the Neural Network that is the clone
     */
    public NeuralNetwork merge(NeuralNetwork p2) {
        //merge
        NeuralNetwork merger = p2.clone();

        for (int i = 0; i < this.weights.length; i++) {
            merger.weights[i] = this.weights[i].merge(p2.weights[i]);
        }
        for (int i = 0; i < this.biases.length; i++)
            for (int j = 0; j < this.biases[i].size(); j++) {
                float r = p.random(1);
                if (r < 0.5)
                    merger.biases[i].set(j, this.biases[i].get(j));
            }
        return merger;
    }

    /**
     * Mutates the biases of the Network
     * and calls the Matrix mutate function
     */
    public void mutate() {
        //mutate bias
        for (ArrayList<Float> bia : biases)
            for (int j = 0; j < bia.size(); j++) {
                float rand = p.random(1);
                if (rand < biasMutationRate) {
                    float editBias = p.random(-1, 1);
                    bia.set(j, editBias);
                }
            }
        //mutate weights
        for (Matrix x : weights)
            x.mutate();
    }

    /**
     * @param value the value you want to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamping
     */
    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}