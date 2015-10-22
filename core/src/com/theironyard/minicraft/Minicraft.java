package com.theironyard.minicraft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Minicraft extends ApplicationAdapter {
    final int WIDTH = 100;
    final int HEIGHT = 100;

    SpriteBatch batch;
    TextureRegion down, up, right, left, currentImage;

    float x = 0;
    float y = 0;
    float xv = 0;
    float yv = 0;
    float time = 0;

    final float MAX_VELOCITY = 100;

    @Override
    public void create() {
        batch = new SpriteBatch();

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
        move();
        draw();

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

        xv *= 0.8;
        yv *= 0.8;
    }

    void draw() {
        time += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0,0.5f,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
