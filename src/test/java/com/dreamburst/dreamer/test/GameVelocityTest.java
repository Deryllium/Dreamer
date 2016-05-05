package com.dreamburst.dreamer.test;

import com.dreamburst.dreamer.core.*;
import com.dreamburst.dreamer.systems.IteratingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameVelocityTest {

    private Engine engine;

    public GameVelocityTest() {
        engine = new Engine();
        engine.add(new VelocitySystem());
    }

    public void testPositionChanged() {
        for (int i = 0; i < 100; i ++) {
            Entity e = new Entity();
            e.add(new PositionComponent(1, 1)).add(new VelocityComponent(1, 1));
            engine.add(e);
        }

        List<Integer> ints = Arrays.asList(1, 11, 5, 2, 444, 125, 88, 164);


        int i = 0;
        long ms = System.currentTimeMillis();

        while (engine.isEnabled()) {
            engine.update();
            i++;
            if (System.currentTimeMillis() - ms >= 1000) {
                engine.disable();
            }
        }

        System.out.println(i);
    }

    private class XYComponent implements Component {
        public int x;
        public int y;

        public XYComponent(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class PositionComponent extends XYComponent {
        public PositionComponent(int x, int y) {
            super(x, y);
        }
    }

    private class VelocityComponent extends XYComponent {
        public VelocityComponent(int x, int y) {
            super(x, y);
        }
    }

    @Components({PositionComponent.class, VelocityComponent.class})
    private class VelocitySystem extends IteratingSystem {
        @Override
        public void update(Entity entity) {
            PositionComponent positionComponent = entity.get(PositionComponent.class);
            VelocityComponent velocityComponent = entity.get(VelocityComponent.class);

            positionComponent.x += velocityComponent.x;
            positionComponent.y += velocityComponent.y;
        }
    }
}
