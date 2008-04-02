/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * $Id: SelectMany_ListboxTag.java,v 1.6 2003/05/02 17:37:49 eburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */


// SelectMany_ListboxTag.java

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectMany_ListboxTag.java,v 1.6 2003/05/02 17:37:49 eburns Exp $
 * 
 * @see Blah
 * @see Bloo
 * 
 *
 */

public class SelectMany_ListboxTag extends FacesTag
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectMany_ListboxTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

//
// General Methods
//

    public String getLocalRendererType() { return "Listbox"; }
    public String getComponentType() { return "SelectMany"; }
    
    public UIComponent createComponent() {
        return (new UISelectMany());
    }

//
// Methods from TagSupport
// 

    public int doEndTag() throws JspException {
    UISelectMany component = (UISelectMany) getComponent();

    int rc = super.doEndTag();

    return rc;
    }


} // end of class SelectMany_MenuTag
