/*
 * $Id: TaglibGenTask.java,v 1.7.4.1 2008/04/07 16:22:10 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.ant;

import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;

import com.sun.faces.generate.PropertyManager;
import com.sun.faces.util.ToolsUtil;

/**
 * <p>Task to create a JSP tags.</p>
 */
public class TaglibGenTask extends AbstractGeneratorTask {

    private static final String GENERATOR_12_CLASS =
        "com.sun.faces.generate.HtmlTaglib12Generator";

    private static final String GENERATOR_21_CLASS =
        "com.sun.faces.generate.HtmlTaglib21Generator";


    // ---------------------------------------------------------- Public Methods


    public void execute() throws BuildException {

        PropertyManager manager = PropertyManager.newInstance(generatorConfig);
        String jspVersion =
              manager.getProperty(PropertyManager.JSP_VERSION_PROPERTY);

        if ("2.1".equals(jspVersion)) {
            setGeneratorClass(GENERATOR_21_CLASS);
        } else if ("1.2".equals(jspVersion)) {
            setGeneratorClass(GENERATOR_12_CLASS);
        } else {
            throw new BuildException("Unsupported JSP version '"
                                     + jspVersion
                                     + '\'');
        }

        super.execute();

    } // END execute

}