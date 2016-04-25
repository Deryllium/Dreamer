package com.dreamburst.dreamer.test;

import com.dreamburst.dreamer.core.*;
import com.dreamburst.dreamer.core.events.EngineEvent;
import com.dreamburst.dreamer.delegate.Event;
import com.dreamburst.dreamer.delegate.EventExecutor;
import com.dreamburst.dreamer.systems.IteratingSystem;

import static org.junit.Assert.assertEquals;

public class GameVelocityTest {

    private Engine engine;

    public GameVelocityTest() {
        engine = new Engine();
        engine.add(new VelocitySystem());
    }

    public void testPositionChanged() {
        Entity entity = new Entity();
        PositionComponent positionComponent = new PositionComponent(5, 5);

        engine.add(entity);
        entity.add(positionComponent).add(new VelocityComponent(1, 1));

        engine.update(1);

        assertEquals(6, positionComponent.x);
        assertEquals(6, positionComponent.y);
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
        }
    }
}
