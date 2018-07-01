package tomcom.kartGame.scenes.menus;

import tomcom.kartGame.game.GameMain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * 
 * Choose between Host or Join Mode
 * 
 * @author Tommy
 *
 */
public class MenuScreen implements Screen {

	private Stage stage;

	public MenuScreen(GameMain game) {
		stage = new Stage();

		Skin mySkin = game.getSkin();

		Gdx.input.setInputProcessor(stage);

		TextButton button1 = new TextButton("HOST", mySkin);
		button1.setBounds(200, 480, 300, 100);
		button1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.switchScreen(new HostScreen(game));
			}
		});

		stage.addActor(button1);

		TextButton button2 = new TextButton("JOIN", mySkin);
		button2.setBounds(200, 360, 300, 100);
		button2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.switchScreen(new JoinScreen(game));
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
		stage.getViewport().update(width, height);
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
