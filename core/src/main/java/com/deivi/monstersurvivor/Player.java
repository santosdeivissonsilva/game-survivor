package com.deivi.monstersurvivor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends GameObject {
    private static final float SCALE = 1 / 32f;
    private static final int LIFE = 5;
    private static final float SPEED = 2f;

    private final Viewport gamViewport;
    private float life = LIFE;
    private final Vector2 moveDirection = new Vector2();
    
    public Player(float x, float y, Viewport gamViewport, Texture texture) {
        super(x, y, texture.getWidth() * SCALE, texture.getHeight() * SCALE, texture);
        this.gamViewport = gamViewport;
    }

    public void reset(float x, float y) {
        rect.setPosition(x, y);
        life = LIFE;
    }

    @Override
    void update(float deltaTime) {
        move(deltaTime);
    }

    private void move(float deltaTime) {
        if (moveDirection.isZero()) return;

        float newX = rect.getX() + moveDirection.x * SPEED * deltaTime;
        float newY = rect.getY() + moveDirection.y * SPEED * deltaTime;
        
        newX = MathUtils.clamp(newX, 0, gamViewport.getWorldHeight() - rect.getWidth());
        newY = MathUtils.clamp(newY, 0, gamViewport.getWorldHeight() - rect.getHeight());

        rect.setPosition(newX, newY);
    }

    public void changeDirection(Vector2 direction) {
        moveDirection.set(direction);
    }

}
