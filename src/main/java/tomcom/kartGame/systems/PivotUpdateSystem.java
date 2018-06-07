package tomcom.kartGame.systems;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.physics.Body2DComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

public class PivotUpdateSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(Body2DComponent.class,
			PivotComponent.class).get();

	private ComponentMapper<Body2DComponent> bc = ComponentMapper
			.getFor(Body2DComponent.class);
	private ComponentMapper<PivotComponent> pc = ComponentMapper
			.getFor(PivotComponent.class);

	public PivotUpdateSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Body2DComponent body = bc.get(entity);
		PivotComponent pivot = pc.get(entity);
		// convert the radians to degrees here
		pivot.setPos(body.getPosition(), body.getAngleInRadians()
				/ MathUtils.degreesToRadians);
	}

}
