package com.mygdx.megamangame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screen.MainGameScreen;

public class MegamanMainClass extends Game {

	public SpriteBatch batch;

	//Declaramos el ancho y el alto de la resolucion del mundo virtual.
	public final static int Virtual_Width = 800;
	public final static int Virtual_Height = 600;

	//Declaramos cuantos pixels corresponden a un metro.
	public final static float PixelsPerMeters = 100;

	@Override
	public void create () {

		//Ponemos la pantalla principal del juego.
		setScreen(new MainGameScreen(this));

		//Declaramos el spritebatch que utilizaremos para dibujar en pantalla.
		batch = new SpriteBatch();

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
