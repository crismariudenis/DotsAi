
# DotsAi

This project was my first attempt to use Neural Networks using an evolution algorithm.

## Game Rules
```text
1.The goal(red dot) spawns at the top and bounces against the walls
2.The players(black dots) spawns at the bottom
3.The best player in previous generation(green dot)
4.The dots die if they touch the wall
5.The dots are removed if they touch the goal
6.The dots recive point every frame if they're near the goal
7.The game end when the all dots are dead or the time passed
```

### The goal of the project is to create an spinning behavious around the target


## Editable neural network shape

#### In the  Main class

```java
public static int[] nnShape = new int[]{4, 8, 4};
```





## Changing the values leads to different results

#### Input Layer

```java
ArrayList<Float> input = new ArrayList<>(Arrays.asList(pos.x + vel.x - (goal.x + goalVel.x), pos.y + vel.y - (goal.y + goalVel.y), pos.x - goal.x, pos.y - goal.y));
```

#### Output Layer

```java
ArrayList<Float> ans = nn.process(pos, vel);
float up = ans.get(0);
float down = ans.get(1);
float right = ans.get(2);
float left = ans.get(3);
```

### The goal  is to create an spinning behavious around the target with minimum numbers of inputs







https://user-images.githubusercontent.com/78813212/184121238-ffb171f2-2e3a-475a-ae04-0f71e24dfcd5.mp4



