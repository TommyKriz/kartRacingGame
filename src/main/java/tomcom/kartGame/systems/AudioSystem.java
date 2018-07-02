package tomcom.kartGame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.audio.Music;

public class AudioSystem extends EntitySystem {

	private Music backgroundMusic;

	public AudioSystem(Music backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		backgroundMusic.setVolume(0.5f);
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
	}

}
