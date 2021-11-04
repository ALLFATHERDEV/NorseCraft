package com.norsecraft.client.ymir.widget;

import com.google.common.collect.Lists;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Similar to the CardLayout in AWT, this panel displays one widget at a time from a list of widgets.
 */
public class YmirCardPanel extends YmirPanel {

    private final List<YmirWidget> cards = Lists.newArrayList();
    private int selectedIndex = 0;

    /**
     * Adds a card to this panel without resizing it.
     *
     * @param card the added card
     */
    public void add(YmirWidget card) {
        this.add(cards.size(), card);
    }

    /**
     * Adds a card to this panel without resizing it.
     *
     * @param index the index of the card
     * @param card  the added card
     */
    public void add(int index, YmirWidget card) {
        cards.add(index, card);
        card.setParent(this);
        card.setLocation(0, 0);
        expandToFit(card);
    }

    /**
     * Adds a card to this panel and resizes it.
     *
     * @param card   the added card
     * @param width  the new width
     * @param height the new height
     */
    public void add(YmirWidget card, int width, int height) {
        this.add(cards.size(), card, width, height);
    }

    /**
     * Adds a card to this panel and resizes it.
     *
     * @param index  the index of the card
     * @param card   the added card
     * @param width  the new width
     * @param height the new height
     */
    public void add(int index, YmirWidget card, int width, int height) {
        if (card.canResize())
            card.setSize(width, height);
        this.add(index, card);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public YmirCardPanel setSelectedIndex(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex >= cards.size()) {
            throw new IndexOutOfBoundsException("Card index " + selectedIndex + " out of bounds: 0 <= index <" + cards.size());
        }

        if (this.selectedIndex != selectedIndex) {
            this.selectedIndex = selectedIndex;
            layout();
        }

        return this;
    }

    public YmirWidget getSelectedCard() {
        return cards.get(getSelectedIndex());
    }

    public YmirCardPanel setSelectedCard(YmirWidget selectedCard) {
        if (!cards.contains(selectedCard)) {
            throw new NoSuchElementException("Widget " + selectedCard + " is not a card in this panel!");
        }

        return setSelectedIndex(cards.indexOf(selectedCard));
    }

    public int getCardAmount() {
        return cards.size();
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, y);
        for (YmirWidget card : cards)
            card.setSize(x, y);
    }

    @Override
    public void layout() {
        children.clear();
        for (YmirWidget child : cards) {
            if (child instanceof YmirPanel) ((YmirPanel) child).layout();
            expandToFit(child);

            if (child == getSelectedCard())
                child.onShown();
            else
                child.onHidden();
        }

        for (YmirWidget child : cards) {
            child.setSize(getWidth(), getHeight());
        }

        children.add(getSelectedCard());
    }

    @Override
    public void validate(GuiInterpretation host) {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No children in card panel");
        }

        layout();
        for (YmirWidget card : cards) {
            card.validate(host);
            if (getSelectedCard() != card) card.onHidden();
        }
    }

    @Override
    public void addPainters() {
        for (YmirWidget card : cards)
            card.addPainters();
    }
}
