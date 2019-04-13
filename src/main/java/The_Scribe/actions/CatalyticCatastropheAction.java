package The_Scribe.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class CatalyticCatastropheAction extends AbstractGameAction {

    private AbstractCreature target;
    private AbstractCreature source;

    public CatalyticCatastropheAction(AbstractCreature target, AbstractCreature source)
    {
        this.target = target;
        this.source = source;
    }
    @Override
    public void update()
    {
        isDone = true;
        if(target.hasPower(PoisonPower.POWER_ID))
        {
            int amount = target.getPower(PoisonPower.POWER_ID).amount;
            int damage = 0;
            while (amount > 0)
            {
                damage += amount;
                amount -= 1;
            }
            AbstractDungeon.actionManager
                    .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(target,
                            new DamageInfo(source, damage, DamageInfo.DamageType.NORMAL),
                            AttackEffect.POISON, true));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.target, this.source, PoisonPower.POWER_ID));
        }

    }
}
