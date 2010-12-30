package com.smartgwt.extensions.containers.client;

import java.util.HashMap;

import com.smartgwt.client.widgets.Canvas;

/**
 * A Canvas that manages its children similar to Swing CardLayout. It treats
 * each component in the container as a card. Only one card is visible at a
 * time, and the container acts as a stack of cards. The first component added
 * to a CardLayout object is the visible component when the container is first
 * displayed.
 * 
 * @author farrukh@wellfleetsoftware.com
 */
public class CardLayoutCanvas extends Canvas {

	private HashMap<Object, Canvas> cards = null;
	private Object currentCardKey = null;

	public CardLayoutCanvas() {
		cards = new HashMap<Object, Canvas>();
	}
	
	public void addCard(Object key, Canvas card) {
		
		if(key == null)
			throw new IllegalArgumentException("key must not be NULL");
		
		if(card == null)
			throw new IllegalArgumentException("card must not be NULL");
		
		card.setWidth("100%");
		card.setHeight("100%");
		card.setPageLeft(0);
		card.setPageTop(0);
		this.addChild(card);
		cards.put(key, card);
		
		if(currentCardKey == null)
			currentCardKey = key;
		else
			card.hide();
	}
	
	public void showCard(Object key) {
		
		if(key == null)
			throw new IllegalArgumentException("key must not be NULL");
		
		for (Object _key : cards.keySet()) {
			Canvas c = cards.get(_key);
			if (key.equals(_key)) {
				c.show();
				currentCardKey = key;
			} else {
				c.hide();
			}
		}
	}
	
	/**
	 * @return the cards
	 */
	public HashMap<Object, Canvas> getCards() {
		return cards;
	}

	/**
	 * @return the currentCard
	 */
	public Canvas getCurrentCard() {
		return  (currentCardKey==null) ? null : cards.get(currentCardKey);
	}
	
	/**
	 * @return the currentCard key
	 */
	public Object getCurrentCardKey() {
		return currentCardKey;
	}
}