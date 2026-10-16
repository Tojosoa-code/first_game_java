package io.github.java_first_game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import io.github.java_first_game.components.Physic;

public class PhysicDebugSystem extends EntitySystem implements Disposable {

    private final World physicWorld;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final Camera camera;

    public PhysicDebugSystem(World physicWorld, Camera camera) {
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.physicWorld = physicWorld;
        this.camera = camera;
        // setProcessing(true);
    }

    @Override
    public void update(float deltaTime) {
        this.box2DDebugRenderer.render(physicWorld, camera.combined);
    }

    @Override
    public void dispose() {
        this.box2DDebugRenderer.dispose();
    }
}
