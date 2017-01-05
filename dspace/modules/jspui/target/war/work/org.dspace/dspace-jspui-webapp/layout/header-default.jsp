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
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.*" %>

<%
    String title = (String) request.getAttribute("dspace.layout.title");
    String navbar = (String) request.getAttribute("dspace.layout.navbar");
    boolean locbar = ((Boolean) request.getAttribute("dspace.layout.locbar")).booleanValue();

    String siteName = ConfigurationManager.getProperty("dspace.name");
    String feedRef = (String)request.getAttribute("dspace.layout.feedref");
   boolean osLink = ConfigurationManager.getBooleanProperty("websvc.opensearch.autolink");
    String osCtx = ConfigurationManager.getProperty("websvc.opensearch.svccontext");
    String osName = ConfigurationManager.getProperty("websvc.opensearch.shortname");
       String extraHeadData = (String)request.getAttribute("dspace.layout.head");
    List parts = (List)request.getAttribute("dspace.layout.linkparts");

    boolean scopus = false;
    String scopusDOI = "";
    String extraBody = "";
    if (request.getAttribute("scopus") != null)
    {
        scopus = ((Boolean)request.getAttribute("scopus")).booleanValue();
        if (scopus)
        {
            scopusDOI = (String)request.getAttribute("scopusDOI");
            extraBody = " onLoad=\"runSearch()\"";
        }
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="">
    <head>
        <title><%= siteName %>: <%= title %></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <link type="text/css" rel="stylesheet" href="/layout/css/ucpa.css?v=1.3.4">
  <link type="text/css" rel="stylesheet" href="/layout/css/ucpa-www.css?v=1.3.4">
  <link type="text/css" rel="stylesheet" href="/layout/css/ucpa-header.css?v=1.3.4">
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


<% if (scopus) { %>
    <script type="text/javascript" src="http://searchapi.scopus.com/scripts/scapi.jsp"></script>

        <script type="text/javascript">
            callback = function(){
                var ccount = scapi.getField(0, "citedbycount");
                if (ccount > 0) {
                    var message = "<center><b>This item has been cited <a href='" + scapi.getField(0, "inwardurl") + "'>" +
                                  ccount + "</a> times</b> " +
                                  "<small>(data provided by <a href='http://www.scopus.com/'>Scopus</a>)</small></center>";

                    document.getElementById("ccount").innerHTML = message;
                }
            }

            runSearch = function(){
            var varSearchObj = new searchObj();
            varSearchObj.setSearch("<%= scopusDOI %>");
            scapi.runSearch(varSearchObj);
            }
        </script>

        <script type="text/javascript">
                scapi.setDeveloperID("eb3f4edb84f540954c0403cade36b8aa");
                //scapi.setDebug(true);
                scapi.setCallback(callback);
        </script>
<% } %>

<!--<link type="text/css" rel="stylesheet" href="/layout/css/ucpa_002.css">
  <link type="text/css" rel="stylesheet" href="/layout/css/ucpa-www.css">
  <link type="text/css" rel="stylesheet" href="/layout/css/ucpa-header.css">
-->  

 <!-- <link type="text/css" rel="stylesheet" href="/layout/css/ucpa.css?v=1.3.4">
  <link type="text/css" rel="stylesheet" href="/layout/css/ucpa-www.css?v=1.3.4">
  <link type="text/css" rel="stylesheet" href="/layout/css/ucpa-header.css?v=1.3.4">-->

<style type="text/css">
    html.js .showOnLoad{
      display: none;
    }
	
	div#ucpa-header
	{
		
    font-family: Tahoma,Verdana,"Arial","Helvetica",sans-serif;
    font-size: 100%;
    margin-bottom: 0;
    margin-left: 0px !important;
    margin-right: 0px !important;

	}
	
	html.ucpa body div#main-wrapper table.centralPane tbody tr td.navigationBar img
	{
	
	}
  </style>

 <script src="/layout/css/ga.js" async="" type="text/javascript"></script>
 
 <style type="text/css">.imgLiquid img {visibility:hidden}</style>
 
 
 
  
 
<!--<link type="text/css" rel="stylesheet" href="/layout/css/ucpa.css?v=1.3.4">
<link type="text/css" rel="stylesheet" href="/layout/ucpa-header_002.css">
<link type="text/css" rel="stylesheet" href="/layout/css/ucpa-www_002.css">
-->
<style>

#ucpa-header > div:last-child {
}
DIV {
    font-family: Tahoma,Verdana,"Arial","Helvetica",sans-serif;
    font-size: 100%;
    margin-bottom: 0;
    margin-left: 10px;
    margin-right: 10px;
}

div#ucpa-header-logos a.ucpa-header-logo-uc
 {
    height: 90px;
    padding: 10px 0;
    position: relative;
    top: 0;
    width: 210px;
}

#header {
 
background: url("/layout/css/bg-not-front-uc-digitalis2.gif") no-repeat scroll center top / 100% 190px #FFFFFF;
    color: #363636;
    font-family: Arial,Tahoma,Verdana;
    font-size: 12px;
    margin: 0;
    padding: 0;
}


.centralPane {
    border: 0 none;
    margin: -20px auto 0;
    padding: 0;
    vertical-align: top;
    width: 960px;
}

div#site-logo-and-name {
    margin-left:25%;
    padding-top:10px;
}

div#ucpa-footer-logos img.footer-logo {
    height: 35px !important;
    margin-top: -40px !important;
}
</style>    
    </head>

    <%-- HACK: leftmargin, topmargin: for non-CSS compliant Microsoft IE browser --%>
    <%-- HACK: marginwidth, marginheight: for non-CSS compliant Netscape browser --%>
    <body<%= extraBody %>>

        <%-- DSpace top-of-page banner --%>
        <%-- HACK: width, border, cellspacing, cellpadding: for non-CSS compliant Netscape, Mozilla browsers --%>
        <!-- <link rel="stylesheet" type="text/css" href="http://193.136.200.27/sites/all/themes/ucdigitalis/ucdigitalis.css">-->
       <!-- <script src="http://193.136.200.27/sites/all/themes/ucdigitalis/js/ucdigitalis.js" type="text/javascript"></script>-->
   <!--script type="text/javascript" src="/sites/all/themes/ucdigitalis/js/uc.lib.js"></script-->
 


<div id="ucpa-header" class="affix-top ucpa ucpa-www">
	<div id="ucpa-header-inner">
		<div class="container-fluid" id="ucpa-header-container">
			<div class="container">
				<div class="row-fluid">
					<div class="hidden-phone" id="ucpa-header-topmenu-wrapper">
						<ul class="inline" id="ucpa-header-topmenu">
							<li class="menu-item"><a href="http://www.uc.pt/futurosestudantes">Futuros Estudantes</a></li>
							<li class="menu-item"><a href="http://www.uc.pt/estudantes">Estudantes</a></li>
							<li class="menu-item"><a href="http://www.uc.pt/antigos-estudantes">Antigos Estudantes</a></li>
							<li class="menu-item"><a href="http://www.uc.pt/colaboradores">Colaboradores</a></li>
              <li class="menu-item"><a href="http://www.uc.pt/empresas">Empresas</a></li>

						</ul>
					</div>
          <div id="ucpa-header-logos">
            <a class="ucpa-header-logo-uc" href="http://www.uc.pt/">
              <img src="/layout/css/logo-uc.png">
            </a>
            <a class="ucpa-header-logo-unesco-pt" href="http://worldheritage.uc.pt/pt">
              <img src="/layout/css/header-logo-unesco-pt.png">
            </a>
          </div>
					
				</div>
			</div>
		</div>

	</div>
</div>



<div id="header">
  <div class="section">
    <div id="site-logo-and-name">

    
      
                    <a id="logo" rel="home" title="Início" href="http://digitalis.uc.pt">
          <img alt="Início" src="/layout/css/logo_ucdigitalis.png">
        </a>
                         
             
     

  
          </div> <!-- /#site-logo-and-name -->
  
          <div class="navigation" id="main-menu">
              </div> <!-- /#main-menu -->
        
    
  </div> <!-- /.section -->
</div>




<div id="main-wrapper">
<!-- /#page, /#page-wrapper -->
                 
                   
                

 
    <br>

        <%-- Localization --%>
<%--  <c:if test="${param.locale != null}">--%>
<%--   <fmt:setLocale value="${param.locale}" scope="session" /> --%>
<%-- </c:if> --%>
<%--        <fmt:setBundle basename="Messages" scope="session"/> --%>

        <%-- Page contents --%>

        <%-- HACK: width, border, cellspacing, cellpadding: for non-CSS compliant Netscape, Mozilla browsers --%>
        <table class="centralPane" border="0" cellpadding="0" cellspacing="0">
<td colspan="3" class="banner"></td>   
            <%-- HACK: valign: for non-CSS compliant Netscape browser --%>
            <tr valign="top">

            <%-- Navigation bar --%>
<%
    if (!navbar.equals("off"))
    {
%>
            <td class="navigationBar">
                <dspace:include page="<%= navbar %>" />
                <!--<img src="<%= request.getContextPath() %>/image/uc/spacer.gif" width="150" height="1" border="0" />-->
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


