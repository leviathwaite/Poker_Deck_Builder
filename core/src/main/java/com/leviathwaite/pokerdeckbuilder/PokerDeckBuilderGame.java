package com.leviathwaite.pokerdeckbuilder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PokerDeckBuilderGame extends ApplicationAdapter {
    private static final int STARTING_HAND_SIZE = 5;

    private final Random random = new Random();
    private SpriteBatch batch;
    private BitmapFont font;
    private DeckState deckState;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        resetRun();
    }

    @Override
    public void render() {
        if (Gdx.input.justTouched()) {
            deckState.drawCard();
        }

        ScreenUtils.clear(0.08f, 0.06f, 0.04f, 1f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        batch.begin();
        font.setColor(Color.GOLD);
        font.draw(batch, "Western Poker Deckbuilder", 40, Gdx.graphics.getHeight() - 40);
        font.setColor(Color.TAN);
        font.draw(batch, "Tap to draw a card", 40, Gdx.graphics.getHeight() - 95);

        font.setColor(Color.WHITE);
        font.draw(batch, "Run score: " + deckState.getScore(), 40, Gdx.graphics.getHeight() - 150);
        font.draw(batch, "Deck: " + deckState.deckCount(), 40, Gdx.graphics.getHeight() - 200);
        font.draw(batch, "Discard: " + deckState.discardCount(), 40, Gdx.graphics.getHeight() - 250);

        int handStartY = Gdx.graphics.getHeight() - 320;
        font.setColor(Color.SKY);
        font.draw(batch, "Hand:", 40, handStartY);

        int offsetY = 50;
        for (int i = 0; i < deckState.handCount(); i++) {
            Card card = deckState.peekHand(i);
            font.setColor(card.suitColor());
            font.draw(batch, card.label(), 70, handStartY - ((i + 1) * offsetY));
        }

        if (deckState.handCount() == 0) {
            font.setColor(Color.SCARLET);
            font.draw(batch, "Outlaws got you. Tap to start a new run.", 40, 120);
        }

        batch.end();

        if (deckState.handCount() == 0 && Gdx.input.justTouched()) {
            resetRun();
        }
    }

    private void resetRun() {
        deckState = DeckState.newRun(random);
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            deckState.drawCard();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    static final class DeckState {
        private final List<Card> deck;
        private final Array<Card> hand;
        private final List<Card> discard;
        private int score;

        private DeckState(List<Card> deck) {
            this.deck = deck;
            this.hand = new Array<>();
            this.discard = new ArrayList<>();
        }

        static DeckState newRun(Random random) {
            List<Card> cards = Card.createStarterDeck();
            Collections.shuffle(cards, random);
            return new DeckState(cards);
        }

        void drawCard() {
            if (deck.isEmpty()) {
                reshuffleDiscard();
            }

            if (deck.isEmpty() || hand.size >= 7) {
                return;
            }

            Card drawn = deck.remove(deck.size() - 1);
            hand.add(drawn);
            score += drawn.rankValue();

            if (hand.size > 5) {
                Card spilled = hand.removeIndex(0);
                discard.add(spilled);
                score = Math.max(0, score - 1);
            }
        }

        private void reshuffleDiscard() {
            if (discard.isEmpty()) {
                hand.clear();
                return;
            }

            deck.addAll(discard);
            discard.clear();
        }

        Card peekHand(int index) {
            return hand.get(index);
        }

        int handCount() {
            return hand.size;
        }

        int deckCount() {
            return deck.size();
        }

        int discardCount() {
            return discard.size();
        }

        int getScore() {
            return score;
        }
    }

    record Card(String rank, String suit) {
        static List<Card> createStarterDeck() {
            List<Card> cards = new ArrayList<>();
            String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
            String[] suits = {"Spades", "Hearts", "Clubs", "Diamonds"};
            for (String suit : suits) {
                for (String rank : ranks) {
                    cards.add(new Card(rank, suit));
                }
            }
            return cards;
        }

        String label() {
            return rank + " of " + suit;
        }

        Color suitColor() {
            return ("Hearts".equals(suit) || "Diamonds".equals(suit)) ? Color.SCARLET : Color.LIGHT_GRAY;
        }

        int rankValue() {
            return switch (rank) {
                case "A" -> 11;
                case "K", "Q", "J", "10" -> 10;
                default -> Integer.parseInt(rank);
            };
        }
    }
}
