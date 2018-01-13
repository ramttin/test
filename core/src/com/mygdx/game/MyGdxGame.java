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
    Texture Heart, DeadHeart, RIGHTWALL, LEFTWALL;
    float W, G;
    float H;
    float deg;
    Circle Aim;
    Circle ball;
    Boolean fired, JUSTHIT;
    Boolean justloaded, gameover;
    float Vx, EnemyVx;
    float Vy, EnemyVy;
    float Vtot = 15;
    float Locx, waiting;
    float Locy, changing;
    Rectangle[] walls;
    Circle Enemy;
    int[][] wallsM = {{0, 0}, {0, 1900}, {900, 900}, {0, 0}};
    int bazel, distancesX, distancesY;
    Circle dot;
    int dotX;
    int dotY;
    int SCORE, HEARTS;
    ShapeRenderer renderer;
    BitmapFont font, fontbig;
    int EnemyX, EnemyY;


    @Override
    public void create() {
//        Sound oggSound = Gdx.audio.newSound(Gdx.files.internal("data/ogg.ogg"));
//        TextArea
        EnemyVx = 4;
        EnemyVy = 0;
        bazel = 120;
        walls = new Rectangle[4];
        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);
        fontbig = new BitmapFont();
        fontbig.getData().setScale(4);
        fontbig.setColor(Color.WHITE);

        batch = new SpriteBatch();
        Heart = new Texture("hearty.png");
        DeadHeart = new Texture("bad_heart.png");
        RIGHTWALL = new Texture("wrighty.png");
        LEFTWALL = new Texture("wlefty.png");
        W = Gdx.graphics.getWidth() / 100f;
        H = Gdx.graphics.getHeight() / 100f;
        dotX = (int) (Math.random() * (100 * W - 4f * bazel) + 2f * bazel);
        dotY = (int) (Math.random() * (100 * H - 4f * bazel) + 2f * bazel);
        EnemyX = (int) (Math.random() * 2f);
        EnemyY = (int) (Math.random() * 2f);
        if (EnemyX > 1) {
            EnemyX = (int) ((100 * H - 2 * bazel) - 35 * H * Math.random());
        } else {
            EnemyX = (int) ((2 * bazel) + 35 * H * Math.random());
        }
        if (EnemyY > 1) {
            EnemyY = (int) ((100 * W - 2 * bazel) - 35 * W * Math.random());
        } else {
            EnemyY = (int) ((2 * bazel) + 35 * W * Math.random());
        }
        int[] wallsH = {(int) (100 * H), bazel, (int) (100 * H), bazel};
        int[] wallsW = {bazel, (int) (100 * W), bazel, (int) (100 * W)};
        int[][] wallsM = {{0, 0}, {0, (int) (100 * H) - bazel}, {(int) (100 * W) - bazel, 0}, {0, 0}};
        renderer = new ShapeRenderer();
        ball = new Circle();
        dot = new Circle();
        Enemy = new Circle();
        SCORE = 0;
        HEARTS = 3;
        Aim = new Circle();
        for (int i = 0; i < 4; i++) {
            walls[i] = new Rectangle(wallsM[i][0], wallsM[i][1], wallsW[i], wallsH[i]);
            walls[i].set(wallsM[i][0], wallsM[i][1], (float) (wallsW[i]), (float) (wallsH[i]));

        }
//        wallsW={10,200,10,200};
//        wallsH=
        justloaded = true;
        fired = false;
        JUSTHIT = false;
        gameover = false;
        changing = 0;
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0.254901961f + SCORE / 100f, 0.57254902f+ SCORE / 100f, 0.84f + SCORE / 100f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND); // Or GL20
        if (gameover) {
            distancesX=(int)(50*W);
            distancesY=(int)(50*H);
            changing++;
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            JUSTHIT = false;

            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0.1f, 0.9f - changing * 0.01f, 0.1f + changing * 0.01f, changing * 0.001f);
            renderer.rect(0, 0, 100 * W, 100 * H);

            renderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
            fontbig.draw(batch, "G A M E   O V E R", 50 * W - 240, 50 * H);
            fontbig.draw(batch, "SCORE  :  " + String.valueOf(SCORE), 50 * W - 185, 50 * H - 90);
            batch.end();
            renderer.end();
            if (input.justTouched()) {


                gameover = false;
                fired = false;
                HEARTS = 3;
                SCORE = 0;



            }
        } else {
            if (HEARTS < 1) {
                gameover = true;
            }
            for (int i = 0; i < 3; i++) {
                batch.begin();
                if (i + 1 <= HEARTS) {
                    batch.draw(Heart, (int) 100 * W - (int) (bazel * 0.8f * (i + 2)), (int) 100 * H - 1.7f* bazel , 60, 60);
                } else {
                    batch.draw(DeadHeart, (int) 100 * W - (int) (bazel * 0.8f * (i + 2)), (int) 100 * H - 1.7f* bazel, 60, 60);
                }
                batch.end();
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
            font.draw(batch, "SCORE :" + String.valueOf(SCORE),  bazel+30, 100 * H - 1.2f * bazel);
//            font.draw(batch, "LIFE :" + String.valueOf(HEARTS), 100 * W - 1.2f* bazel, 100 * H - 1.2f * bazel);

            batch.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0.1f, 0.68f, 0.31f, 1);

            for (int i = 0; i < 4; i++) {
                renderer.rect(walls[i].x, walls[i].y, walls[i].getWidth(), walls[i].getHeight());

            }
            renderer.end();
            batch.begin();
            int wallsheight=bazel * LEFTWALL.getHeight() / LEFTWALL.getWidth();
            for(int i=0;i<(int)(1+100*H/wallsheight);i++){
                batch.draw(LEFTWALL, walls[0].x, walls[0].y+bazel + i*wallsheight, (float) bazel, bazel * LEFTWALL.getHeight() / LEFTWALL.getWidth());
            }
            for(int i=0;i<(int)(1+100*H/wallsheight);i++){
                batch.draw(RIGHTWALL, walls[2].x, walls[0].y +bazel+ i*wallsheight, (float) bazel, bazel * LEFTWALL.getHeight() / LEFTWALL.getWidth());
            }
            batch.end();

            if (!fired) {
                Locx = 50 * W;
                Locy = 50 * H;
                changing = 0;

                deg = deg + 1f;
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(0.2f, 0.8f, 0.7f, 0.5f);
                ball.set((int) (Locx), (int) (Locy), (int) (2 * H));
                renderer.circle(ball.x, ball.y, ball.radius);
                renderer.end();
//                renderer.begin(ShapeRenderer.ShapeType.Filled);
//                renderer.setColor(250, 0, 0, 1);
//                Aim.set((int) ((6 + ball.radius) * (Math.cos((deg * 3.14f / 180f))) + ball.x), (int) ((6 + ball.radius) * (Math.sin((float) (deg * Math.PI / 180f)))) + ball.y, 6);
//                renderer.circle(Aim.x, Aim.y, Aim.radius);
//                renderer.end();
//                Vx =- Vtot * (float) Math.cos(deg * 3.14f / 180f);
//                Vy = Vtot * (float) Math.sin(deg * 3.14f / 180f);


                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(0.7f, 0, 0, 1);
                Enemy.set(EnemyX, EnemyY, (int) (2 * H));
                renderer.circle(Enemy.x, Enemy.y, Enemy.radius);
                renderer.end();
                if (Gdx.input.isTouched()) {
                    for (int i = 0; i < 10; i++) {
                        distancesX = (int) (Gdx.input.getX() - ball.x);
                        distancesY = (int) (Gdx.input.getY() - ball.y);
                        renderer.begin(ShapeRenderer.ShapeType.Filled);
                        renderer.setColor(1, 1, 1, 1);
                        renderer.circle(ball.x + distancesX * (i + 1) / 10, ball.y - distancesY * (i + 1) / 10, (i + 10));
                        renderer.end();

                    }
                }
                if ((distancesX * distancesX + distancesY * distancesY) > 50) {
//            readytofire=True;
//        }
//            if(readytofire) {
                    if (!Gdx.input.isTouched()) {
                        fired = true;
                        Vx = Vtot * (float) distancesX / (float) Math.sqrt((distancesX * distancesX + distancesY * distancesY));
                        Vy = -Vtot * (float) distancesY / (float) Math.sqrt((distancesX * distancesX + distancesY * distancesY));

                    }
                }
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
                if (JUSTHIT & waiting % 20 > 9) {
                    renderer.setColor(0.1f, 0.1f, 1, 0.0f);
                    batch.begin();
                    batch.getColor().a = 0.5f;
//                    batch.draw(Heart, (int) 100 * W - (int) (bazel * 1.5 * (HEARTS + 2)), (int) 100 * H - 2 * bazel * 2, 60, 60);
                    batch.draw(Heart, (int) 100 * W - (int) (bazel * 0.8f * (HEARTS + 2)), (int) 100 * H - 1.7f* bazel, 60, 60);

                    batch.end();
                } else {
                    renderer.setColor(0.8f, 0.8f, 0.8f, 1);

                }
                ball.set((int) (Locx), (int) (Locy), (int) (2 * H + SCORE * 2));
                renderer.circle(ball.x, ball.y, ball.radius);
                renderer.end();

            }

            renderer.end();
//            if (input.justTouched()) {
//                fired = true;
//            }


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
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    @Override
    public void dispose() {
        batch.dispose();
//        img.dispose();
    }
}
