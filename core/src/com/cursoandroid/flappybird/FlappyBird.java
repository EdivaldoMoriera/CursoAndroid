package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	private  SpriteBatch batch;
	private Texture passaro;
	private  Texture fundo;

	//atributos de configuracao
	private int movimento = 0;
	private int larguraDoDispositivo;
	private  int alturaDoDispositivo;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");
		fundo = new Texture("fundo.png");

		larguraDoDispositivo = Gdx.graphics.getWidth();
		alturaDoDispositivo = Gdx.graphics.getWidth();
	}

	@Override
	public void render () {
		movimento ++;
		batch.begin();

		batch.draw(fundo, 0,0,larguraDoDispositivo, alturaDoDispositivo);
		batch.draw(passaro,movimento,400);


		batch.end();


	}

}