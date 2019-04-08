package The_Scribe.cards;




import The_Scribe.actions.ScribeSelfDamageAction;
import The_Scribe.actions.ChainedSpellTargetingAction;
import The_Scribe.powers.*;
import The_Scribe.unused.unusedPowers.SpellVulnerable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.*;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static The_Scribe.patches.ScribeHoveredMonsterPatch.scribeHoveredMonster;


public class Cast_Spell extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to The_Scribe/ScribeMod.java, Line ~140 (Image path section).
     *
     *
     */


    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("Cast_Spell");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG_ATTACK = ScribeMod.makePath(ScribeMod.SCRIBE_CAST);
    public static final String IMG_SKILL = ScribeMod.makePath(ScribeMod.SCRIBE_CAST_SKILL);

    public static final String NAME = cardStrings.NAME;

    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 0;

    private static int LightningDMGCounter = 0;
    private static int LightningDMG = 0;
    private static int FrostBlockCounter = 0;
    private static int FrostBlock = 0;
    private static int DarkPoisonCounter = 0;
    private static int DarkPoison = 0;
    private static int DarkVulnerableCounter = 0;
    private static int DarkVulnerable = 0;
    private static int DarkWeakCounter = 0;
    private static int DarkWeak = 0;
    private static int Split = 0;
    private static int ClarityCounter = 0;
    private static int Clarity = 0;
    private static int SpellEffectivityCounter = 0;
    private static int SelfDamage = 0;
    private static int SelfDamageCounter = 0;
    private static int SpellChainingNumber = 0;

    private ChainedSpellTargetingAction chainedSpell;
    public static ArrayList<AbstractMonster> ChainedSpellTargetMonstersList = new ArrayList<>();

    public static double SpellEffectivenessModifier = 1;

    private static AbstractMonster targetMonster = null;
    private AbstractMonster monsterToCheck = null;

    private static boolean dontUpdateTheArrayListImUsingIt = false;

    private int counter = 0;

    // /STAT DECLARATION/





    public Cast_Spell() {
        super(ID, NAME, IMG_SKILL, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.isEthereal = true;
        this.baseDamage = LightningDMG;
        this.baseBlock = FrostBlock;
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
        while(Split > 0)
        {
            theScribeAdditionalCasts(p, m);
            Split--;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSplit(p, -1), -1));

        }
        theScribeCast(p, m);
        removeScribedScrollPower();
        dontUpdateTheArrayListImUsingIt = false;
    }

    public void applyPowers()
    {
        theScribeGetPowers();
        super.applyPowers();
        theScribeSetDescriptions();
        theScribeCastSkillOrAttack();
    }

    public void theScribeCastSkillOrAttack()
    {
        if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            this.type = CardType.ATTACK;
            this.textureImg = IMG_ATTACK;
            this.loadCardImage(IMG_ATTACK);
        }
        else {
            this.type = CardType.SKILL;
            this.textureImg = IMG_SKILL;
            this.loadCardImage(IMG_SKILL);
        }

        if((AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)
                || AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)
                || AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)
                || AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID))
                && (AbstractDungeon.player.hasPower(SpellBlock.POWER_ID) || AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)
                || AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)))
        {
            this.target = CardTarget.SELF_AND_ENEMY;
        }
        else if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID) || AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)
                || AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            this.target = CardTarget.SELF;
        }
        else {
            this.target = CardTarget.ENEMY;
        }

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
                    if (scribeHoveredMonster != null && AbstractDungeon.player.hasPower(SpellChaining.POWER_ID)) {

                        if (this.counter == 0) {
                            chainedSpell = new ChainedSpellTargetingAction(this, true);
                            this.counter++;
                            this.monsterToCheck = scribeHoveredMonster;
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


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new Cast_Spell();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.initializeDescription();
        }
    }

    private void theScribeAdditionalCasts(AbstractPlayer p, AbstractMonster m) {
        Iterator monsterList = ChainedSpellTargetMonstersList.iterator();
        while(monsterList.hasNext())
        {
            targetMonster = (AbstractMonster)monsterList.next();
            if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(targetMonster.hb.cX, targetMonster.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(targetMonster.drawX, targetMonster.drawY)));
                    CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.NONE, true));
                }
            }
        }

        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block, true));
            }
        }

        monsterList = ChainedSpellTargetMonstersList.iterator();
        while(monsterList.hasNext())
        {
            targetMonster = (AbstractMonster)monsterList.next();
            if (AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new PoisonPower(targetMonster, p, DarkPoison), DarkPoison, true));
                }
            }

            if (AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new VulnerablePower(targetMonster, DarkVulnerable, false), DarkVulnerable, true));
                }
            }

            if (AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new WeakPower(targetMonster, DarkWeak, false), DarkWeak, true, AbstractGameAction.AttackEffect.NONE));
                }
            }
        }

        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToTop(new GainEnergyAction(Clarity));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager
                        .addToBottom(new ScribeSelfDamageAction(p,
                                new DamageInfo(p, SelfDamage, DamageInfo.DamageType.THORNS),
                                AbstractGameAction.AttackEffect.NONE, true));
            }
        }
    }

    private void theScribeCast(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                Iterator monsterList = ChainedSpellTargetMonstersList.iterator();
                while(monsterList.hasNext())
                {
                    targetMonster = (AbstractMonster)monsterList.next();
                    //System.out.println("theScribeCast LIGHTNING: " + targetMonster);

                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(targetMonster.hb.cX, targetMonster.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(targetMonster.drawX, targetMonster.drawY)));
                    CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.NONE, true));

                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellAttack(p, -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount, true));
            }

        }

        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellBlock(p, -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount, true));
            }
        }

        //Applying Powers to Enemies
        //System.out.println("Targets List Size: " + ChainedSpellTargetMonstersList.size());
        Iterator monsterList = ChainedSpellTargetMonstersList.iterator();
        while(monsterList.hasNext())
        {
            targetMonster = (AbstractMonster)monsterList.next();
            //System.out.println("theScribeCast SPELL-POISON-VULNERABLE-WEAK: " + targetMonster);
            if (AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new PoisonPower(targetMonster, p, DarkPoison), DarkPoison, true));
                }
            }

            if (AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new VulnerablePower(targetMonster, DarkVulnerable, false), DarkVulnerable, true));
                }
            }

            if (AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new WeakPower(targetMonster, DarkWeak, false), DarkWeak, true, AbstractGameAction.AttackEffect.NONE));
                }
            }
        }

        //Removing Powers from player
        if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID))
        {
            if(AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellPoison(p, -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount, true));
            }
        }
        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID))
        {
            if(AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellVulnerable(p, -AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount, true));
            }
        }
        if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID))
        {
            if(AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellWeak(p, -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount, true));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToTop(new GainEnergyAction(Clarity));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellClarity(p, -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount, true));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount != 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p, -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount, true));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager
                        .addToBottom(new ScribeSelfDamageAction(p,
                                new DamageInfo(p, SelfDamage, DamageInfo.DamageType.THORNS),
                                AbstractGameAction.AttackEffect.NONE, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSelfDamage(p, -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount, true));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellChaining.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellChaining(p, -AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount, true));

            }
        }
    }


    private void theScribeGetPowers() {
        SpellEffectivenessModifier = 1;

        if(AbstractDungeon.player.hasPower(SpellChaining.POWER_ID)) {
            SpellChainingNumber = AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount;
        }
        else
        {
            SpellChainingNumber = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            SpellEffectivenessModifier = SpellEffectivenessModifier + (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount * 0.01 * 25);
            SpellEffectivityCounter = AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount;
        }
        else {
            SpellEffectivityCounter = 0;
        }
        if (AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
            Split = AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount;
        }
        else {
            Split = 0;
        }

        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            ClarityCounter = AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount;
            Clarity = (int)Math.ceil(AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount
                    * SpellEffectivenessModifier);
        }
        else {
            ClarityCounter = 0;
            Clarity = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            LightningDMGCounter = AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount;
            LightningDMG = (int)Math.ceil(AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount
                    * SpellEffectivenessModifier);
            baseDamage = LightningDMG;
        }
        else {
            LightningDMGCounter = 0;
            LightningDMG = 0;
            baseDamage = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            FrostBlockCounter = AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount;
            FrostBlock = (int)Math.ceil(AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount
                    * SpellEffectivenessModifier);
            baseBlock = FrostBlock;
        }
        else {
            FrostBlockCounter = 0;
            FrostBlock = 0;
            baseBlock = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
            DarkPoison = (int)Math.ceil(AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount
                    * SpellEffectivenessModifier);
            DarkPoisonCounter = AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount;

        }
        else {
            DarkPoisonCounter = 0;DarkPoison = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
            DarkVulnerable = (int)Math.ceil(AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount
                    * SpellEffectivenessModifier);
            DarkVulnerableCounter = AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount;
        }
        else {
            DarkVulnerableCounter = 0;
            DarkVulnerable = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
            DarkWeak = (int)Math.ceil(AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount
                    * SpellEffectivenessModifier);
            DarkWeakCounter = AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount;
        }
        else {
            DarkWeakCounter = 0;
            DarkWeak = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            SelfDamage = (int)Math.ceil(AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount
                    * SpellEffectivenessModifier);
            SelfDamageCounter = AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount;
        }
        else {
            SelfDamage = 0;
            SelfDamageCounter = 0;
        }
    }

    private void removeScribedScrollPower()
    {
        if(AbstractDungeon.player.hasPower(ScribedScrollAcquirePower.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, ScribedScrollAcquirePower.POWER_ID));
        }
    }

    private void theScribeSetDescriptions() {
        int descriptionCounter = 0;

        this.rawDescription = DESCRIPTION;
        if(LightningDMGCounter == 0 && FrostBlockCounter == 0 && DarkPoisonCounter == 0 && DarkVulnerableCounter == 0 &&
                DarkWeakCounter == 0 && Split == 0 && SelfDamageCounter  == 0)
        {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[0];
        }
        if(LightningDMGCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[2];
            descriptionCounter += 1;
        }
        if(FrostBlockCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[3];
            descriptionCounter += 1;
        }
        if(DarkPoisonCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[4];
            descriptionCounter += 1;
        }
        if(DarkVulnerableCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[5];
            descriptionCounter += 1;
        }
        if(DarkWeakCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[6];
            descriptionCounter += 1;
        }
        if(Clarity > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[9];
            descriptionCounter += 1;
        }
        if(SelfDamageCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[11];
            descriptionCounter += 1;
        }
        if(SpellEffectivityCounter != 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[7];
        }
        if(Split > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[8];
        }
        if(SpellChainingNumber == 1) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[10];
        }
        else if(SpellChainingNumber > 1) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[13];
        }
        if(descriptionCounter >= 2) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[12];
        }
        this.initializeDescription();
    }


}