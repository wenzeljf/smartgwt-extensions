/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartgwt.extensions.client.containers;

import com.smartgwt.client.widgets.Canvas;
import java.util.HashMap;

/**
 * A Canvas that manages its children similar to Swing CardLayout.
 * It treats each component in the container as a card. Only one card is visible at a time, and the container acts as a stack of cards. The first component added to a CardLayout object is the visible component when the container is first displayed.
 *
 * @author farrukh@wellfleetsoftware.com
 */
public class CardLayoutCanvas extends Canvas {

    HashMap<Object, Canvas> cards = null;
    private Canvas currentCard = null;

    public CardLayoutCanvas() {
        cards = new HashMap<Object, Canvas>();
    }

    public void addCard(Object key, Canvas card) {
        card.setWidth("100%");
        card.setHeight("100%");
        card.setPageLeft(0);
        card.setPageTop(0);
        this.addChild(card);
        cards.put(key, card);
        currentCard = card;
    }

    public void showCard(Object key) {
        for (Object _key : cards.keySet()) {
            Canvas c = cards.get(_key);
            if (key == _key) {
                c.show();
                currentCard = c;
            } else {
                c.hide();
            }
        }
    }

    /**
     * @return the currentCard
     */
    public Canvas getCurrentCard() {
        return currentCard;
    }

    /*
    //Issue: show() draws all cards when it should only draw the shown card
    @Override
    public void show() {

    }*/
}