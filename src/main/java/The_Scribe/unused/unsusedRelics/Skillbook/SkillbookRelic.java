package The_Scribe.unused.unsusedRelics.Skillbook;

import The_Scribe.ScribeMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class SkillbookRelic extends CustomRelic implements SkillbookRelicDescription {


    //For mods that want to add their own Skillbook and use Custom Relic
    //pTips can be null or empty if no additional PowerTips are needed
    protected SkillbookRelic(String setId, Texture img, Texture outline, RelicTier tier, LandingSound sfx, ArrayList<PowerTip> pTips) {
        super(setId, img, outline, tier, sfx);
        tips.clear();
        tips.add(new PowerTip(name, description));
        if(ScribeMod.skillbookCardpool()) {
            tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        }
        if(pTips != null && !pTips.isEmpty()) {
            tips.addAll(pTips);
        }
        initializeTips();
    }

    public static boolean hasSkillbookRelic(AbstractPlayer p) {
        for(AbstractRelic r : p.relics) {
            if(r instanceof SkillbookRelic) {
                return true;
            }
        }
        return false;
    }

    public void modifyCardPool() { }

    protected void mixCardpools(ArrayList<AbstractCard> cardList) {
        for (AbstractCard c : cardList) {
            if(c.rarity != AbstractCard.CardRarity.BASIC) {
                switch (c.rarity) {
                    case COMMON: {
                        AbstractDungeon.commonCardPool.addToTop(c);
                        AbstractDungeon.srcCommonCardPool.addToBottom(c);
                        continue;
                    }
                    case UNCOMMON: {
                        AbstractDungeon.uncommonCardPool.addToTop(c);
                        AbstractDungeon.srcUncommonCardPool.addToBottom(c);
                        continue;
                    }
                    case RARE: {
                        AbstractDungeon.rareCardPool.addToTop(c);
                        AbstractDungeon.srcRareCardPool.addToBottom(c);
                        continue;
                    }
                    case CURSE: {
                        AbstractDungeon.curseCardPool.addToTop(c);
                        AbstractDungeon.srcCurseCardPool.addToBottom(c);
                    }
                }
            }
        }
    }
}