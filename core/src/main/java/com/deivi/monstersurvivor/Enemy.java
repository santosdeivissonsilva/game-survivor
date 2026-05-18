package com.deivi.monstersurvivor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Enemy extends GameObject {
    private static final float SCALE = 1 / 32f;
    private static final float SPEED = 1.5f;

    private final Player player;

    public Enemy(float x, float y, Texture texture, Player player) {
        super(x, y, texture.getWidth() * SCALE, texture.getHeight() * SCALE, texture);
        this.player = player;
    }

    void update(float deltaTime) {
        Vector2 direction = player.getCenter(TMP_VEC2)
            .sub(rect.x + rect.width / 2, rect.y + rect.height / 2)
            .nor()
            .scl(SPEED * deltaTime);
        
        rect.setPosition(rect.getX() + direction.x, rect.getY() + direction.y);
    }

    public static Enemy spawn(Viewport viewport, Texture texture, Player player) {
        int edge = MathUtils.random(3);
        float x, y;

        switch (edge) {
            case 0 -> {
                // top
                x = MathUtils.random(0, 1) * viewport.getWorldWidth();
                y = viewport.getWorldHeight();
            }
            
            case 1 -> {
                // right
                x = viewport.getWorldWidth();
                y = MathUtils.random(0, 1) * viewport.getWorldHeight();
            }

            case 2 -> {
                // bottom
                x = MathUtils.random(0, 1) * viewport.getWorldWidth();
                y = -texture.getHeight() * SCALE;
            }

            default -> {
                // left
                x = -texture.getWidth() * SCALE;
                y = MathUtils.random(0, 1) * viewport.getWorldHeight();
            }
        }

        return new Enemy(x, y, texture, player);
    }
}