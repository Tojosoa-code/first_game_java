package io.github.java_first_game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Graphic implements Component {
    public static final ComponentMapper<Graphic> MAPPER = ComponentMapper.getFor(Graphic.class);

    private TextureRegion textureRegion;
    private final Color color;

    public Graphic(Color color, TextureRegion textureRegion) {
        this.color = color;
        this.textureRegion = textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public Color getColor() {
        return color;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
