package com.deivi.monstersurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    private static final float WORD_WIDTH = 16f;
    private static final float WORD_HEIGHT = 9f;
    private static final float ENEMY_SPAWN_INTERVAL = 1.5f;
    private static final float DAMAGE_PER_SECOND = 1f;

    private final Batch batch;
    private final BitmapFont font;

    private final Texture bgdTexture = new Texture(Gdx.files.internal("bgd.png"));
    private final Texture playerTexture = new Texture(Gdx.files.internal("player.png"));
    private final Texture enemyTexture = new Texture(Gdx.files.internal("slime.png"));
    private final Viewport gameViewport = new ExtendViewport(WORD_WIDTH, WORD_HEIGHT);
    private final Viewport uiViewport = new ScreenViewport();
    private final GlyphLayout layout = new GlyphLayout();
    private final Vector2 inputMovement = new Vector2();

    private final Player player = new Player(
            WORD_WIDTH / 2f,
            WORD_HEIGHT / 2f,
            gameViewport,
            playerTexture);

    private final Array<Enemy> enemies = new Array<>();
    private float enemySpawnTimer;

    public GameScreen(MonstersSuvivor game) {
        this.batch = game.getBatch();
        this.font = game.getFont();

        bgdTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void show() {
        resetGame();
    }

    private void resetGame() {
        player.reset(WORD_WIDTH / 2f, WORD_HEIGHT / 2f);

        enemies.clear();
        enemySpawnTimer = 0f;
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
        if (player.isAlive()) {
            processInput();
            updateLogic(deltaTime);
            checkCollision(deltaTime);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetGame();
        }
        draw();
    }

    private void checkCollision(float deltaTime) {
        int numHits = 0;
        for (Enemy enemy : enemies) {
            if (player.overlaps(enemy)) {
                ++numHits;
            }
        }

        if (numHits > 0) {
            player.subLife(DAMAGE_PER_SECOND * numHits * deltaTime);
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        batch.begin();

        drawBackground();
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }
        player.draw(batch);

        batch.end();

        uiViewport.apply();
        batch.setProjectionMatrix(uiViewport.getCamera().combined);
        batch.begin();
        font.draw(batch, "Life: " + String.format("%.1f", player.getLife()), 20f, uiViewport.getWorldHeight() - 60);
        if (player.isDead()) {
            layout.setText(font, "GAME OVER");
            font.draw(batch, layout, uiViewport.getWorldWidth() / 2 - layout.width / 2,
                    uiViewport.getWorldHeight() / 2 + 40);
            layout.setText(font, "Press R to Restart");
            font.draw(batch, layout, uiViewport.getWorldWidth() / 2 - layout.width / 2,
                    uiViewport.getWorldHeight() / 2 - 30);
        }
        batch.end();
    }

    private void updateLogic(float deltaTime) {
        player.update(deltaTime);

        enemySpawnTimer += deltaTime;
        if (enemySpawnTimer >= ENEMY_SPAWN_INTERVAL) {
            enemySpawnTimer = 0f;
            Enemy enemy = Enemy.spawn(gameViewport, enemyTexture, player);
            enemies.add(enemy);
        }

        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
        }
    }

    private void drawBackground() {
        float u2 = gameViewport.getWorldWidth() / WORD_WIDTH;
        float v2 = gameViewport.getWorldHeight() / WORD_HEIGHT;
        batch.draw(bgdTexture,
                0, 0,
                gameViewport.getWorldWidth(), gameViewport.getWorldHeight(),
                0, 0,
                u2, v2);
    }

    @Override
    public void dispose() {
        bgdTexture.dispose();
        playerTexture.dispose();
        enemyTexture.dispose();
    }
}
