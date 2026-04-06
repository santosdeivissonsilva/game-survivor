package com.deivi.monstersurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ControlScreen extends ScreenAdapter {
    private final MonstersSuvivor game;
    private final Batch batch;
    private final BitmapFont font;
    private final Viewport viewport = new ScreenViewport();
    private final GlyphLayout layout = new GlyphLayout();

    public ControlScreen(MonstersSuvivor game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
            return;
        }

        ScreenUtils.clear(Color.BLACK);

        float centerx = viewport.getWorldWidth() / 2;
        float y = viewport.getWorldHeight() / 2 + 100;
        
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        layout.setText(font, "CONTROLS");
        font.draw(batch, layout, centerx - layout.width / 2, y);
        y -= 50;

        layout.setText(font, "W / S / A / D - Move");
        font.draw(batch, layout, centerx - layout.width / 2, y);
        y -= 40;

        layout.setText(font, "R - Restart (when dead)");
        font.draw(batch, layout, centerx - layout.width / 2, y);
        y -= 70;

        layout.setText(font, "Press SPACE to start");
        font.draw(batch, layout, centerx - layout.width / 2, y);

        batch.end();
    }
}
