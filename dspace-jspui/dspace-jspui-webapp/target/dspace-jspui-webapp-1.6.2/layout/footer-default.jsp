<%--
  -- footer-home.jsp
  --
  -- Version: $Revision: 1706 $
  --
  -- Date: $Date: 2006-12-07 03:53:18 +0000 (Thu, 07 Dec 2006) $
  --
  -- Copyright (c) 2002, Hewlett-Packard Company and Massachusetts
  -- Institute of Technology.  All rights reserved.
  --
  -- Redistribution and use in source and binary forms, with or without
  -- modification, are permitted provided that the following conditions are
  -- met:
  --
  -- - Redistributions of source code must retain the above copyright
  -- notice, this list of conditions and the following disclaimer.
  --
  -- - Redistributions in binary form must reproduce the above copyright
  -- notice, this list of conditions and the following disclaimer in the
  -- documentation and/or other materials provided with the distribution.
  --
  -- - Neither the name of the Hewlett-Packard Company nor the name of the
  -- Massachusetts Institute of Technology nor the names of their
  -- contributors may be used to endorse or promote products derived from
  -- this software without specific prior written permission.
  --
  -- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  -- ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  -- LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
  -- A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
  -- HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  -- INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  -- BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
  -- OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
  -- ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
  -- TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
  -- USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
  -- DAMAGE.
  --%>

<%--
  - Footer for home page
  --%>
<style>
	#ucpa-footer #ucpa-footer-inner #ucpa-footer-followus {
    float: right;
     margin-right: 90px;
}




</style>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>

<%
    String sidebar = (String) request.getAttribute("dspace.layout.sidebar");
    int overallColSpan = 3;
    if (sidebar == null)
    {
        overallColSpan = 3;
    }
%>
                    <%-- End of page content --%>
                    <p>&nbsp;</p>
                </td>

            <%-- Right-hand side bar if appropriate --%>
<%
    if (sidebar != null)
    {
%>
                <td class="sidebar">
                    <%= sidebar %>
                    <br/><img src="<%= request.getContextPath() %>/image/uc/spacer.gif" width="200" height="1" border="0" />
                </td>
<%
    }
%>
            </tr>
            <tr>
            		
            </tr>
        </table>


<div id="ucpa-footer" class="ucpa ucpa-www">
	<div class="container" id="ucpa-footer-inner">
		<div class="row-fluid">
      <div class="span12">
        <div class="row-fluid">
          <div class="span4" id="ucpa-footer-copyright">
            UNIVERSIDADE DE COIMBRA &copy; 2013
          </div>
          <div class="span7" id="ucpa-footer-menu">
            <ul class="inline ucpa-menu">
              <li class="menu-item">
                <a href="https://digitalis.uc.pt/contactos">Contactos</a>
              </li>
              <li class="menu-item">
                <a href="http://www.uc.pt/go/site_survey">Dê a sua opinião</a>
              </li>
              <li class="menu-item">
                <a class="ucpa-openHelpForm" href="https://digitalis.uc.pt/sugestoes">Sugestões/Reclamações</a>
              </li>
              <li class="menu-item">
                <a href="https://digitalis.uc.pt/pt-pt/termos">Termos de Uso e Privacidade</a>
              </li>
            </ul>
          </div>
 

          <div class="span6" id="ucpa-footer-followus">
            Siga-nos!
            <a target="_blank" href="https://www.facebook.com/pages/Bibliotecas-da-Universidade-de-Coimbra/116613321697537"><img class="footer-logo facebook" alt="" src="//apps.uc.pt/ui/static/images/facebook-32.png"></a>
            <a target="_blank" href="http://twitter.com/Univ_de_Coimbra"><img class="footer-logo twitter" alt="" src="//apps.uc.pt/ui/static/images/twitter-32.png"></a>
            <a target="_blank" href="http://www.youtube.com/user/UCBibliotecas"><img class="footer-logo youtube" alt="" src="//apps.uc.pt/ui/static/images/youtube-32.png"></a>
          </div>
<div class="span6" id="ucpa-footer-logos">      
  <img class="footer-logo" src="/layout/css/sponsors.png">      
</div>
          <!--<div class="span6" id="ucpa-footer-logos">
            <a class="footer-logo compete-logo" href="#"></a>
            <a class="footer-logo qren-logo" href="#"></a>
            <a class="footer-logo euro-logo" href="#"></a>
            <a class="footer-logo webq-logo" href="#"></a>
            <a class="footer-logo emprendia-logo" href="#"></a>
          </div>-->
        </div>
      </div>
		</div>
	</div>
</div>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-32010662-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>
    </body>
</html>
