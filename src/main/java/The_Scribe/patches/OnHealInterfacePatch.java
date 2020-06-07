package The_Scribe.patches;

import The_Scribe.cards.CardGainHealthInterface;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class OnHealInterfacePatch {
    @SpirePatch(
            clz = AbstractCreature.class,
            method = "heal",
            paramtypez = {int.class, boolean.class}
    )
    public static class onHeal
    {
        @SpirePostfixPatch
        public static void check(AbstractCreature __instance, int healAmount, boolean showEffect)
        {
            if(AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
            {
                for(AbstractCard c : AbstractDungeon.player.hand.group)
                {
                    if(c instanceof CardGainHealthInterface)
                    {
                        ((CardGainHealthInterface) c).onHeal();
                    }
                }
            }
        }
    }
}
