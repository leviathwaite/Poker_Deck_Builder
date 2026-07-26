package com.leviathwaite.pokerdeckbuilder;

import static org.junit.Assert.assertEquals;

import java.util.Random;
import org.junit.Test;

public class PokerDeckBuilderGameTest {
    @Test
    public void starterDeckHas52Cards() {
        assertEquals(52, PokerDeckBuilderGame.Card.createStarterDeck().size());
    }

    @Test
    public void drawAddsCardsToHand() {
        PokerDeckBuilderGame.DeckState state = PokerDeckBuilderGame.DeckState.newRun(new Random(1));
        state.drawCard();
        state.drawCard();

        assertEquals(2, state.handCount());
    }
}
