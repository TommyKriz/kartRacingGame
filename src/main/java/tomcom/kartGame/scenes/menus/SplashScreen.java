package tomcom.kartGame.scenes.menus;

import tomcom.kartGame.game.GameMain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen {

	private long showTime;

	private GameMain game;

	public SplashScreen(GameMain game) {
		this.game = game;
		showTime = TimeUtils.millis();
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (TimeUtils.timeSinceMillis(showTime) > 10) {
			game.setScreen(new MenuScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}
