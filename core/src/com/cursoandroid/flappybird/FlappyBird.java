package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private  SpriteBatch batch;
	private Texture[] passaros;
	private  Texture fundo;
	private  Texture canoBaixo;
	private Texture canoTopo;


	//atributos de configuracao
	private int larguraDoDispositivo;
	private  int alturaDoDispositivo;
	private int estadoDoJogo = 0;
	private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialNaVertical;
	private float posicaoMovimentoHorizontal;
	private  float espacoEntreCanos;
	private float deltaTime;// batimento da asa
	private Random numeroRandomico;//tamanho do cano variacao
	private float alturaEntreCanosRandomico;//espaço para o passaro passar
	private BitmapFont fonte;//fonte do contador
	private boolean marcouPonto = false;// por padrao
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		numeroRandomico = new Random();
		fonte  = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);//configurar fonte tamnho 6
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");


		fundo = new Texture("fundo.png");

		larguraDoDispositivo = Gdx.graphics.getWidth();
		alturaDoDispositivo = Gdx.graphics.getHeight();
		posicaoInicialNaVertical = alturaDoDispositivo/2;
		posicaoMovimentoHorizontal = larguraDoDispositivo;
		espacoEntreCanos = 200;
	}

	@Override
	public void render () {

		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 10;
		if (variacao > 2) variacao = 0;

		if (estadoDoJogo == 0){
			if (Gdx.input.justTouched()){
				estadoDoJogo = 1;
			}
		}else {

			posicaoMovimentoHorizontal -= deltaTime * 200;
			//Gdx.app.log("variação", "variacao: "+Gdx.graphics.getDeltaTime());
			velocidadeQueda++;

			if (Gdx.input.justTouched()) {
				//Gdx.app.log("toque", "toque na tela");
				velocidadeQueda = -10;

			}

			if (posicaoInicialNaVertical > 0 || velocidadeQueda < 0)
				posicaoInicialNaVertical = posicaoInicialNaVertical - velocidadeQueda;


			//verific se o cano saiu inteiramente da tela
			if (posicaoMovimentoHorizontal < -canoTopo.getWidth()) {
				posicaoMovimentoHorizontal = larguraDoDispositivo;
				alturaEntreCanosRandomico = numeroRandomico.nextInt(400) - 200;
				marcouPonto = false;
			}

			//verificar a pontuação

			if (posicaoMovimentoHorizontal< 120){
				if (!marcouPonto){
					pontuacao++;
					marcouPonto = true;
				}

			}
		}
		batch.begin();


		batch.draw(fundo, 0,0,larguraDoDispositivo, alturaDoDispositivo);
		batch.draw(canoTopo, posicaoMovimentoHorizontal,alturaDoDispositivo/2 + espacoEntreCanos/2 + alturaEntreCanosRandomico);
		batch.draw(canoBaixo,posicaoMovimentoHorizontal,alturaDoDispositivo/2 - canoBaixo.getHeight() - espacoEntreCanos/2
				+ alturaEntreCanosRandomico);
		batch.draw(passaros[(int)variacao],120,posicaoInicialNaVertical);
		fonte.draw(batch, String.valueOf(pontuacao),larguraDoDispositivo/2, alturaDoDispositivo - 50);


		batch.end();


	}

}
