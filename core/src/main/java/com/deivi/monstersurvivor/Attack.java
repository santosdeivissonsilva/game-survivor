package com.deivi.monstersurvivor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Attack extends GameObject{
    private static final float DURATION = 0.4f;
    private static final float SIZE = 1;

    private float lifeSpan = DURATION;
    private final Animation<Texture> animation;

    public Attack(Vector2 position, Vector2 direction, Animation<Texture> animation) {
        super(
                position.x - SIZE / 2 + direction.x * SIZE * 0.75f,
                position.y - SIZE / 2 + direction.y * SIZE * 0.75f,
                SIZE, SIZE);
        this.animation = animation;
    }

    @Override
    void update(float deltaTime) {
        this.lifeSpan -= deltaTime;
    }

    public boolean isDone() {
        return lifeSpan <= 0;
    }

    @Override
    public void draw (Batch batch) {
        float animationDuration = animation.getAnimationDuration();
        float animationPerc = 1f - (Math.max(0f, lifeSpan) / DURATION);
        float stateTime = animationDuration * animationPerc;
        texture = animation.getKeyFrame(stateTime, true);
        super.draw(batch);
    }
}
