package tomcom.kartGame.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Kart Game Physics Spike";
		cfg.width = GameConfig.SCREEN_WIDTH;
		cfg.height = GameConfig.SCREEN_HEIGHT;
		cfg.resizable = false;
		new LwjglApplication(new GameMain(), cfg);
	}

}
