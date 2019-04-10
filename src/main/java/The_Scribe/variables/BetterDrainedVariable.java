package The_Scribe.variables;

import The_Scribe.cards.AbstractScribeCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class BetterDrainedVariable extends DynamicVariable {
    @Override
    public String key() {
        return "BETTERDRAINED";
        // What you put in your localization file between ! to show your value. Eg, !myKey!.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractScribeCard) {
            AbstractScribeCard card1 = (AbstractScribeCard) card;
            return card1.isDrainedModified;
        } else {
            return false;
        }
        // Set to true if the value is modified from the base value.
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractScribeCard) {
            AbstractScribeCard card1 = (AbstractScribeCard) card;
            return card1.drained;
        } else {
            return 0;
        }
        // What the dynamic variable will be set to on your card. Usually uses some kind of int you store on your card.
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractScribeCard) {
            AbstractScribeCard card1 = (AbstractScribeCard) card;
            return card1.drained;
        } else {
            return 0;
        }
        // Should generally just be the above.
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if(card instanceof AbstractScribeCard) {
            AbstractScribeCard card1 = (AbstractScribeCard) card;
            return card1.upgradeDrained;
        }
        else
        {
            return false;
        }
        // Set to true if this value is changed on upgrade
    }
}