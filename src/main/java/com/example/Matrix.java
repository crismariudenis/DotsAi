package com.example;

import java.util.ArrayList;
import java.util.Random;

import static com.example.Main.p;
import static com.example.Parameters.*;


/**
 * @author Denis Crismariu
 */
public class Matrix {
    float[][] w;
    int height;
    int width;
    float mini, maxi;

    /**
     * Generates the matrix of weights
     * @param height the height of the matrix
     * @param width the width of the matrix
     * @param mini the min value in the matrix
     * @param maxi the max value in the matrix
     */
    Matrix(int height, int width, float mini, float maxi) {
        this.height = height;
        this.width = width;
        this.mini = mini;
        this.maxi = maxi;
        w = new float[height][width];
        initialize(mini, maxi);
    }

    //initialize the Matrix with random values

    /**
     * Initializing with random values
     * @param mini the min value in the matrix
     * @param maxi the max value in the matrix
     */
    private void initialize(float mini, float maxi) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Random R = new Random();
                w[i][j] = mini + R.nextFloat() * (maxi - mini);
            }
    }

    /**
     * Function to print the matrix
     */
    void print() {
        for (int i = 0; i < height; i++, System.out.println())
            for (int j = 0; j < width; j++) {
                System.out.print(w[i][j] + " ");
            }
    }

    /**
     * Calculating the input to the next layer
     * @param input input layer
     * @param bias bias values for next level
     * @return the output after the matrix multiplication
     */
    public ArrayList<Float> calc(ArrayList<Float> input, ArrayList<Float> bias) {
        ArrayList<Float> ans = new ArrayList<>();
        if (height != input.size()) {
            System.out.println("Height of the matrix=" + height + " differs from the with of the vector=" + input.size());
            System.exit(0);
        } else {
            for (int i = 0; i < width; i++) {
                float aux = bias.get(i);
                for (int j = 0; j < height; j++)
                    aux += w[j][i] * input.get(j);
                ans.add(aux);
            }
        }
        return ans;
    }

    /**
     * Mutate the weights
     */
    public void mutate() {
        for (int i = 0; i < w.length; i++)
            for (int j = 0; j < w[i].length; j++) {
                float rand = p.random(1);
                if (rand < weightsMutationRate) {
                    float editWeight = p.random(-1, 1);
                    w[i][j] += editWeight;
                }
            }
    }
    /**
     * Merge wro matrices with 50% each
     * @param x the other matrix
     * @return the combination of the Matrices
     */
    public Matrix merge(Matrix x) {
        Matrix ans = this.copy();

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                float r = p.random(1);
                if (r < 0.5)
                    ans.w[i][j] = x.w[i][j];
            }
        return ans;
    }

    /**
     * Creating a copy of the matrix
     * @return the copy of the Matrix
     */
    public Matrix copy() {
        Matrix clone = new Matrix(height, width, -100, 100);
        clone.width = this.width;
        clone.height = this.height;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                clone.w[i][j] = this.w[i][j];
        return clone;
    }

    //compare the number of differences between two matrices

    /**
     * Returns the number of differences in matrix
     * @param m the second matrix
     * @return number of differences
     */
    public int compare(Matrix m) {
        int ans = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (m.w[i][j] != this.w[i][j])
                    ans++;
        return ans;
    }

    /**
     * Function for accessing values from the matrix
     * @param x coordinate
     * @param y coordinate
     * @return the values at that location
     */
    public float get(int x, int y) {
        return w[x][y];
    }

}