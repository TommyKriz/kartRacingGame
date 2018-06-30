package tomcom.kartGame.scenes.menus;

import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.game.resources.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Enter Host's IP-Address, join if available, display error message if not
 * 
 * @author Tommy
 *
 */
public class JoinScreen implements Screen {

	private Stage stage;

	private String errorMsg = "";

	private BitmapFont font;

	public JoinScreen(GameMain game) {
		stage = new Stage();

		font = new BitmapFont();

		Skin mySkin = ResourceManager.getSkin();

		Gdx.input.setInputProcessor(stage);

		TextField textField = new TextField("", mySkin);
		textField.setBounds(200, 300, 400, 100);

		stage.addActor(textField);

		TextButton button2 = new TextButton("JOIN", mySkin);
		button2.setBounds(200, 160, 300, 100);
		button2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				// TODO: new Screen in HOST Mode
				// new XXXlevel(false);

				// TODO: check for refused / not working connection
				errorMsg = " Could not connect to " + textField.getText();

			}
		});

		stage.addActor(button2);

		TextButton back = new TextButton("Back", mySkin);
		back.setBounds(500, 60, 100, 100);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		stage.addActor(back);

	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		stage.act(delta);

		Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();

		stage.getBatch().begin();

		font.setColor(Color.WHITE);
		font.draw(stage.getBatch(),
				" Enter Host's IP Address Here (f.ex.: 192.10.23.43)", 210, 550);

		font.setColor(Color.RED);
		font.draw(stage.getBatch(), errorMsg, 210, 440);

		stage.getBatch().end();

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
