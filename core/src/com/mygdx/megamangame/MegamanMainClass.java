package com.mygdx.megamangame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screen.MainGameScreen;

public class MegamanMainClass extends Game {

	public SpriteBatch batch;

	//Declaramos el assetmanager.
	public static AssetManager assetManager;

	//Declaramos el ancho y el alto de la resolucion del mundo virtual.
	public final static int Virtual_Width = 800;
	public final static int Virtual_Height = 600;

	//Declaramos cuantos pixels corresponden a un metro.
	public final static float PixelsPerMeters = 100;

	//Creamos valores para los filtros de box2d para colisiones.
	public final static short DEFAULT_BIT = 1;
	public final static short FLOOR_BIT = 2;
	public final static short MEGAMAN_BIT = 4;
	public final static short FLYINGGROUND_BIT = 8;
	public final static short COIN_BIT = 16;
	public final static short DESTROYED_BIT = 32;

	@Override
	public void create () {

		//Declaramos el spritebatch que utilizaremos para dibujar en pantalla.
		batch = new SpriteBatch();

		//Creamos el assetManager.
		assetManager = new AssetManager();
		assetManager.load("audio/introsong.mp3", Sound.class);

		assetManager.finishLoading();
		//Ponemos la pantalla principal del juego.
		setScreen(new MainGameScreen(this));


	}

	@Override
	public void render () {
		//Pedimos que se dibuje todo lo que corresponda en la jerarquia.
		super.render();
	}
	
	@Override
	public void dispose () {

		//Eliminamos el spritebatch.
		batch.dispose();
	}
}
