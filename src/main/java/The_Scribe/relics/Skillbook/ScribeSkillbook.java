package The_Scribe.relics.Skillbook;

import The_Scribe.ScribeMod;
import The_Scribe.characters.TheScribe;
import The_Scribe.patches.LibraryTypeEnum;
import The_Scribe.patches.ScribeCardTags;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Iterator;


public class ScribeSkillbook extends SkillbookRelic {
    public static final String ID = ScribeMod.makeID("ScribeSkillbook");
    public static final String IMG = ScribeMod.makePath(ScribeMod.SKILLBOOK);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.SKILLBOOK_OUTLINE);
    public static final ArrayList<PowerTip> pTips = null;
    private static boolean isPlayerTurn = false;

    public ScribeSkillbook() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.BOSS, LandingSound.FLAT, pTips);
    }

    @Override
    public String getUpdatedDescription() {
        if(ScribeMod.hasAspiration) {
            if (ScribeMod.skillbookCardpool()) {
                return DESCRIPTIONS[0] + DESCRIPTIONS[1];
            } else {
                return DESCRIPTIONS[0];
            }
        }
        else
        {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
        this.isPlayerTurn = true;
    }

    @Override
    public void onPlayerEndTurn() {
        this.isPlayerTurn = false;
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(this.counter == 0)
        {
            if(this.isPlayerTurn)
            {
                if(drawnCard.cost > 0)
                {
                    drawnCard.setCostForTurn(drawnCard.costForTurn - 1);
                    drawnCard.isCostModifiedForTurn = true;
                    drawnCard.tags.add(ScribeCardTags.REDUCED_COST_BY_ONE);
                }
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(this.counter == 0)
        {
            c.tags.remove(ScribeCardTags.REDUCED_COST_BY_ONE);
            changeWholeHandCost(1);
        }
        this.counter++;
    }

    public void changeWholeHandCost(int amount)
    {
        Iterator handIterator = AbstractDungeon.player.hand.group.iterator();
        while(handIterator.hasNext()) {
            AbstractCard c = (AbstractCard)handIterator.next();
            if(c.hasTag(ScribeCardTags.REDUCED_COST_BY_ONE)) {
                c.setCostForTurn(c.costForTurn + amount);
                c.isCostModifiedForTurn = false;
            }
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if(ScribeMod.skillbookCardpool()) {
            System.out.println("Scribe Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(LibraryTypeEnum.SCRIBE_BLUE);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof TheScribe) && (!hasSkillbookRelic(AbstractDungeon.player)) && ScribeMod.hasAspiration;
    }

    public AbstractRelic makeCopy() {
        return new ScribeSkillbook();
    }
}