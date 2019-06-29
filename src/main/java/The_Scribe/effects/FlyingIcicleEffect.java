package The_Scribe.effects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.orbs.Frost;

public class FlyingIcicleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float destY;
    private float scaleMultiplier;
    private static final float DUR = 0.5F;
    private Texture img;
    private boolean playedSound = false;

    public FlyingIcicleEffect(float x, float y, float fAngle, boolean shouldFlip) {
        Texture img1 = null;
        Texture img2 = null;
        Texture img3 = null;
        if (img1 == null) {
            img1 = ImageMaster.loadImage("images/orbs/frostRight.png");
            img2 = ImageMaster.loadImage("images/orbs/frostLeft.png");
            img3 = ImageMaster.loadImage("images/orbs/frostMid.png");
        }
        switch(MathUtils.random(2)) {
            case 0:
                this.img = img1;
                break;
            case 1:
                this.img = img2;
                break;
            default:
                this.img = img3;
        }
        this.x = x - (float)this.img.getWidth() / 2.0F;
        this.destY = y;
        this.y = this.destY - (float)this.img.getHeight() / 2.0F;
        this.startingDuration = 0.5F;
        this.duration = 0.5F;
        this.scaleMultiplier = MathUtils.random(1.2F, 1.5F);
        this.scale = 1F * Settings.scale;
        if (shouldFlip) {
            this.rotation = fAngle - 180.0F;
        } else {
            this.rotation = fAngle;
        }

        this.color = new Color(0.9F, 0.9F, 1.0F, 0.0F);
    }

    private void playRandomSfX() {
        int frostCount = 20;
        float pitch = 0.8F;
        pitch -= (frostCount * 0.025F);
        pitch += MathUtils.random(-0.2F, 0.2F);
        CardCrawlGame.sound.playA("ORB_FROST_EVOKE", pitch);

    }

    public void update() {
        if (!this.playedSound) {
            this.playRandomSfX();
            this.playedSound = true;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        Vector2 derp = new Vector2(MathUtils.cos(0.017453292F * this.rotation), MathUtils.sin(0.017453292F * this.rotation));
        this.x += derp.x * Gdx.graphics.getDeltaTime() * 3500.0F * this.scaleMultiplier * Settings.scale;
        this.y += derp.y * Gdx.graphics.getDeltaTime() * 3500.0F * this.scaleMultiplier * Settings.scale;
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.duration > 0.25F) {
            this.color.a = Interpolation.pow5In.apply(1.0F, 0.0F, (this.duration - 0.25F) * 4.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration * 4.0F);
        }

        this.scale += Gdx.graphics.getDeltaTime() * this.scaleMultiplier;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale, this.scale * 1.5F, this.rotation + 90F, 0, 0, this.img.getWidth(), this.img.getHeight(), false, false);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale * 0.75F, this.scale * 0.75F, this.rotation + 90F, 0, 0, this.img.getWidth(), this.img.getHeight(), false, false);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
