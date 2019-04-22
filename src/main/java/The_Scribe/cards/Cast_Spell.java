package The_Scribe.cards;

import The_Scribe.actions.PurgeSpecificCardAction;
import The_Scribe.actions.ScribeSelfDamageAction;
import The_Scribe.powers.*;
import The_Scribe.unused.unusedPowers.SpellVulnerable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
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
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class Cast_Spell extends CustomCard {
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
    private static int PiercingBoltsCounter = 0;
    private static double PiercingBoltsAmount = 0;

    public ArrayList<AbstractMonster> ChainedSpellTargetMonstersList = new ArrayList<>();

    public static double SpellEffectivenessModifier = 1;

    private static AbstractMonster targetMonster = null;

    private int counter = 0;

    // /STAT DECLARATION/

    public Cast_Spell() {
        super(ID, NAME, IMG_SKILL, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 0;
        this.baseBlock = 0;
        this.purgeOnUse = true;
        this.isEthereal = true;
        this.ChainedSpellTargetMonstersList = new ArrayList<>();

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //dontUpdateTheArrayListImUsingIt = true;
        chainedSpellAcquireBonusTargets(m);

        applyPowers();

        while(Split > 0)
        {
            theScribeAdditionalCasts(p, m);
            Split--;
            if(AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, AbstractDungeon.player.getPower(SpellSplit.POWER_ID), 1));
            }

        }
        theScribeCast(p, m);
        AbstractScribeCard.removeScribedScrollPower();
        //dontUpdateTheArrayListImUsingIt = false;
    }

    private void chainedSpellAcquireBonusTargets(AbstractMonster m){
        AbstractMonster monster;
        ArrayList<AbstractMonster> returnArray = new ArrayList<>();

        if (AbstractDungeon.getCurrRoom().monsters.monsters.size() > 1) {
            Iterator<AbstractMonster> monsterIterator = AbstractDungeon.getMonsters().monsters.iterator();
            ArrayList<AbstractMonster> monsterArray = new ArrayList<>();
            ArrayList<AbstractMonster> processedArray = new ArrayList<>();
            while (monsterIterator.hasNext()) {
                monster = monsterIterator.next();
                if (!monster.equals(m)) {
                    if(!monster.isDeadOrEscaped() && monster.currentHealth > 0 && !monster.halfDead) {
                        monsterArray.add(monster);
                    }
                }
            }
            if (!(monsterArray.size() <= 0)) {
                int i = 0;
                int ChainingAmount = 0;
                if (AbstractDungeon.player.hasPower(SpellChaining.POWER_ID)) {
                    ChainingAmount = AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount;
                }
                if (ChainingAmount > monsterArray.size()) {
                    ChainingAmount = monsterArray.size();
                }
                while (i < ChainingAmount) { // change to < ChainingAmount
                    AbstractMonster processing = getRandomMonsterFromMonsterArray(new Random(), monsterArray);
                    if (processedArray.contains(processing)) {
                        processing = getRandomMonsterFromMonsterArray(new Random(), monsterArray);
                    } else {
                        processedArray.add(processing);
                        i++;
                    }//Infinite while loop?
                }
                while (processedArray.size() > 0) {
                    returnArray.add(processedArray.get(0));
                    processedArray.remove(0);
                }
            }
        }
        this.ChainedSpellTargetMonstersList = new ArrayList<>(returnArray);
        if(!this.ChainedSpellTargetMonstersList.contains(m))
        {
            this.ChainedSpellTargetMonstersList.add(m);
        }
    }

    private AbstractMonster getRandomMonsterFromMonsterArray(Random rng, ArrayList<AbstractMonster> monsterArray)
    {
        return  monsterArray.get(rng.random(monsterArray.size() - 1));
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
        if(this.baseDamage > 0 || AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
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
                || AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID) || this.baseDamage > 0)
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
    public void calculateCardDamage(AbstractMonster m)
    {
        super.calculateCardDamage(m);
    }

    /*@Override
    public void update()
    {
        super.update();

        //System.out.println("isHoveringDropZone: " + AbstractDungeon.player.isHoveringDropZone);
        //System.out.println("hoveredMonster: " + AbstractDungeon.getCurrRoom().monsters.hoveredMonster);
        //System.out.println("hoveredCard: " + AbstractDungeon.player.hoveredCard);
        if(AbstractDungeon.player != null) {
            if (AbstractDungeon.getCurrRoom() != null) {
                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {

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
    }*/


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
        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block, true));
        }


        ArrayList<AbstractMonster> monsterList = new ArrayList<>(this.ChainedSpellTargetMonstersList);
        int i = 0;
        while(i < monsterList.size())
        {
            targetMonster = monsterList.get(i);
            this.calculateCardDamage(targetMonster);
            if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID) || this.baseDamage > 0) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(targetMonster.hb.cX, targetMonster.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(targetMonster.drawX, targetMonster.drawY)));
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                if(AbstractDungeon.player.hasPower(SpellPiercingBolts.POWER_ID) && targetMonster.currentBlock > 0)
                {
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, (int)Math.ceil(this.damage * PiercingBoltsAmount), this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.NONE, true));
                }
                else {
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.NONE, true));
                }
            }
            i++;
        }

        int j = 0;
        while(j < monsterList.size())
        {
            targetMonster = monsterList.get(j);
            if (AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new PoisonPower(targetMonster, p, DarkPoison), DarkPoison, true));
            }

            if (AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new VulnerablePower(targetMonster, DarkVulnerable, false), DarkVulnerable, true));
            }

            if (AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new WeakPower(targetMonster, DarkWeak, false), DarkWeak, true, AbstractGameAction.AttackEffect.NONE));
            }
            j++;
        }

        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(Clarity));
        }

        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            AbstractDungeon.actionManager
                    .addToBottom(new ScribeSelfDamageAction(p,
                            new DamageInfo(p, SelfDamage, DamageInfo.DamageType.THORNS),
                            AbstractGameAction.AttackEffect.NONE, true));
        }
    }

    private void theScribeCast(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block, true));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellBlock.POWER_ID));
        }

        ArrayList<AbstractMonster> monsterList = new ArrayList<>(this.ChainedSpellTargetMonstersList);
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID) || this.baseDamage > 0) {
            int i = 0;
            while(i < monsterList.size())
            {
                targetMonster = monsterList.get(i);
                this.calculateCardDamage(targetMonster);
                //System.out.println("theScribeCast LIGHTNING: " + targetMonster);

                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(targetMonster.hb.cX, targetMonster.hb.cY, AbstractGameAction.AttackEffect.NONE)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(targetMonster.drawX, targetMonster.drawY)));
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
                if(AbstractDungeon.player.hasPower(SpellPiercingBolts.POWER_ID) && targetMonster.currentBlock > 0)
                {
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, (int)Math.ceil(this.damage * PiercingBoltsAmount), this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.NONE, true));
                }
                else {
                    AbstractDungeon.actionManager
                            .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.NONE, true));
                }
                i++;
            }
            if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellAttack.POWER_ID));
            }

        }

        //Applying Powers to Enemies
        System.out.println("Targets List Size: " + monsterList.size());
        int j = 0;
        while(j < monsterList.size())
        {
            targetMonster = monsterList.get(j);
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
            j++;
        }

        //Removing Powers from player
        if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellPoison.POWER_ID));
        }
        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellVulnerable.POWER_ID));
        }
        if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellWeak.POWER_ID));
        }

        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(Clarity));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellClarity.POWER_ID));
        }

        if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellEffectiveness.POWER_ID));
        }

        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            AbstractDungeon.actionManager
                    .addToBottom(new ScribeSelfDamageAction(p,
                            new DamageInfo(p, SelfDamage, DamageInfo.DamageType.THORNS),
                            AbstractGameAction.AttackEffect.NONE, true));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellSelfDamage.POWER_ID));
        }

        if(AbstractDungeon.player.hasPower(SpellChaining.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellChaining.POWER_ID));
        }

        if(AbstractDungeon.player.hasPower(SpellPiercingBolts.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SpellPiercingBolts.POWER_ID));
        }

    }


    private void theScribeGetPowers() {
        SpellEffectivenessModifier = 1;
        SpellEffectivityCounter = 0;
        if (AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            //EXCLUDES BLANK SCROLL POWER
            SpellEffectivenessModifier = SpellEffectivenessModifier + (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount * 0.01 * 25);
            SpellEffectivityCounter = AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount;
        }
        if(AbstractDungeon.player.hasPower(BlankScrollPower.POWER_ID)) {
            SpellEffectivenessModifier += (AbstractDungeon.player.getPower(BlankScrollPower.POWER_ID).amount)* 0.01 * 25;
            //INCLUDES BLANK SCROLL POWER
            SpellEffectivityCounter += AbstractDungeon.player.getPower(BlankScrollPower.POWER_ID).amount;
        }
        if(!(AbstractDungeon.player.hasPower(BlankScrollPower.POWER_ID) || AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID))){
            SpellEffectivityCounter = 0;
        }

        if(AbstractDungeon.player.hasPower(SpellChaining.POWER_ID)) {
            SpellChainingNumber = AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount;
        }
        else
        {
            SpellChainingNumber = 0;
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
        LightningDMGCounter = 0;
        LightningDMG = 0;
        this.baseDamage = 0;
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)){

            LightningDMGCounter = AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount;
            LightningDMG = (int) Math.ceil(AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount
                    * SpellEffectivenessModifier); // EXCLUDES CAPACITANCE SCROLL
            this.baseDamage = LightningDMG;
        }
        if(AbstractDungeon.player.hasPower(CapacitanceScrollPower.POWER_ID))
        {
            LightningDMGCounter += AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount;
            LightningDMG += (int)Math.ceil((AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount) * SpellEffectivenessModifier); // INCLUDES CAPACITANCE SCROLL
            this.baseDamage = LightningDMG;
        }
        if(!(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID) || AbstractDungeon.player.hasPower(CapacitanceScrollPower.POWER_ID))){
            LightningDMGCounter = 0;
            LightningDMG = 0;
            this.baseDamage = 0;
        }

        if(AbstractDungeon.player.hasPower(SpellPiercingBolts.POWER_ID)) {
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
            PiercingBoltsAmount = 0;
        }

        if (AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            FrostBlockCounter = AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount;
            FrostBlock = (int)Math.ceil(AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount
                    * SpellEffectivenessModifier);
            this.baseBlock = FrostBlock;
        }
        else {
            FrostBlockCounter = 0;
            FrostBlock = 0;
            this.baseBlock = 0;
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

    private void theScribeSetDescriptions() {
        int descriptionCounter = 0;

        this.rawDescription = DESCRIPTION;
        if(LightningDMGCounter == 0 && FrostBlockCounter == 0 && DarkPoisonCounter == 0 && DarkVulnerableCounter == 0 &&
                DarkWeakCounter == 0 && SelfDamageCounter  == 0)
        {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[0];
        }
        if(FrostBlockCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[3];
            descriptionCounter += 1;
        }
        if(LightningDMGCounter > 0) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[2];
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
        if(PiercingBoltsCounter > 0)
        {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[14] + PiercingBoltsAmount + EXTENDED_DESCRIPTION[15];
        }
        if(descriptionCounter >= 2) {
            this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[1] + EXTENDED_DESCRIPTION[12];
        }
        this.initializeDescription();
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        AbstractDungeon.actionManager.addToTop(new PurgeSpecificCardAction(this, AbstractDungeon.player.hand));
    }

}