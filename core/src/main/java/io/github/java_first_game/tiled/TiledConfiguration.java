package io.github.java_first_game.tiled;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.java_first_game.Core;
import io.github.java_first_game.asset.AssetService;
import io.github.java_first_game.asset.AtlasAsset;
import io.github.java_first_game.components.*;
import io.github.java_first_game.components.Transform;
import io.github.java_first_game.fsm.Fsm;

public class TiledConfiguration {
    private static final Vector2 DEFAULT_PHYSICS_SCALING = new Vector2(1f, 1f);

    private final Engine engine;
    private final AssetService assetService;
    private final World physicsWorld;

    public TiledConfiguration(Engine engine, AssetService assetService, World physicsWorld) {
        this.engine = engine;
        this.assetService = assetService;
        this.physicsWorld = physicsWorld;
    }


    public void onLoadTile(TiledMapTile tiledMapTile, float x, float y) {
        createBody(
            tiledMapTile.getObjects(),
            new Vector2(x, y),
            DEFAULT_PHYSICS_SCALING,
            BodyDef.BodyType.StaticBody,
            Vector2.Zero,
            "environment"
        );
    }

    private Body createBody(MapObjects mapObjects, Vector2 position, Vector2 scaling, BodyDef.BodyType bodyType, Vector2 relativeTo, Object userData) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;

        Body body = physicsWorld.createBody(bodyDef);
        body.setUserData(userData);

        for (MapObject object : mapObjects) {
            FixtureDef fixtureDef = TiledPhysics.fixtureDefOf(object, scaling, relativeTo);
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(object.getName());
            fixtureDef.shape.dispose();
        }
        return body;
    }

    public void onLoadObject(TiledMapTileMapObject tileMapObject) {
        Entity entity = this.engine.createEntity();
        TiledMapTile tile = tileMapObject.getTile();
        TextureRegion textureRegion = getTextureRegion(tile);
        int z = tile.getProperties().get("z", 1, Integer.class);
        entity.add(new Graphic(Color.WHITE.cpy(), textureRegion));
        addEntityTransform(
            tileMapObject.getX(), tileMapObject.getY(), z,
            textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
            tileMapObject.getScaleX(), tileMapObject.getScaleY(),
            entity
        );
        addEntityController(tileMapObject, entity);
        addEntityMove(tile, entity);
        addEntityAnimation(tile, entity);
        BodyDef.BodyType bodyType = getObjectBodyType(tile);
        addEntityPhysic(tile.getObjects(), bodyType, Vector2.Zero, entity);
        addEntityCameraFollow(tileMapObject, entity);
        entity.add(new Facing(Facing.FacingDirection.DOWN));
        entity.add(new Fsm(entity));

        this.engine.addEntity(entity);
    }

    private void addEntityCameraFollow(TiledMapTileMapObject tileMapObject, Entity entity) {
        Boolean camFollow = tileMapObject.getProperties().get("camFollow", false, Boolean.class);

        if (!camFollow) return;

        entity.add(new CameraFollow());
    }

    private BodyDef.BodyType getObjectBodyType(TiledMapTile tile) {
        String classType = tile.getProperties().get("type", "", String.class);
        if ("Prop".equals(classType)) {
            return BodyDef.BodyType.StaticBody;
        }
        return BodyDef.BodyType.DynamicBody;
    }

    private void addEntityPhysic(MapObjects objects, BodyDef.BodyType bodyType, Vector2 relativeTo, Entity entity) {
        if (objects.getCount() == 0) return;

        Transform transform = Transform.MAPPER.get(entity);
        Body body = createBody(objects, transform.getPosition(), transform.getScaling(), bodyType, relativeTo, entity);
        entity.add(new Physic(body, transform.getPosition().cpy()));
    }

    private void addEntityAnimation(TiledMapTile tile, Entity entity) {
        String animationStr = tile.getProperties().get("animation", "", String.class);
        if (animationStr.isBlank()) return;
        Animation2D.AnimationType animationType = Animation2D.AnimationType.valueOf(animationStr);
        String atlasAssetStr = tile.getProperties().get("atlasAsset", "OBJECTS", String.class);
        AtlasAsset atlasAsset = AtlasAsset.valueOf(atlasAssetStr);
        FileTextureData textureData = (FileTextureData) tile.getTextureRegion().getTexture().getTextureData();
        String atlasKey = textureData.getFileHandle().nameWithoutExtension();
        float speed = tile.getProperties().get("animationSpeed", 0f, Float.class);
        entity.add(new Animation2D(atlasAsset, atlasKey, animationType, Animation.PlayMode.LOOP, speed));

    }

    private void addEntityMove(TiledMapTile tile, Entity entity) {
        float speed = tile.getProperties().get("speed", 0f, Float.class);
        if (speed == 0f) return;

        entity.add(new Move(speed));
    }

    private void addEntityController(TiledMapTileMapObject tiledMapObject, Entity entity) {
        boolean controller = tiledMapObject.getProperties().get("controller", false, Boolean.class);
        if (!controller) return;

        entity.add(new Controller());
    }

    private void addEntityTransform(
        float x, float y, int z,
        float w, float h,
        float scaleX, float scaleY,
        Entity entity) {
        Vector2 position = new Vector2(x, y);
        Vector2 size = new Vector2(w, h);
        Vector2 scaling = new Vector2(scaleX, scaleY);

        position.scl(Core.UNIT_SCALE);
        size.scl(Core.UNIT_SCALE);

        entity.add(new Transform(position, z, size, scaling, 0f));
    }

    private TextureRegion getTextureRegion(TiledMapTile tile) {
        String atlasAssetStr = tile.getProperties().get("atlasAsset", AtlasAsset.OBJECTS.name(), String.class);
        AtlasAsset atlasAsset = AtlasAsset.valueOf(atlasAssetStr);
        TextureAtlas textureAtlas = this.assetService.get(atlasAsset);
        FileTextureData textureData = (FileTextureData) tile.getTextureRegion().getTexture().getTextureData();
        String atlasKey = textureData.getFileHandle().nameWithoutExtension();
        TextureAtlas.AtlasRegion region = textureAtlas.findRegion(atlasKey + "/" + atlasKey);
        if (region != null) {
            return region;
        } else {
            Gdx.app.error("TiledConfiguration", "Region not found in atlas: " + atlasKey);
        }

        return tile.getTextureRegion();
    }
}
