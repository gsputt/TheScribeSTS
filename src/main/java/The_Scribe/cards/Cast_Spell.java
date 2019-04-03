package The_Scribe.cards;




import The_Scribe.actions.ScribeSelfDamageAction;
import The_Scribe.powers.*;
import The_Scribe.unused.unusedPowers.SpellVulnerable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
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
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.Iterator;


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
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_CAST);

    public static final String NAME = cardStrings.NAME;

    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardTarget TARGET_ALL = CardTarget.ALL;
    private static final CardType TYPE = CardType.ATTACK;
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

    public static double SpellEffectivenessModifier = 1;

    private static boolean targetAllEnemies = false;

    // /STAT DECLARATION/





    public Cast_Spell() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.isEthereal = true;
        this.baseDamage = LightningDMG;
        this.baseBlock = FrostBlock;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        while(Split > 0)
        {

            if(targetAllEnemies) {
                this.isMultiDamage = true;
                theScribeAdditionalCastsTargetAll(p);
            }
            else {
                theScribeAdditionalCasts(p, m);
            }
            Split--;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSplit(p, -1), -1));

        }
        if(targetAllEnemies) {
            this.isMultiDamage = true;
            theScribeCastTargetAll(p);
        }
        else {
            theScribeCast(p, m);
        }
        removeScribedScrollPower();
    }

    public void applyPowers()
    {
        theScribeGetPowers();
        super.applyPowers();
        theScribeSetDescriptions();
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

    private void theScribeAdditionalCastsTargetAll(AbstractPlayer p) {
        if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                Iterator monstersLightning = AbstractDungeon.getMonsters().monsters.iterator();
                AbstractMonster monsterVFX;
                while (monstersLightning.hasNext()) {
                    monsterVFX = (AbstractMonster) monstersLightning.next();
                    if (!monsterVFX.isDeadOrEscaped() && !monsterVFX.halfDead) {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(monsterVFX.hb.cX, monsterVFX.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(monsterVFX.drawX, monsterVFX.drawY)));
                    }
                }
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
            }
        }

        Iterator monstersDebuff = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        AbstractMonster monsterApplyDebuff;
        while(monstersDebuff.hasNext()) {
            monsterApplyDebuff = (AbstractMonster) monstersDebuff.next();
            if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monsterApplyDebuff, p, new PoisonPower(monsterApplyDebuff, p, DarkPoison), DarkPoison));
                }
            }
            if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monsterApplyDebuff, p, new VulnerablePower(monsterApplyDebuff, DarkVulnerable, false), DarkVulnerable));
                }
            }
            if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monsterApplyDebuff, p, new WeakPower(monsterApplyDebuff, DarkWeak, false), DarkWeak));
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
                                AbstractGameAction.AttackEffect.NONE));
            }
        }

    }

    private void theScribeCastTargetAll(AbstractPlayer p) {
        if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                Iterator monstersLightning = AbstractDungeon.getMonsters().monsters.iterator();
                AbstractMonster monsterVFX;
                while (monstersLightning.hasNext()) {
                    monsterVFX = (AbstractMonster) monstersLightning.next();
                    if (!monsterVFX.isDeadOrEscaped() && !monsterVFX.halfDead) {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(monsterVFX.hb.cX, monsterVFX.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(monsterVFX.drawX, monsterVFX.drawY)));
                    }
                }
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellAttack(p, -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellBlock(p, -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount));
            }
        }

        Iterator monstersDebuff = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        AbstractMonster monsterApplyDebuff;
        while(monstersDebuff.hasNext()) {
            monsterApplyDebuff = (AbstractMonster) monstersDebuff.next();
            if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monsterApplyDebuff, p, new PoisonPower(monsterApplyDebuff, p, DarkPoison), DarkPoison));
                }
            }
            if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monsterApplyDebuff, p, new VulnerablePower(monsterApplyDebuff, DarkVulnerable, false), DarkVulnerable));
                }
            }
            if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
                if (AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monsterApplyDebuff, p, new WeakPower(monsterApplyDebuff, DarkWeak, false), DarkWeak));
                }
            }
        }

        if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellPoison(p, -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount));
            }
        }
        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellVulnerable(p, -AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount));
            }
        }
        if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellWeak(p, -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount));
            }
        }
        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToTop(new GainEnergyAction(Clarity));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellClarity(p, -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount));
            }
        }
        if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount != 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p, -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount));
            }
        }
        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager
                        .addToBottom(new ScribeSelfDamageAction(p,
                                new DamageInfo(p, SelfDamage, DamageInfo.DamageType.THORNS),
                                AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSelfDamage(p, -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount));
            }
        }

    }

    private void theScribeAdditionalCasts(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(m.drawX, m.drawY)));
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                AbstractDungeon.actionManager
                        .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                AbstractGameAction.AttackEffect.NONE));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, DarkPoison), DarkPoison));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, DarkVulnerable, false), DarkVulnerable));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, DarkWeak, false), DarkWeak, true, AbstractGameAction.AttackEffect.NONE));
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
                                AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    private void theScribeCast(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(m.drawX, m.drawY)));
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                AbstractDungeon.actionManager
                        .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellAttack(p, -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellBlock(p, -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, DarkPoison), DarkPoison));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellPoison(p, -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, DarkVulnerable, false), DarkVulnerable));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellVulnerable(p, -AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, DarkWeak, false), DarkWeak, true, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellWeak(p, -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager.addToTop(new GainEnergyAction(Clarity));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellClarity(p, -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount != 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p, -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount > 0) {
                AbstractDungeon.actionManager
                        .addToBottom(new ScribeSelfDamageAction(p,
                                new DamageInfo(p, SelfDamage, DamageInfo.DamageType.THORNS),
                                AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSelfDamage(p, -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount), -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount));
            }
        }
    }


    private void theScribeGetPowers() {
        SpellEffectivenessModifier = 1;

        if(AbstractDungeon.player.hasPower(SpellTargetsAll.POWER_ID)) {
            target = TARGET_ALL;
            targetAllEnemies = true;
        }
        else {
            target = TARGET;
            targetAllEnemies = false;
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
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(
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
        if(targetAllEnemies) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[10];
        }
        if(descriptionCounter >= 2) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[12];
        }
        this.initializeDescription();
    }

}