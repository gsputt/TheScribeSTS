package The_Scribe.cards;

import The_Scribe.patches.ScribeCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import The_Scribe.powers.SpellAttack;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class ArcaneArcing extends AbstractScribeCard implements CardSpellEffectInterface, CardSpellsInterface {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("ArcaneArcing");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_ARCANE_ARCING);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = -1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DAMAGE = 3;


    // /STAT DECLARATION/

    public ArcaneArcing() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseSpellDamage = DAMAGE;
        this.spellDamage = this.baseSpellDamage;
        tags.add(ScribeCardTags.SPELL_ATTACK);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean spellAttackAcquire = false;
        if (this.energyOnUse < EnergyPanel.totalCount) {
            this.energyOnUse = EnergyPanel.totalCount;
        }
        int ChemicalXVariable = 0;
        if (p.hasRelic("Chemical X")) {
            ChemicalXVariable += 2;
            p.getRelic("Chemical X").flash();
        }
        for(int i = 0; i < this.energyOnUse + ChemicalXVariable; i++)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellAttack(p, this.spellDamage), this.spellDamage));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new ArcaneArcing();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeSpellDamage(UPGRADE_PLUS_DAMAGE);
            this.initializeDescription();
        }
    }
}