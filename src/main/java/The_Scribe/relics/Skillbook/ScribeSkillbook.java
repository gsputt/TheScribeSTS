package The_Scribe.relics.Skillbook;

import The_Scribe.ScribeMod;
import The_Scribe.characters.TheScribe;
import The_Scribe.patches.LibraryTypeEnum;
import The_Scribe.patches.ScribeCardTags;
import The_Scribe.powers.*;
import aspiration.Aspiration;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static aspiration.Aspiration.logger;
import static aspiration.relics.skillbooks.SkillbookRelic.hasSkillbookRelic;

public class ScribeSkillbook extends ScribeSkillbookRelic {
    public static final String ID = ScribeMod.makeID("ScribeSkillbook");
    public static final String IMG = ScribeMod.makePath(ScribeMod.SKILLBOOK);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.SKILLBOOK_OUTLINE);


    public ScribeSkillbook() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        if (Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(c.hasTag(ScribeCardTags.SPELL_ATTACK) || c.hasTag(ScribeCardTags.SPELL_BLOCK)
                || c.hasTag(ScribeCardTags.SPELL_CLARITY) || c.hasTag(ScribeCardTags.SPELL_POISON)
                || c.hasTag(ScribeCardTags.SPELL_WEAK) || c.hasTag(ScribeCardTags.SPELL_SELF_DAMAGE)
                || c.hasTag(ScribeCardTags.SPELLSTONE_EFFECT) || c.hasTag(ScribeCardTags.SPELL_EFFECT_SCROLL))
        {
            if(this.counter == 0)
            {
                boolean check = false;
                if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
                    check = true;
                }
                if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
                    check = true;
                }
                if(!check) {
                   makeEcho(c);
                }
            }
            this.counter++;
        }
    }


    public void makeEcho(AbstractCard card) {
        AbstractCard c = card;
        if(c != null) {
            c = c.makeStatEquivalentCopy();
            if(c.exhaust == false) {
                c.exhaust = true;
                c.rawDescription += " NL Exhaust.";
            }
            if(c.isEthereal == false)
            {
                c.isEthereal = true;
                c.rawDescription += " NL Ethereal.";
            }
            c.name = "Echo: " + c.name;
            c.initializeDescription();
            if(AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE)
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
            }
            else
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
            }

            this.flash();
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Scribe Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(LibraryTypeEnum.SCRIBE_BLUE);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof TheScribe) && (!hasSkillbookRelic(AbstractDungeon.player) || !hasScribeSkillbookRelic(AbstractDungeon.player));
    }

    public AbstractRelic makeCopy() {
        return new ScribeSkillbook();
    }

    public static boolean hasScribeSkillbookRelic(AbstractPlayer p) {
        Iterator var1 = p.relics.iterator();

        AbstractRelic r;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            r = (AbstractRelic)var1.next();
        } while(!(r instanceof ScribeSkillbookRelic));

        return true;
    }
}