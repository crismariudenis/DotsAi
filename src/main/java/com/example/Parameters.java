package com.example;


public  class Parameters {
    public static int[] nnShape = new int[]{4,8,4};
    public static final float weightsMutationRate = 0.001f;
    public static final float biasMutationRate = 0.01f;


    //----------Network Shape----------
    public static final int  maxNrLayers = 7;
    public static final int minNrLayers = 2;

    public static final int minNrNodes = 4;
    public static final int maxNrNodes = 12;
    //--------------------------------------

    //----------Network Values----------
    public static final float minWeight = -5;
    public static final float maxWeight = 5;
    public static final float minBias = -5;
    public static final float maxBias = 5;
    //--------------------------------------
    public static final  int maxNrSteps = 1000;

}
