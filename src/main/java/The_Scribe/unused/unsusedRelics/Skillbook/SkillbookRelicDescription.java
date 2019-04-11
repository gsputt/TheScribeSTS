package The_Scribe.unused.unsusedRelics.Skillbook;

import The_Scribe.ScribeMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;

public interface SkillbookRelicDescription {
    class data
    {
        private static final String ID = ScribeMod.makeID("Skillbook");
        private static final String[] DESCRIPTIONS;
        static
        {
            RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
            DESCRIPTIONS = relicStrings.DESCRIPTIONS;
        }
    }

    default String[] SKILLBOOK_DESCRIPTIONS()
    {
        String[] tmp = new String[data.DESCRIPTIONS.length-1];
        tmp[0] = data.DESCRIPTIONS[0];
        if(ScribeMod.skillbookCardpool()) {
            tmp[1] = data.DESCRIPTIONS[1];
        } else {
            tmp[1] = data.DESCRIPTIONS[2];
        }
        tmp[2] = data.DESCRIPTIONS[3];

        return data.DESCRIPTIONS;
    }
}