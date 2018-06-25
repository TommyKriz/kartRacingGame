package tomcom.kartGame.systems;

import tomcom.kartGame.config.EntityConfig;
import tomcom.kartGame.entities.EntityBuilder;
import tomcom.kartGame.utils.TrackEditorSaver;

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
			placeRoadblock(getClickWorldCoords());
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {

			Gdx.app.log("TrackEditorSystem", "Saving roadblocks to file");

			final OrthographicCamera cam = getEngine().getSystem(
					CameraSystem.class).getWorldCamera();

			new TrackEditorSaver(placedRoadblocks, cam.position, cam.zoom);
		}
	}

	private Vector3 getClickWorldCoords() {
		return worldCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input
				.getY(), 0));

	}

	private void placeRoadblock(Vector3 worldCoords) {

		Gdx.app.log("TrackEditorSystem",
				"Placing Roadblock @ " + worldCoords.toString());

		Circle roadblockToBePlaced = new Circle(worldCoords.x, worldCoords.y,
				EntityConfig.ROADBLOCK_R);

		if (!placedRoadblocks.contains(roadblockToBePlaced, false)) {

			for (Circle alreadyPlacedRoadblock : placedRoadblocks) {
				if (roadblockToBePlaced.overlaps(alreadyPlacedRoadblock)) {
					return;
				}
			}
			placedRoadblocks.add(roadblockToBePlaced);
			getEngine().addEntity(
					EntityBuilder.buildRoadBlock(worldCoords.x, worldCoords.y));
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		worldCamera = engine.getSystem(CameraSystem.class).getWorldCamera();
	}

}