package The_Scribe.effects;

import The_Scribe.ScribeMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;


public class AbsoluteZeroSnowflakeEffect extends AbstractGameEffect {
    public static final String IMG = ScribeMod.makePath(ScribeMod.ABSOLUTE_ZERO_SNOWFLAKE_EFFECT);
    private TextureRegion img = null;
    private Texture texture = new Texture(IMG);

    private static final float START_VY;
    private static final float START_VX;
    private float rotationSpeed;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float targetX;
    private float targetY;
    private float alpha;
    private float circleTimer1;
    private float circleTimer2;
    private float circleTimer3;
    private float circleTimer4;
    private float staggerTimer;

    public AbsoluteZeroSnowflakeEffect(float x, float y, float targetX, float targetY) {

        if (this.img == null) {
            this.img = new TextureRegion(texture);
        }

        this.alpha = 0.0F;
        this.circleTimer1 = MathUtils.random(0F, 0.25F);
        this.circleTimer2 = MathUtils.random(0F, 0.25F);
        this.circleTimer3 = MathUtils.random(0F, 0.25F);
        this.circleTimer4 = MathUtils.random(0F, 0.25F);

        this.x = x; //- (float) this.img.getRegionWidth() / 2.0F;
        this.y = y; //- (float) this.img.getRegionHeight() / 2.0F;
        this.targetX = targetX;
        this.targetY = targetY;
        this.staggerTimer = 0.5F;

        this.vX = START_VX;
        this.rotationSpeed = MathUtils.random(200.0F, 500.0F);
        this.vY = START_VY;
        this.scale = Settings.scale * 0.1F;
        this.color = Settings.CREAM_COLOR;
    }

    public void update() {
        if (this.staggerTimer > 0.0F) {
            this.staggerTimer -= Gdx.graphics.getDeltaTime();
        } else {
            //fadeOut
            if (this.alpha != 0.0F) {
                this.alpha -= Gdx.graphics.getDeltaTime() * 2.0F;
                if (this.alpha < 0.0F) {
                    this.alpha = 0.0F;
                }
            }
            if (this.scale != Settings.scale * 0.3F) {
                this.scale += Gdx.graphics.getDeltaTime() * 0.2F;
                if (this.scale > Settings.scale * 0.3F) {
                    this.scale = Settings.scale * 0.3F;
                }
            }
        }
        this.rotation += Gdx.graphics.getDeltaTime() * this.rotationSpeed;
        this.x += Gdx.graphics.getDeltaTime() * this.vX;
        this.y += Gdx.graphics.getDeltaTime() * this.vY;
        if (this.circleTimer1 > 0.0F) {
            this.circleTimer1 -= Gdx.graphics.getDeltaTime();
            this.vY = MathUtils.lerp(this.vY, -200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
            this.vX = MathUtils.lerp(this.vX, 200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);

        } else {
            if (this.circleTimer2 > 0.0F) {
                this.circleTimer2 -= Gdx.graphics.getDeltaTime();
                this.vY = MathUtils.lerp(this.vY, 200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
                this.vX = MathUtils.lerp(this.vX, 200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
            } else {
                if (this.circleTimer3 > 0.0F) {
                    this.circleTimer3 -= Gdx.graphics.getDeltaTime();
                    this.vY = MathUtils.lerp(this.vY, 200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
                    this.vX = MathUtils.lerp(this.vX, -200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
                } else {
                    if (this.circleTimer4 > 0.0F) {
                        this.circleTimer4 -= Gdx.graphics.getDeltaTime();
                        this.vY = MathUtils.lerp(this.vY, -200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
                        this.vX = MathUtils.lerp(this.vX, -200.0F * Settings.scale, Gdx.graphics.getDeltaTime() * 5.0F);
                    } else {
                        this.vY = MathUtils.lerp(this.vY, 0.0F, Gdx.graphics.getDeltaTime() * 5.0F);
                        this.vX = MathUtils.lerp(this.vX, 0.0F, Gdx.graphics.getDeltaTime() * 5.0F);
                        this.x = MathUtils.lerp(this.x, this.targetX, Gdx.graphics.getDeltaTime() * 4.0F);
                        this.y = MathUtils.lerp(this.y, this.targetY, Gdx.graphics.getDeltaTime() * 4.0F);
                    }
                }
            }
        }
        if (Math.abs(this.x - this.targetX) < 5.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.getRegionWidth() /2.0F * this.scale, (float)this.img.getRegionHeight() /2.0F * this.scale, (float)this.img.getRegionWidth(), (float) this.img.getRegionHeight(), this.scale, this.scale, this.rotation);
    }

    public void dispose() {
    }

    static {
        START_VY = 200.0F * Settings.scale;
        START_VX = 200.0F * Settings.scale;
    }
}
