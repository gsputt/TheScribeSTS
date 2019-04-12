package The_Scribe.patches;


import The_Scribe.ScribeMod;
import The_Scribe.interfaces.onRemovePowerHook;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(
        clz = RemoveSpecificPowerAction.class,
        method = "update"
)
public class WordsOfPowerOnRemovePowerPatch
{

    @SpireInsertPatch(
            locator = Locator.class,
            localvars ={"removeMe"}
    )
    public static void WordsOfPowerHook(RemoveSpecificPowerAction __instance, AbstractPower removeMe)
    {
        for(AbstractPower p : AbstractDungeon.player.powers)
        {
            if(p instanceof onRemovePowerHook)
            {
                ((onRemovePowerHook) p).receiveRemovedPower(removeMe);
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "onModifyPower");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }

    }
}
