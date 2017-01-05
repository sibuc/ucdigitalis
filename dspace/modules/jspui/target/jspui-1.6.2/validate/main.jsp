<%--
  - main.jsp
  -
  - Version: $Revision: 3705 $
  -
  - Date: $Date: 2009-04-11 17:02:24 +0000 (Sat, 11 Apr 2009) $
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
  - Main Validate page
  -
  -
  - Attributes:
  -    validate.user:    current user (EPerson)
  -    validate.collection: collection to validate (Collection)

  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page  import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.app.webui.servlet.ValidateCollectionServlet" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.DCValue" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.eperson.Group"   %>
<%@ page import="java.util.List" %>

<%
    EPerson user = (EPerson) request.getAttribute("validate.user");

    Collection coll =
        (Collection) request.getAttribute("validate.collection");

%>

<dspace:layout titlekey="jsp.validatecollection" nocache="true">

<table width="100%" border="0">
        <tr>
            <td align="left">
                <h1>
                    <fmt:message key="jsp.validatecollection"/>: <%= coll.getName() %> : <%= coll.getHandle() %>
                </h1>
            </td>
        </tr>
        <tr>
            <td>
           <form action="<%= request.getContextPath() %>/validate" method="post">
                        <input type="hidden" name="collection_id" value="<%= coll.getID() %>" />
                        <input type="hidden" name="user_id" value="<%=user.getID() %>" />
                        <br>
                        <fmt:message key="jsp.validate.main.livros.checkbox"/>
                        <input type="checkbox" name="tipodoc" value="Livros" />
                        <br>
                        <fmt:message key="jsp.validate.main.caplivros.checkbox"/>
                        <input type="checkbox" name="tipodoc" value="CapLivros" />
                        <br>
                        <fmt:message key="jsp.validate.main.artigos.checkbox"/>
                        <input type="checkbox" name="tipodoc" value="Artigos" />
                        <input type="submit" name="submit" value="<fmt:message key="jsp.validate.main.submit.button"/>" />
          </form>
            </td>
        </tr>
</table>

</dspace:layout>
