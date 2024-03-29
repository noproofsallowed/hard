package net.ws3.hard.controller;

import java.util.HashMap;
import java.util.Map;

import net.ws3.hard.model.HardModel;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class HardController {
	public enum Keys {
		LEFT, RIGHT, UP, DOWN
	}
	
	private HardModel model;
	private static Map<Keys, Boolean> keys;
	private float touchpadX, touchpadY;
	private boolean isTouched;
	private int touchX, touchY;
	private int draggedX, draggedY;
	private float length;
	
	public HardController(HardModel model){
		this.model = model;
		
		isTouched = false;
		
		touchpadX = 0f;
		touchpadY = 0f;
		
		keys = new HashMap<Keys, Boolean>();
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
	}
	
	public void setTouchpadPercents(float x, float y){
		touchpadX = x;
		touchpadY = y;
	}
	
	public void leftPressed(){
		keys.put(Keys.LEFT, true);
	}
	public void leftReleased(){
		keys.put(Keys.LEFT, false);
	}
	
	public void rightPressed(){
		keys.put(Keys.RIGHT, true);
	}
	public void rightReleased(){
		keys.put(Keys.RIGHT, false);
	}
	
	public void upPressed(){
		keys.put(Keys.UP, true);
	}
	public void upReleased(){
		keys.put(Keys.UP, false);
	}
	
	public void downPressed(){
		keys.put(Keys.DOWN, true);
	}
	public void downReleased(){
		keys.put(Keys.DOWN, false);
	}
	
	public void setTouchDown(int x, int y){
		touchX = x;
		touchY = y;
	}
	
	public void setTouchDragged(int x, int y){
		System.out.println("X: " + x + " - Y: " + y);
		isTouched = true;
		draggedX = x;
		draggedY = y;
	}
	
	public void setTouchUp(){
		isTouched = false;
	}
	
	public void updateTouch(){
		if(isTouched){
			setTouchDown(draggedX, draggedY);
		}
	}
	
	private void processInput(float delta, Vector2 v){
		if(keys.get(Keys.LEFT))
			v.x -= 200 * delta;
		
		if(keys.get(Keys.RIGHT))
			v.x += 200 * delta;
		
		if(keys.get(Keys.UP))
			v.y += 200 * delta;
		
		if(keys.get(Keys.DOWN))
			v.y -= 200 * delta;
		
		if(isTouched){
			/*Rectangle player = model.getPlayer();
			v.x = (touchPlayerX + draggedX - touchX - player.x) * delta * 20f;
			v.y = (touchPlayerY + draggedY - touchY - player.y) * delta * 20f;
			length = (float) Math.sqrt(v.x * v.x + v.y * v.y);
			if(length > 270 * delta){
				v.x *= (270 * delta) / length;
				v.y *= (270 * delta) / length;
			}*/
			if((draggedX - touchX) * (draggedX - touchX) + (draggedY - touchY) * (draggedY - touchY) >= 3){
				v.x = (draggedX - touchX) * delta * 40f;
				v.y = (draggedY - touchY) * delta * 40f;
				touchX = draggedX;
				touchY = draggedY;
				length = (float) Math.sqrt(v.x * v.x + v.y * v.y);
				if(length > 270 * delta){
					v.x *= (270 * delta) / length;
					v.y *= (270 * delta) / length;
				}
			}
			else
				System.out.println("draggedX: " + draggedX + " - draggedY: " + draggedY + " - touchX:" + touchX + " - touchY: " + touchY);
		}
		else{
			v.x += touchpadX * 240 * delta;
			v.y += touchpadY * 240 * delta;
		}
	}
	
	public void update(float delta){
		Vector2 destination = new Vector2(0f, 0f);
		Rectangle player = model.getPlayer();
		processInput(delta, destination);
		
		if(!model.isOutOfMap(player.x + destination.x, player.y, player.width, player.height))
			player.x += destination.x;
		if(!model.isOutOfMap(player.x, player.y + destination.y, player.width, player.height))
			player.y += destination.y;
	}
}