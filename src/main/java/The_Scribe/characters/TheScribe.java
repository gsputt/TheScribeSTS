package The_Scribe.characters;

import java.util.ArrayList;

import The_Scribe.ScribeMod;
import The_Scribe.relics.StarterRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import The_Scribe.cards.*;
import The_Scribe.patches.*;

public class TheScribe extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(TheScribe.class.getName());


// =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

// =============== /BASE STATS/ =================

    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "TheScribeResources/images/char/scribe/orb/layer1.png",
            "TheScribeResources/images/char/scribe/orb/layer2.png",
            "TheScribeResources/images/char/scribe/orb/layer3.png",
            "TheScribeResources/images/char/scribe/orb/layer4.png",
            "TheScribeResources/images/char/scribe/orb/layer5.png",
            "TheScribeResources/images/char/scribe/orb/layer6.png",
            "TheScribeResources/images/char/scribe/orb/layer1d.png",
            "TheScribeResources/images/char/scribe/orb/layer2d.png",
            "TheScribeResources/images/char/scribe/orb/layer3d.png",
            "TheScribeResources/images/char/scribe/orb/layer4d.png",
            "TheScribeResources/images/char/scribe/orb/layer5d.png",};

// =============== /TEXTURES OF BIG ENERGY ORB/ ===============

// =============== CHARACTER CLASS START =================

    public TheScribe(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "TheScribeResources/images/char/scribe/orb/vfx.png", null,
                new SpriterAnimation(
                        "TheScribeResources/images/char/scribe/Spriter/SpellScribeAnimation.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================

        initializeClass(null, // required call to load textures and setup energy/loadout
                ScribeMod.makePath(ScribeMod.THE_SCRIBE_SHOULDER_1), // campfire pose
                ScribeMod.makePath(ScribeMod.THE_SCRIBE_SHOULDER_2), // another campfire pose
                ScribeMod.makePath(ScribeMod.THE_SCRIBE_CORPSE), // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================

       /* this.loadAnimation(
                ScribeMod.makePath(ScribeMod.THE_SCRIBE_SKELETON_ATLAS),
                ScribeMod.makePath(ScribeMod.THE_SCRIBE_SKELETON_JSON),
                1.0f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());*/

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
        this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

// =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo("The Spell Scribe",
                "A Scribe that works only with spells. NL " + "Uses Spell Effects and Spell Modifiers to cast custom spells.",
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        retVal.add(StarterAttack.ID);
        retVal.add(StarterAttack.ID);

        retVal.add(LightningStrike.ID);
        retVal.add(LightningStrike.ID);

        //retVal.add(StarterAttack.ID);
        //retVal.add(StarterAttack.ID);
        //retVal.add(StarterAttack.ID);

        retVal.add(StarterDefend.ID);
        retVal.add(StarterDefend.ID);
        retVal.add(StarterDefend.ID);
        //retVal.add(StarterDefend.ID);
        retVal.add(IcyDefend.ID);
        retVal.add(IcyDefend.ID);

        retVal.add(StarterModifier.ID);

        //retVal.add(ArcaneBarrier.ID);

        //retVal.add(Insight.ID);
        //retVal.add(Split.ID);
        //retVal.add(Eldritch.ID);
        //retVal.add(Ice.ID);
        //retVal.add(Thunder.ID);
        //retVal.add(BloodMagicks.ID);
        //retVal.add(Thwack.ID);
        //retVal.add(Clarity.ID);
        //retVal.add(Focused.ID);
        //retVal.add(Ghost_Realm.ID);
        //retVal.add(SacrificeRitual.ID);
        //retVal.add(Echo.ID);
        //retVal.add(LightningBolt.ID);
        //retVal.add(MirrorImage.ID);
        //retVal.add(Meditate.ID);
        //retVal.add(MagicMissiles.ID);
        //retVal.add(IcicleBlast.ID);


        return retVal;
    }

    // Starting Relics
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(StarterRelic.ID);

        UnlockTracker.markRelicAsSeen(StarterRelic.ID);

        return retVal;
    }

    // Character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("RELIC_DROP_MAGICAL", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // Character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.SCRIBE_BLUE;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return ScribeMod.SCRIBE_BLUE;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return "The Spell Scribe";
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new StarterModifier();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return "The Spell Scribe";
    }

    // Should return a new instance of your character, sending this.name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new TheScribe(this.name, this.chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return ScribeMod.SCRIBE_BLUE;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return ScribeMod.SCRIBE_BLUE;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.BLUNT_HEAVY };
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return "You prepare your spells...";
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return "Navigating an unlit street, you come across several hooded figures in the midst of some dark ritual. As you approach, they turn to you in eerie unison. The tallest among them bares fanged teeth and extends a long, pale hand towards you. NL ~\"Join~ ~us~ ~scribe,~ ~and~ ~feel~ ~the~ ~warmth~ ~of~ ~the~ ~Spire.\"~";
    }
}