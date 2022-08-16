package com.example;

import processing.core.PVector;

import static com.example.Main.*;
import static com.example.Main.p;

/**
 * @author Denis Crismariu
 */
public class Goal {
    public PVector pos, vel;

    /**
     * Generate the goal position
     * 5% chance to be parallel to one of the walls
     */
    public Goal() {
        if (p.random(0, 1) < 0.05) {
            int shiftX = 20;
            int shiftY = 20;
            float rand = p.random(0, 1);

            if (rand < 0.25) {
                //up edge
                pos = new PVector(p.random(shiftX, p.width - shiftX), shiftY);
                vel = new PVector(p.random(-2, 2), 0);
            } else if (rand < 0.5) {
                //down edge
                pos = new PVector(p.random(shiftX, p.width - shiftX), p.height - shiftY);
                vel = new PVector(p.random(-2, 2), 0);
            } else if (rand < 0.75) {
                //right edge
                pos = new PVector(p.width - shiftX, p.random(shiftY, p.height - shiftY));
                vel = new PVector(0, p.random(-2, 2));
            } else {
                //left edge
                pos = new PVector(shiftX, p.random(shiftY, p.height - shiftY));
                vel = new PVector(0, p.random(-2, 2));
            }
        } else {
            pos = new PVector(p.random(100, p.width - 100), 100);
            float x = p.random(-2, 2);
            float y = p.random(-2, 2);
            vel = new PVector(x, -y);
        }
    }

    /**
     * If the mouseMode is active the goal follows the mouse
     * else the goal bounces against the wall with the vel
     */
    public void update() {
        if (!mouseMode) {
            if (pos.x + vel.x > p.width - 10 || pos.x + vel.x < 10)
                vel.x *= -1;
            if (pos.y + vel.y > p.height - 10 || pos.y + vel.x < 10)
                vel.y *= -1;
            pos.add(vel);
        } else {
            vel = new PVector(p.mouseX, p.mouseY).sub(pos);
            pos = new PVector(p.mouseX, p.mouseY);
        }
        draw();
    }

    /**
     * Draws the goal
     */
    private void draw() {
        p.fill(255, 0, 0);
        p.ellipse(pos.x, pos.y, 10, 10);
    }

}

