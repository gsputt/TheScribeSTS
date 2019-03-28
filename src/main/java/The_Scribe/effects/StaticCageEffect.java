
package The_Scribe.effects;

import The_Scribe.ScribeMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;

public class StaticCageEffect extends AbstractGameEffect {
    public static final String IMG = ScribeMod.makePath(ScribeMod.STATIC_CAGE_EFFECT);
    private float x;
    private float y;
    private float scaleMultiplier;
    private TextureRegion img = null;
    private Texture texture = new Texture(IMG);

    public StaticCageEffect(float x, float y) {
        if (this.img == null) {
            this.img = new TextureRegion(texture);
        }
        this.x = x;
        this.y = y;
        this.color = Color.WHITE.cpy();
        this.duration = 0.5F;
        this.startingDuration = 0.5F;
        this.scaleMultiplier = 2F;
    }

    public void update() {

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration * 2.0F);
        this.scale += Gdx.graphics.getDeltaTime() * this.scaleMultiplier;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x - (this.img.getRegionWidth()/2.0F * this.scale), this.y - (this.img.getRegionHeight() /2.0F * this.scale), 0,0, this.img.getRegionWidth(), this.img.getRegionHeight(), this.scale, this.scale, 0);

    }

    public void dispose() {
    }
}
