package io.github.java_first_game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import io.github.java_first_game.Core;
import io.github.java_first_game.asset.AssetService;
import io.github.java_first_game.asset.AtlasAsset;

public class LoadingScreen extends ScreenAdapter {
    private final Core game;
    private final AssetService assetService;

    public LoadingScreen(Core game, AssetService assetService) {
        this.game = game;
        this.assetService = assetService;
    }

    @Override
    public void show() {
        for (AtlasAsset atlas : AtlasAsset.values()) {
            assetService.queue(atlas);
        }
    }

    @Override
    public void render(float delta) {
        if (this.assetService.update()) {
            Gdx.app.debug("LoadingScreen", "Finished asset loading");
            createScreens();
            this.game.removeScreen(this);
            this.dispose();
            this.game.setScreen(GameScreen.class);
        } else {
            Gdx.app.debug("LoadingScreen", "Progress : " + (int) (assetService.getProgress() * 100) + "%");
        }
    }

    private void createScreens() {
        this.game.addScreen(new GameScreen(this.game));
    }
}
