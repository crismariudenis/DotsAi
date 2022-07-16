package com.example;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

import static com.example.Main.p;

public class Matrix {
    float[][] w;
    int height;
    int width;
    float mini, maxi;

    Matrix(int height, int width, float mini, float maxi) {
        this.height = height;
        this.width = width;
        this.mini = mini;
        this.maxi = maxi;
        w = new float[height][width];
        initialize(mini, maxi);
    }

    private void initialize(float mini, float maxi) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Random R = new Random();
                w[i][j] = (float) mini + R.nextFloat() * (maxi - mini);//----------------------DELETE INT
            }
    }

    void print() {
        for (int i = 0; i < height; i++, System.out.println())
            for (int j = 0; j < width; j++) {
                System.out.print(w[i][j] + " ");
            }
    }

    public ArrayList<Float> calc(ArrayList<Float> input, ArrayList<Float> bias) {
        ArrayList<Float> ans = new ArrayList<Float>();
        if (height != input.size()) {
            System.out.println("Height of the matrix=" + height + " differs from the with of the vector=" + input.size());
            System.exit(0);
        } else {
            for (int i = 0; i < width; i++) {
                float aux = bias.get(i);
                for (int j = 0; j < height; j++) {
                    aux += w[j][i] * input.get(j);
                }
                ans.add(aux);
            }
        }
        return ans;
    }

    public void mutate() {
        float mutationRate = 0.001f;
        for (int i = 0; i < w.length; i++)
            for (int j = 0; j < w[i].length; j++) {
                float rand = p.random(1);
                if (rand < mutationRate) {
                    float editWeight = p.random(-1, 1);
                    w[i][j] += editWeight;
                }
            }
    }

    public Matrix merge(Matrix x) {
        //this & x
        Matrix ans = this.copy();

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                float r = p.random(1);
                if (r < 0.5)
                    ans.w[i][j] = x.w[i][j];
            }
        return ans;
    }

    public Matrix copy() {
        //create a new Random Matrix
        Matrix clone = new Matrix(height, width, -100, 100);
        clone.width = this.width;
        clone.height = this.height;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                clone.w[i][j] = this.w[i][j];
            }
        return clone;
    }

    public int compare(Matrix m) {
        int ans = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (m.w[i][j] != this.w[i][j])
                    ans++;
        return ans;
    }

    public float get(int x, int y) {
        return w[x][y];
    }

}