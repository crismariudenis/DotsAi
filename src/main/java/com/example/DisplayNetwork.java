package com.example;

import static com.example.Main.*;
import static com.example.Main.p;

public class DisplayNetwork {
    float x, y, scale;

    NeuralNetwork nn;

    DisplayNetwork(float x, float y, float scale, NeuralNetwork nn) {
        this.x = x;
        this.y = y;
        this.nn = nn;
        this.scale=scale;
    }

    void show() {
        p.fill(0, 255, 0);
        p.translate(x,y);
        p.rect(0,0,100,100);
        int maxi=-1;
        for(int x: nnShape)
            maxi=max(x,maxi);
        //o o o
        float width=(nnShape.length+ nnShape.length-1);
        float height=maxi+maxi-1;

    }
}
