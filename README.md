
# DotsAi

This project was my first attempt to use Neural Networks using an evolution algorithm.


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
    
