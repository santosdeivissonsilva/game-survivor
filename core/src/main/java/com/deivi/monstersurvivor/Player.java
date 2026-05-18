package com.deivi.monstersurvivor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends GameObject {
    private static final float SCALE = 1 / 32f;
    private static final int LIFE = 5;
    private static final float SPEED = 2f;
    private static final float ATTACK_COOLDOWN = 1.6f;

    private final Viewport gamViewport;
    private float life = LIFE;
    private final Vector2 moveDirection = new Vector2();
    private final Vector2 lastDirection = new Vector2(1,0);
    private float attackTimer;
    private final Array<Attack> attacks = new Array<>();
    private final Animation<Texture> attaclAnimation;
    private final Sound attackSfx;

    public Player(float x,
                float y,
                Viewport gamViewport,
                Texture texture,
                Animation<Texture> attackAnimation,
                Sound slashSfx) {
        super(x, y, texture.getWidth() * SCALE, texture.getHeight() * SCALE, texture);
        this.gamViewport = gamViewport;
        this.attaclAnimation = attackAnimation;
        this.attackSfx = slashSfx;
    }

    public void reset(float x, float y) {
        rect.setPosition(x, y);
        life = LIFE;
        attackTimer = ATTACK_COOLDOWN;
        attacks.clear();
    }

    @Override
    void update(float deltaTime) {
        if(canAttack(deltaTime)) {
            var playerCenter = getCenter(TMP_VEC2);
            attackSfx.play();
            attacks.add(new Attack(playerCenter, lastDirection, attaclAnimation));
        }

        var iterator = attacks.iterator();
        while (iterator.hasNext()) {
            var attack = iterator.next();
            attack.update(deltaTime);
            if (attack.isDone()) {
                iterator.remove();
            }
        }

        move(deltaTime);
    }

    private boolean canAttack(float deltaTime) {
        attackTimer -= deltaTime;
        if (attackTimer <= 0f) {
            attackTimer = ATTACK_COOLDOWN;
            return true;
        }
        return false;
    }

    private void move(float deltaTime) {
        if (moveDirection.isZero())
            return;

        float newX = rect.getX() + moveDirection.x * SPEED * deltaTime;
        float newY = rect.getY() + moveDirection.y * SPEED * deltaTime;

        newX = MathUtils.clamp(newX, 0, gamViewport.getWorldWidth() - rect.getWidth());
        newY = MathUtils.clamp(newY, 0, gamViewport.getWorldHeight() - rect.getHeight());

        rect.setPosition(newX, newY);
    }

    public void changeDirection(Vector2 direction) {
        if (!direction.isZero()) {
            lastDirection.set(direction);
        }
        moveDirection.set(direction);
    }

    public void subLife(float amount) {
        this.life -= amount;
    }

    public float getLife() {
        return Math.max(life, 0f);
    }

    public boolean isAlive() {
        return life > 0f;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public Array<Attack> getAttacks() {
        return attacks;
    }
}
