package The_Scribe.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TimeDilationEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private static AtlasRegion img = null;

    public TimeDilationEffect(float x, float y) {
        if (img == null) {
            img = AbstractPower.atlas.findRegion("128/time");
        }

        this.startingDuration = 2.0F;
        this.duration = this.startingDuration;
        this.scale = Settings.scale * 1.0F;
        this.x = x;
        this.y = y;
        this.color = Color.WHITE.cpy();
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
        this.scale = Interpolation.fade.apply(Settings.scale * 0.0F, Settings.scale * 3.0F, this.duration);
        if (this.duration < 1.0F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y, (float)img.packedWidth / 2.0F, (float)img.packedHeight / 2.0F, (float)img.packedWidth, (float)img.packedHeight, this.scale, this.scale, this.duration * 360.0F);
    }

    public void dispose() {
    }
}
