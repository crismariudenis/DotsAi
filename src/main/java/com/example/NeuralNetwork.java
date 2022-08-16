package com.example;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

import static com.example.Main.*;
import static com.example.Main.p;

public class NeuralNetwork {
    Matrix[] weights; // Matrix[i] should be from i to i+1
    ArrayList<Float>[] bias;// bias[i] should be the biases from level i
    float minWeight = -5;
    float maxWeight = 5;
    float minBias = -5;
    float maxBias = 5;
    int step = 0;
    int maxNrSteps = 1000;

    NeuralNetwork(int[] v) {
        bias = (ArrayList<Float>[]) new ArrayList[v.length];
        weights = new Matrix[v.length - 1];
        for (int i = 0; i < v.length; i++) {
            if (i != v.length - 1)//last layer
                weights[i] = new Matrix(v[i], v[i + 1], minWeight, maxWeight);
            bias[i] = new ArrayList<>();
            initialize(bias[i], v[i]);
        }
    }
    //returns the output after the matrices multiplications

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

    ArrayList<Float> process(PVector pos, PVector vel) {
        ArrayList<Float> input = getInput(pos, vel);
        if (input.size() != nnShape[0]) {
            System.out.println("Number of inputs= " + input.size() + " doesn't match the NN shape which is= " + Main.nnShape[0]);
            System.exit(0);
        } else {
            //loop through all the layers except the last
            for (int i = 0; i < nnShape.length - 1; i++)
                //Multiply the weight matrix by the input
                input = weights[i].calc(sigmoid(input), bias[i + 1]);
        }
        //clamp the output between [-1,1]
        for (int i = 0; i < input.size(); i++)
            input.set(i, clamp(input.get(i), -1, 1));

        return input;
    }

    //initialize with random values
    private void initialize(ArrayList<Float> v, int size) {
        for (int i = 0; i < size; i++) {
            Random R = new Random();
            v.add(minBias + R.nextFloat() * (maxBias - minBias));
        }
    }

    private ArrayList<Float> sigmoid(ArrayList<Float> v) {
        ArrayList<Float> ans = new ArrayList<>();
        for (Float x : v) ans.add(1 / (1 + exp(-x)));
        return ans;
    }

    //returns a clone of a NeuralNetwork
    public NeuralNetwork clone() {
        NeuralNetwork clone = new NeuralNetwork(nnShape);
        //clone the bias by value
        clone.bias = (ArrayList<Float>[]) new ArrayList[this.bias.length];
        for (int i = 0; i < this.bias.length; i++) {
            clone.bias[i] = new ArrayList<>();
            for (int j = 0; j < this.bias[i].size(); j++)
                clone.bias[i].add(this.bias[i].get(j));
        }
        //clone the weights
        clone.weights = new Matrix[this.weights.length];
        for (int i = 0; i < this.weights.length + 1; i++)
            if (i != this.weights.length)//last layer
                clone.weights[i] = weights[i].copy();

        return clone;
    }

    //merge 2 NeuralNetwork
    public NeuralNetwork merge(NeuralNetwork p2) {
        //merge
        NeuralNetwork merger = p2.clone();

        for (int i = 0; i < this.weights.length; i++) {
            merger.weights[i] = this.weights[i].merge(p2.weights[i]);
        }
        for (int i = 0; i < this.bias.length; i++)
            for (int j = 0; j < this.bias[i].size(); j++) {
                float r = p.random(1);
                if (r < 0.5)
                    merger.bias[i].set(j, this.bias[i].get(j));
            }
        return merger;
    }

    public void mutate() {
        //mutate bias
        for (ArrayList<Float> bia : bias)
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

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}