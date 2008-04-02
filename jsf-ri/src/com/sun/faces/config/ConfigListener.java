/*
 * $Id: ConfigListener.java,v 1.25 2004/01/22 00:06:11 rkitain Exp $
 */
/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.webapp.FacesServlet;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sun.faces.util.Util;
import org.xml.sax.InputSource;


public class ConfigListener implements ServletContextListener
{
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ConfigListener.class);

    //
    // Class Variables
    //
    // A set of current ClassLoader(s)
    // This set is examined to determine if the contextInitialized method
    // has already executed for the current web app.
    //
    private static Set loaders = new HashSet();

    //
    // Instance Variables
    //
    private ConfigParser configParser = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ConfigListener()
    {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from ServletContextListener
    //

    public void contextInitialized(ServletContextEvent e) 
    {
	// If this method has already executed for this webapp...
	//

	if (isContextInitialized()) {
	    return;
	}

	ServletContext servletContext = e.getServletContext();        
	String initParamFileList = null;
	InputStream jarInputStream = null;
        InputSource source = null;
        
        // Step 0, parse obtain the url-pattern information
        // for the FacesServlet.  This information is passed
        // onto the ConfigParser for later use.
        WebXmlParser webXmlParser = new WebXmlParser(servletContext);
        List mappings = webXmlParser.getFacesServletMappings();
        
        // construct a new ConfigParser instance passing the url pattern
        configParser = new ConfigParser(servletContext, mappings);
	
	// Step 1, load our own JSF_RI_CONFIG
	URL configURL = 
	    Util.getCurrentLoader(this).getResource(RIConstants.JSF_RI_CONFIG);
	Util.doAssert(null != configURL);
        if (log.isDebugEnabled()) {
            log.debug("Loading JSF_RI_CONFIG configuration resources from " +
                      RIConstants.JSF_RI_CONFIG);
        }
	try {
	    URLConnection conn = configURL.openConnection();
	    conn.setUseCaches(false);
            jarInputStream = conn.getInputStream();
	    source = new InputSource(configURL.toExternalForm());
	    source.setByteStream(jarInputStream);
            configParser.parseConfig(source);
	    jarInputStream.close();
	}
	catch (Exception ee) {
	    try {
		if (null != jarInputStream) {
		    jarInputStream.close();
		}
	    } 
	    catch (IOException ioe) {
		jarInputStream = null;
	    }
	    
	    Object[] obj = new Object[1];
	    obj[0] = jarInputStream;
	    String msg = Util.getExceptionMessage(
	        Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj);
	    if (log.isWarnEnabled()) {
	        log.warn(msg, ee);
	    }
	    throw new FacesException(msg, ee);
	}
	// It's an error if this doesn't load.
        if (log.isDebugEnabled()) {
            log.debug("Loading JSF_RI_CONFIG completed");
        }

	// Step 2: scan resources for "META-INF/faces-config" 
	scanResourcesForConfigFile(servletContext, configParser);
	
	// Step 3. If the init parameter exists, load the config from
	// there
	if (null != (initParamFileList = 
		     servletContext.getInitParameter(FacesServlet.CONFIG_FILES_ATTR))) {
	    StringTokenizer toker = new StringTokenizer(initParamFileList, 
							",");
	    String cur;
	    while (toker.hasMoreTokens()) {
		cur = toker.nextToken();
		if (null != cur && 0 < cur.length()) {
		    cur = cur.trim();
		    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Trying to load application file " + cur);
                        }
			configParser.parseConfig(cur, servletContext);
                        if (log.isDebugEnabled()) {
                            log.debug("Application file " + cur + " loaded");
                        }
		    } catch (Exception ee) {
                        Object[] obj = new Object[1];
                        obj[0] = cur;
			String msg = null;
			// In case we the localized messages are not available
			// yet from the config file.
			try {
			    msg = Util.getExceptionMessage(
			        Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj);
			} catch (Exception me) {
			    msg = "Problem parsing file:"+cur;
			}
	                if (log.isWarnEnabled()) {
			    log.warn(msg, ee);
			}
			throw new FacesException(msg, ee);
		    }
		}
	    }
	}

	// Step 4, load the app's "/WEB-INF/faces-config.xml"
	try {
            if (log.isDebugEnabled()) {
                log.debug("Trying to default configuration file");
            }
	    configParser.parseConfig("/WEB-INF/faces-config.xml",
						      servletContext);
            if (log.isDebugEnabled()) {
                log.debug("Default configuration file loaded");
            }

	} catch (FileNotFoundException fnfe) {
	    // do nothing, it is permissable to have no faces-config
	    // file here.  This is one of three places where any
	    // exception is suppressed in this file.  The other
	    // places are in scanResourcesForConfigFile().
        } catch (Exception exp) {
	    // if we do have a faces-config file here, it must parse
	    // without error.
	    String msg = Util.getExceptionMessage(Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID);
 	    if (log.isWarnEnabled()) {
	        log.warn(msg, exp);
	    }
	    throw new FacesException(msg, exp);
	}
        servletContext.setAttribute(RIConstants.CONFIG_ATTR, new Boolean(true)); 
        configParser = null;
    }

    public void contextDestroyed(ServletContextEvent e) {  
        e.getServletContext().removeAttribute(RIConstants.CONFIG_ATTR);
	synchronized(loaders) {
	    loaders.remove(Util.getCurrentLoader(loaders));
	}
        if (log.isTraceEnabled()) {
	    log.trace("CONTEXT DESTROYED CALLED...");
        }
    }

    /**
     * <p>Scan classLoader resources for a "META-INF/faces-config.xml"
     * resource.  If such a resource is found, parse it.
     *
     * @param servletContext The <code>ServletContext</code> for the
     *  current web application
     * @param configParser Parser for processing configuration resources
     */
    protected void scanResourcesForConfigFile(ServletContext servletContext,
                                        ConfigParser configParser) {

        // Calculate a URL for the config resource (if it exists) in this JAR
        URL resourceURL = null;
	Enumeration configFiles = null;
	ClassLoader classLoader = Util.getCurrentLoader(this);
	try {
	    configFiles = classLoader.getResources("META-INF/faces-config.xml");
	} catch (IOException e) {
	    // Surpress exception - so we can continue and search elsewhere..
            if (log.isDebugEnabled()) {
                log.debug("IOException while scanning jar for config file:", e);
            }
	}

	while (configFiles.hasMoreElements()) {
	    resourceURL = (URL)configFiles.nextElement();
            // Create an InputSource for this resource (if it exists)
	    URLConnection conn = null;
            InputStream stream = null;
            InputSource source = null;
            try {
	        conn = resourceURL.openConnection();
	        conn.setUseCaches(false);
                source = new InputSource(resourceURL.toExternalForm());
                stream = conn.getInputStream();
                source.setByteStream(stream);
            } catch (IOException e) {
	        // suppress this exception.  This is one of three places in
	        // this file where an exception is suppressed.  The others
	        // are above in this method and when no
	        // WEB-INF/faces-config.xml file is found.
	    
                source = null;
            }
            if (source == null) {
	        try {
	            if (stream != null) {
		        stream.close();
		    }
                } catch (Exception e) {
	            if (log.isWarnEnabled()) {
		        log.warn(Util.getExceptionMessage(
			    Util.CANT_CLOSE_INPUT_STREAM_ID), e);
		    }
	        }    
	        continue;  // examine the next resource....
            }
            // The resource exists, so parse it
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Parsing application configuration resource " +
                          resourceURL.toExternalForm());
                }
                configParser.parseConfig(source);
                if (log.isDebugEnabled()) {
                    log.debug("Finished application configuration resource " +
                          resourceURL.toExternalForm());
                }
            } catch (Exception e) {
                // PENDING(craigmcc) - We need to do a very thorough review
                // of our exception handling strategies throughout the RI
                // this kind of thing should really be handled at a higher level
	        Object[] obj = new Object[1];
	        obj[0]=resourceURL.toExternalForm();
	        String msg = Util.getExceptionMessage(
	            Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj);
	        if (log.isWarnEnabled()) {
	            log.warn(msg, e);
	        }
                throw new FacesException(msg, e);
            }
	}
    }

    // Utility method to determine if the contextInitialized method
    // has executed already for the current webapp;
    //
    private boolean isContextInitialized() {
	//
	// Do this to ensure this method nly executes once per webapp
	//
	synchronized(loaders) {
	    if (!loaders.contains(Util.getCurrentLoader(loaders))) {
	        loaders.add(Util.getCurrentLoader(loaders));
		if (log.isTraceEnabled()) {
		    log.trace("Added Classloader");
		}
		return false;
	    } else {
		if (log.isTraceEnabled()) {
		    log.trace("Listener already completed for this webapp");
		}
	        return true;
	    }
	}
    }
} 
