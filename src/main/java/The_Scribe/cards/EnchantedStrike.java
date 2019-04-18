package The_Scribe.cards;

import The_Scribe.actions.ChainedSpellTargetingAction;
import The_Scribe.powers.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

import static The_Scribe.patches.ScribeHoveredMonsterPatch.scribeHoveredMonster;

public class EnchantedStrike extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("EnchantedStrike");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_ENCHANTED_STRIKE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static double PiercingBoltsAmount;
    private static int PiercingBoltsCounter;

    private static boolean dontUpdateTheArrayListImUsingIt = false;
    private static AbstractMonster targetMonster = null;
    private AbstractMonster monsterToCheck = null;
    private ChainedSpellTargetingAction chainedSpell;
    public static ArrayList<AbstractMonster> ChainedSpellTargetMonstersList = new ArrayList<>();
    private int counter = 0;

    // /STAT DECLARATION/

    public EnchantedStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;
        tags.add(AbstractCard.CardTags.STRIKE);
        chainedSpell = null;
        ChainedSpellTargetMonstersList = new ArrayList<>();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dontUpdateTheArrayListImUsingIt = true;
        if(!ChainedSpellTargetMonstersList.contains(m))
        {
            ChainedSpellTargetMonstersList.add(m);
        }

        ArrayList<AbstractMonster> monsterList = new ArrayList<>(ChainedSpellTargetMonstersList);
        int j = 0;
        while(j < monsterList.size()) {
            targetMonster = monsterList.get(j);

            int i = this.magicNumber;
            while (i > 0) {
                if (targetMonster.currentBlock > 0 && PiercingBoltsCounter > 0) {
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, (int) Math.ceil(this.damage * PiercingBoltsAmount), this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                } else {
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                i--;
            }

            if (targetMonster.currentBlock > 0 && PiercingBoltsCounter > 0) {
                AbstractDungeon.actionManager
                        .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                new DamageInfo(p, (int) Math.ceil(this.damage * PiercingBoltsAmount), this.damageTypeForTurn),
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            } else {
                AbstractDungeon.actionManager
                        .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }

            j++;
        }
        dontUpdateTheArrayListImUsingIt = false;
    }

    @Override
    public void update()
    {
        super.update();

        //System.out.println("isHoveringDropZone: " + AbstractDungeon.player.isHoveringDropZone);
        //System.out.println("hoveredMonster: " + AbstractDungeon.getCurrRoom().monsters.hoveredMonster);
        //System.out.println("hoveredCard: " + AbstractDungeon.player.hoveredCard);
        if(AbstractDungeon.player != null) {
            if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {

                if (AbstractDungeon.player.isHoveringDropZone && !dontUpdateTheArrayListImUsingIt) {
                    if (scribeHoveredMonster != null && AbstractDungeon.player.hasPower(SpellChaining.POWER_ID) && AbstractDungeon.player.hoveredCard == this) {

                        if (this.counter == 0) {
                            chainedSpell = new ChainedSpellTargetingAction(this, true);
                            this.counter++;
                            this.monsterToCheck = scribeHoveredMonster;
                            //System.out.println("Made A new ChainedSpellTargetingAction");
                        }
                        if (this.counter != 0 && this.monsterToCheck != scribeHoveredMonster) {
                            //System.out.println("Changed Target");
                            if (chainedSpell != null) {
                                chainedSpell.end();
                                chainedSpell = null;
                            }
                            if (this.counter != 0) {
                                this.counter = 0;
                            }
                        }
                    } else {
                        if (chainedSpell != null) {
                            chainedSpell.end();
                            chainedSpell = null;
                        }
                        if (this.counter != 0) {
                            this.counter = 0;
                        }
                    }
                }
            }
        }
    }

    public void applyPowers() {
        super.applyPowers();
        int damageToDo = DAMAGE;
        if(this.upgraded) {
            damageToDo = DAMAGE + UPGRADE_PLUS_DMG;
        }
        if (AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            this.baseDamage = (int)(damageToDo*(1 + (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount * 0.01 * 25)));
            this.isDamageModified = true;
        }
        else if(AbstractDungeon.player.hasPower(BlankScrollPower.POWER_ID)) {
            this.baseDamage = (int)(damageToDo*(1 + (AbstractDungeon.player.getPower(BlankScrollPower.POWER_ID).amount * 0.01 * 25)));
            this.isDamageModified = true;
        }
        if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID) && AbstractDungeon.player.hasPower(BlankScrollPower.POWER_ID))
        {
            this.baseDamage = (int)(damageToDo*(1 + ((AbstractDungeon.player.getPower(BlankScrollPower.POWER_ID).amount + AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount) * 0.01 * 25)));
            this.isDamageModified = true;
        }
        if(AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
            this.baseMagicNumber = AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount;
            this.magicNumber = this.baseMagicNumber;
        }
        else
        {
            this.baseMagicNumber = 0;
            this.magicNumber = this.baseMagicNumber;
        }
        if(!(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID) || AbstractDungeon.player.hasPower(BlankScrollPower.POWER_ID)))
        {
            this.baseDamage = DAMAGE;
            if(this.upgraded)
            {
                this.baseDamage = DAMAGE + UPGRADE_PLUS_DMG;
            }
        }
        if(AbstractDungeon.player.hasPower(SpellPiercingBolts.POWER_ID))
        {
            PiercingBoltsCounter = AbstractDungeon.player.getPower(SpellPiercingBolts.POWER_ID).amount;
            double DiminishingStacks = 2;
            double DiminishAmount = 0.5;
            if(PiercingBoltsCounter > 1)
            {
                for(int i = 1; i <= PiercingBoltsCounter - 1; i++)
                {
                    DiminishingStacks += Math.pow(DiminishAmount, i);
                }
            }
            PiercingBoltsAmount = DiminishingStacks;
        }
        else
        {
            PiercingBoltsCounter = 0;
        }
        setDescription();
    }

    private void setDescription()
    {
        this.rawDescription = DESCRIPTION;
        if(this.magicNumber > 0) {
            this.rawDescription += EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[1];
        }
        if(AbstractDungeon.player.hasPower(SpellChaining.POWER_ID))
        {
            if(AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount == 1)
            {
                this.rawDescription += EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[2];
            }
            else
            {
                this.rawDescription += EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[3];
            }
        }
        if(AbstractDungeon.player.hasPower(SpellPiercingBolts.POWER_ID))
        {
            this.rawDescription += EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[4] + PiercingBoltsAmount + EXTENDED_DESCRIPTION[5];
        }
        initializeDescription();
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new EnchantedStrike();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.initializeDescription();
        }
    }
}