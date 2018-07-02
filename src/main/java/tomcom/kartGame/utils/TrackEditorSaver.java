package tomcom.kartGame.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TrackEditorSaver {

	public TrackEditorSaver(Array<Circle> placedRoadblocks,
			Vector3 cameraPosition, float cameraZoom) {
		try {
			Files.write(
					Paths.get("Track.txt"),
					constructTrackSaveTextContent(placedRoadblocks,
							cameraPosition, cameraZoom).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String constructTrackSaveTextContent(
			Array<Circle> placedRoadblocks, Vector3 cameraPosition,
			float cameraZoom) {
		return writeCode(placedRoadblocks) + "\nCAMERA POS: "
				+ cameraPosition.toString() + "  CAMERA ZOOM:  " + cameraZoom;
	}

	private String writeCode(Array<Circle> placedRoadblocks) {
		StringBuilder sb = new StringBuilder();
		for (Circle c : placedRoadblocks) {
			sb.append('\n');
			sb.append("engine.addEntity(EntityBuilder.buildRoadBlock(");
			sb.append(c.x);
			sb.append("f,");
			sb.append(c.y);
			sb.append("f, roadblockTexture));");
		}
		return sb.toString();
	}
}
