/**
Copyright (c) <2009> <Pete Boysen>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.smartgwt.extensions.util.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.FormErrorOrientation;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.Validator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * UI factory to simplify and reduce size of smartGWT code.
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class UI {

	private static Validator emptyValidator;

	private static String backgroundColor = "#D3DAED";

	public static HLayout getHLayout() {
	   final HLayout layout = new HLayout();
		setCanvasDefaults(layout);
		return layout;
	}

	public static VLayout getVLayout() {
	   final VLayout layout = new VLayout();
		setCanvasDefaults(layout);
		return layout;
	}

   public static HStack getHStack() {
      final HStack stack = new HStack();
      stack.setPadding(5);
      stack.setMembersMargin(10);
      setCanvasDefaults(stack);
      return stack;
   }

   public static VStack getVStack() {
      final VStack stack = new VStack();
      setCanvasDefaults(stack);
      stack.setPadding(5);
      stack.setMembersMargin(10);
      stack.setDefaultLayoutAlign(Alignment.CENTER);
      return stack;
   }

   public static TabSet getTabSet() {
	   TabSet tabSet = new TabSet();
	   tabSet.setWidth100();
	   tabSet.setHeight100();
	   return tabSet;
   }
   
   public static IButton getButton(String title, String prompt, final ButtonListener listener) {
      final IButton button = new IButton(title);
      button.setID(Util.newID());
      button.setAutoFit(true);
      button.setPrompt("<nobr>" + prompt + "</nobr>");
      button.addClickHandler(new ClickHandler() {
         public void onClick(ClickEvent e) {
            IButton b = (IButton) e.getSource();
            if (listener != null)
               listener.buttonPressed(b.getID());
         }
      });
      return button;
   }

   public static Canvas getToolbar(String[][] buttons, final ButtonListener listener) {
      final VStack stack = new VStack();
      stack.setWidth100();
      stack.setDefaultLayoutAlign(Alignment.RIGHT);
      HStack toolbar = new HStack();
      toolbar.setMembersMargin(4);
      toolbar.setWidth(50);
      toolbar.setHeight(20);
      // need to translate id to index in toolbar
      final String[] ids = new String[buttons.length];
      ButtonListener bl = new ButtonListener() {
         public void buttonPressed(String id) {
            if (listener != null)
               for (int i = 0; i < ids.length; i++)
                  if (id.equals(ids[i]))
                     listener.buttonPressed(String.valueOf(i));
         }
      };
      for (int i = 0; i < buttons.length; i++) {
         IButton b = getButton(buttons[i][0], buttons[i][1], bl);
         toolbar.addMember(b);
         ids[i] = b.getID();
      }
      stack.addMember(toolbar);
      return stack;
   }

	public static DynamicForm getForm(int cols) {
	   final DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setMargin(0);
		form.setBackgroundColor(getBackgroundColor());
		form.setPadding(5);
		form.setNumCols(cols);
		form.setTitleSuffix("");
		form.setErrorOrientation(FormErrorOrientation.TOP);
		form.setShowErrorText(true);
		return form;
	}

   public static ListGrid getAutoFetchListGrid() {
      return getListGrid(true);
   }

   public static ListGrid getListGrid() {
      return getListGrid(false);
   }

   private static ListGrid getListGrid(boolean autoFetchData) {
      final ListGrid listGrid = new ListGrid();
      listGrid.setWidth100();
      listGrid.setHeight100();
      listGrid.setShowHeader(true);
      listGrid.setAlternateRecordStyles(true);
      listGrid.setShowAllRecords(true);
      listGrid.setSelectionType(SelectionStyle.SINGLE);
      if (autoFetchData) {
         listGrid.setAutoFetchData(true);
      }
      return listGrid;
   }

	public static Validator getEmptyStringValidator() {
		if (emptyValidator == null) {
			emptyValidator = new CustomValidator() {
				protected boolean condition(Object value) {
					String valueStr = (String) value;
					return value != null && valueStr.length() > 0;
				}
			};
			emptyValidator.setErrorMessage("Please enter a value");
		}
		return emptyValidator;
	}

   public static void show(Canvas canvas) {
      canvas.animateShow(AnimationEffect.FADE,null,300);
   }

   public static void showNearClick(Canvas canvas, int offsetX, int offsetY) {
      canvas.moveTo(EventHandler.getX() + offsetX, EventHandler.getY() + offsetY);
      show(canvas);
   }

   public static void hide(Canvas canvas) {
      canvas.animateHide(AnimationEffect.FADE,null,300);
   }

   public static void move(Canvas canvas) {
      canvas.animateMove(100,0);
   }

   public static String getBackgroundColor() {
      return backgroundColor;
   }

	public static void setBackgroundColor(String bcolor) {
		backgroundColor = bcolor;
	}

	private static void setCanvasDefaults(Canvas canvas) {
		canvas.setWidth100();
		canvas.setHeight100();
	}

}
