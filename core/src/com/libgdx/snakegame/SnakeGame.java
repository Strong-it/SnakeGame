package com.libgdx.snakegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class SnakeGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture snakeHead;
	private Texture apple;
	private Texture snakeBody;
	private static final float MOVE_TIME = 0.5f; // ̰����ÿ�����Լ��ƶ�һ������
	private float timer = MOVE_TIME;
	private static final int SNAKE_MOVEMENT = 32; // ̰���߱����СΪ32*32��ÿ���ƶ�һ��
	private int snakeX = 0, snakeY = 0;
	private boolean appleAvailable = false;
	private int appleX, appleY;
	private float delta = 0;

	private Array<BodyPart> bodyParts = new Array<BodyPart>();

	@Override
	public void create() {
		batch = new SpriteBatch();
		snakeHead = new Texture(Gdx.files.internal("snakehead.png"));
		apple = new Texture(Gdx.files.internal("apple.png"));
		snakeBody = new Texture(Gdx.files.internal("snakeBody.png"));
	}

	@Override
	public void render() {
		delta = Gdx.graphics.getDeltaTime();

		queryInput();
		timer -= delta;
		if (timer <= 0) {
			timer = MOVE_TIME;
			moveSnake();
			checkForOutOfBounds();
			updateBodyPartsPosition();
		}

		clearScreen();
		checkAppleCollision();
		checkAndPlaceApple();
		
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
	 * ���ƻ���Ƿ���̰���߷�����ײ��λ���غϣ���������ײ ��Ϊÿ���ƶ�����32������
	 */
	private void checkAppleCollision() {

		if (appleAvailable && appleX == snakeX && appleY == snakeY) {
			// ��̰���߳���ƻ��֮�����̰���ߵ����壬��Ȼ��insert��Body����ĵ�һ��
			// ����ͨ��updateBodyPartsPosition�������Ϊ���һ��
			BodyPart bodyPart = new BodyPart(snakeBody);
			bodyPart.updateBodyPosition(snakeX, snakeY);
			bodyParts.insert(0, bodyPart);
			appleAvailable = false;
		}
	}

	private void draw() {

		batch.begin();
		batch.draw(snakeHead, snakeX, snakeY);
		for (BodyPart bodyPart : bodyParts) {
			bodyPart.draw(batch);
		}
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
		// ��ʵƻ��ֻ��һ����ͨ��appleAvailable����ƻ����λ�ã����ı�λ��
		if (!appleAvailable) {
			do {
				appleX = MathUtils.random(Gdx.graphics.getWidth() / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
				appleY = MathUtils.random(Gdx.graphics.getHeight() / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
				appleAvailable = true;
			} while (appleX == snakeX && appleY == snakeY); // ���ƻ����λ�ú�̰���ߵ�λ���ظ�����ô��Ҫ��������һ��ƻ��
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

	private int snakeXBeforeUpdate = 0, snakeYBeforeUpdate = 0;

	private void moveSnake() {
		// ����ʼ���ƶ���̰���ߵ�ǰ��λ��
		snakeXBeforeUpdate = snakeX;
		snakeYBeforeUpdate = snakeY;

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

	private void updateBodyPartsPosition() {
		if (bodyParts.size > 0) {
			// ÿ��ֻ���ƶ�һ������
			BodyPart bodyPart = bodyParts.removeIndex(0);
			bodyPart.updateBodyPosition(snakeXBeforeUpdate, snakeYBeforeUpdate);
			bodyParts.add(bodyPart);
		}
	}
	
	private void queryInput() {

		boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
		boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

		if (lPressed)
			snakeDirection = LEFT;
		if (rPressed)
			snakeDirection = RIGHT;
		if (uPressed)
			snakeDirection = UP;
		if (dPressed)
			snakeDirection = DOWN;
	}

	public class BodyPart {
		private int x, y;
		private Texture texture;
		
		public BodyPart(Texture texture) {
			this.texture = texture;
		}
		
		public void updateBodyPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void draw(Batch batch) {
			// ��̰�����ƶ���ͷ֮�󣬲Ż�������
			if (!(x == snakeX && y == snakeY)) {
				batch.draw(texture, x, y);
			}
		}
	}
}
