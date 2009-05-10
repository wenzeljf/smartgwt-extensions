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
package com.smartgwt.extensions.server.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Displays all files stored in the file system of your application, either app resources or
 * files stored by users.  A typical url would be
 * 	  /thinkspace/download/project/2/lib/myfile.gif
 * where /thinkspace is the webapp and the following entries are in web.xml:
 * 	<servlet>
 *  	<servlet-name>DownloadServlet</servlet-name>
 *      <servlet-class>com.smartgwt.extensions.server.servlets.DownloadServlet</servlet-class>
 *  </servlet>
 * and
 * 	    <servlet-mapping>
 *		   <servlet-name>DownloadServlet</servlet-name>
 * 		   <url-pattern>/download/*</url-pattern>
 *     </servlet-mapping>
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class DownloadServlet extends HttpServlet {
   private static Log log = LogFactory.getLog(ProjectServlet.class);
   public static SimpleDateFormat hdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

   public DownloadServlet() {
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response) {
      // this shouldn't happen so ignore
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) {
	   // url: /thinkspace/download/project/2/.....
   	  String path = request.getPathInfo();
   	  String[] items = path.split("/");
   	  if (items.length < 3 || !items[1].equals("project")) {
   		  // if no path info then nothing can be downloaded
   		  reportError(response);
   		  return;
   	  }
      InputStream in = null;
	  try {
	   	  int pid = Integer.parseInt(items[2]);
/*	   	  Project project = ProjectService.get().getProjectById(pid);
	   	  String contextName = project.getContext();
	      ProjectState state = (ProjectState) request.getSession().getAttribute(contextName);
	      if (state == null) {
	    	  // if no state then user has not authenticated
	    	  reportError(response);
	    	  return;
	      }
*/
	   	  // only files in lib directories can be downloaded
	      boolean hasPermission = false;
	      for (int i = items.length-1; i > 2; i--) {
	    	  if (items[i].equals("lib")) {
	    		  hasPermission = true;
	    		  break;
	    	  }
	      }
	      if (!hasPermission) {
	    	  reportError(response);
	    	  return;
	      }
/*          File f = state.getFileManager().getFile(path);
          String type = ContentService.get().getContentType(path);
          response.setContentType(type);
          response.setContentLength((int)f.length());
          response.setHeader("Content-Disposition", "inline; filename="+Util.encode(f.getName()));
          response.setHeader("Last-Modified",hdf.format(new Date(f.lastModified())));
          in = new BufferedInputStream(new FileInputStream(f));
*/          ServletOutputStream out = response.getOutputStream();
          byte[] buf = new byte[8192];
          int len = 0;
          while ((len = in.read(buf)) > 0) {
        	  out.write(buf,0,len);
          }
          out.close();
          out.flush();
	  } catch (Exception e) {
		  reportError(response);
		  log.error(e);
	  } finally {
		  if (in != null)
			  try { in.close(); } catch (Exception e){};
	  }
   }
   
   private void reportError(HttpServletResponse response) {
	   try {
		   response.sendError(HttpServletResponse.SC_FORBIDDEN,"You are not allowed to view that file");
	   } catch (IOException e) {   
	   }
   }
}