package The_Scribe.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "updateInput"
)


public class ScribeHoveredMonsterPatch {

    public static AbstractMonster scribeHoveredMonster = null;
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {

            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getMethodName().equals("updateSingleTargetInput")) {
                    m.replace("{" +
                            "$_ = $proceed($$);" +
                            "The_Scribe.patches.ScribeHoveredMonsterPatch.Nested.PassHoveredMonster(this.hoveredMonster);" +
                            "}");
                }
            }
        };
    }

    public static class Nested
    {
        public static void PassHoveredMonster(AbstractMonster m)
        {
            scribeHoveredMonster = m;
            //System.out.println("HoveredMonster: " + scribeHoveredMonster);
        }
    }

}
