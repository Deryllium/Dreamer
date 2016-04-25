package com.dreamburst.dreamer;

import com.dreamburst.dreamer.test.GameVelocityTest;

public class Dreamer {

    private static final GameVelocityTest gameVelocityTest = new GameVelocityTest();

    public static void main(String[] args) {
        gameVelocityTest.testPositionChanged();
    }
}
