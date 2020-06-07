
package The_Scribe.effects;

import The_Scribe.ScribeMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ThrownScrollEffect extends AbstractGameEffect {

    public static final String IMG = ScribeMod.makePath(ScribeMod.THROWN_SCROLL_VFX);
    private TextureRegion img = null;
    private Texture texture = new Texture(IMG);

    private float startX;
    private float startY;
    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private float progress;

    public ThrownScrollEffect(float x, float y, float targetX, float targetY) {
        if (this.img == null) {
            this.img = new TextureRegion(texture);
        }


        this.x = this.startX = x;
        this.y = this.startY = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.color = Color.WHITE.cpy();
        this.duration = 0.4F;
        this.startingDuration = 0.4F;
        this.scale = 0.3F * Settings.scale;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.progress += Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
        this.x = Interpolation.linear.apply(this.startX, this.targetX, progress/this.startingDuration);
        this.y = Interpolation.linear.apply(this.startY, this.targetY, progress/this.startingDuration);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x - (this.img.getRegionWidth() / 2.0F), this.y - (this.img.getRegionHeight() / 2.0F), this.img.getRegionWidth() / 2.0F, this.img.getRegionHeight() / 2.0F, (float)this.img.getRegionWidth(), (float)this.img.getRegionHeight(), this.scale, this.scale, this.duration * 720.0F);
    }

    public void dispose() {
    }
}
