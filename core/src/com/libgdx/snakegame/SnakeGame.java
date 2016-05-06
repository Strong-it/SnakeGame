package com.libgdx.snakegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class SnakeGame extends ApplicationAdapter {
	
	private SpriteBatch batch;
    private Texture snakeHead;
    private Texture apple;
    private static final float MOVE_TIME = 0.5f;  // ̰����ÿ�����Լ��ƶ�һ������
    private float timer = MOVE_TIME;
    private static final int SNAKE_MOVEMENT = 32; // ̰���߱����СΪ32*32��ÿ���ƶ�һ��
    private int snakeX = 0, snakeY = 0;
    private boolean appleAvailable = false;
    private int appleX, appleY;
	private float delta = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
        snakeHead = new Texture(Gdx.files.internal("snakehead.png"));
        apple = new Texture(Gdx.files.internal("apple.png"));
	}

	@Override
	public void render () {
		delta = Gdx.graphics.getDeltaTime();
		
		queryInput();
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            moveSnake();
            checkForOutOfBounds();
        }

        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        checkAppleCollision();
        checkAndPlaceApple();
        clearScreen();
        draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		snakeHead.dispose();
		apple.dispose();
	}
	
	/**
	 * ���ƻ���Ƿ���̰���߷�����ײ��λ���غϣ���������ײ
	 * ��Ϊÿ���ƶ�����32������
	 */
    private void checkAppleCollision() {

        if (appleAvailable && appleX == snakeX && appleY == snakeY) {
            appleAvailable = false;
        }
    }

    private void draw() {

        batch.begin();
        batch.draw(snakeHead, snakeX, snakeY);
        if (appleAvailable) {
            batch.draw(apple, appleX, appleY);
        }
        batch.end();
    }
    
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    
    private void checkAndPlaceApple() {

        if (!appleAvailable) {
            do {
                appleX = MathUtils.random(Gdx.graphics.getWidth() / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
                appleY = MathUtils.random(Gdx.graphics.getHeight() / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
                appleAvailable = true;
            } while (appleX == snakeX && appleY == snakeY);  // ���ƻ����λ�ú�̰���ߵ�λ���ظ�����ô��Ҫ��������һ��ƻ��
        }
    }
    
    /**
     * ���̰��������λ�õı߽�
     */
    private void checkForOutOfBounds() {
        if (snakeX >= Gdx.graphics.getWidth()) { // ���̰���������ұߣ���ô�ƶ��������
            snakeX = 0;
        }
        if (snakeX < 0) {
            snakeX = Gdx.graphics.getWidth() - SNAKE_MOVEMENT;
        }
        if (snakeY >= Gdx.graphics.getHeight()) {
            snakeY = 0;
        }
        if (snakeY < 0) {
            snakeY = Gdx.graphics.getHeight() - SNAKE_MOVEMENT;
        }
    }
    
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private int snakeDirection = RIGHT;

    private void moveSnake() {
        switch (snakeDirection) {
            case RIGHT: {
                snakeX += SNAKE_MOVEMENT;
                return;
            }
            case LEFT: {
                snakeX -= SNAKE_MOVEMENT;
                return;
            }
            case UP: {
                snakeY += SNAKE_MOVEMENT;
                return;
            }
            case DOWN: {
                snakeY -= SNAKE_MOVEMENT;
                return;
            }
        }
    }

    private void queryInput() {

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (lPressed) snakeDirection = LEFT;
        if (rPressed) snakeDirection = RIGHT;
        if (uPressed) snakeDirection = UP;
        if (dPressed) snakeDirection = DOWN;
    }
}
