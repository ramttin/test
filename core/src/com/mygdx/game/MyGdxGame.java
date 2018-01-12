package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import org.omg.IOP.ENCODING_CDR_ENCAPS;

import static com.badlogic.gdx.Gdx.input;


public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    float W;
    float H;
    float deg;
    Circle Aim;
    Circle ball;
    Boolean fired,JUSTHIT;
    Boolean justloaded,gameover;
    float Vx,EnemyVx;
    float Vy,EnemyVy;
    float Vtot = 15;
    float Locx,waiting;
    float Locy,changing;
    Rectangle[] walls;
    Circle Enemy;
    int[][] wallsM = {{0, 0}, {0, 1900}, {900, 900}, {0, 0}};
    int bazel;
    Circle dot;
    int dotX;
    int dotY;
    int SCORE,HEARTS;
    ShapeRenderer renderer;
    BitmapFont font,fontbig;
    int EnemyX,EnemyY;


    @Override
    public void create() {
//        Sound oggSound = Gdx.audio.newSound(Gdx.files.internal("data/ogg.ogg"));
//        TextArea
        EnemyVx=4;
        EnemyVy=0;
        bazel = 50;
        walls = new Rectangle[4];
        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);
        fontbig = new BitmapFont();
        fontbig.getData().setScale(4);
        fontbig.setColor(Color.WHITE);

        batch = new SpriteBatch();
//        img = new Texture("circle.png");
        W = Gdx.graphics.getWidth() / 100f;
        H = Gdx.graphics.getHeight() / 100f;
        dotX=(int)(Math.random()*(100*W-4f*bazel)+2f*bazel);
        dotY=(int)(Math.random()*(100*H-4f*bazel)+2f*bazel);
        EnemyX=(int)(Math.random()*2f);
        EnemyY=(int)(Math.random()*2f);
        if(EnemyX>1){
            EnemyX=(int)((100*H-2*bazel)-35*H*Math.random());
        }else {
            EnemyX=(int)((2*bazel)+35*H*Math.random());
        }
        if(EnemyY>1){
            EnemyY=(int)((100*W-2*bazel)-35*W*Math.random());
        }else {
            EnemyY=(int)((2*bazel)+35*W*Math.random());
        }
        int[] wallsH = {(int) (100 * H), bazel, (int) (100 * H), bazel};
        int[] wallsW = {bazel, (int) (100 * W), bazel, (int) (100 * W)};
        int[][] wallsM = {{0, 0}, {0, (int) (100 * H) - bazel}, {(int) (100 * W) - bazel, 0}, {0, 0}};
        renderer = new ShapeRenderer();
        ball = new Circle();
        dot = new Circle();
        Enemy = new Circle();
        SCORE=0;
        HEARTS=3;
        Aim = new Circle();
        for (int i = 0; i < 4; i++) {
            walls[i] = new Rectangle(wallsM[i][0], wallsM[i][1], wallsW[i], wallsH[i]);
            walls[i].set(wallsM[i][0], wallsM[i][1], (float) (wallsW[i]), (float) (wallsH[i]));

        }
//        wallsW={10,200,10,200};
//        wallsH=
        justloaded = true;
        fired = false;
        JUSTHIT=false;
        gameover=false;
        changing=0;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0 + SCORE / 100f, 0 + SCORE / 100f, 0.2f + SCORE / 100f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (gameover) {
            changing++;
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0.1f, 0.9f-changing*0.01f, 0.1f+changing*0.01f, changing*0.001f);
            renderer.rect(0,0,100*W,100*H);
            renderer.end();
            batch.begin();
            fontbig.draw(batch, "G A M E   O V E R", 50 * W - 240, 50 * H);
            fontbig.draw(batch, "SCORE  :  "+String.valueOf(SCORE), 50 * W - 185, 50 * H-90);
            batch.end();
            renderer.end();
            if (input.justTouched()) {
                gameover=false;
                fired=false;
                HEARTS=3;
                SCORE=0;
            }
        } else {
            if(HEARTS<1){
                gameover=true;
            }


            if (JUSTHIT) {
                waiting++;
                if (waiting > 120) {
                    JUSTHIT = false;
                    waiting = 0;
                }
            }
            batch.begin();
//        font.setColor(Color.WHITE);
            font.draw(batch, "SCORE :" + String.valueOf(SCORE), 2 * bazel, 100 * H - 2 * bazel);
            font.draw(batch, "LIFE :" + String.valueOf(HEARTS), 100 * W - 4 * bazel, 100 * H - 2 * bazel);

            batch.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0.1f, 0.6f, 0.4f, 1);
            for (int i = 0; i < 4; i++) {
                renderer.rect(walls[i].x, walls[i].y, walls[i].getWidth(), walls[i].getHeight());
            }
            renderer.end();

            if (!fired) {
                Locx = 50 * W;
                Locy = 50 * H;
                changing=0;

                deg = deg +1f;
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(0, 20, 20, 20);
                ball.set((int) (Locx), (int) (Locy), (int) (2 * H));
                renderer.circle(ball.x, ball.y, ball.radius);
                renderer.end();
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(250, 0, 0, 1);
                Aim.set((int) ((6 + ball.radius) * (Math.cos((deg * 3.14f / 180f))) + ball.x), (int) ((6 + ball.radius) * (Math.sin((float) (deg * Math.PI / 180f)))) + ball.y, 6);
                renderer.circle(Aim.x, Aim.y, Aim.radius);
                renderer.end();
                Vx =- Vtot * (float) Math.cos(deg * 3.14f / 180f);
                Vy = Vtot * (float) Math.sin(deg * 3.14f / 180f);


                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(0.7f, 0, 0, 1);
                Enemy.set(EnemyX, EnemyY, (int) (2 * H));
                renderer.circle(Enemy.x, Enemy.y, Enemy.radius);
                renderer.end();

            } else {

                dot.set(dotX, dotY, 4);

                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(0.4f, 0.4f, 1, 0.2f);
                renderer.circle(dot.x, dot.y, dot.radius + 3);
                renderer.end();

                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(1, 1f, 1, 1);
                renderer.circle(dot.x, dot.y, dot.radius);
                renderer.end();


                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(0.7f, 0, 0, 1);
                Enemy.set(EnemyX, EnemyY, (int) (2 * H));
                renderer.circle(Enemy.x, Enemy.y, Enemy.radius);
                renderer.end();
                if (Math.random() < 0.01f) {
                    float somenum = EnemyVy;
                    EnemyVy = EnemyVx;
                    EnemyVx = somenum;
                }
                EnemyX += EnemyVx;
                EnemyY += EnemyVy;


                Locx += Vx;
                Locy += Vy;

                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(0.8f, (float) (0.8 * (120 - waiting) / 120f), (float) (0.8 * (120 - waiting) / 120f), 0);
                ball.set((int) (Locx), (int) (Locy), (int) (2 * H + SCORE * 2));
                renderer.circle(ball.x, ball.y, ball.radius);
                renderer.end();

            }
            renderer.end();
            if (input.justTouched()) {
                fired = true;
            }
            if (fired) {
                if (input.justTouched()) {
                    Vx = -Vx;

                }
            }
//==============================Ball hitting Wall======================
            if (Intersector.overlaps(ball, dot)) {
                dotX = (int) (Math.random() * (100 * W - 4f * bazel) + 2f * bazel);
                dotY = (int) (Math.random() * (100 * H - 4f * bazel) + 2f * bazel);
                SCORE++;
            }
            if (!JUSTHIT) {
                if (Intersector.overlaps(ball, Enemy)) {
                    HEARTS = HEARTS - 1;
                    JUSTHIT = true;
                }
            }
            for (int i = 0; i < 4; i++) {
                if (Intersector.overlaps(ball, walls[i])) {
                    if (i == 0) {
                        Vx = -(Vx);
                        if (Locx < bazel + ball.radius) {
                            Locx = bazel + ball.radius;
                        }

                    } else if (i == 2) {
                        Vx = -(Vx);
                        if (Locx > W * 100 - bazel - ball.radius) {
                            Locx = W * 100 - bazel - ball.radius;
                        }

                    } else if (i == 3) {
                        Vy = -(Vy);

                    } else {
                        Vy = -(Vy);
                    }
                }
            }

//==============================Enemey hitting Wall======================
            for (int i = 0; i < 4; i++) {
                if (Intersector.overlaps(Enemy, walls[i])) {
                    if (i == 0) {
                        EnemyVx = -(EnemyVx);
                        if (EnemyX < bazel + (int) Enemy.radius) {
                            EnemyX = bazel + (int) Enemy.radius;
                        }

                    } else if (i == 2) {
                        EnemyVx = -(EnemyVx);
                        if (EnemyX > W * 100 - bazel - (int) Enemy.radius) {
                            EnemyX = (int) (W * 100 - bazel - Enemy.radius);
                        }

                    } else if (i == 3) {
                        EnemyVy = -(EnemyVy);
                        if (EnemyY < bazel + (int) Enemy.radius) {
                            EnemyY = (int) (bazel + (int) Enemy.radius);
                        }
                    } else {
                        EnemyVy = -(EnemyVy);
                        if (EnemyY > 100 * H - bazel - (int) Enemy.radius) {
                            EnemyY = (int) (100 * H - bazel - (int) Enemy.radius);
                        }
                    }
                }
            }


        }
    }


    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
