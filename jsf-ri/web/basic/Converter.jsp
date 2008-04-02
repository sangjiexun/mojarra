<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Converter Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="WEB-INF/lib/basic.tld" prefix="basic" %>

    <H3> JSF Converter Test Page </H3>
    <hr>
       <fmt:setBundle
	    basename="basic.Resources"
	    scope="session" var="basicBundle"/>

       <jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />
       <f:use_faces>  
        <h:form id="converterForm" formName="converterForm" >

            <h4><i> Store Values To Model:</i> <h4>
            <table>
            <tr>
              <td><b><h:output_text id="secretdateconvert"
                         value="(secret) Date [MMM DD, YYYY Format]: " /></b></td>
              <td><h:input_secret id="secretdatetext" 
                      valueRef="LoginBean.date"
                      converter="date"/></td>
              <td><h:output_errors id="secretdateerr"
                      for="secretdatetext"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="dateconvert"
                         value="Date [MMM DD, YYYY Format]: " /></b></td>
              <td><h:input_text id="datetext" 
                      valueRef="LoginBean.date"
                      converter="date"/></td>
              <td><h:output_errors id="dateerr"
                      for="datetext"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="timeconvert"
                         value="Time [HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_text id="timetext" 
                      valueRef="LoginBean.time"
                      converter="time"/></td>
              <td><h:output_errors id="timeerr"
                      for="timetext"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="datetimeconvert"
                         value="Date/Time [MMM DD, YYYY HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_text id="datetimetext" 
                      valueRef="LoginBean.dateTime"
                      converter="datetime"/></td>
              <td><h:output_errors id="datetimeerr"
                      for="datetimetext"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="boolconvert"
                         value="Boolean [true/false]: " /></b></td>
              <td><h:input_text id="booltext" 
                      valueRef="LoginBean.validUser"
                      converter="boolean"/></td>
            </tr>
            <tr>
              <td><b><h:output_text id="byteLabel" value="Byte" /></b></td>
              <td>
                   <h:input_text id="byteInput"
                       valueRef="LoginBean.byte"
                       converter="number">
                       <f:attribute name="numberStyle" value="integer"/>
                    </h:input_text>
              </td>
              <td> <h:output_errors id="errByte"
                       for="byteInput"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="doubleLabel" 
                         value="Double" /></b></td>
              <td>
                   <h:input_text id="doubleInput"
                       valueRef="LoginBean.double"
                       converter="number">
                       <f:attribute name="numberStyle" value="number"/>
                    </h:input_text>
              </td>
              <td> <h:output_errors id="errDouble"
                       for="doubleInput"/> </td>
             </tr>
            <tr>
              <td><b><h:output_text id="floatLabel" 
                         value="Float" /></b></td>
              <td>
                   <h:input_text id="floatInput"
                       valueRef="LoginBean.float"
                       converter="number">
                       <f:attribute name="numberStyle" value="number"/>
                    </h:input_text>
              </td>
              <td> <h:output_errors id="errFloat"
                       for="floatInput"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="intLabel" 
                         value="Int" /></b></td>
              <td>
                   <h:input_text id="intInput"
                       valueRef="LoginBean.int"
                       converter="number">
                       <f:attribute name="numberStyle" value="integer"/>
                    </h:input_text>
              </td>
              <td> <h:output_errors id="errInt"
                       for="intInput"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="longLabel" 
                         value="Long" /></b></td>
              <td>
                   <h:input_text id="longInput"
                       valueRef="LoginBean.long"
                       converter="number">
                       <f:attribute name="numberStyle" value="number"/>
                    </h:input_text>
              </td>
              <td> <h:output_errors id="errLong"
                       for="longInput"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="shortLabel" 
                         value="Short" /></b></td>
              <td>
                   <h:input_text id="shortInput"
                       valueRef="LoginBean.short"
                       converter="number">
                       <f:attribute name="numberStyle" value="number"/>
                    </h:input_text>
              </td>
              <td> <h:output_errors id="errShort"
                       for="shortInput"/> </td>
            </tr>
            </table
            <p>
            <h4><i> Display Values From Model:</i> <h4>
            <table>
            <tr>
                <td><b><h:output_text id="odateLabel"
                           value="Date [LONG Format]:"/></b></td>
                <td>
                    <h:output_text id="dateOutput"
                        valueRef="LoginBean.date"
                        converter="date">
                        <f:attribute name="dateStyle" value="long"/>
                    </h:output_text>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="otimeLabel"
                           value="Time [long Format]:"/></b></td>
                <td>
                    <h:output_text id="timeOutput"
                        valueRef="LoginBean.time"
                        converter="time">
                        <f:attribute name="timeStyle" value="long"/>
                    </h:output_text>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="ocurrencyLabel"
                           value="Currency Format:"/></b></td>
                <td>
                    <h:output_text id="currencyOutput"
                        valueRef="LoginBean.double"
                        converter="number">
                        <f:attribute name="numberStyle" value="currency"/>
                    </h:output_text>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="opercentLabel"
                           value="Percent Format:"/></b></td>
                <td>
                    <h:output_text id="percentOutput"
                        valueRef="LoginBean.double"
                        converter="number">
                        <f:attribute name="numberStyle" value="percent"/>
                    </h:output_text>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="opatternLabel"
                           value="Pattern Format [####]:"/></b></td>
                <td>
                    <h:output_text id="patternOutput"
                        valueRef="LoginBean.double"
                        converter="number">
                        <f:attribute name="formatPattern" value="####"/>
                    </h:output_text>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="opatternLabel1"
                           value="Pattern Format [##,##]:"/></b></td>
                <td>
                    <h:output_text id="patternOutput1"
                        valueRef="LoginBean.double"
                        converter="number">
                        <f:attribute name="formatPattern" value="##,##"/>
                    </h:output_text>
                </td>
            </tr>
            </table>
            <p>
            <table>
            <tr>
                <td>
                <h:command_button id="convert" commandName="convert"
                    label="Convert">
                </h:command_button>
                </td> 
            </tr>
          </table>
        </h:form>
       </f:use_faces>
</HTML>
