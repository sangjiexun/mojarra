<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <BODY>
        <H3> JSF Basic Components Test Page </H3>


       <f:view>
        <h:form id="/basicForm">

            <h:textentry_input id="userName" />

            <h:command_button id="login" />

	    <!-- <h:command_link id="/basicForm/login" /> -->

            <h:output_text id="userLabel" />

            <h:selectboolean_checkbox id="validUser" />

            <h:selectone_listbox id="appleQuantity" />

            <h:selectone_radio id="shipType" />

            <h:textentry_secret id="password" />

            <h:textentry_textarea id="address" />


        </h:form>
       </f:view>

    </BODY>
</HTML>
