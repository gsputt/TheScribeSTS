package The_Scribe.relics.Skillbook;

import aspiration.Aspiration;
import aspiration.relics.skillbooks.SkillbookRelicDescription;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class ScribeSkillbookRelic extends ScribeAspirationRelic implements SkillbookRelicDescription {

    protected ScribeSkillbookRelic(String setId, String imgName, String outlineName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, outlineName, tier, sfx);
        tips.clear();
        tips.add(new PowerTip(name, description));
        if(Aspiration.skillbookCardpool()) {
            tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        }
        initializeTips();
    }

    protected ScribeSkillbookRelic(String setId, String imgName, String outlineName, RelicTier tier, LandingSound sfx, PowerTip pTip) {
        super(setId, imgName, outlineName, tier, sfx);
        tips.clear();
        tips.add(new PowerTip(name, description));
        if(Aspiration.skillbookCardpool()) {
            tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        }
        tips.add(pTip);
        initializeTips();
    }

    protected ScribeSkillbookRelic(String setId, String imgName, String outlineName, RelicTier tier, LandingSound sfx, ArrayList<PowerTip> pTips) {
        super(setId, imgName, outlineName, tier, sfx);
        tips.clear();
        tips.add(new PowerTip(name, description));
        if(Aspiration.skillbookCardpool()) {
            tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        }
        tips.addAll(pTips);
        initializeTips();
    }

    public static boolean hasScribeSkillbookRelic(AbstractPlayer p) {
        for(AbstractRelic r : p.relics) {
            if(r instanceof ScribeSkillbookRelic) {
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
