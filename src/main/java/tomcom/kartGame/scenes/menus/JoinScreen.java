package tomcom.kartGame.scenes.menus;

import tomcom.kartGame.config.GameConfig;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.scenes.Map1;
import tomcom.kartGame.systems.Network.ClientCommands;
import tomcom.kartGame.systems.Network.ClientSystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

	private static final float SMALL_BUTTON_SIZE = 100;

	private static final int OFFSET = 30;

	private Stage stage;

	private String errorMsg = "";

	private BitmapFont font;
	private Engine engine;

	public JoinScreen(GameMain game) {
		stage = new Stage();

		font = new BitmapFont();
		engine = new Engine();
		Skin mySkin = game.getSkin();

		Gdx.input.setInputProcessor(stage);

		TextField textField = new TextField("", mySkin);
		textField.setBounds(200, 300, 400, SMALL_BUTTON_SIZE);

		stage.addActor(textField);
		TextButton back = new TextButton("Back", mySkin);
		back.setBounds(GameConfig.SCREEN_WIDTH - SMALL_BUTTON_SIZE - OFFSET,
				OFFSET, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.switchScreen(new MenuScreen(game));
			}
		});
		stage.addActor(back);
		TextButton button2 = new TextButton("JOIN", mySkin);
		button2.setBounds(200, 160, 300, SMALL_BUTTON_SIZE);
		button2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				engine.addSystem(new ClientSystem(textField.getText(),54321));
				back.remove();
				textField.remove();
				Label connected =  new Label("Connected! Please wait for the host to start the game!", mySkin);
				connected.setBounds(200, 160, 300, 100);
				stage.addActor(connected);
				button2.remove();
				ClientCommands.loadLevelReceived.add(new Listener<Integer>() {

					@Override
					public void receive(Signal<Integer> arg0, Integer arg1) {
						game.setScreen(new Map1(game, engine));
						
					}
					
				});
				errorMsg = " Could not connect to " + textField.getText();

			}
		});



		stage.addActor(button2);

		

	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		engine.update(delta);
		stage.act(delta);

		Gdx.gl.glClearColor(0.914f, 0.933f, 0.957f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();

		stage.getBatch().begin();

		font.setColor(Color.BLACK);
		font.draw(stage.getBatch(),
				" Enter Host's IP Address Here (f.ex.: 192.10.23.43)", 210, 550);

		font.setColor(Color.RED);
		font.draw(stage.getBatch(), errorMsg, 210, 440);

		stage.getBatch().end();

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
