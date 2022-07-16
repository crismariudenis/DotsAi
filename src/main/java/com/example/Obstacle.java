package com.example;

import processing.core.PVector;

class Obstacle {
    int x, y, w, h;

    Obstacle(int a, int b, int c, int d) {
        x = a;
        y = b;
        w = c;
        h = d;
    }

    public void show() {
        Main.p.fill(0, 0, 255);
        Main.p.rect(x, y, w, h);
    }

    public boolean hit(SmartDot dot) {
        return dot.pos.x >= x && dot.pos.x <= x + w && dot.pos.y >= y && dot.pos.y <= y + h;
    }
}