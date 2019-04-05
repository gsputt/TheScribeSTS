package The_Scribe.cards;


import The_Scribe.actions.MoveSpecificTaggedCardsToHandTheyCost0ThisTurnAction;
import The_Scribe.patches.ScribeCardTags;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class ForkedBolt extends AbstractScribeCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("ForkedBolt");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_FORKED_BOLT);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int CARDS_TO_FETCH = 2;
    private static final int UPGRADE_CARDS_TO_FETCH = 1;

    // /STAT DECLARATION/

    public ForkedBolt() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = CARDS_TO_FETCH;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int i = 0;
        while(i < this.magicNumber)
        {
            AbstractCard c = returnTrulyRandomCardFromMasterDeckListInCombat(AbstractDungeon.cardRandomRng, ScribeCardTags.SPELL_ATTACK);
            if(c != null) {
                c = c.makeStatEquivalentCopy();
                c.setCostForTurn(0);
                if (c.exhaust == false) {
                    c.exhaust = true;
                    c.rawDescription += " NL Exhaust.";
                }
                if (c.isEthereal == false) {
                    c.isEthereal = true;
                    c.rawDescription += " NL Ethereal.";
                }
                if (!c.name.contains("Echo: ")) {
                    c.name = "Echo: " + c.name;
                }
                c.initializeDescription();
                if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
                }
                i++;
            }
        }
    }

    private static AbstractCard returnTrulyRandomCardFromMasterDeckListInCombat(Random rng, AbstractCard.CardTags tag) {
        ArrayList<AbstractCard> list = new ArrayList();
        Iterator masterDeckIterator = AbstractDungeon.player.masterDeck.group.iterator();

        while(masterDeckIterator.hasNext()) {
            AbstractCard c = (AbstractCard)masterDeckIterator.next();
            if(c.hasTag(tag)) {
                list.add(c);
            }
        }

        if(list.size() <= 0)
        {
            return null;
        }
        else {
            return (AbstractCard) list.get(rng.random(list.size() - 1));
        }
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new ForkedBolt();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CARDS_TO_FETCH);
            this.initializeDescription();
        }
    }
}