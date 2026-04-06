package com.deivi.monstersurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter{
    private static final float WORD_WIDTH = 16f;
    private static final float WORD_HEIGHT = 9f;

    private final Batch batch;
    private final Texture bgdTexture = new Texture(Gdx.files.internal("bgd.png"));
    private final Texture playerTexture = new Texture(Gdx.files.internal("player.png"));
    private final Viewport gameViewport = new ExtendViewport(WORD_WIDTH, WORD_HEIGHT);
    private final Vector2 inputMovement = new Vector2();

    private final Player player = new Player(
        WORD_WIDTH / 2f, 
        WORD_HEIGHT / 2f, 
        gameViewport, 
        playerTexture
    );

    public GameScreen(MonstersSuvivor game) {
        this.batch = game.getBatch();

        bgdTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
    }

    @Override
    public void show() {
        resetGame();
    }

    private void resetGame() {
        player.reset(WORD_WIDTH / 2, WORD_HEIGHT / 2);
    }

    private void processInput() {
        inputMovement.setZero();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            inputMovement.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            inputMovement.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputMovement.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputMovement.x += 1;
        }

        inputMovement.nor();
        player.changeDirection(inputMovement);
    }

    @Override
    public void render(float deltaTime) {
        processInput();
        updateLogic(deltaTime);

        ScreenUtils.clear(Color.BLACK);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        batch.begin();

        drawBackground();
        player.draw(batch);

        batch.end();
    }

    private void updateLogic(float deltaTime) {
        player.update(deltaTime);
    }

    private void drawBackground() {
        float u2 = gameViewport.getWorldWidth() / WORD_WIDTH;
        float v2 = gameViewport.getWorldHeight() / WORD_HEIGHT;
        batch.draw(bgdTexture,
            0, 0, 
            gameViewport.getWorldWidth(), gameViewport.getWorldHeight(),
            0, 0,
            u2, v2
        );
    }

    @Override
    public void dispose() {
        bgdTexture.dispose();
        playerTexture.dispose();
    }
}
