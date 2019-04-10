package The_Scribe.patches;


import The_Scribe.cards.AbstractScribeCard;
import The_Scribe.cards.IcyDefend;
import The_Scribe.cards.LightningStrike;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        cls = "conspire.cards.colorless.GhostlyDefend",
        method = SpirePatch.CONSTRUCTOR,
        paramtypes = "com.megacrit.cardcrawl.cards.AbstractCard",
        optional = true
)

@SpirePatch(
        cls = "conspire.cards.colorless.GhostlyStrike",
        method = SpirePatch.CONSTRUCTOR,
        paramtypes = "com.megacrit.cardcrawl.cards.AbstractCard",
        optional = true
)

public class ConspireLoneGhostPatch {

    @SpirePostfixPatch
    public static void GhostlyPatch(AbstractCard __instance, AbstractCard card)
    {
        if(card.cardID.equals(LightningStrike.ID))
        {
            __instance.baseDamage = ((AbstractScribeCard)card).baseSpellDamage;
            //System.out.println("SCRIBE POSTFIX PATCH LIGHTNING STRIKE");
        }
        else if(card.cardID.equals(IcyDefend.ID))
        {
            __instance.baseBlock = card.baseMagicNumber;
            //System.out.println("SCRIBE POSTFIX PATCH ICY DEFEND");
        }
       //System.out.println("SCRIBE GHOSTLY PATCH COMPLETE");
    }

}
