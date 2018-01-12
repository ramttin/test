package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Shape;

import static com.badlogic.gdx.Gdx.input;


public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    float W;
    float H;
    Circle Aim;
    Circle ball;


    ShapeRenderer renderer;


    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("circle.png");
        W = Gdx.graphics.getWidth() / 100f;
        H = Gdx.graphics.getHeight() / 100f;
        renderer = new ShapeRenderer();
        ball = new Circle();
        Aim = new Circle();
    }

    @Override
    public void render() {
//		if (input.justTouched()){
//			float acc= input.getAzimuth();
//			Gdx.app.log("hello",String.valueOf(acc));
//
//		}
        batch.begin();

        batch.end();
        rad++
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 20, 20, 20);
        ball.set((int)(50 * W), (int)(50 * H), (int)(2 * H));
//        ball.set(100, 100, 15);
        renderer.circle(ball.x, ball.y, ball.radius);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(250,250,250,100);
        Aim.set((int)(sin(rad)),)
//		batch.draw(img,50*W,50*H,40*R,40*R);

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
