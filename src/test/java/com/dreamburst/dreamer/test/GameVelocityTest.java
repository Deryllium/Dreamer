package com.dreamburst.dreamer.test;

import com.dreamburst.dreamer.core.*;
import com.dreamburst.dreamer.systems.IteratingSystem;

public class GameVelocityTest {

    private Engine engine;

    public GameVelocityTest() {
        engine = new Engine();
        engine.add(new VelocitySystem());
    }

    public void testPositionChanged() {
        Entity one = new Entity();
        Entity two = new Entity();

        engine.add(one);
        engine.add(two);

        one.add(new PositionComponent(1, 1)).add(new VelocityComponent(2, 2));

        while (engine.isEnabled()) {
            engine.update(1);
        }
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
        public void update(Entity entity, float delta) {
            PositionComponent positionComponent = entity.get(PositionComponent.class);
            VelocityComponent velocityComponent = entity.get(VelocityComponent.class);

            positionComponent.x += velocityComponent.x;
            positionComponent.y += velocityComponent.y;

            System.out.println(positionComponent.x + ", " + positionComponent.y);
        }
    }
}
