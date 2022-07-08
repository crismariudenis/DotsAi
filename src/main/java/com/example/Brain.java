package com.example;
import processing.core.PVector;

import static java.lang.Math.PI;
import static com.example.Main.*;
import static com.example.Main.p;
class Brain {
    PVector[] directions;
    int step = 0;

    Brain(int size) {
        directions = new PVector[size];
        randomize();
    }

    public void randomize() {
        for (int i = 0; i < directions.length; i++) {
            float randomAngle = Main.p.random((float) (2 * PI));
            directions[i] = PVector.fromAngle(randomAngle);
        }
    }

    public Brain clone() {
        Brain clone = new Brain(directions.length);
        for (int i = 0; i < directions.length; i++)
            clone.directions[i] = directions[i].copy();

        return clone;
    }

    public void mutate() {
        float mutationRate = 0.1f;
        for (int i = 0; i < directions.length; i++) {
            float rand = Main.p.random(1);
            if (rand < mutationRate) {
                //mutate the direction
                float randomAngle = Main.p.random((float) (2 * PI));
                directions[i] = PVector.fromAngle(randomAngle);
            }
        }
    }
}
