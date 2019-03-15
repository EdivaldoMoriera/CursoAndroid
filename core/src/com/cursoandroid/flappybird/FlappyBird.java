package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.font.GlyphJustificationInfo;
import java.util.Random;

import javax.swing.plaf.synth.SynthViewportUI;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Circle passaroCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	private ShapeRenderer shapeRenderer;// desenhar sa formas
	private Texture gameOver;


	//atributos de configuracao
	private float larguraDoDispositivo;
	private float alturaDoDispositivo;
	private int estadoDoJogo = 0;
	private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialNaVertical;
	private float posicaoMovimentoHorizontal;
	private float espacoEntreCanos;
	private float deltaTime;// batimento da asa
	private Random numeroRandomico;//tamanho do cano variacao
	private float alturaEntreCanosRandomico;//espaço para o passaro passar
	private BitmapFont fonte;//fonte do contador
	private BitmapFont mensagem;//escreve na tela
	private boolean marcouPonto = false;// por padrao

	//Camera
	private OrthographicCamera camera;
	private Viewport viewport;
	private  final float VIRTUAL_WIDITH = 768;
	private  final float VIRTUAL_HEIGHT = 1024;




	@Override
	public void create() {
		batch = new SpriteBatch();
		numeroRandomico = new Random();
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);//configurar fonte tamnho 6

		mensagem = new BitmapFont();
		mensagem.setColor(Color.WHITE);
		mensagem.getData().setScale(3);

		passaroCirculo = new Circle();
		retanguloCanoTopo = new Rectangle();
		retanguloCanoBaixo = new Rectangle();
		shapeRenderer = new ShapeRenderer();


		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");


		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");
		fundo = new Texture("fundo.png");
		gameOver = new Texture("game_over.png");

		//configurações da camera
		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDITH/2, VIRTUAL_HEIGHT/2,0);
		viewport  = new StretchViewport(VIRTUAL_WIDITH, VIRTUAL_HEIGHT, camera);
		larguraDoDispositivo = VIRTUAL_WIDITH;//largura virtual
		alturaDoDispositivo = VIRTUAL_HEIGHT;//altura virtual
		//larguraDoDispositivo = Gdx.graphics.getWidth();//tamnho real
		//alturaDoDispositivo = Gdx.graphics.getHeight();tamanho real

		posicaoInicialNaVertical = alturaDoDispositivo / 2;
		posicaoMovimentoHorizontal = larguraDoDispositivo;
		espacoEntreCanos = 300;
	}

	@Override
	public void render() {

		camera.update();

		//limpar frames anteriores
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 10;
		if (variacao > 2) variacao = 0;

		if (estadoDoJogo == 0) {//jogo parado
			if (Gdx.input.justTouched()) {
				estadoDoJogo = 1;//jogo iniciado
			}
		} else {
			//inicializar
			velocidadeQueda++;
			if (posicaoInicialNaVertical > 0 || velocidadeQueda < 0)
				posicaoInicialNaVertical = posicaoInicialNaVertical - velocidadeQueda;

			if (estadoDoJogo == 1) {
				posicaoMovimentoHorizontal -= deltaTime * 200;
				//Gdx.app.log("variação", "variacao: "+Gdx.graphics.getDeltaTime());
				if (Gdx.input.justTouched()) {
					//Gdx.app.log("toque", "toque na tela");
					velocidadeQueda = -15;
				}

				//verific se o cano saiu inteiramente da tela
				if (posicaoMovimentoHorizontal < -canoTopo.getWidth()) {
					posicaoMovimentoHorizontal = larguraDoDispositivo;
					alturaEntreCanosRandomico = numeroRandomico.nextInt(400) - 200;
					marcouPonto = false;
				}

				//verificar a pontuação

				if (posicaoMovimentoHorizontal < 120) {
					if (!marcouPonto) {
						pontuacao++;
						marcouPonto = true;
					}

				}

			} else {//tela de gamer over

			    if (Gdx.input.justTouched()){
			        estadoDoJogo = 0;
			        pontuacao = 0;
			        velocidadeQueda = 0;
			        posicaoInicialNaVertical = alturaDoDispositivo/2;
                    posicaoMovimentoHorizontal = larguraDoDispositivo;

                }

			}

		}

		batch.begin();

			//configurar os dados de projeção da camera

		batch.setProjectionMatrix(camera.combined);

		batch.draw(fundo, 0, 0, larguraDoDispositivo, alturaDoDispositivo);
		batch.draw(canoTopo, posicaoMovimentoHorizontal, alturaDoDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomico);
		batch.draw(canoBaixo, posicaoMovimentoHorizontal, alturaDoDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2
				+ alturaEntreCanosRandomico);
		batch.draw(passaros[(int) variacao], 120, posicaoInicialNaVertical);
		fonte.draw(batch, String.valueOf(pontuacao), larguraDoDispositivo / 2, alturaDoDispositivo - 50);

		if (estadoDoJogo == 2) {
			batch.draw(gameOver, larguraDoDispositivo / 2 - gameOver.getWidth() / 2, alturaDoDispositivo / 2);
			mensagem.draw(batch, "Toque na tela para reiniciar o jogo",larguraDoDispositivo/2 - 200,
					alturaDoDispositivo/2 - gameOver.getHeight()/2);
		}
		batch.end();

		passaroCirculo.set(120 + passaros[0].getWidth() / 2, posicaoInicialNaVertical +
				passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

		retanguloCanoBaixo = new Rectangle(
				posicaoMovimentoHorizontal, alturaDoDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2
				+ alturaEntreCanosRandomico, canoBaixo.getWidth(), canoBaixo.getHeight()

		);

		retanguloCanoTopo = new Rectangle(
				posicaoMovimentoHorizontal, alturaDoDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomico
				, canoTopo.getWidth(), canoTopo.getHeight()

		);

		//desenhar formas

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y,retanguloCanoBaixo.width,retanguloCanoBaixo.height);
		shapeRenderer.rect(retanguloCanoTopo.x, retanguloCanoTopo.y,retanguloCanoTopo.width, retanguloCanoTopo.height );
		shapeRenderer.end();*/

		//Teste colisão todo vez que o passaro se sobrepor ao cano o app vai mostrar que houve colisão
		if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo)
		 || posicaoInicialNaVertical <= 0 || posicaoInicialNaVertical >= alturaDoDispositivo) {
		//Gdx.app.log("colsão", "ouve colisão");

			estadoDoJogo = 2;

		}

	}
	@Override

	public void resize(int width, int height){
		viewport.update(width, height);

	}
}