package com.deivi.monstersurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter{
    private static final float WORD_WIDTH = 16f;
    private static final float WORD_HEIGHT = 9f;

    private final Batch batch;
    private final Texture bgdTexture = new Texture(Gdx.files.internal("bgd.png"));
    private final Viewport gameViewport = new ExtendViewport(WORD_WIDTH, WORD_HEIGHT);

    public GameScreen(MonstersSuvivor game) {
        this.batch = game.getBatch();

        bgdTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        batch.begin();

        float u2 = gameViewport.getWorldWidth() / WORD_WIDTH;
        float v2 = gameViewport.getWorldHeight() / WORD_HEIGHT;
        batch.draw(bgdTexture,
            0, 0, 
            gameViewport.getWorldWidth(), gameViewport.getWorldHeight(),
            0, 0,
            u2, v2
        );

        batch.end();
    }

    @Override
    public void dispose() {
        bgdTexture.dispose();
    }
}
