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
package com.smartgwt.extensions.htmleditor.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.extensions.util.client.ButtonListener;
import com.smartgwt.extensions.util.client.UI;
import com.smartgwt.extensions.util.client.Util;

/**
 * HTMLEditor displays and edits HTML using the FCKEditor.  
 * It also can return a list of Form Items (@see Item) which can be used
 * for client-side answer judging (@see ItemEditor).  The editor displays images, flash
 * and other files using the resPath variable which is passed to a download servlet
 * (@see DownloadServlet).  See comment in initEditor for changing the basePath of the servlet
 * to your own webapp path.  In the example, tomcat/webapps/thinkspace is the webapps folder
 * of the application.  
 * 
 * For deployment, you have two choices:
 * 1. Put fckeditor in the public folder and add
 *     <script src="fckeditor/fckeditor.js"/>
 *     to your gwt.xml file.
 *     Use if you only have one entry point in your application.
 * 2. Put fckeditor directly below the webapp folder and put
 *     	<script language="javascript" src="../../fckeditor/fckeditor.js"></script>
 *    in the body of your application HTML file with the src specified relative to your public folder.  This will generate some errors in hosted mode
 *    that can be ignored.  Use if you have multiple entry points that need html editing.
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class HTMLEditor extends Canvas {
    private String elementID;
    private String cssWidth,cssHeight;
    private String toolBar = "Default";
    private HTMLFlow textArea;
    private boolean editing = false;
    private String savedHTML ="";
    private String resPath;
    private String stylesPath;
    private HTMLSaveListener listener;
    private ButtonListener buttonListener;
    
    public HTMLEditor(String resPath, String stylesPath) {
    	this("600","400", resPath, stylesPath);
    }

    public HTMLEditor(String cssWidth, String cssHeight, String resPath, String stylesPath) {
    	this.cssWidth = cssWidth;
    	this.cssHeight = cssHeight;
        setWidth(cssWidth);
        setHeight(cssHeight);
        this.resPath = resPath;
        textArea = new HTMLFlow("");
		textArea.setWidth(cssWidth);
        textArea.setHeight(cssHeight);
        elementID = SC.generateID();
        addChild(textArea);
     }
    
	/**
     * Returns the HTML currently contained in the editor
     * 
     * @return the HTML currently contained in the editor
     */
    public String getHTML() {
    	if (editing) {
    		return Util.convertToAbsolute(jsniGetText(elementID),"/thinkspace/download"+resPath);
    	} else
    		return savedHTML;
     }
    
    /**
     * Returns <code>null</code> as this editor doesn't support straight text editing.
     * 
     * @return <code>null</code> as this editor doesn't support straight text editing.
     */
    public String getText() {
        return null;
    }
    
    /**
     * Sets the HTML currently contained in the editor
     * 
     * @param html the HTML currently contained in the editor
     */
    public void setHTML(String htmlText) {
    	savedHTML = htmlText;
    	if (editing)
    		jsniSetText(elementID, htmlText);
    	else {
    		// need to convert all relative references to point to download servlet
    		String newText = Util.convertToAbsolute(htmlText,"/thinkspace/download"+resPath);
    		textArea.setContents("<div id=\""+elementID+"\">"+newText+"</div>");
    		textArea.setCanFocus(true);
    		final HTMLEditor me = this;
    		Timer t = new Timer() {
    			public void run() {
    				setButtonClick(me,elementID);
    			}
    		};
    		t.schedule(100);
    	}
    }
    
    /**
     * Does nothing as straight text editing is not supported by this editor
     * 
     * @param text ignored
     */
    public void setText(String text) {
        //Do nothing
    }

	public void edit() {
        textArea.setContents("<form id=\"form-"+elementID+"\"><textarea id=\""+elementID+"\" wrap=\"virtual\">"+savedHTML+"</textarea></form>");
        textArea.hide();
        final HTMLEditor editor = this;
        Timer timer = new Timer() {
        	public void run() {
        		initEditor(editor,elementID,cssWidth,cssHeight,resPath,stylesPath,toolBar);
        		editing = true;
        		textArea.show();
        	}
        };
        timer.schedule(100);
    }
    
	public void setHTMLSaveListener(HTMLSaveListener listener) {
		this.listener = listener;
	}
	
    public void save() {
    	SC.ask("Save content?", new BooleanCallback() {
    		public void execute(Boolean value) {
    			if (value != null && value) {
    		    	String html = jsniGetText(elementID);
    		    	editing = false;
    		    	setHTML(html);
    		    	if (listener != null)
    		    		listener.onSave(html);
    			} else {
    				editing = false;
    				setHTML(savedHTML);
    			}
    		}
    	});
    }
    
    public String getToolBar() {
		return toolBar;
	}

	public void setToolBar(String toolBar) {
		this.toolBar = toolBar;
	}

	public void readFile(String filePath) {
		try {
			String url = "/thinkspace/download"+filePath;
			RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,url);
			final HTMLEditor editor = this;
			rb.setCallback(new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					editor.setHTML(response.getText());
				}
				public void onError(Request request, Throwable t) {				
				}
			});
			rb.send();
		} catch (RequestException re) {
			
		}
	}
	
	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	public List<Item> getItems() {
		List<Item> list = new ArrayList<Item>();
		try {
		LinkedHashMap<String,Item> items = new LinkedHashMap<String,Item>();
		String itemString = getFormItems(elementID);
		if (itemString.length() == 0)
			return list;
		String[] s = itemString.split("\t",-1);
		HashMap<String,Map<String,String>> values = new HashMap<String,Map<String,String>>();
		for (int i = 0; i < s.length; i+=3) {
			String type = s[i];
			String name = s[i+1];
			String value = s[i+2];
			if (type.equals("radio")) {
				Map<String,String> rvalues = values.get(name);
				Item item = items.get(name);
				String[] vals = value.split(":");
				if (rvalues == null) {
					item = new Item("",name,type,vals[0],0,"");
					rvalues = new HashMap<String,String>();
					values.put(name, rvalues);
					items.put(name,item);
				}
				if (vals.length == 2)
					item.setValue(vals[0]);
				rvalues.put(value,value);				
				items.put(name,item);				
			} else if (type.equals("select")) {
				String[] vals = name.split(":");
				name = vals[0];
				Item item = new Item("",name,type,vals[1],0,"");
				LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
				for (String option: value.split(",")) {
					String[] optvalue = option.split(":");
					map.put(optvalue[0], optvalue[1]);
				}
				item.setMap(map);
				items.put(name,item);				
			} else if (type.equals("checkbox")) {
				Item item = new Item("",name,type,value,0,"");
				LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
				map.put("true", "true");
				map.put("false", "false");
				item.setMap(map);
				items.put(name,item);				
			} else {
				Item item = new Item("",name,type,value,0,"");
				items.put(name,item);				
			}
		}
		for (Map.Entry<String,Map<String,String>> e: values.entrySet()) {
			String name = e.getKey();
			Item ans = items.get(name);
			ans.setMap(e.getValue());
		}
		for (Item item: items.values()) 
			list.add(item);
		} catch (Exception e) {
			SC.say(e.toString().substring(0,200));
		}
		return list;
	}

	public void submit(String title) {
		if (buttonListener != null)
			buttonListener.buttonPressed(title);
	}
	
	public ButtonListener getButtonListener() {
		return buttonListener;
	}

	public void setButtonListener(ButtonListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	private static native String getFormItems(String id) /*-{
		var result = "";
		if (!$doc.getElementsByTagName) return result;
		var div = $doc.getElementById(id);
		if (!div) return result;
		var inputs = div.getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) {
			var input = inputs[i];
			if (input.type == 'button' || input.type == 'submit') continue;
				result += input.type + '\t';
			if (input.type == 'checkbox') {
				result += input.name + '\t';
				result += input.checked + '\t';
			} else if (input.type == 'radio') {
				result += input.name + '\t';
				if (input.checked)
					result += input.value + ':checked\t';
				else
					result += input.value + '\t';
			} else {
				result += input.name + '\t';
				result += input.value + '\t';
			}
		}
		var selects = div.getElementsByTagName('select');
		for (var i = 0; i < selects.length; i++) {
			var select = selects[i];
			result += "select" + '\t';
			result += select.name + ":" + select.options[select.selectedIndex].value + '\t';
			var value = "";
			for (var j = 0; j < select.length; j++) {
				value += select.options[j].value+':'+select.options[j].text + ",";
			}
			result += value + '\t';
		}
		var areas = div.getElementsByTagName('textarea');
		for (var i = 0; i < areas.length; i++) {
			var area = areas[i];
			result += "textarea" + '\t';
			result += area.name + '\t';
			result += area.value + '\t';
		}
		if (result.length > 0)
			return result.substring(0,result.length-1);
		else
			return result;
	}-*/;
	
	public native void showHint(String itemId, String hint) /*-{
		if (!$doc.getElementsByName) return;
		var items = $doc.getElementsByName(itemId);
		if (items == null) return;
		var span = document.createElement("span");
		span.id = itemId+"_hint";
		span.className = "hint";
		span.innerHTML = hint;
		var parent = items[0].parentNode;
		parent.appendChild(span);
	}-*/;
	
	public native void removeHint(String itemId) /*-{
		if (!$doc.getElementById) return;
		var span = $doc.getElementById(itemId+"_hint");
		if (span == null) return;
		span.parentNode.removeChild(span);
	}-*/;

	private static native void setButtonClick(HTMLEditor htmleditor, String id) /*-{
		if (!$doc.getElementsByTagName) return;
		var div = $doc.getElementById(id);
		if (!div) return;
		var inputs = div.getElementsByTagName('input');
		function submitTitle(title) {
			return function() {
				htmleditor.@com.smartgwt.extensions.client.htmleditor.HTMLEditor::submit(Ljava/lang/String;)(title);
				return false;
			}
		}
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].type == 'submit' || inputs[i].type == 'button') {
				inputs[i].onclick = submitTitle(inputs[i].name);
			}
		}
	}-*/;
	
	private static native String jsniGetText(String elementID) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.GetXHTML(true);
            } else {
                //The instance isn't bound yet
                return null;
            }
        } else {
            //We're not bound yet in some way
            return null;
        }
    }-*/;

    private static native void jsniSetText(String elementID, String html) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.SetHTML(html);
            } else {
                //The instance isn't bound yet
                return;
            }
        } else {
            //We're not bound yet in some way
            return;
        }
    }-*/;
    
    /**
     * Create the actual editor and set up paths to various resources.  References to /thinkspace should be changed
     * to whatever servlet path you are using for your application.
     * @param htmleditor the current html editor
     * @param name name of editor (elementID)
     * @param width absolute width of editor
     * @param height absolute height of editor
     * @param resPath resource path of referenced files
     * @param stylesPath path of fckstyles.xml file
     * @param toolbar toolbar to use for this editor
     */
    private native void initEditor(HTMLEditor htmleditor, String name, String width, String height, String resPath, String stylesPath, String toolbar) /*-{
		try {
			// must compute host domain for use by the baseHRef variable since it must be absolute, not relative for a base variable.
			var hostURL = window.location.href.substring(0,window.location.href.indexOf('/thinkspace'));
			var editor = new $wnd.FCKeditor(name,parseInt(width),parseInt(height),toolbar,'');
			editor.BasePath = '/thinkspace/fckeditor/';
			var browserURL = editor.BasePath+'editor/filemanager/browser/default/browser.html?';
			var conn = 'Connector='+encodeURIComponent('/thinkspace/think?context=ThinkSpace&model=ThinkSpace&xq=connector.xq&path='+resPath+'&CurrentFolder='+resPath); 		
			editor.Config['BaseHref'] = hostURL+'/thinkspace/download'+resPath;
			editor.Config['LinkBrowserURL'] = browserURL+conn;
			editor.Config['ImageBrowserURL'] = browserURL+conn+'&Type=Image';
			editor.Config['FlashBrowserURL'] = browserURL+conn+'&Type=Flash';
			if (stylesPath != null) {
				editor.Config['StylesXmlPath'] = '/thinkspace/download'+stylesPath+'fckstyles.xml';
				editor.Config['TemplatesXmlPath'] = '/thinkspace/download'+stylesPath+'fcktemplates.xml';
			}
			editor.Config['FloatingPanelsZIndex'] = '8000001';
			editor.ReplaceTextarea();
	    	$doc.forms['form-'+name].onsubmit = function () {
	       		htmleditor.@com.smartgwt.extensions.client.htmleditor.HTMLEditor::save()();
	       		return false;
			};
		} catch (e) {
			alert(e.name+':'+e.message);
		}
    }-*/;
}
