package io.github.java_first_game.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;

public enum SoundAsset implements Asset<Sound> {
    SWORD_HIT("sword_hit.wav");

    private final AssetDescriptor<Sound> descriptor;

    SoundAsset(String soundFile) {
        this.descriptor = new AssetDescriptor<>("audio/" + soundFile, Sound.class);
    }

    @Override
    public AssetDescriptor<Sound> getDescriptor() {
        return descriptor;
    }
}
