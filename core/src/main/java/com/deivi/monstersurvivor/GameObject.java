package com.deivi.monstersurvivor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    protected static final Vector2 TMP_VEC2 = new Vector2();

    protected final Rectangle rect;
    protected Texture texture;

    public GameObject(float x, float y, float w, float h, Texture texture) {
        this.rect = new Rectangle(x, y, w, h);
        this.texture = texture;
    }

    public GameObject(float x, float y, float w, float h) {
        this(x, y, w, h, null);
    }

    public boolean overlaps(GameObject other) {
        return rect.overlaps(other.rect);
    }

    public void draw(Batch batch) {
        if(texture == null) return;

        batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
    }

    public Vector2 getCenter(Vector2 out) {
        out.set(rect.x + rect.width / 2, rect.y + rect.height / 2);
        return out;
    }

    abstract void update(float deltaTime);
}
