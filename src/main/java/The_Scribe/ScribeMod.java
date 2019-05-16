package The_Scribe;

import The_Scribe.characters.TheScribe;
import basemod.ModLabeledToggleButton;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;

//import The_Scribe.potions.*;
import The_Scribe.patches.*;
import The_Scribe.relics.*;
import The_Scribe.variables.*;
import The_Scribe.cards.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class ScribeMod implements
        PostInitializeSubscriber, EditCardsSubscriber, EditStringsSubscriber,
        EditRelicsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber,
        PostBattleSubscriber, OnStartBattleSubscriber
{
    //PrePlayerUpdateSubscriber, OnReceivePowerPower

    public static final Logger logger = LogManager.getLogger(ScribeMod.class.getName());

    //MOD SETTINGS PANEL
    private static final String MODNAME = "The Spell Scribe";
    private static final String AUTHOR = "Left Click, ComingVirus";
    private static final String DESCRIPTION = "An attempt to create a character with a modular spell system";
    //MOD SETTINGS PANEL

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color SCRIBE_BLUE = CardHelper.getColor(12.0f, 10.0f, 227.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // Image folder name - This is where your image folder is.
    // This is good practice in case you ever need to move/rename it without screwing up every single path.
    // In this case, it's resources/TheScribeResources/images (and then, say, /cards/Strike.png).

    private static final String SCRIBE_MOD_ASSETS_FOLDER = "TheScribeResources/images";

    // Card backgrounds
    private static final String ATTACK_SCRIBE_BLUE = "512/bg_attack_scribe_blue.png";
    private static final String POWER_SCRIBE_BLUE = "512/bg_power_scribe_blue.png";
    private static final String SKILL_SCRIBE_BLUE = "512/bg_skill_scribe_blue.png";
    private static final String ENERGY_ORB_SCRIBE_BLUE = "512/card_scribe_blue_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

    private static final String ATTACK_SCRIBE_BLUE_PORTRAIT = "1024/bg_attack_scribe_blue.png";
    private static final String POWER_SCRIBE_BLUE_PORTRAIT = "1024/bg_power_scribe_blue.png";
    private static final String SKILL_SCRIBE_BLUE_PORTRAIT = "1024/bg_skill_scribe_blue.png";
    private static final String ENERGY_ORB_SCRIBE_BLUE_PORTRAIT = "1024/card_scribe_blue_orb.png";

    // Card images
    public static final String SCRIBE_STARTER_ATTACK = "cards/Strike.png";
    public static final String SCRIBE_STARTER_DEFEND = "cards/Defend.png";
    public static final String SCRIBE_CAST = "cards/Cast.png";
    public static final String SCRIBE_INSIGHT = "cards/Insight.png";
    public static final String SCRIBE_ARCANE_BARRIER = "cards/ArcaneBarrier.png";
    public static final String SCRIBE_LIGHTNING = "cards/Lightning.png";
    public static final String SCRIBE_FROST = "cards/Frost.png";
    public static final String SCRIBE_DARK = "cards/Eldritch.png";
    public static final String SCRIBE_SPLIT = "cards/Split.png";
    public static final String SCRIBE_BLOOD_MAGICKS = "cards/BloodMagicks.png";
    public static final String SCRIBE_THWACK = "cards/Thwack.png";
    public static final String SCRIBE_CLARITY = "cards/Clarity.png";
    public static final String SCRIBE_FOCUSED = "cards/Focused.png";
    public static final String SCRIBE_GHOST_REALM = "cards/PlaceholderPower.png";
    public static final String SCRIBE_SACRIFICE_RITUAL = "cards/SacrificeRitual.png";
    public static final String SCRIBE_ECHO = "cards/Echo.png";
    public static final String SCRIBE_LIGHTNING_BOLT = "cards/LightningBolt.png";
    public static final String SCRIBE_MIRROR_IMAGE = "cards/MirrorImage.png";
    public static final String SCRIBE_MEDITATE = "cards/Meditate.png";
    public static final String SCRIBE_MAGIC_MISSILES = "cards/MagicMissiles.png";
    public static final String SCRIBE_POISON_GAS = "cards/Poison.png";
    public static final String SCRIBE_AIR_PRISON = "cards/Vulnerable.png";
    public static final String SCRIBE_BLUNT_WEAPONS = "cards/Weak.png";
    public static final String SCRIBE_ICICLE_BLAST = "cards/IcicleBlast.png";
    public static final String SCRIBE_OPEN_MIND = "cards/OpenMind.png";
    public static final String SCRIBE_PRICE_OF_KNOWLEDGE = "cards/PriceOfKnowledge.png";
    public static final String SCRIBE_BARRIER_OF_LIGHT = "cards/BarrierOfLight.png";
    public static final String SCRIBE_MANABLAST = "cards/Manablast.png";
    public static final String SCRIBE_ELDRITCH_APPLY_POISON = "cards/Poison.png";
    public static final String SCRIBE_ELDRITCH_APPLY_VULNERABLE = "cards/Vulnerable.png";
    public static final String SCRIBE_ELDRITCH_APPLY_WEAK = "cards/Weak.png";
    public static final String SCRIBE_CONVERSION = "cards/Conversion.png";
    public static final String SCRIBE_ESCALATION = "cards/Escalation.png";
    public static final String SCRIBE_INVERSION = "cards/Inversion.png";
    public static final String SCRIBE_SPLINTER = "cards/Splinter.png";
    public static final String SCRIBE_AMPLIFYING_SPELLSTONE = "cards/AmpSpellstone.png";
    public static final String SCRIBE_ENCHANTED_STRIKE = "cards/EnchantedStrike.png";
    public static final String SCRIBE_FOCUSING_BLOW = "cards/FocusingBlow.png";
    public static final String SCRIBE_LIGHTNING_BLADE = "cards/LightningBlade.png";
    public static final String SCRIBE_OVERCHARGE = "cards/Overcharge.png";
    public static final String SCRIBE_LIQUIDUM_SCROLL = "cards/LiquidumScroll.png";
    public static final String SCRIBE_SCINDO_SPELLSTONE = "cards/ScindoSpellstone.png";
    public static final String SCRIBE_BLANK_SCROLL = "cards/BlankScroll.png";
    public static final String SCRIBE_WORDS_OF_POWER = "cards/WordsOfPower.png";
    public static final String SCRIBE_STEM_THE_FLOW = "cards/StemTheFlow.png";
    public static final String SCRIBE_BLOOD_BULWARK = "cards/BloodBulwark.png";
    public static final String SCRIBE_FALSE_LIFE = "cards/FalseLife.png";
    public static final String SCRIBE_INSCRIBE_DEATH = "cards/InscribeDeath.png";
    public static final String SCRIBE_WEIRD_BLAST = "cards/WeirdBlast.png";
    public static final String SCRIBE_WEIRD_SHIELD  = "cards/WeirdShield.png";
    public static final String SCRIBE_STATIC_CAGE = "cards/StaticCage.png";
    public static final String SCRIBE_TELUM_SPELLSTONE = "cards/TelumSpellstone.png";
    public static final String SCRIBE_FLOW_LIKE_WATER = "cards/FlowLikeWater.png";
    public static final String SCRIBE_CAPACITANCE_SCROLL = "cards/CapacitanceScroll.png";
    public static final String SCRIBE_INCENDIO = "cards/Incendio.png";
    public static final String SCRIBE_EXSANGUINATE = "cards/Exsanguinate.png";
    public static final String SCRIBE_CONFLAGRATE = "cards/Conflagrate.png";
    public static final String SCRIBE_ABSOLUTE_ZERO = "cards/AbsoluteZero.png";
    public static final String SCRIBE_ANOMALOUS_TEXTS = "cards/AnomalousTexts.png";
    public static final String SCRIBE_RECKLESS_RESPONSE = "cards/RecklessResponse.png";
    public static final String SCRIBE_FORKED_BOLT = "cards/ForkedBolt.png";
    public static final String SCRIBE_FOCUSED_BOLT = "cards/FocusingBolt.png";
    public static final String SCRIBE_MAELSTROM = "cards/Deluge.png";
    public static final String SCRIBE_CRIPPLING_CHARGE = "cards/CripplingCharge.png";
    public static final String SCRIBE_SCROLL_OF_POISON = "cards/ScrollOfPoison.png";
    public static final String SCRIBE_CRYSTAL_SHIELD = "cards/CrystalShield.png";
    public static final String SCRIBE_FOCUSING_BARRIER = "cards/FocusingBarrier.png";
    public static final String SCRIBE_RECURRING_RIME = "cards/RecurringRime.png";
    public static final String SCRIBE_ARCANE_ARCING = "cards/ArcaneArcing.png";
    public static final String SCRIBE_LIGHTNING_STRIKE = "cards/LightningStrike.png";
    public static final String SCRIBE_ICY_DEFEND = "cards/IcyDefend.png";
    public static final String SCRIBE_STARTER_MODIFIER = "cards/DetailedAnnotation.png";
    public static final String SCRIBE_SCROLL_OF_SHADOWS = "cards/PlaceholderPower.png";
    public static final String SCRIBE_SCROLL_OF_CHAINING = "cards/PlaceholderPower.png";
    public static final String SCRIBE_STATIC_SERIES = "cards/PlaceholderAttack.png";
    public static final String SCRIBE_CAST_SKILL = "cards/CastSkill.png";
    public static final String SCRIBE_SCRIPTED_STARFALL = "cards/PlaceholderPower.png";
    public static final String SCRIBE_TIME_DILATION = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_SCROLL_OF_SCRYING = "cards/PlaceholderPower.png";
    public static final String SCRIBE_PIERCING_BOLTS = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_CATALYTIC_CATASTROPHE = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_WEIRD_TOXIN = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_GLACIAL_GUARD = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_FROSTVENOM_TOXIN = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_TOXIC_SPELLSTONE = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_QUICK_STUDY = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_INVIGORATING_STRIKE = "cards/PlaceholderAttack.png";
    public static final String SCRIBE_PAPER_CUT = "cards/PlaceholderAttack.png";
    public static final String SCRIBE_FRANTIC_SEARCH = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_OVEREXERT = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_THROWN_SCROLL = "cards/PlaceholderAttack.png";
    public static final String SCRIBE_DIVINATION = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_GATHER_COMPONENTS = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_SANGUINE_SURGE = "cards/PlaceholderAttack.png";
    public static final String SCRIBE_GLACIES_SPELLSTONE = "cards/PlaceholderSkill.png";
    public static final String SCRIBE_COMMON_POWER = "cards/PlaceholderPower.png";
    public static final String SCRIBE_UNCOMMON_ATTACK = "cards/Attack.png";
    public static final String SCRIBE_UNCOMMON_SKILL = "cards/Skill.png";
    public static final String SCRIBE_UNCOMMON_POWER = "cards/Power.png";
    public static final String SCRIBE_RARE_ATTACK = "cards/Attack.png";
    public static final String SCRIBE_RARE_SKILL = "cards/Skill.png";
    public static final String SCRIBE_RARE_POWER = "cards/Power.png";


    // Power images
    public static final String COMMON_POWER = "powers/placeholder_power.png";
    public static final String UNCOMMON_POWER = "powers/placeholder_power.png";
    public static final String RARE_POWER = "powers/placeholder_power.png";
    public static final String SPELL_ATTACK = "powers/SpellAttack.png";
    public static final String SPELL_DEFEND = "powers/SpellBlock.png";
    public static final String SPELL_POISON = "powers/SpellPoison.png";
    public static final String SPELL_VULNERABLE = "powers/SpellVulnerable.png";
    public static final String SPELL_WEAK = "powers/SpellWeak.png";
    public static final String SPELL_SPLIT = "powers/SpellSplit.png";
    public static final String BLOOD_MAGICKS_POWER = "powers/BloodMagicksPower.png";
    public static final String SPELL_CLARITY = "powers/SpellClarity.png";
    public static final String SPELL_FOCUSED = "powers/SpellFocused.png";
    public static final String SPELL_TARGETS_ALL = "powers/SpellTargetsAll.png";
    public static final String DRAINED_POWER = "powers/Drained.png";
    public static final String ESCALATION_POWER = "powers/EscalationPower.png";
    public static final String THORNS_DOWN = "powers/ThornsDown.png";
    public static final String SPELL_SELF_DAMAGE = "powers/SpellSelfDamage.png";
    public static final String LIQUIDUM_POWER = "powers/LiquidumPower.png";
    public static final String BLANK_SCROLL_POWER = "powers/BlankScrollPower.png";
    public static final String WORDS_OF_POWER_POWER = "powers/WordsOfPowerPower.png";
    public static final String STEM_THE_FLOW_POWER = "powers/StemTheFlowPower.png";
    public static final String FALSE_LIFE_POWER = "powers/FalseLifePower.png";
    public static final String ENERGIZED_POWER = "powers/ScribeEnergized.png";
    public static final String FLOW_LIKE_WATER_POWER = "powers/FlowLikeWaterPower.png";
    public static final String CAPACITANCE_SCROLL_POWER = "powers/CapacitanceScrollPower.png";
    public static final String ABSOLUTE_ZERO_POWER = "powers/AbsoluteZeroPower.png";
    public static final String MAELSTROM_POWER = "powers/DelugePower.png";
    public static final String SCROLL_OF_POISON_POWER = "powers/ScrollOfPoisonPower.png";
    public static final String SCRIBED_SCROLL_ACQUIRE_POWER = "powers/placeholder_power.png";
    public static final String SCROLL_OF_SHADOWS_POWER = "powers/placeholder_power.png";
    public static final String SPELL_CHAIN = "powers/placeholder_power.png";
    public static final String SCROLL_OF_CHAINING_POWER = "powers/placeholder_power.png";
    public static final String SCRIPTED_STARFALL_POWER = "powers/placeholder_power.png";
    public static final String SCROLL_OF_SCRYING_POWER = "powers/placeholder_power.png";
    public static final String SPELL_PIERCING_BOLTS = "powers/placeholder_power.png";
    public static final String REMOVE_SPLIT_AT_END_OF_TURN_POWER = "powers/placeholder_power.png";


    // Relic images
    public static final String STARTER_RELIC_1 = "relics/StarterRelic.png";
    public static final String STARTER_RELIC_OUTLINE_1 = "relics/outline/StarterRelicOutline.png";
    public static final String STARTER_RELIC_2 = "relics/SpellScribesPen.png";
    public static final String STARTER_RELIC_OUTLINE_2 = "relics/outline/SpellScribesPenOutline.png";
    public static final String OSMOTIC_FILTER = "relics/OsmoticFilterRelic.png";
    public static final String OSMOTIC_FILTER_OUTLINE = "relics/outline/OsmoticFilterOutline.png";
    public static final String STORM_SCALE = "relics/StormScaleRelic.png";
    public static final String STORM_SCALE_OUTLINE = "relics/outline/StormScaleOutline.png";
    public static final String PERMAFROST_PEN = "relics/PermafrostPen.png";
    public static final String PERMAFROST_PEN_OUTLINE = "relics/outline/PermafrostPenOutline.png";
    public static final String ALCHEMICAL_ALTER = "relics/AlchemicalAlter.png";
    public static final String ALCHEMICAL_ALTER_OUTLINE = "relics/outline/AlchemicalAlterOutline.png";
    public static final String CLARITY_BOSS_RELIC = "relics/InvisibleInkwell.png";
    public static final String CLARITY_BOSS_RELIC_OUTLINE = "relics/outline/InvisibleInkwellOutline.png";
    public static final String RUNIC_REPEATER = "relics/RunicRepeater.png";
    public static final String RUNIC_REPEATER_OUTLINE = "relics/outline/RunicRepeaterOutline.png";
    public static final String MEDITATION_CIRCLE = "relics/MeditationCircleRelic.png";
    public static final String MEDITATION_CIRCLE_OUTLINE = "relics/outline/MeditationCircleRelicOutline.png";

    public static final String SKILLBOOK = "relics/ScribeSkillbook.png";
    public static final String SKILLBOOK_OUTLINE = "relics/outline/ScribeSkillbookOutline.png";



    public static final String PLACEHOLDER_RELIC_2 = "relics/placeholder_relic2.png";
    public static final String PLACEHOLDER_RELIC_OUTLINE_2 = "relics/outline/placeholder_relic2.png";

    //Custom VFX
    public static final String STATIC_CAGE_EFFECT = "vfx/Static_Cage_Effect.png";
    public static final String ABSOLUTE_ZERO_SNOWFLAKE_EFFECT = "vfx/Absolute_Zero_Snowflake_Particle.png";
    public static final String THROWN_SCROLL_VFX = "vfx/ThrownScrollVFX.png";


    // Character assets
    private static final String THE_SCRIBE_BUTTON = "charSelect/ScribeCharacterButton.png";
    private static final String THE_SCRIBE_PORTRAIT = "charSelect/ScribeCharacterPortraitBG.png";
    public static final String THE_SCRIBE_SHOULDER_1 = "char/scribe/shoulder.png";
    public static final String THE_SCRIBE_SHOULDER_2 = "char/scribe/shoulder2.png";
    public static final String THE_SCRIBE_CORPSE = "char/scribe/corpse.png";

    //Mod Badge
    public static final String BADGE_IMAGE = "Badge.png";

    // Animations atlas and JSON files
    public static final String THE_SCRIBE_SKELETON_ATLAS = "char/scribe/skeleton.atlas";
    public static final String THE_SCRIBE_SKELETON_JSON = "char/scribe/skeleton.json";


    //private static ArrayList<onRemovePowerHook> onRemovePowerSubscribers = new ArrayList<>();

    private static SpireConfig modConfig = null;

    /*// =============== /CUSTOM SUBSCRIBER STUFF/ ==================
    public static void subscribe(ISubscriber subscriber){
        subscribeIfInstance(onRemovePowerSubscribers, subscriber, onRemovePowerHook.class);
    }

    public static void unsubscribe(ISubscriber subscriber){
        unsubscribeIfInstance(onRemovePowerSubscribers, subscriber, onRemovePowerHook.class);
    }

    private static <T> void unsubscribeIfInstance(ArrayList<T> list, ISubscriber sub, Class<T> clazz) {
        if (clazz.isInstance(sub)) {
            list.remove(clazz.cast(sub));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void subscribeIfInstance(ArrayList<T> list, ISubscriber sub, Class<T> clazz) {
        if (clazz.isInstance(sub)) {
            list.add((T) sub);
        }
    }

    public static void publishOnRemovePower(AbstractPower power)
    {
        for (onRemovePowerHook subscriber : onRemovePowerSubscribers) {
            subscriber.receiveRemovedPower(power);
        }
    }
    // =============== /CUSTOM SUBSCRIBER STUFF/ ==================*/

    // =============== /INPUT TEXTURE LOCATION/ =================

    // =============== IMAGE PATHS =================

    // This is the command that will link up your core assets folder (line 89) ("TheScribeResources/images")
    // together with the card image (everything above) ("cards/Attack.png") and it puts a "/" between them.
    // When adding a card image, you can, in fact, just do "TheScribeResources/images/cards/Attack.png" in the actual card file.
    // This however, is good practice in case you want to change your "/images" folder at any point in time.

    /**
     * @param resource the resource, must *NOT* have a leading "/"
     * @return the full path
     */
    public static final String makePath(String resource) {
        return SCRIBE_MOD_ASSETS_FOLDER + "/" + resource;
    }

    // =============== /IMAGE PATHS/ =================

    // =============== SUBSCRIBE, CREATE THE COLOR, INITIALIZE =================

    public ScribeMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);

        logger.info("Done subscribing");

        logger.info("Creating the color " + AbstractCardEnum.SCRIBE_BLUE.toString());

        BaseMod.addColor(AbstractCardEnum.SCRIBE_BLUE, SCRIBE_BLUE, SCRIBE_BLUE, SCRIBE_BLUE,
                SCRIBE_BLUE, SCRIBE_BLUE, SCRIBE_BLUE, SCRIBE_BLUE, makePath(ATTACK_SCRIBE_BLUE),
                makePath(SKILL_SCRIBE_BLUE), makePath(POWER_SCRIBE_BLUE),
                makePath(ENERGY_ORB_SCRIBE_BLUE), makePath(ATTACK_SCRIBE_BLUE_PORTRAIT),
                makePath(SKILL_SCRIBE_BLUE_PORTRAIT), makePath(POWER_SCRIBE_BLUE_PORTRAIT),
                makePath(ENERGY_ORB_SCRIBE_BLUE_PORTRAIT), makePath(CARD_ENERGY_ORB));

        logger.info("Done creating the color");
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("Initializing ScribeMod Mod. Feed Cookies to continue");
        ScribeMod The_Scribe = new ScribeMod();
        logger.info(" ScribeMod Mod Initialized - Cookies have been fed");

        try {
            Properties defaults = new Properties();
            defaults.put("SkillbookCardpool", Boolean.toString(true));
            modConfig = new SpireConfig("Aspiration", "Config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR, INITIALIZE/ =================

    // ============== /CROSSOVER CHECKS/ ==============
    public static final boolean hasAspiration;
    public static final boolean hasDisciple;
    public static final boolean hasConspire;

    static {
        hasAspiration = Loader.isModLoaded("aspiration");
        if (hasAspiration) {
            logger.info("Detected Mod: Aspiration");
        }
        hasDisciple = Loader.isModLoaded("chronomuncher");
        if(hasDisciple) {
            logger.info("Detected Mod: The Disciple");
        }
        hasConspire = Loader.isModLoaded("conspire");
        if(hasConspire) {
            logger.info("Detected Mod: Conspire");
        }
    }

    public static boolean skillbookCardpool()
    {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("SkillbookCardpool");
    }

    // ============== /CROSSOVER CHECKS/ ==============


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        // Add the Custom Dynamic Variables

        //BaseMod.addDynamicVariable(new StrikeLightning());
        //BaseMod.addDynamicVariable(new BlockFrost());
        BaseMod.addDynamicVariable(new PoisonDark());
        //BaseMod.addDynamicVariable(new VulnerableDark());
        BaseMod.addDynamicVariable(new WeakDark());
        BaseMod.addDynamicVariable(new SplitNumber());
        BaseMod.addDynamicVariable(new ClarityVariable());
        BaseMod.addDynamicVariable(new EffectivenessVariable());
        BaseMod.addDynamicVariable(new UniqueCounter());
        BaseMod.addDynamicVariable(new SelfDamage());
        BaseMod.addDynamicVariable(new OverchargeVariable());
        BaseMod.addDynamicVariable(new BetterSpellEffectiveness());
        BaseMod.addDynamicVariable(new BetterSplitVariable());
        BaseMod.addDynamicVariable(new BetterDrainedVariable());
        BaseMod.addDynamicVariable(new BetterSpellAttack());
        BaseMod.addDynamicVariable(new BetterSelfDamage());
        BaseMod.addDynamicVariable(new AnomalousTextsVariable());
        BaseMod.addDynamicVariable(new BetterSpellBlock());
        BaseMod.addDynamicVariable(new ChainingVariable());
        BaseMod.addDynamicVariable(new SecondMagicNumberVariable());


        logger.info("Adding cards");
        // Add the cards
        BaseMod.addCard(new StarterAttack());
        BaseMod.addCard(new StarterDefend());
        BaseMod.addCard(new Insight());
        BaseMod.addCard(new Cast_Spell());
        BaseMod.addCard(new ArcaneBarrier());
        BaseMod.addCard(new Thunder());
        BaseMod.addCard(new Ice());
        BaseMod.addCard(new Eldritch());
        BaseMod.addCard(new Split());
        BaseMod.addCard(new BloodMagicks()); //REWORKED NOW
        BaseMod.addCard(new Thwack());
        BaseMod.addCard(new Clarity());
        BaseMod.addCard(new Focused());
        BaseMod.addCard(new Ghost_Realm()); //ADD BACK LATER WHEN REWORKED
        BaseMod.addCard(new SacrificeRitual());
        BaseMod.addCard(new Echo());
        BaseMod.addCard(new LightningBolt());
        BaseMod.addCard(new Meditate());
        BaseMod.addCard(new MirrorImage());
        BaseMod.addCard(new MagicMissiles());
        //BaseMod.addCard(new PoisonGas()); //DEPRECATED
        //BaseMod.addCard(new AirPrison());
        BaseMod.addCard(new BluntWeapons());
        BaseMod.addCard(new IcicleBlast());
        //BaseMod.addCard(new OpenMind());
        BaseMod.addCard(new PriceOfKnowledge());
        BaseMod.addCard(new BarrierOfLight());
        //BaseMod.addCard(new Manablast());
        //BaseMod.addCard(new EldritchApplyPoison());
        //BaseMod.addCard(new EldritchApplyVulnerable());
        //BaseMod.addCard(new EldritchApplyWeak());
        //BaseMod.addCard(new Conversion());
        BaseMod.addCard(new Escalation());
        //BaseMod.addCard(new Inversion());
        //BaseMod.addCard(new Splinter());
        BaseMod.addCard(new AmplifyingSpellstone());
        BaseMod.addCard(new EnchantedStrike());
        BaseMod.addCard(new FocusingBlow());
        BaseMod.addCard(new LightningBlade());
        BaseMod.addCard(new Overcharge());
        BaseMod.addCard(new LiquidumScroll());
        BaseMod.addCard(new ScindoSpellstone());
        BaseMod.addCard(new BlankScroll());
        BaseMod.addCard(new StemTheFlow());
        //BaseMod.addCard(new BloodBulwark());
        BaseMod.addCard(new FalseLife());
        BaseMod.addCard(new WordsOfPower());
        BaseMod.addCard(new InscribeDeath());
        BaseMod.addCard(new WeirdBlast());
        BaseMod.addCard(new WeirdShield());
        BaseMod.addCard(new StaticCage());
        BaseMod.addCard(new TelumSpellstone());
        BaseMod.addCard(new FlowLikeWater());
        BaseMod.addCard(new CapacitanceScroll());
        BaseMod.addCard(new Incendio());
        //BaseMod.addCard(new Exsanguinate());
        BaseMod.addCard(new Conflagrate());
        BaseMod.addCard(new AbsoluteZero());
        BaseMod.addCard(new AnomalousTexts());
        BaseMod.addCard(new RecklessResponse());
        BaseMod.addCard(new ForkedBolt());
        BaseMod.addCard(new FocusedBolt());
        BaseMod.addCard(new Maelstrom());
        BaseMod.addCard(new CripplingCharge());
        BaseMod.addCard(new ScrollOfPoison());
        BaseMod.addCard(new CrystalShield());
        BaseMod.addCard(new FocusingBarrier());
        BaseMod.addCard(new RecurringRime());
        BaseMod.addCard(new ArcaneArcing());
        BaseMod.addCard(new LightningStrike());
        BaseMod.addCard(new IcyDefend());
        BaseMod.addCard(new StarterModifier());
        BaseMod.addCard(new ScrollOfShadows());
        BaseMod.addCard(new ScrollOfChaining());
        BaseMod.addCard(new StaticSeries());
        BaseMod.addCard(new ScriptedStarfall());
        BaseMod.addCard(new TimeDilation());
        BaseMod.addCard(new ScrollOfScrying());
        BaseMod.addCard(new PiercingBolts());
        BaseMod.addCard(new CatalyticCatastrophe());
        BaseMod.addCard(new WeirdToxin());
        BaseMod.addCard(new GlacialGuard());
        BaseMod.addCard(new FrostvenomToxin());
        BaseMod.addCard(new ToxicSpellstone());
        BaseMod.addCard(new QuickStudy());
        BaseMod.addCard(new InvigoratingStrike());
        //BaseMod.addCard(new PaperCut());
        BaseMod.addCard(new FranticSearch());
        BaseMod.addCard(new Overexert());
        BaseMod.addCard(new ThrownScroll());
        BaseMod.addCard(new Divination());
        BaseMod.addCard(new GatherComponents());
        BaseMod.addCard(new SanguineSurge());
        BaseMod.addCard(new GlaciesSpellstone());

        /*BaseMod.addCard(new DefaultAttackWithVariable());

        BaseMod.addCard(new DefaultCommonPower());
        BaseMod.addCard(new DefaultUncommonSkill());
        BaseMod.addCard(new DefaultUncommonAttack());
        BaseMod.addCard(new DefaultUncommonPower());
        BaseMod.addCard(new DefaultRareAttack());
        BaseMod.addCard(new DefaultRareSkill());
        BaseMod.addCard(new DefaultRarePower());*/

        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        UnlockTracker.unlockCard(StarterAttack.ID);
        UnlockTracker.unlockCard(StarterDefend.ID);
        UnlockTracker.unlockCard(Insight.ID);
        UnlockTracker.unlockCard(Cast_Spell.ID);
        UnlockTracker.unlockCard(ArcaneBarrier.ID);
        UnlockTracker.unlockCard(Thunder.ID);
        UnlockTracker.unlockCard(Ice.ID);
        UnlockTracker.unlockCard(Eldritch.ID);
        UnlockTracker.unlockCard(Split.ID);
        UnlockTracker.unlockCard(BloodMagicks.ID); //REWORKED NOW
        UnlockTracker.unlockCard(Thwack.ID);
        UnlockTracker.unlockCard(Clarity.ID);
        UnlockTracker.unlockCard(Focused.ID);
        UnlockTracker.unlockCard(Ghost_Realm.ID); //ADD BACK LATER WHEN REWORKED
        UnlockTracker.unlockCard(SacrificeRitual.ID);
        UnlockTracker.unlockCard(Echo.ID);
        UnlockTracker.unlockCard(LightningBolt.ID);
        UnlockTracker.unlockCard(Meditate.ID);
        UnlockTracker.unlockCard(MirrorImage.ID);
        UnlockTracker.unlockCard(MagicMissiles.ID);
        //UnlockTracker.unlockCard(PoisonGas.ID); //DEPRECATED
        //UnlockTracker.unlockCard(AirPrison.ID);
        UnlockTracker.unlockCard(BluntWeapons.ID);
        UnlockTracker.unlockCard(IcicleBlast.ID);
        //UnlockTracker.unlockCard(OpenMind.ID);
        UnlockTracker.unlockCard(PriceOfKnowledge.ID);
        UnlockTracker.unlockCard(BarrierOfLight.ID);
        //UnlockTracker.unlockCard(Manablast.ID);
        //UnlockTracker.unlockCard(EldritchApplyPoison.ID);
        //UnlockTracker.unlockCard(EldritchApplyVulnerable.ID);
        //UnlockTracker.unlockCard(EldritchApplyWeak.ID);
        //UnlockTracker.unlockCard(Conversion.ID);
        UnlockTracker.unlockCard(Escalation.ID);
        //UnlockTracker.unlockCard(Inversion.ID);
        //UnlockTracker.unlockCard(Splinter.ID);
        UnlockTracker.unlockCard(AmplifyingSpellstone.ID);
        UnlockTracker.unlockCard(EnchantedStrike.ID);
        UnlockTracker.unlockCard(FocusingBlow.ID);
        UnlockTracker.unlockCard(LightningBlade.ID);
        UnlockTracker.unlockCard(Overcharge.ID);
        UnlockTracker.unlockCard(LiquidumScroll.ID);
        UnlockTracker.unlockCard(ScindoSpellstone.ID);
        UnlockTracker.unlockCard(BlankScroll.ID);
        UnlockTracker.unlockCard(StemTheFlow.ID);
        //UnlockTracker.unlockCard(BloodBulwark.ID);
        UnlockTracker.unlockCard(FalseLife.ID);
        UnlockTracker.unlockCard(WordsOfPower.ID);
        UnlockTracker.unlockCard(InscribeDeath.ID);
        UnlockTracker.unlockCard(WeirdBlast.ID);
        UnlockTracker.unlockCard(WeirdShield.ID);
        UnlockTracker.unlockCard(StaticCage.ID);
        UnlockTracker.unlockCard(TelumSpellstone.ID);
        UnlockTracker.unlockCard(FlowLikeWater.ID);
        UnlockTracker.unlockCard(CapacitanceScroll.ID);
        UnlockTracker.unlockCard(Incendio.ID);
        //UnlockTracker.unlockCard(Exsanguinate.ID);
        UnlockTracker.unlockCard(Conflagrate.ID);
        UnlockTracker.unlockCard(AbsoluteZero.ID);
        UnlockTracker.unlockCard(AnomalousTexts.ID);
        UnlockTracker.unlockCard(RecklessResponse.ID);
        UnlockTracker.unlockCard(ForkedBolt.ID);
        UnlockTracker.unlockCard(FocusedBolt.ID);
        UnlockTracker.unlockCard(Maelstrom.ID);
        UnlockTracker.unlockCard(CripplingCharge.ID);
        UnlockTracker.unlockCard(ScrollOfPoison.ID);
        UnlockTracker.unlockCard(CrystalShield.ID);
        UnlockTracker.unlockCard(FocusingBarrier.ID);
        UnlockTracker.unlockCard(RecurringRime.ID);
        UnlockTracker.unlockCard(ArcaneArcing.ID);
        UnlockTracker.unlockCard(LightningStrike.ID);
        UnlockTracker.unlockCard(IcyDefend.ID);
        UnlockTracker.unlockCard(StarterModifier.ID);
        UnlockTracker.unlockCard(ScrollOfShadows.ID);
        UnlockTracker.unlockCard(ScrollOfChaining.ID);
        UnlockTracker.unlockCard(StaticSeries.ID);
        UnlockTracker.unlockCard(ScriptedStarfall.ID);
        UnlockTracker.unlockCard(TimeDilation.ID);
        UnlockTracker.unlockCard(ScrollOfScrying.ID);
        UnlockTracker.unlockCard(PiercingBolts.ID);
        UnlockTracker.unlockCard(CatalyticCatastrophe.ID);
        UnlockTracker.unlockCard(WeirdToxin.ID);
        UnlockTracker.unlockCard(GlacialGuard.ID);
        UnlockTracker.unlockCard(FrostvenomToxin.ID);
        UnlockTracker.unlockCard(ToxicSpellstone.ID);
        UnlockTracker.unlockCard(QuickStudy.ID);
        UnlockTracker.unlockCard(InvigoratingStrike.ID);
        //UnlockTracker.unlockCard(PaperCut.ID);
        UnlockTracker.unlockCard(FranticSearch.ID);
        UnlockTracker.unlockCard(Overexert.ID);
        UnlockTracker.unlockCard(ThrownScroll.ID);
        UnlockTracker.unlockCard(Divination.ID);
        UnlockTracker.unlockCard(GatherComponents.ID);
        UnlockTracker.unlockCard(SanguineSurge.ID);
        UnlockTracker.unlockCard(GlaciesSpellstone.ID);

        /*UnlockTracker.unlockCard(DefaultAttackWithVariable.ID);
        UnlockTracker.unlockCard(StarterDefend.ID);
        UnlockTracker.unlockCard(DefaultCommonPower.ID);
        UnlockTracker.unlockCard(DefaultUncommonSkill.ID);
        UnlockTracker.unlockCard(DefaultUncommonAttack.ID);
        UnlockTracker.unlockCard(DefaultUncommonPower.ID);
        UnlockTracker.unlockCard(DefaultRareAttack.ID);
        UnlockTracker.unlockCard(DefaultRareSkill.ID);
        UnlockTracker.unlockCard(DefaultRarePower.ID);*/

        logger.info("Done adding cards!");
    }

    // ================ /ADD CARDS/ ===================


    // ==============SPELL MANAGEMENT================
    /*@Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        Cast_Spell.LightningDMGCounter = SpellAttack.amountToCast;
        return true;
    }*/
    // ==============SPELL MANAGEMENT================



    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheScribeEnum.THE_SCRIBE.toString());

        BaseMod.addCharacter(new TheScribe("the Spell Scribe", TheScribeEnum.THE_SCRIBE),
                makePath(THE_SCRIBE_BUTTON), makePath(THE_SCRIBE_PORTRAIT), TheScribeEnum.THE_SCRIBE);

        receiveEditPotions();
        logger.info("Done editing characters");
    }

    // =============== /LOAD THE CHARACTER/ =================

    // ================ ADD POTIONS ===================


    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific, just remove the player class at the end (in this case the "TheScribeEnum.THE_SCRIBE")
        //BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheScribeEnum.THE_SCRIBE);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new StarterRelic(), AbstractCardEnum.SCRIBE_BLUE);
        //BaseMod.addRelicToCustomPool(new StarterRelic2(), AbstractCardEnum.SCRIBE_BLUE);
        BaseMod.addRelicToCustomPool(new OsmoticFilter(), AbstractCardEnum.SCRIBE_BLUE);
        BaseMod.addRelicToCustomPool(new StormScale(), AbstractCardEnum.SCRIBE_BLUE);
        BaseMod.addRelicToCustomPool(new PermafrostPen(), AbstractCardEnum.SCRIBE_BLUE);
        BaseMod.addRelicToCustomPool(new AlchemicalAlter(), AbstractCardEnum.SCRIBE_BLUE);
        BaseMod.addRelicToCustomPool(new ClarityBossRelic(), AbstractCardEnum.SCRIBE_BLUE);
        BaseMod.addRelicToCustomPool(new RunicRepeater(), AbstractCardEnum.SCRIBE_BLUE);
        BaseMod.addRelicToCustomPool(new MeditationCircle(), AbstractCardEnum.SCRIBE_BLUE);
        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        if(hasAspiration) {
            //BaseMod.addRelic(new ScribeSkillbook(), RelicType.SHARED);
        }

        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================

    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                "TheScribeResources/localization/ScribeMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                "TheScribeResources/localization/ScribeMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                "TheScribeResources/localization/ScribeMod-Relic-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                "TheScribeResources/localization/ScribeMod-Potion-Strings.json");

        logger.info("Done editting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        final String[] Cast = { "Cast", "casts", "cast"};
        BaseMod.addKeyword("scribe", "Cast", Cast, "Use up all your stored Spell Effects and Spell Modifiers to cause effects. Additionally causes Scribed Spells to stop being added to your hand until another Spell Effect is played.");

        final String[] Spectral = { "Spectral", "spectral"};
        BaseMod.addKeyword("scribe","Spectral", Spectral, "Purged from your hand at the end of your turn.");

        final String[] SpellEffect = { "Spell Effect", "Spell Effects", "spell_effect", "spell_effects"};
        BaseMod.addKeyword("scribe","Spell Effect", SpellEffect, "Adds an effect to your next spell. Additionally adds a Scribed Spell to your hand and at the start of every turn if you do not have one.");

        final String[] SpellModifier = { "Spell Modifier", "Spell Modifiers", "spell_modifier", "spell_modifiers"};
        BaseMod.addKeyword("scribe","Spell Modifier", SpellModifier, "Modifies the effects of your next spell.");

        final String[] Drained = { "drained", "drain"};
        BaseMod.addKeyword("scribe","Drained", Drained, "Lose 1 Energy at the start of your turn. At the start of each turn, drained is reduced by 1.");

        final String[] SpellEffectiveness = {"Spell Effectiveness", "spell_effectiveness", "spell_effectivity"};
        BaseMod.addKeyword("scribe","Spell Effectiveness", SpellEffectiveness, "Your next spell will be X % more effective. ALL Spell Effects will be multiplied by X %.");

        final String[] SpellAdditionalCasts = {"Additional Casts", "Additional Cast", "additional_casts", "additional_cast"};
        BaseMod.addKeyword("scribe","Additional Casts", SpellAdditionalCasts, "Your next spell will cast X additional times.");

        final String[] Thunder = {"Thunder", "thunder"};
        BaseMod.addKeyword("scribe","Thunder", Thunder, "Any and all instances of Spell Effect: Damage.");

        final String[] Echo = {"Echo", "echo", "echoes"};
        BaseMod.addKeyword("scribe","Echo", Echo, "Echoes are copies of cards with Ethereal and Exhaust.");

    }

    // ================ /LOAD THE KEYWORDS/ ===================

    // =============== POST-INITIALIZE =================


    @Override
    public void receivePostInitialize() {

        logger.info("Loading badge image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = new Texture(makePath(BADGE_IMAGE));

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton skillbookBtn = new ModLabeledToggleButton("Allow Scribe's Skillbook to add cards from the Spell Scribe to card rewards screens.", 350.0F, 700.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, skillbookCardpool(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("SkillbookCardpool", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        //if(hasAspiration) {
            //settingsPanel.addUIElement(skillbookBtn);
        //}
        //else
        //{
            settingsPanel.addUIElement(new ModLabel("ScribeMod doesn't have any settings!", 400.0f, 700.0f,
                    settingsPanel, (me) -> {
            }));
        //}
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");

    }
    // =============== / POST-INITIALIZE/ =================


    //SCRIBED SCROLL STUFF
    @Override
    public void receivePostBattle(AbstractRoom battleRoom)
    {
        AbstractScribeCard.ScribedScrollAcquireCounter = 0;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room)
    {
        AbstractScribeCard.ScribedScrollAcquireCounter = 0;
    }
    //SCRIBED SCROLL STUFF




    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return "theScribe:" + idText;
    }

}