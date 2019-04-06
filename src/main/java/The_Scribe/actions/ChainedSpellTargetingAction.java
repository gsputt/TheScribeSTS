package The_Scribe.actions;

import The_Scribe.cards.Cast_Spell;
import The_Scribe.powers.SpellChaining;
import basemod.BaseMod;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class ChainedSpellTargetingAction implements RenderSubscriber {
    private boolean isVisible;
    private AbstractMonster monster;
    private AbstractCard card;
    private int counter;
    public ArrayList<AbstractMonster> returnedArray = null;
    public ChainedSpellTargetingAction(AbstractCard card, boolean visible)
    {
        BaseMod.subscribe(this);
        this.isVisible = visible;
        this.card = card;
        this.counter = 0;
        this.returnedArray = null;
    }

    @Override
    public void receiveRender(SpriteBatch sb)
    {
        render(sb);
    }

    public void render(SpriteBatch sb)
    {
        if(this.isVisible)
        {
            if(this.counter == 0) {
                this.returnedArray = wtfIsThisMessOfCode();
                this.counter++;
            }
            if(this.returnedArray != null && this.returnedArray.size() > 0)
            {
                Cast_Spell.ChainedSpellTargetMonstersList = this.returnedArray;
                int i = 0;
                while(i < this.returnedArray.size())
                {
                    this.returnedArray.get(i).renderReticle(sb);
                    i++;
                }
            }
        }
    }

    private ArrayList<AbstractMonster> wtfIsThisMessOfCode()
    {
        ArrayList<AbstractMonster> returnArray = new ArrayList<>();
        if(this.card == AbstractDungeon.player.hoveredCard && AbstractDungeon.player.isHoveringDropZone) {
            if (AbstractDungeon.getCurrRoom().monsters.hoveredMonster != null && !AbstractDungeon.getCurrRoom().monsters.hoveredMonster.isDead && !AbstractDungeon.getCurrRoom().monsters.hoveredMonster.escaped) {
                if (AbstractDungeon.getCurrRoom().monsters.monsters.size() > 1) {
                    Iterator<AbstractMonster> monsterIterator = AbstractDungeon.getMonsters().monsters.iterator();
                    ArrayList<AbstractMonster> monsterArray = new ArrayList<>();
                    ArrayList<AbstractMonster> processedArray = new ArrayList<>();
                    while (monsterIterator.hasNext()) {
                        monster = monsterIterator.next();
                        if (!monster.equals(AbstractDungeon.getCurrRoom().monsters.hoveredMonster)) {
                            monsterArray.add(monster);
                        }
                    }
                    if (!(monsterArray.size() <= 0)) {
                        int i = 0;
                        int ChainingAmount = 0;
                        if(AbstractDungeon.player.hasPower(SpellChaining.POWER_ID))
                        {
                            ChainingAmount = AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount;
                        }
                        if(ChainingAmount > AbstractDungeon.getCurrRoom().monsters.monsters.size())
                        {
                            ChainingAmount = AbstractDungeon.getCurrRoom().monsters.monsters.size()-1;
                        }
                        while (i < ChainingAmount) { // change to < ChainingAmount
                            AbstractMonster processing = getRandomMonsterFromMonsterArray(AbstractDungeon.miscRng, monsterArray);
                            if (processedArray.contains(processing)) {
                                processing = getRandomMonsterFromMonsterArray(AbstractDungeon.miscRng, monsterArray);
                            } else {
                                processedArray.add(processing);
                                i++;
                            }
                        }
                        while (processedArray.size() > 0) {
                            returnArray.add(processedArray.get(0));
                            processedArray.remove(0);
                        }
                    }
                }
            }
        }
        return returnArray;
    }

    public void end()
    {
        this.isVisible = false;
        this.returnedArray = null;
        this.counter = 0;
    }


    private AbstractMonster getRandomMonsterFromMonsterArray(Random rng, ArrayList<AbstractMonster> monsterArray)
    {
        return  monsterArray.get(rng.random(monsterArray.size() - 1));
    }
}

