package The_Scribe.unused.unusedVariables;

import The_Scribe.powers.SpellEffectiveness;
import The_Scribe.unused.unusedPowers.SpellVulnerable;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class VulnerableDark extends DynamicVariable
{   // Custom Dynamic Variables are what you do if you need your card text to display a cool, changing number that the base game doesn't provide.
    // If the !D! and !B! (for Damage and Block) etc. are not enough for you, this is how you make your own one. It Changes In Real Time!


    // This is what you type in your card string to make the variable show up. Remember to encase it in "!"'s in the json!
    @Override
    public String key()
    {
        return "DarkVulnerable";
    }

    // Checks whether the current value is different than the base one.
    // For example, this will check whether your damage is modified (i.e. by strength) and color the variable appropriately (Green/Red).
    @Override
    public boolean isModified(AbstractCard card)
    {
        return false;
    }

    // The value the variable should display.
    @Override
    public int value(AbstractCard card)
    {
        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID) && AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID))
        {
            return (int)Math.ceil(AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount
                    * (1 + (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount * 0.01 * 25)));
        }
        else if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID))
        {
            return (int)Math.ceil(AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount);
        }
        else
        {
            return 0;
        }
    }

    // The baseValue the variable should display.
    // just like baseBlock or baseDamage, this is what the variable should reset to by default. (the base value before any modifications)
    @Override
    public int baseValue(AbstractCard card)
    {
        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID) && AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID))
        {
            return (int)Math.ceil(AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount
                    * (1 + (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount * 0.01 * 25)));
        }
        else if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID))
        {
            return (int)Math.ceil(AbstractDungeon.player.getPower(SpellVulnerable.POWER_ID).amount);
        }
        else
        {
            return 0;
        }
    }

    // If the card has it's damage upgraded, this variable will glow green on the upgrade selection screen as well.
    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card.upgraded;
    }
}