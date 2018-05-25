package tomcom.kartGame.systems;

import tomcom.kartGame.scenes.EntityBuilder;
import tomcom.kartGame.scenes.EntityConfig;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TrackEditorSystem extends EntitySystem {

	OrthographicCamera worldCamera;

	Array<Circle> placedRoadblocks = new Array<>();

	@Override
	public void update(float deltaTime) {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			placeRoadblock(worldCamera.unproject(new Vector3(Gdx.input.getX(),
					Gdx.input.getY(), 0)));
		}
	}

	private void placeRoadblock(Vector3 clickCoords) {
		Circle roadblockToBePlaced = new Circle(clickCoords.x, clickCoords.y,
				EntityConfig.ROADBLOCK_R);

		if (!placedRoadblocks.contains(roadblockToBePlaced, false)) {

			for (Circle alreadyPlacedRoadblock : placedRoadblocks) {
				if (roadblockToBePlaced.overlaps(alreadyPlacedRoadblock)) {
					return;
				}
			}

			Gdx.app.log("TrackEditorSystem", "Placing roadblock at "
					+ clickCoords.x + "|" + clickCoords.y);
			placedRoadblocks.add(roadblockToBePlaced);
			getEngine().addEntity(
					EntityBuilder.buildInvisibleRoadBlock(clickCoords.x,
							clickCoords.y));
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		worldCamera = engine.getSystem(CameraSystem.class).getWorldCamera();
	}

}