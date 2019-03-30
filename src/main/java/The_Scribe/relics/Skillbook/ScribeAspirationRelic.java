package The_Scribe.relics.Skillbook;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class ScribeAspirationRelic extends CustomRelic
{
    public ScribeAspirationRelic(String setId, String IMG, String OUTLINE, RelicTier tier, LandingSound sfx)
    {
        super(setId, new Texture(IMG), new Texture(OUTLINE), tier, sfx);

    }

    public boolean deckDescriptionSearch(String keyword1, String keyword2)
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if(c.rawDescription.toLowerCase().contains(keyword1.toLowerCase()) || c.rawDescription.toLowerCase().contains(keyword2.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) { }
    public void onRelicGet(AbstractRelic r) { }
}