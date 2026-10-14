package io.github.java_first_game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.java_first_game.Core;
import io.github.java_first_game.asset.AssetService;
import io.github.java_first_game.asset.MapAsset;
import io.github.java_first_game.input.GameControllerState;
import io.github.java_first_game.input.KeyboardController;
import io.github.java_first_game.systems.*;
import io.github.java_first_game.tiled.TiledConfiguration;
import io.github.java_first_game.tiled.TiledService;

import java.util.function.Consumer;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen extends ScreenAdapter {
    private final Core game;
    private final Engine engine;
    private final TiledService tiledService;
    private final TiledConfiguration tiledConfiguration;
    private final KeyboardController keyboardController;

    public GameScreen(Core game) {
        this.game = game;
        this.tiledService = new TiledService(game.getAssetService());
        this.engine = new Engine();
        this.tiledConfiguration = new TiledConfiguration(this.engine, game.getAssetService());
        this.keyboardController = new KeyboardController(GameControllerState.class, engine);

        this.engine.addSystem(new ControllerSystem());
        this.engine.addSystem(new MoveSystem());
        this.engine.addSystem(new FsmSystem());
        this.engine.addSystem(new FacingSystem());
        this.engine.addSystem(new AnimationSystem(game.getAssetService()));
        this.engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));
    }

    @Override
    public void show() {
        game.setInputProcessors(keyboardController);
        keyboardController.setActiveState(GameControllerState.class);

        Consumer< TiledMap> renderconsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        this.tiledService.setMapChangeConsumer(renderconsumer);
        this.tiledService.setLoadObjectConsumer(this.tiledConfiguration::onLoadObject);

        TiledMap tiledMap = this.tiledService.loadMap(MapAsset.MAIN);
        this.tiledService.setMap(tiledMap);
    }

    @Override
    public void hide() {
        this.engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1 / 30f);
        this.engine.update(delta);
    }

    @Override
    public void dispose() {
        for (EntitySystem system : this.engine.getSystems()) {
            if (system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }
    }
}
