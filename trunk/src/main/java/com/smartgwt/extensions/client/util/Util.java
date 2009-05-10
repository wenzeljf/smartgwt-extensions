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
package com.smartgwt.extensions.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class Util {

   public static String getDataURL() {
		if (GWT.isScript()){
			return "/thinkspace/think";
		} else {
			return GWT.getModuleBaseURL()+"test";
		}
	}

	public static String newID() {
		return SC.generateID();
	}

	public static Map<String,String> getMap(String[][] args) {
		HashMap<String,String> map = new HashMap<String,String>();
		for (String[] arg: args)
			map.put(arg[0], arg[1]);
		return map;
	}

	public static String getCSVString(List<String> items) {
		String csv = "";
		if (items.size() > 0) {
			for (String s: items)
				csv += s.trim() +",";
			return csv.substring(0,csv.length()-1);
		} else
			return csv;			
	}
	public static List<String> getStringList(String csv) {
		String[] labelList = csv.split(",");
		List<String> list = new ArrayList<String>();
		for (String tag: labelList) {
			String t = tag.trim();
			if (t.length() > 0)
				list.add(t);
		}
		return list;
	}
	
	public static native void redirect(String url)/*-{
		$wnd.location.href = url;
	}-*/;

	public static native boolean matches(String rexpr, String ans)/*-{
  		var re = new RegExp(rexpr);
  		return ans.match(re);
	}-*/;

	public static native String convertToAbsolute(String html, String baseHref) /*-{
		var re = /<(.*?)(src|href)=\"(?!http)(.*?)\"(.*?)>/gim;
		var value = html.replace(re,"<$1$2=\"" + baseHref + "$3\"$4$5>");
		return value;
	}-*/;

	public static native String convertToRelative(String html, String baseHref) /*-{
		var text = html.split(baseHref);
		return text.join('');
	}-*/;

    public static native String getSelectedText() /*-{
		if ($wnd.getSelection)
	    	return $wnd.getSelection();
	    else if ($doc.getSelection)
	        return $doc.getSelection();
	    else if ($doc.selection)
	        return $doc.selection.createRange().text;
	    else
	    	return '';
	}-*/;

    public static native String getSelectedHtml() /*-{
		if ($doc.selection && $doc.selection.createRange)
			return ($doc.selection.createRange()).htmlText;
		if ($wnd.getSelection) {
			var sel = $wnd.getSelection();
			var html = "";
			for (var i=0;i<sel.rangeCount;i++) {
				var d = document.createElement("span");
				var r = sel.getRangeAt(i);
				var parent_element = r.commonAncestorContainer;
				var prev_html = parent_element.innerHTML;
				r.surroundContents(d);
				html += d.innerHTML;
				parent_element.innerHTML = prev_html;
			}
			return html;
		}
		return '';
	}-*/;
}
