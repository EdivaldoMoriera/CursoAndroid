package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	private  SpriteBatch batch;
	private Texture[] passaros;
	private  Texture fundo;

	//atributos de configuracao
	private int larguraDoDispositivo;
	private  int alturaDoDispositivo;
	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialNaVertical;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

		larguraDoDispositivo = Gdx.graphics.getWidth();
		alturaDoDispositivo = Gdx.graphics.getHeight();
		posicaoInicialNaVertical = alturaDoDispositivo/2;
	}

	@Override
	public void render () {
		variacao += Gdx.graphics.getDeltaTime()*10;
		//Gdx.app.log("variação", "variacao: "+Gdx.graphics.getDeltaTime());
		velocidadeQueda++;

		if (variacao > 2) variacao = 0;
		batch.begin();

		if (posicaoInicialNaVertical > 0)
		posicaoInicialNaVertical = posicaoInicialNaVertical -  velocidadeQueda;

		batch.draw(fundo, 0,0,larguraDoDispositivo, alturaDoDispositivo);
		batch.draw(passaros[(int)variacao],30,posicaoInicialNaVertical);


		batch.end();


	}

}
