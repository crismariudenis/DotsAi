
# DotsNeat

This project was my first atempt to use Neural Networks using an evolution algorithm.


## Editable neural network shape

#### In the  Main class

```java
    public static int[] nnShape = new int[]{4, 8, 4};
```


## Change the input values

#### In the  NeuralNetwork class

```java
    ArrayList<Float> input = new ArrayList<>(Arrays.asList(pos.x+vel.x-(goal.x+ goalVel.x), pos.y+vel.y-(goal.y+ goalVel.y),pos.x-goal.x,pos.y-goal.y));
```

## Editable neural network

#### In the  Main class

```java
  public static int[] nnShape = new int[]{8, 6, 4, 2};
```


## Changeing the values leads to different results

#### Input Layer

```java
  ArrayList<Float> input = new ArrayList<>(Arrays.asList(pos.x, pos.y, vel.x, vel.y, acc.x, acc.y, target.x, target.y));
```

#### Output Layer

```java
  ArrayList<Float> ans = nn.process(pos, vel);
  float up = ans.get(0);
  float down = ans.get(1);
  float right = ans.get(2);
  float left = ans.get(3);
```
    
