package net.ws3.hard.screens;

import net.ws3.hard.BlueCircleAccessor;
import net.ws3.hard.CircleWrapperAccessor;
import net.ws3.hard.HardGame;
import net.ws3.hard.controller.HardController;
import net.ws3.hard.model.CircleWrapper;
import net.ws3.hard.model.HardModel;
import net.ws3.hard.model.Level;
import net.ws3.hard.view.HardRenderer;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen implements Screen, InputProcessor{
	private HardGame game;
	private HardModel model;
	private HardRenderer renderer;
	private HardController controller;
	private InputMultiplexer im;
	private Stage stage;
	private Skin touchpadSkin;
	private Touchpad touchpad;
	private TouchpadStyle touchpadStyle;
	private Label deathCount;
	private Button soundButton;
	private Skin skin;
	
	public GameScreen(HardGame game, Level level){
		this.game = game;
		Tween.registerAccessor(Circle.class, new BlueCircleAccessor());
		Tween.registerAccessor(CircleWrapper.class, new CircleWrapperAccessor());
		
		model = new HardModel();
		model.loadLevel(level);
		
		renderer = new HardRenderer(model, false);
		controller = new HardController(model);
		
		touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground", new Texture(Gdx.files.internal("touchBackground.png")));
		touchpadSkin.add("touchKnob", new Texture(Gdx.files.internal("touchKnob.png")));
		touchpadStyle = new TouchpadStyle();
		touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
		touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
		touchpad = new Touchpad(10, touchpadStyle);
		touchpad.setBounds(640, 10, 150, 150);
		stage = new Stage(new StretchViewport(800, 480));
		stage.addActor(touchpad);
		
		skin = new Skin(Gdx.files.internal("hud/hud.json"), new TextureAtlas(Gdx.files.internal("hud/hud.atlas")));
		
		deathCount = new Label("" + model.getDeathCount(), skin, "deathCount");
		deathCount.setAlignment(Align.center);
		deathCount.setBounds(0, 401, 61, 79);
		stage.addActor(deathCount);
		
		soundButton = new Button(skin, "sound");
		soundButton.setBounds(800 - 64, 480 - 64, 64, 64);
		stage.addActor(soundButton);
		
		im = new InputMultiplexer();
		Array<InputProcessor> processors = new Array<InputProcessor>();
		processors.add(this);
		processors.add(stage);
		im.setProcessors(processors);
		
		Gdx.input.setInputProcessor(im);
	}
	
	public void render(float delta){
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		controller.setTouchpadPercents(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
		model.updateTweens(delta);
		controller.update(delta);
		model.collisionControl();
		model.eatYellowFuckers();
		model.savePosition();
		deathCount.setText("" + model.getDeathCount());
		
		if(model.isEndGame())
			game.setScreen(new LevelScreen(game));
		renderer.render();
		stage.act(delta);
		stage.draw();
	}
	
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		renderer.dispose();
		stage.dispose();
		skin.dispose();
		touchpadSkin.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.A)
			controller.leftPressed();
		if(keycode == Keys.D)
			controller.rightPressed();
		if(keycode == Keys.W)
			controller.upPressed();
		if(keycode == Keys.S)
			controller.downPressed();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.A)
			controller.leftReleased();
		if(keycode == Keys.D)
			controller.rightReleased();
		if(keycode == Keys.W)
			controller.upReleased();
		if(keycode == Keys.S)
			controller.downReleased();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}