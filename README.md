# Poker Deck Builder

Western poker roguelike design document (distilled version). This is the current **core specification** for prototype implementation. **Weapons and pips are tabled** for later.

## 1) Core Combat Loop
- Draw 5 cards.
- Select a subset.
- Form a poker hand.
- Resolve all selected card effects + hand bonus.
- Discard played cards.
- Draw back to 5 (or modified hand size).

If you pass/end turn without playing:
- Discard entire hand.
- Draw a fresh 5 next turn.
- No bonuses and no card effects.

Hand size defaults to 5 and can be modified by relics, buffs, and status effects.

## 2) Card System

### 2.1 Suits (Card Types)
- **Hearts**: Healing (salves, bandages, whiskey; detailed effects below)
- **Clubs**: Attacks (bullets, punches, knives; detailed effects below)
- **Diamonds**: Money/Betting (gold, pot; see Section 5)
- **Spades**: Stance/Defense (cover, hiding, elevation; see Section 4)

### 2.2 Card Ranks & Base Effects
- Number cards (3–10): base effect = rank value.
- Face cards (J/Q/K): stronger base effects + Showdown triggers.
  - J: utility (draw/discard/manipulation)
  - Q: AoE or multi-target
  - K: heavy single-target or execute

#### Aces: "The Pivot"
- **Low Ace (1)**: in A-2-3-4-5 straight, or as lowest card in hand.
  - Underdog: if your HP < enemy HP, hand deals +50% damage/healing.
- **High Ace (14)**: all other contexts.
  - Ace High: this card triggers at 1.5x magnitude.

Ace-suit synergies:
- Ace + any Club pair: pair bonus becomes triple instead of double.
- Ace + 2+ Diamonds in hand: gained gold is locked (persists even on loss).
- Ace + healing hand: overheal converts to Grit (temporary HP).
- Ace + stance hand: Cover -> Bunker, Elevated -> Sniper's Nest.

### 2.3 The Cursed 2s (Risk/Reward)
2s trigger downside immediately, then upside conditionally:

| Card | Downside (Immediate) | Upside (Conditional) |
|---|---|---|
| 2 "Desperation Strike" | Take 4 damage | Deal 12 damage to one enemy |
| 2 "Loaded Bet" | Lose 8 gold (or to 0) | Gain 20 gold at end of combat if you win |
| 2 "Tainted Salve" | Heal enemy for 6 | Heal yourself for 15 |
| 2 "Reckless Cover" | Enter Exposed (+50% damage taken, 2 turns) | Gain Bunker next turn (block all, retaliate) |

Resolution order: downside before hand bonus. Pair of 2s means both downsides first, then both upsides.

### 2.4 Spoiled Aces (Curse Mechanic)
Acquired via cursed relics, enemy abilities, or losing bets. They invert normal Ace behavior.

| Spoiled Ace | Curse Effect | Redemption Trigger | Cleansed Effect |
|---|---|---|---|
| Rusty Bullet | Deal 8 damage to self | Play in hand that kills an enemy | Silver Bullet: execute enemies below 25% HP |
| Marked Deck | Enemy gains 15 block | Play in Flush of Diamonds | High Roller: combat gold x1.5 |
| Leech | Heal enemy for 10 | Play in Full House | Doc's Touch: overheal -> permanent max HP |
| Broken Compass | Enter Lost stance (random) | Play in Straight Flush | True North: choose any stance, lasts 2 turns |

Redemption is permanent for the run.

### 2.5 Jokers
Typed jokers:
- Heart Joker: heal 3 HP; in hand doubles hand healing effects.
- Club Joker: deal 3 random damage; in hand, damage ignores 50% block.
- Diamond Joker: gain 5 gold; in hand +50% gold from hand.
- Spade Joker: gain 2 block; in hand stance effects last +1 turn.
- Untyped Joker: draw 1 card; choose any suit bonus at half strength.

Combinations:
- Joker + Joker (same suit): trigger 3x.
- Joker + Joker (different suits): both apply.
- Joker + Ace: Joker copies Ace suit for Flush checks but keeps own effect.

## 3) Poker Hands & Showdown Bonuses
All recognized hands grant card effect sum + hand-type bonus.

| Poker Hand | Showdown Bonus | Flavor |
|---|---|---|
| High Card | +1 to card effect | Lone Gunman |
| Pair | Paired card effect triggers twice | Double Draw |
| Two Pair | Choose one pair; hits all enemies | Crossfire |
| Three of a Kind | Triplet effect tripled | Triple Threat |
| Straight | Draw 2 cards | On the Draw |
| Flush | Suit mastery: massive suit amplification | Suit Sweep |
| Full House | 3+2 effects at triple/double strength | Dead Man's Hand |
| Four of a Kind | Quad effect ignores block/defense | Four Aces |
| Straight Flush | All 5 effects trigger, hand returns to deck | Royal Run |
| Royal Flush | Full heal + max damage + max gold + perfect stance + Joker effect | Ace in the Hole |

Impossible hands (via relics/buffs expanding hand size):
- Five of a Kind: effect 5x, then card removed from deck for rest of combat.
- Double Flush: choose one flush, get second flush suit bonus at half strength.
- Six Card Straight: all effects trigger, draw 3 cards.

## 4) Stance System (Spades)

| Stance | Entered By | Effect | Poker Synergy |
|---|---|---|---|
| Normal | Default | No bonus/penalty | — |
| Cover | Low Spades (2–5) | +5 block, -2 damage dealt | Pair in Cover doubles block instead of damage |
| Hiding | Mid Spades (6–9) | Untargetable, -50% damage | Straight while Hiding: Ambush (exit Hiding, deal 3x damage) |
| Elevated | High Spades (10–K) | +50% damage, enemies focus you | Flush of Spades: Sniper's Perch (ignore block, can't be countered) |
| Dust Devil | Ace or Joker+Spades | Next attack hits all enemies, then breaks | Royal Flush in Spades: Dust Storm (3 turns Dust Devil) |

## 5) Betting System (Diamonds)

### Pot Rules
- Pot starts at 0 each combat.
- Diamond cards add to pot when played.
- Pot is collected as gold at combat end.
- Pot is lost if you flee or die.

| Diamond Rank | Pot Contribution |
|---|---|
| 2–5 | +2 |
| 6–9 | +5 |
| 10–K | +10 |
| A | +15 (All In: Pot x2 if win) |

Betting actions:
- Check: pass turn, draw new hand, no pot change.
- Raise: play Diamonds-only hand, no combat effects, pot += sum(card values) x2.

House edge: some enemies steal 20% of pot when they hit you.

## 6) Even/Odd and Red/Black Synergies

Even/Odd:
- Even cards (2/4/6/8/10): Ricochet potential (with relics, bounce to secondary targets).
- Odd cards (3/5/7/9/A): Precision (with relics, ignore 25% block).

Red/Black sequencing:
- Red -> Black: Blood Money (+2 gold on black card).
- Black -> Red: Hot Lead (+2 damage on red card).

Example cards:
- Ricochet Shot (6): deal 6; if last card even, bounce for 3.
- Blood Money (7): gain 7 gold; if last card black, +3 gold but take 2 damage.
- Dusty Trail (3): enter Cover; if 3+ black cards in hand, becomes Bunker.
- Ladies' Night (Q): heal 12; with K or J in hand, heal becomes AoE.
- Dead Man's Draw (Black Ace): draw 2; if both red, discard both and deal 10 to all.

## 7) Meta-Progression
Between runs:
- Cards reset to basic starter deck.
- Weapons would persist between runs when implemented (feature tabled for now).
- Relics: some persist (TBD).
- Unlocks: new cards, joker types, spoiled ace redemption paths.

## 8) Side Points (Tabled for Later)
- Pips system options (Count/Ante/Table) are postponed.
- Weapons systems are postponed (passive modifiers rejected; mechanical identities preferred if revisited; fighting styles as simpler alternative).
- Additional tabled mechanics:
  - Pip-grid positioning
  - Mid-combat weapon switching
  - Dual-wield relics
  - Spoiled jokers beyond untyped randomness
  - Expanded color joker set
  - 6+ card hand evaluation as core mechanic

## 9) Design Pillars
1. Poker hand evaluation is the core verb.
2. Suits communicate identity immediately.
3. Risk/reward must be explicit (2s, aces, jokers).
4. Hand quality should outweigh single-card quality.
5. Passing is a valid tactical choice.
6. Meta progression is unlock-focused; weapons are side content.

## 10) Open Questions
1. Face cards: unique named effects vs scaled numeric effects?
2. Exact numerical tuning for damage/healing/gold/block.
3. Starting deck size and reward pacing.
4. Shop system scope (buy/remove cards vs relic-only).
5. Enemy design (poker hands vs ability-only AI).
6. Boss mechanics (hand-gated vulnerability?).
7. Spoiled Ace removability vs forced redemption.
8. Joker acquisition model (drops, shop, events).
