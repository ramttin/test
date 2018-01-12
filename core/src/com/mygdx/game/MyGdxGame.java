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
    int deg;
    Circle Aim;
    Circle ball;
    Boolean fired;



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
        deg=deg+5;
//        Gdx.app.log("degree",String.valueOf(deg));
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 20, 20, 20);
        ball.set((int)(50 * W), (int)(50 * H), (int)(2 * H));
//        ball.set(100, 100, 15);
        renderer.circle(ball.x, ball.y, ball.radius);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(250,0,0,1);
        Gdx.app.log("degree",String.valueOf((1)*(Math.cos((deg)))));
        Gdx.app.log("degree2",String.valueOf(deg));

        Aim.set((int)((6+ball.radius)*(Math.cos((deg*3.14f/180f)))+ball.x),(int)((6+ball.radius)*(Math.sin((float)(deg*Math.PI/180f))))+ball.y,6);
//        Aim.set(ball.x,ball.y,4);
//		batch.draw(img,50*W,50*H,40*R,40*R);
//        Aim.set(deg,deg,40);
        renderer.circle(Aim.x, Aim.y, Aim.radius);

        renderer.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
