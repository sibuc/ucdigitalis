<%--
  - header-home.jsp
  -
  - Version: $Revision: 1645 $
  -
  - Date: $Date: 2006-10-25 12:09:14 +0100 (Wed, 25 Oct 2006) $
  -
  - Copyright (c) 2002, Hewlett-Packard Company and Massachusetts
  - Institute of Technology.  All rights reserved.
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are
  - met:
  -
  - - Redistributions of source code must retain the above copyright
  - notice, this list of conditions and the following disclaimer.
  -
  - - Redistributions in binary form must reproduce the above copyright
  - notice, this list of conditions and the following disclaimer in the
  - documentation and/or other materials provided with the distribution.
  -
  - - Neither the name of the Hewlett-Packard Company nor the name of the
  - Massachusetts Institute of Technology nor the names of their
  - contributors may be used to endorse or promote products derived from
  - this software without specific prior written permission.
  -
  - THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  - ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  - LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
  - A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
  - HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  - INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  - BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
  - OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
  - ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
  - TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
  - USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
  - DAMAGE.
  --%>

<%--
  - HTML header for main home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="org.dspace.app.webui.util.JSPManager" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.app.util.Util" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.*" %>

<%
    String title = (String) request.getAttribute("dspace.layout.title");
    String navbar = (String) request.getAttribute("dspace.layout.navbar");
    boolean locbar = ((Boolean) request.getAttribute("dspace.layout.locbar")).booleanValue();

    String siteName = ConfigurationManager.getProperty("dspace.name");
    String baseUrl = ConfigurationManager.getProperty("dspace.baseUrl");
    String feedRef = (String)request.getAttribute("dspace.layout.feedref");
    boolean osLink = ConfigurationManager.getBooleanProperty("websvc.opensearch.autolink");
    String osCtx = ConfigurationManager.getProperty("websvc.opensearch.svccontext");
    String osName = ConfigurationManager.getProperty("websvc.opensearch.shortname");
    List parts = (List)request.getAttribute("dspace.layout.linkparts");
    String extraHeadData = (String)request.getAttribute("dspace.layout.head");
    String dsVersion = Util.getSourceVersion();
    String generator = dsVersion == null ? "DSpace" : "DSpace "+dsVersion;

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title><%= siteName %>: <%= title %></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta name="Generator" content="<%= generator %>" />
        <link href="/ucdigitalis.css" type="text/css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css.jsp" type="text/css" />
        <link rel="stylesheet" href="<%= request.getContextPath() %>/stats/styles.css.jsp" type="text/css" />
        <link rel="stylesheet" href="<%= request.getContextPath() %>/print.css" media="print" type="text/css" />
        <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon"/>
<%
    if (!"NONE".equals(feedRef))
    {
        for (int i = 0; i < parts.size(); i+= 3)
        {
%>
        <link rel="alternate" type="application/<%= (String)parts.get(i) %>" title="<%= (String)parts.get(i+1) %>" href="<%= request.getContextPath() %>/feed/<%= (String)parts.get(i+2) %>/<%= feedRef %>"/>
  <%
          }
      }

     if (osLink)
     {
  %>
         <link rel="search" type="application/opensearchdescription+xml" href="<%= request.getContextPath() %>/<%= osCtx %>description.xml" title="<%= osName %>"/>
 <%
     }

     if (extraHeadData != null)
         { %>
 <%= extraHeadData %>
 <%
         }
 %>

     <script type="text/javascript" src="<%= request.getContextPath() %>/utils.js"></script>
     <script type="text/javascript" src="<%= request.getContextPath() %>/static/js/scriptaculous/prototype.js"> </script>
     <script type="text/javascript" src="<%= request.getContextPath() %>/static/js/scriptaculous/effects.js"> </script>
     <script type="text/javascript" src="<%= request.getContextPath() %>/static/js/scriptaculous/builder.js"> </script>
     <script type="text/javascript" src="<%= request.getContextPath() %>/static/js/scriptaculous/controls.js"> </script>
     <script type="text/javascript" src="<%= request.getContextPath() %>/static/js/choice-support.js"> </script>

    </head>

    <%-- HACK: leftmargin, topmargin: for non-CSS compliant Microsoft IE browser --%>
    <%-- HACK: marginwidth, marginheight: for non-CSS compliant Netscape browser --%>
    <body>

        <%-- DSpace top-of-page banner --%>
        <%-- HACK: width, border, cellspacing, cellpadding: for non-CSS compliant Netscape, Mozilla browsers --%>
        <table class="pageBanner" width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td class="banner" colspan="3"><a id="EG" href="<%= baseUrl %>"><img src="<%= request.getContextPath() %>/image/uc/bannerTop.jpg" border="0" /></a></td>
            </tr>
        </table>

        <%-- Localization --%>
<%--  <c:if test="${param.locale != null}">--%>
<%--   <fmt:setLocale value="${param.locale}" scope="session" /> --%>
<%-- </c:if> --%>
<%--        <fmt:setBundle basename="Messages" scope="session"/> --%>

        <%-- Page contents --%>

        <%-- HACK: width, border, cellspacing, cellpadding: for non-CSS compliant Netscape, Mozilla browsers --%>
        <table class="centralPane" border="0" cellpadding="0" cellspacing="0">

            <%-- HACK: valign: for non-CSS compliant Netscape browser --%>
            <tr valign="top">

            <%-- Navigation bar --%>
<%
    if (!navbar.equals("off"))
    {
%>
            <td class="navigationBar">
                <dspace:include page="<%= navbar %>" />
                <img src="<%= request.getContextPath() %>/image/uc/spacer.gif" width="150" height="1" border="0" />
            </td>
<%
    }
%>
            <%-- Page Content --%>

            <%-- HACK: width specified here for non-CSS compliant Netscape 4.x --%>
            <%-- HACK: Width shouldn't really be 100%, but omitting this means --%>
            <%--       navigation bar gets far too wide on certain pages --%>
            <td class="pageContents" width="100%">

                <%-- Location bar --%>
<%
    if (locbar)
    {
%>
                <dspace:include page="/layout/location-bar.jsp" />
<%
    }
%>