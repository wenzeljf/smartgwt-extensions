/**
 * 
 */
package com.smartgwt.extensions.utility.requestrouter.json;

import com.smartgwt.extensions.utility.requestrouter.AJAXRequestHandler;

/**
 * @author marcellolarocca@gmail.com
 *
 */
public abstract class JSONRequestHandler implements AJAXRequestHandler {
	
	public abstract void onSuccess(String jso );
}
