<%--
  - navbar-default.jsp - 1.5.0.
  -
  - Version: $Revision: 2144 $
  -
  - Date: $Date: 2007-08-19 16:25:58 -0500 (Sun, 19 Aug 2007) $
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
  - Default navigation bar
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="/WEB-INF/dspace-tags.tld" prefix="dspace" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.browse.BrowseIndex" %>
<%@ page import="org.dspace.browse.BrowseInfo" %>
<%@ page import="java.util.Map" %>
<%
    // Is anyone logged in?
    EPerson user = (EPerson) request.getAttribute("dspace.current.user");

    // Is the logged in user an admin
    Boolean admin = (Boolean)request.getAttribute("is.admin");
    boolean isAdmin = (admin == null ? false : admin.booleanValue());

    // Get the current page, minus query string
    String currentPage = UIUtil.getOriginalURL(request);
    int c = currentPage.indexOf( '?' );
    if( c > -1 )
    {
        currentPage = currentPage.substring( 0, c );
    }

    // E-mail may have to be truncated
    String navbarEmail = null;

    if (user != null)
    {
        navbarEmail = user.getEmail();
        if (navbarEmail.length() > 18)
        {
            navbarEmail = navbarEmail.substring(0, 17) + "...";
        }
    }
    
    // get the browse indices
    
	BrowseIndex[] bis = BrowseIndex.getBrowseIndices();
    BrowseInfo binfo = (BrowseInfo) request.getAttribute("browse.info");
    String browseCurrent = "";
    if (binfo != null)
    {
        BrowseIndex bix = binfo.getBrowseIndex();
        // Only highlight the current browse, only if it is a metadata index,
        // or the selected sort option is the default for the index
        if (bix.isMetadataIndex() || bix.getSortOption() == binfo.getSortOption())
        {
            if (bix.getName() != null)
    			browseCurrent = bix.getName();
        }
    }
%>

<%-- Search Box --%>
<form method="get" action="<%= request.getContextPath() %>/simple-search">

<%
    if (user != null)
    {
%>
  <p class="loggedIn"><fmt:message key="jsp.layout.navbar-default.loggedin">
      <fmt:param><%= navbarEmail %></fmt:param>
  </fmt:message>
    (<a href="<%= request.getContextPath() %>/logout"><fmt:message key="jsp.layout.navbar-default.logout"/></a>)</p>
<%
    }
%>
  <table width="100%" class="searchBox">
    <tr>
      <td>
        <table width="100%" border="0" cellspacing="0" >
          <tr>
            <td class="searchBoxLabel"><label for="tequery"><fmt:message key="jsp.layout.navbar-default.search"/></label></td>
          </tr>
          <tr>
            <td class="searchBoxLabelSmall" valign="middle" nowrap="nowrap">
              <%-- <input type="text" name="query" id="tequery" size="10"/><input type=image border="0" src="<%= request.getContextPath() %>/image/search-go.gif" name="submit" alt="Go" value="Go"/> --%>
              <input type="text" name="query" id="tequery" size="16"/></td>
<td width="20"><input style="border:none;" src="<%= request.getContextPath() %>/image/uc/icon_search.gif" value="<fmt:message key="jsp.layout.navbar-default.go"/>"type="image" alt="Pesquisar" /></td>
          </tr>
          <tr>
          <td colspan="2"><a href="<%= request.getContextPath() %>/advanced-search"><fmt:message key="jsp.layout.navbar-default.advanced"/></a>
<%
			if (ConfigurationManager.getBooleanProperty("webui.controlledvocabulary.enable"))
			{
%>        
              <br/><a href="<%= request.getContextPath() %>/subject-search"><fmt:message key="jsp.layout.navbar-default.subjectsearch"/></a>
<%
            }
%>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</form>

<%-- HACK: width, border, cellspacing, cellpadding: for non-CSS compliant Netscape, Mozilla browsers --%>
<table style="padding-top:15px;" width="100%" border="0" cellspacing="0" cellpadding="0">
<%--  Sobre o repositório --%>

  <tr class="navigationBarItem">
    <td>
      <img alt="" src="<%= request.getContextPath() %>/image/<%= (currentPage.endsWith("/index.jsp") ? "arrow-highlight" : "arrow") %>.gif" width="16" height="16"/>
    </td>

    <td nowrap="nowrap" class="navigationBarItem">
      <a href="<%= request.getContextPath() %>/"><fmt:message key="jsp.layout.navbar-default.home"/></a>
    </td>
  </tr>
<%--  Sobre o repositório:fim --%>



<%-- Ajuda --%>
  <tr class="navigationBarItem">
    <td>
      <img alt="" src="<%= request.getContextPath() %>/image/<%= ( currentPage.endsWith( "/help" ) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
    </td>
    <td nowrap="nowrap" class="navigationBarItem">
            <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\")%>"><fmt:message key="jsp.layout.navbar-default.help"/></dspace:popup>
    </td>
  </tr>

  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>


  <tr>
    <td nowrap="nowrap" colspan="2" class="navigationBarSublabel"><fmt:message key="jsp.layout.navbar-default.browse"/></td>
  </tr>

  <tr class="navigationBarItem">
    <td>
      <img alt="" src="<%= request.getContextPath() %>/image/<%= ( currentPage.endsWith( "/community-list" ) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
    </td>
    <td nowrap="nowrap" class="navigationBarItem">
      <a href="<%= request.getContextPath() %>/community-list"><fmt:message key="jsp.layout.navbar-default.communities-collections"/></a>
    </td>
  </tr>
<%-- Estatisticas --%>
   <%
   if (ConfigurationManager.getBooleanProperty("stats.enable"))
   {
   %>
         <tr class="navigationBarItem">
           <td>
             <img alt="" src="<%= request.getContextPath() %>/image/<%= ( currentPage.endsWith( "/stats" ) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
           </td>
           <td nowrap="nowrap" class="navigationBarItem">
             <a href="<%= request.getContextPath() %>/stats"><fmt:message key="jsp.layout.navbar-admin.statistics"/></a>
           </td>
         </tr>
  <%
   }
  %>


<%-- Insert the dynamic browse indices here --%>

<%
	for (int i = 0; i < bis.length; i++)
	{
		BrowseIndex bix = bis[i];
		String key = "browse.menu." + bix.getName();
	%>
		<tr class="navigationBarItem">
    		<td>
      			<img alt="" src="<%= request.getContextPath() %>/image/<%= ( browseCurrent.equals(bix.getName()) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
    		</td>
    		<td nowrap="nowrap" class="navigationBarItem">
      			<a href="<%= request.getContextPath() %>/browse?type=<%= bix.getName() %>"><fmt:message key="<%= key %>"/></a>
    		</td>
  		</tr>
	<%	
	}
%>

<%-- End of dynamic browse indices --%>

  <tr>
    <td nowrap="nowrap" colspan="2" class="navigationBarSublabel"><fmt:message key="jsp.layout.navbar-default.sign"/></td>
  </tr>
  <tr class="navigationBarItem">
    <td>
      <img alt="" src="<%= request.getContextPath() %>/image/<%= ( currentPage.endsWith( "/mydspace" ) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
    </td>
    <td nowrap="nowrap" class="navigationBarItem">
      <a href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.layout.navbar-default.users"/></a><br/>
      <fmt:message key="jsp.layout.navbar-default.users-authorized" />
    </td>
  </tr> 

<%-- Falta o registo de novo utilizador --%>

  <tr class="navigationBarItem">
    <td>
      <img alt="" src="<%= request.getContextPath() %>/image/<%= ( currentPage.endsWith( "/profile" ) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
    </td>
    <td nowrap="nowrap" class="navigationBarItem">
      <a href="<%= request.getContextPath() %>/profile"><fmt:message key="jsp.layout.navbar-default.edit"/></a>
    </td>
  </tr>

 <tr class="navigationBarItem">
    <td>
      <img alt="" src="<%= request.getContextPath() %>/image/<%= ( currentPage.endsWith( "/subscribe" ) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
    </td>
    <td nowrap="nowrap" class="navigationBarItem">
      <a href="<%= request.getContextPath() %>/subscribe"><fmt:message key="jsp.layout.navbar-default.receive"/></a>
    </td>
  </tr>



<%
  if (isAdmin)
  {
%>  
  <tr class="navigationBarItem">
    <td>
      <img alt="" src="<%= request.getContextPath() %>/image/<%= ( currentPage.endsWith( "/profile" ) ? "arrow-highlight" : "arrow" ) %>.gif" width="16" height="16"/>
    </td>
    <td nowrap="nowrap" class="navigationBarItem">
      <a href="<%= request.getContextPath() %>/dspace-admin"><fmt:message key="jsp.administer"/></a>
    </td>
  </tr>
<%
  }
%>

  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>

</table>
