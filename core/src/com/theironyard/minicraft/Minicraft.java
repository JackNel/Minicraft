package com.theironyard.minicraft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Minicraft extends ApplicationAdapter {
    final int WIDTH = 20;
    final int HEIGHT = 20;
    SpriteBatch batch;
    TextureRegion down, up, right, left, currentImage;
    FitViewport viewport;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer renderer;

    float x = 0;
    float y = 0;
    float xv = 0;
    float yv = 0;
    float time = 0;

    final float MAX_VELOCITY = 200;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera = new OrthographicCamera();

        camera.update();
        tiledMap = new TmxMapLoader().load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        Texture tiles = new Texture("tiles.png");
        TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);
        down = grid[6][0];
        up = grid[6][1];
        right = grid[6][3];
        left = new TextureRegion(right);
        left.flip(true, false);
        currentImage = down;
    }

    @Override
    public void render() {
        resize(viewport.getScreenWidth(), viewport.getScreenHeight());
        move();
        draw();

        if (y < 0) {
            y = 0;
        }
        if (x < 0) {
            x = 0;
        }
        if (y > viewport.getWorldHeight() - 100) {
            y = viewport.getWorldHeight() - 100;
        }
        if (x > viewport.getWorldWidth() - 260) {
            x = viewport.getWorldWidth() - 260;
        }
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.setToOrtho(false, width/4, height/4);
    }

    void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yv = MAX_VELOCITY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yv = MAX_VELOCITY * -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xv = MAX_VELOCITY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xv = MAX_VELOCITY * -1;
        }

        x += xv * Gdx.graphics.getDeltaTime();
        y += yv * Gdx.graphics.getDeltaTime();

        xv *= 0.7;
        yv *= 0.7;
    }

    void draw() {
        time += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0,0.5f,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Rectangle cameraRect = new Rectangle();
        cameraRect.setCenter(x, y);
        cameraRect.setSize(camera.viewportWidth, camera.viewportHeight);
        //Rectangle mapRect = new Rectangle(0, 0, 4800, 6400);
       // if (mapRect.contains(cameraRect)) {
            camera.position.x = x;
            camera.position.y = y;
            camera.update();
       // }

        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (Math.round(yv) > 0) {
            currentImage = up;
        }
        else if (Math.round(yv) < 0) {
            currentImage = down;
        }
        else if (Math.round(xv) > 0) {
            currentImage = right;
        }
        else if (Math.round(xv) < 0) {
            currentImage = left;
        }
        batch.draw(currentImage, x, y, WIDTH, HEIGHT);
        batch.end();
    }
}
