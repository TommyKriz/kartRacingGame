package tomcom.kartGame.scenes;

import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.game.resources.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {

	private Stage stage;

	public MenuScreen(GameMain game) {
		stage = new Stage();

		Skin mySkin = ResourceManager.getSkin();

		Gdx.input.setInputProcessor(stage);

		TextButton button = new TextButton("Test Level", mySkin);
		button.setBounds(0, 0, 300, 100);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new TestLevel(game));
			}
		});

		stage.addActor(button);

		TextButton button1 = new TextButton("Hagenberg Demo Level", mySkin);
		button1.setBounds(0, 120, 300, 100);
		button1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new HagenbergDemo(game));
			}
		});

		stage.addActor(button1);

		TextButton button2 = new TextButton("Tommys Vienna Level", mySkin);
		button2.setBounds(0, 320, 300, 100);
		button2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new TommysVienna(game));
			}
		});

		stage.addActor(button2);

	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		stage.act(delta);

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
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
		stage.dispose();
	}

}
