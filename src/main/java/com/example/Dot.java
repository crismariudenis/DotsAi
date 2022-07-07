package com.example;
import processing.core.PVector;
import static java.lang.Math.PI;
import static com.example.Main.*;
import static com.example.Main.p;
class Dot {
    PVector pos;
    PVector vel;
    PVector acc;
    Brain brain;

    int r=2;
    boolean dead=false;

    float fitness=0.0f;
    boolean reachedGoal=false;
    boolean isBest = false;

    Dot() {
        brain= new Brain(400);
        pos = new PVector(Main.p.width/2, Main.p.height-10);
        vel=new PVector(0, 0);
        acc=new PVector(0, 0);
    }

    public void show() {
        if (isBest) {
            Main.p.fill(0, 255, 0);
            Main.p.ellipse(pos.x, pos.y, 4*r, 4*r);
        } else {
            Main.p.fill(0);
            Main.p.ellipse(pos.x, pos.y, 2*r, 2*r);
        }
    }


    public void move() {
        if (brain.directions.length>brain.step) {
            acc=brain.directions[brain.step];
            brain.step++;
        } else {
            dead=true;
        }
//        System.out.println(acc.x+" "+acc.y);
        vel.add(acc);
        vel.limit(5);
        pos.add(vel);
    }

    public void update()
    {
        if (!dead && !reachedGoal) {
            move();
            //hits wall
            if (min(pos.x, pos.y)<r || pos.x> Main.p.width-r || pos.y> Main.p.height-r) {
                dead=true;
                return;
            }
            //hits obstacles
            for (int i = 0; i< Main.nrObstacles; i++)
                if (Main.walls[i].hit(this)){
                    dead=true;
                    return;
                }
            //hits goal
            if(dist(pos.x, pos.y, Main.goal.x, Main.goal.y)<5)
                reachedGoal=true;
        }
    }

    public void calculateFitness() {
        if (reachedGoal) {
            fitness=1.0f/16.0f+10000.0f/(float)(brain.step*brain.step);
        } else {
            float distanceToGoal= dist(pos.x, pos.y, Main.goal.x, Main.goal.y);
            fitness=1.0f/(distanceToGoal*distanceToGoal);
        }
    }
    //clone it
    public Dot givemeBaby() {
        ///-----------------Can merge multiple parent for more complex projects-----------------
        Dot baby=new Dot();
        baby.brain=brain.clone();
        return baby;
    }
}
