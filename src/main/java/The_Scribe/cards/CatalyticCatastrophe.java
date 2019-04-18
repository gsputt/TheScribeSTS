package The_Scribe.cards;

import The_Scribe.actions.CatalyticCatastropheAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static The_Scribe.patches.ScribeHoveredMonsterPatch.scribeHoveredMonster;

public class CatalyticCatastrophe extends AbstractScribeCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("CatalyticCatastrophe");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_CATALYTIC_CATASTROPHE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int POISON = 3;
    private static final int UPGRADE_POISON = 2;
    private static final int BASE_DAMAGE = 3;
    private static final int UPGRADE_BASE_DAMAGE = 3;
    private int counter = 0;

    // /STAT DECLARATION/

    public CatalyticCatastrophe() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = POISON;
        this.magicNumber = this.baseMagicNumber;
        this.baseSecondMagicNumber = BASE_DAMAGE;
        this.secondMagicNumber = this.baseSecondMagicNumber;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new CatalyticCatastropheAction(m, p));
    }

    @Override
    public void update()
    {
        super.update();
        if(AbstractDungeon.player != null) {
            if (AbstractDungeon.getCurrRoom() != null) {
                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {

                    if (AbstractDungeon.player.isHoveringDropZone) {
                        if (scribeHoveredMonster != null && scribeHoveredMonster.hasPower(PoisonPower.POWER_ID) && AbstractDungeon.player.hoveredCard == this) {
                            if (this.counter == 0) {

                                int amount = scribeHoveredMonster.getPower(PoisonPower.POWER_ID).amount + this.magicNumber;
                                amount = (int)Math.ceil((double)amount / (double)2);
                                int damage = 0;
                                while (amount > 0)
                                {
                                    damage += amount;
                                    amount -= 1;
                                }
                                this.secondMagicNumber = damage;
                                this.isSecondMagicNumberModified = true;

                                this.counter++;
                            }
                        } else {
                            if (this.counter != 0) {
                                this.secondMagicNumber = this.baseSecondMagicNumber;
                                this.isSecondMagicNumberModified = false;
                                this.counter = 0;
                            }
                        }
                    }
                }
            }
        }
    }


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new CatalyticCatastrophe();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POISON);
            this.upgradeSecondMagicNumber(UPGRADE_BASE_DAMAGE);
            this.initializeDescription();
        }
    }
}