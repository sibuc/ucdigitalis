<%--
  - styles.css.jsp
  -
  - Version: $Revision: 4603 $
  -
  - Date: $Date: 2009-12-03 08:17:54 +0000 (Thu, 03 Dec 2009) $
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
  - Main DSpace Web UI stylesheet
  -
  - This is a JSP so it can be tailored for different browser types
  --%>
<%@ page import="org.dspace.app.webui.util.JSPManager" %>

<%
    // Make sure the browser knows we're a stylesheet
    response.setContentType("text/css");

    String imageUrl   = request.getContextPath() + "/image/";

    // Netscape 4.x?
    boolean usingNetscape4 = false;
    String userAgent = request.getHeader( "User-Agent" );
    if( userAgent != null && userAgent.startsWith( "Mozilla/4" ) )
    {
        usingNetscape4 = true;
    }
%>

A { color: #576C82; text-decoration:none; }
A:HOVER { color: #66788A; text-decoration:underline; }

BODY { font-family: Tahoma, Verdana, Arial, Helvetica, sans-serif;
       font-size: 8pt;
       font-style: normal;
       color: #444;
       background: #fff;
       margin: 0;
       padding: 0;
       margin-left:0px;
       margin-right:0px; 
       margin-top:0px; 
       margin-bottom:10px }
       
input, select, textarea{
       font-family:Tahoma, Verdana, Arial, Helvetica, sans-serif;
       font-weight: normal;
       font-size:100%;
       color:#555;
       border: 1px solid #999;
       background-color:#FFFFFF; }

<%-- Note: Font information must be repeated for broken Netscape 4.xx --%>
H1 { margin-left: 10px;
     margin-right: 10px;
     font-size: 140%;
     font-weight: bold;
     font-style: normal;
     font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
     color: #576C82 }

H2 { margin-left: 10px;
     margin-right: 10px;
     font-size: 120%;
     font-style: normal;
     font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
     color: #576C82 }

H3 { margin-left: 10px;
     margin-right: 10px;
     font-size: 110%;
     font-weight: bold;
     font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
     color: #444 }

H4 { margin-left: 10px;
     margin-right: 10px;
     font-size: 110%;
     font-weight: normal;
     font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
     line-height: 14pt;
     color: #576C82 }

H5 { margin-left: 20px;
     margin-right: 10px;
     font-size: 110%;
     font-weight: normal;
     font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
     line-height: 9pt;
     color: #576C82 }


object { display: inline; }

p {  margin-left: 10px;
     margin-right: 10px;
     font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
     font-size: 100% }
     
<%-- This class is here so that a "DIV" by default acts as a "P".    --%>
<%-- This is necessary since the "dspace:popup" tag must have a "DIV" --%>
<%-- (or block element) surrounding it in order to be valid XHTML 1.0 --%>
DIV { margin-left: 10px;
      margin-right: 10px;
      margin-bottom: 15px; 
      font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
      font-size: 100%;} 

UL { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
     font-size: 100% }

<%-- This class is here so the standard style from "P" above can be applied --%>
<%-- to anything else. --%>
.standard { margin-left: 10px;
            margin-right: 10px;
            font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
            font-size: 100% } 

.langChangeOff { text-decoration: none;
                 color : #bbbbbb;
                 cursor : default;
                 font-size: 100% }

.langChangeOn { text-decoration: underline;
                color: #576C82;
                cursor: pointer;
                font-size: 100% }

.pageBanner { width: 960px;
              border: 0;
              margin: 0 auto;
              background: #ffffff;
              color: #444;
              padding: 0;
              vertical-align: middle }

.tagLine { vertical-align: bottom;
           padding: 10px;
           border: 0;
           margin: 0;
           background: #ffffff;
           color: #ff6600 }

.tagLineText { background: #ffffff;
               color: #ff6600;
               font-size: 10pt;
               font-weight: bold;
               border: 0;
               margin: 0 }

.topUCright { background: #576C82 url(<%= imageUrl %>uc/topright_uc_bg.png) repeat-x; }

.banner{  padding:10px 0 0 0;
				}
        
.bottomUC-l, .bottomUC-r{ border-top: 4px solid #E5E5E5; margin: 15px 0 0 0; padding: 5px 0 0 0; }
.bottomUC-rcprt{ border-top: 4px solid #E5E5E5; margin: 15px 0 0 0; padding: 5px 0 0 0; color: #999999 }

.stripe { background: #576C82 url(<%= imageUrl %>stripe.gif) repeat-x;
          vertical-align: top;
          border: 0;
          padding: 0;
          margin: 0;
          color: #ffffff }

.locationBar { font-size: 100%;
               font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
               text-align: left }

.centralPane { margin: 0 auto;
               vertical-align: top;
               padding: 0;
               border: 0;
               width: 960px; }

<%-- HACK: Width shouldn't really be 100%:  Really, this is asking that the --%>
<%--       table cell be the full width of the table.  The side effect of --%>
<%--       this should theoretically be that other cells in the row be made --%>
<%--       a width of 0%, but in practice browsers will only take this 100% --%>
<%--       as a hint, and just make it as wide as it can without impinging --%>
<%--       the other cells.  This, fortunately, is precisely what we want. --%>
.pageContents { FONT-FAMILY: Tahoma, Verdana, Arial, Helvetica, sans-serif;
                background: white;
                color: #444;
                vertical-align: top;
                width: 100% }

.navigationBarTable{ width: 170px;
                     padding: 2px;
                     margin: 2px;
                     border: 0;
                     background-color: #F5F5F5; }

.navigationBar { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                 font-size: 100%;
                 font-style: normal;
                 font-weight: bold;
                 padding:10px;
                 color: #252645;
                 text-decoration: none;
                 background-color: #F5F5F5; }

.navigationBarSublabel{  font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                         font-size: 100%;
                         font-style: normal;
                         font-weight: bold;
                         color: #444;
                         text-decoration: none;
                         background-color: #F5F5F5;
                         white-space: nowrap }

<%-- HACK: Shouldn't have to repeat font information and colour here, it --%>
<%--       should be inherited from the parent, but isn't in Netscape 4.x, --%>
<%--       IE or Opera.  (Only Mozilla functions correctly.) --%>

.navigationBarItem { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                     font-size: 100%;
                     font-style: normal;
                     font-weight: normal;
                     color: #252645;
                     background-color: #F5F5F5;
                     text-decoration: none;
                     vertical-align: middle;
                     white-space: nowrap }
                     
.navigationBarItemA { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                     font-size: 110%;
                     font-style: normal;
                     font-weight: normal;
                     color: #252645;
                     background-color: #FFFFFF;
                     text-decoration: none;
                     margin-left: 20px;
                     vertical-align: middle;
                     white-space:nowrap }

.navigationBarItem td { padding:2px; }
.navigationBarItemA td { padding:2px; }

.loggedIn { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
            font-size: 100%;
            font-style: normal;
            font-weight: normal;
            color: #882222;
            background: #ffffff }

.pageFooterBar { width: 100%;
                 border: 0;
                 margin: 0;
                 padding: 0;
                 background: #ffffff;
                 color: #444;
                 vertical-align: middle }

.pageFootnote { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                font-size: 100%;
                font-style: normal;
                font-weight: normal;
                background: #ffffff;
                color: #252645;
                text-decoration: none;
                text-align: left;
                vertical-align: middle;
                margin-left: 10px;
                margin-right: 10px }

.sidebar { background: #ffffff;
           color: #444; }

.communityLink { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                 font-size: 100%;
                 font-weight: bold }

.communityDescription { margin-left: 20px;
                        margin-right: 10px;
                        font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                        font-size: 100%;
                        font-weight: normal;
                        list-style-type: none }

.collectionListItem { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                      font-size: 100%;
                      font-weight: normal }

.collectionDescription { margin-left: 20px;
                     margin-right: 10px;
                     font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                     font-size: 100%;
                     font-weight: normal;
                     list-style-type: none }

.miscListItem { margin-left: 20px;
                margin-right: 10px;
                font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                font-size: 100%;
                list-style-type: none }

.copyrightText { margin-left: 20px;
                 margin-right: 20px;
                 text-align: center;
                 font-style: italic;
                 font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                 font-size: 100%;
                 list-style-type: none }

.browseBarLabel { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                  font-size: 100%;
                  font-style: normal;
                  font-weight: bold;
                  color: #444;
                  background: #ffffff;
                  vertical-align: middle;
                  text-decoration: none }

.browseBar { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
             font-size: 100%;
             font-style: normal;
             font-weight: bold;
             background: #ffffff;
             color: #252645;
             vertical-align: middle;
             text-decoration: none }

.itemListCellOdd { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                   font-size: 100%;
                   font-style: normal;
                   font-weight: normal;
                   color: #444;
                   vertical-align: middle;
                   text-decoration: none;
                   background: #ffffff }


.itemListCellEven { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                    font-size: 100%;
                    font-style: normal;
                    font-weight: normal;
                    color: #444;
                    vertical-align: middle;
                    text-decoration: none;
                    /*background: #eeeeee*/ }

.itemListCellHilight { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                       font-size: 100%;
                       font-style: normal;
                       font-weight: normal;
                       color: #444;
                       vertical-align: middle;
                       text-decoration: none;
                       /*background: #eee*/ }

.topNavLink { margin-left: 10px;
          margin-right: 10px;
          font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
          font-size: 100%;
          text-align: center }

.submitFormLabel { margin-left: 10px;
           margin-right: 10px;
           font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                   font-weight: bold;
           font-size: 100%;
           text-align: right }

.submitFormHelp {  margin-left: 10px;
           margin-right: 10px;
           font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
           font-size: 100%;
           text-align: center }
           

.submitFormWarn {  margin-left: 10px;
           margin-right: 10px;
           font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
           font-weight: bold;
           font-size: 12pt;
           color: #ff6600;
           text-align: center }

.uploadHelp { margin-left: 20px;
              margin-right: 20px;
              font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
              font-size: 100%;
              text-align: left }

.submitFormDateLabel {  margin-left: 10px;
                        margin-right: 10px;
                        font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                        font-size: 100%;
                        font-style: italic;
                        text-align: center }

.submitProgressTable{ margin: 0;
                      padding: 0;
                      border: 0;
                      vertical-align: top;
                      text-align: center;
                      white-space: nowrap }

.submitProgressButton{ border: 0 }

.submitProgressButtonDone{ border: 0;
                           background-image: url(<%= imageUrl %>/submit/done.gif);
                           background-position: center;
                           height: 30px;
                           width: 90px;
                           font-size: 100%;
                           color: #444;
                           background-repeat: no-repeat; }

.submitProgressButtonCurrent{ border: 0;
                           background-image: url(<%= imageUrl %>/submit/current.gif);
                           background-position: center;
                           height: 30px;
                           width: 90px;
                           font-size: 100%;
                           color: white;
                           background-repeat: no-repeat; }

.submitProgressButtonNotDone{ border: 0;
                           background-image: url(<%= imageUrl %>/submit/notdone.gif);
                           background-position: center;
                           height: 30px;
                           width: 90px;
                           font-size: 100%;
                           color: #444;
                           background-repeat: no-repeat; }

.miscTable { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
             font-size: 100%;
             font-style: normal;
             font-weight: normal;
             color: #444;
             vertical-align: middle;
             text-decoration: none;
             /*background: #eee*/ }

.miscTableNoColor { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
             font-size: 12pt;
             font-style: normal;
             font-weight: normal;
             color: #444;
             vertical-align: middle;
             text-decoration: none;
             background: #ffffff }

<%-- The padding element breaks Netscape 4 - it puts a big gap at the top
  -- of the browse tables if it's present.  So, we decide here which
  -- padding elements to use. --%>
<%
    String padding = "padding: 3px";

    if( usingNetscape4 )
    {
        padding = "padding-left: 3px;  padding-right: 3px; padding-top: 1px";
    }
%>


.oddRowOddCol{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
               font-size: 100%;
               font-style: normal;
               font-weight: normal;
               color: #444;
               vertical-align: middle;
               text-decoration: none;
               background: #ffffff;
               <%= padding %> }

.evenRowOddCol{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                font-size: 100%;
                font-style: normal;
                font-weight: normal;
                color: #444;
                vertical-align: middle;
                text-decoration: none;
                background: #fff;
                <%= padding %>  }

.oddRowEvenCol{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                font-size: 100%;
                font-style: normal;
                font-weight: normal;
                color: #444;
                vertical-align: middle;
                text-decoration: none;
                /*background: #eeeeee;*/
                <%= padding %>  }

.evenRowEvenCol{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                 font-size: 100%;
                 font-style: normal;
                 font-weight: normal;
                 color: #444;
                 vertical-align: middle;
                 text-decoration: none;
                 /*background: #eee;*/
                 <%= padding %>  }

.highlightRowOddCol{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                     font-size: 100%;
                     font-style: normal;
                     font-weight: normal;
                     color: #444;
                     vertical-align: middle;
                     text-decoration: none;
                     background: #ccccee;
                     <%= padding %> }

.highlightRowEvenCol{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                      font-size: 100%;
                      font-style: normal;
                      font-weight: normal;
                      color: #444;
                      vertical-align: middle;
                      text-decoration: none;
                      background: #bbbbcc;
                      <%= padding %> }

.itemDisplayTable{ text-align: center;
                   border: 0;
                   color: #444 }

.metadataFieldLabel{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                     font-size: 100%;
                     font-style: normal;
                     font-weight: bold;
                     color: #444;
                     vertical-align: top;
                     text-align: right;
                     text-decoration: none;
                     white-space: nowrap;
                     <%= padding %> }

.metadataFieldValue{ font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                     font-size: 100%;
                     font-style: normal;
                     font-weight: normal;
                     color: #444;
                     vertical-align: top;
                     text-align: left;
                     text-decoration: none;
                     <%= padding %> }  <%-- width 100% ?? --%>

.recentItem { margin-left: 10px;
              margin-right: 10px;
              font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
              font-size: 100% }

.searchBox { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
             font-size: 100%;
             font-style: normal;
             font-weight: bold;
             color: #444;
             vertical-align: middle;
             text-decoration: none;
             background: #e5e5e5;
             padding: 0;
             border: 0;
             margin: 0 }

.searchBoxLabel { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                  font-size: 100%;
                  font-style: normal;
                  font-weight: bold;
                  color: #444;
                  background: #e5e5e5;
                  text-decoration: none;
                  vertical-align: middle }

.searchBoxLabelSmall { font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
                  font-size: 100%;
                  font-style: normal;
                  font-weight: bold;
                  color: #444;
                  background: #e5e5e5;
                  text-decoration: none;
                  vertical-align: middle }

.attentionTable 
{
    font-style: normal;
    font-weight: normal;
    color: #444;
    vertical-align: middle;
    text-decoration: none;
    background: #cc9966;
}

.attentionCell 
{
    background: #ffffcc;
    text-align: center;
}

.help {font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
        background: #ffffff;
        margin-left:10px;}

.help h2{text-align:center;
                font-size:1100%;
                color:#444;}

.help h3{font-weight:bold;
         margin-left:0px;}

.help h4{font-weight:bold;
         font-size: 100%;
         margin-left:5px;}

.help h5{font-weight:bold;
         margin-left:10px;
         line-height:.5;}

.help p {font-size:100%;}

.help table{margin-left:8px;
            width:90%;}

.help table.formats{font-size:100%;}

.help ul {font-size:100%;}

.help p.bottomLinks {font-size:100%;
                    font-weight:bold;}

.help td.leftAlign{font-size:100%;}
.help td.rightAlign{text-align:right;
                    font-size:100%;}                    

<%-- The following rules are used by the controlled-vocabulary add-on --%>

ul.controlledvocabulary  {
		list-style-type:none; }

	
.controlledvocabulary ul  li ul {
	     list-style-type:none;
		display:none; }

input.controlledvocabulary  {
		border:0px; }

img.controlledvocabulary {
		margin-right:8px ! important;
		margin-left:11px ! important;
		cursor:hand; }                    

.submitFormHelpControlledVocabularies {  
		   margin-left: 10px;
           margin-right: 10px;
           font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
           font-size: 100%;
           text-align: left; }           

.controlledVocabularyLink {  
           font-family: Tahoma, Verdana, "Arial", "Helvetica", sans-serif;
           font-size: 100%; }   
           
.browse_buttons
{
        float: right;
        padding: 1px;
        margin: 1px;
}

#browse_navigation
{
        margin-bottom: 10px;
}

#browse_controls
{
        margin-bottom: 10px;
}

.browse_range
{
        margin-top: 5px;
        margin-bottom: 5px;
}


<%-- styles added by authority control --%>
div.autocomplete {
    background-color:white;
    border:1px solid #888888;
    margin:0;
    padding:0;
    position:absolute;
    width:250px;
}

div.autocomplete ul {
    list-style-type:none;
    margin:0;
    padding:0;
}

div.autocomplete ul li {
    cursor:pointer;
}

div.autocomplete ul li.selected {
    text-decoration: underline;
}
div.autocomplete ul li:hover {
    text-decoration: underline;
}

div.autocomplete ul li span.value {
    display:none;
}


/* this magic gets the 16x16 icon to show up.. setting height/width didn't
   do it, but adding padding actually made it show up. */
img.ds-authority-confidence,
span.ds-authority-confidence
{ width: 16px; height: 16px; margin: 5px; background-repeat: no-repeat;
  padding: 0px 2px; vertical-align: bottom; color: transparent;}
img.ds-authority-confidence.cf-unset,
span.ds-authority-confidence.cf-unset
  { background-image: url(<%= request.getContextPath() %>/image/authority/bug.png);}
img.ds-authority-confidence.cf-novalue,
span.ds-authority-confidence.cf-novalue
  { background-image: url(<%= request.getContextPath() %>/image/confidence/0-unauthored.gif);}
img.ds-authority-confidence.cf-rejected,
img.ds-authority-confidence.cf-failed,
span.ds-authority-confidence.cf-rejected,
span.ds-authority-confidence.cf-failed
  { background-image: url(<%= request.getContextPath() %>/image/confidence/2-errortriangle.gif); }
img.ds-authority-confidence.cf-notfound,
span.ds-authority-confidence.cf-notfound
  { background-image: url(<%= request.getContextPath() %>/image/confidence/3-thumb1.gif); }
img.ds-authority-confidence.cf-ambiguous,
span.ds-authority-confidence.cf-ambiguous
  { background-image: url(<%= request.getContextPath() %>/image/confidence/4-question.gif); }
img.ds-authority-confidence.cf-uncertain,
span.ds-authority-confidence.cf-uncertain
  { background-image: url(<%= request.getContextPath() %>/image/confidence/5-pinion.gif); }
img.ds-authority-confidence.cf-accepted,
span.ds-authority-confidence.cf-accepted
  { background-image: url(<%= request.getContextPath() %>/image/confidence/6-greencheck.gif); }

/* hide authority-value inputs in forms */
input.ds-authority-value { display:none; }

/** XXX Change to this to get the authority value to show up for debugging:
 input.ds-authority-value { display:inline; }
**/

/* ..except, show authority-value inputs in on the Item EditMetadata page */
table.miscTable input.ds-authority-value { display: inline; }

table.authority-statistics {padding: 5px; margin-bottom: 15px;}
table.authority-statistics table {float: left; text-align: center;}

.statsTable {
        border: 1px gray solid;
        width: 85%;
}

.statsTable td {
        font-size: 0.8em;
}


div.authority-key-nav-link, div.authority-page-nav-link {margin-top: 20px;}

div#authority-message {
    width: 80%;
    display:block;
    text-align: center;
    margin-left: 10%;
    padding: 10px;
    border: 1px dashed #FFCC00;
    background-color: #FFF4C8;
 }

a.authority {
    background: transparent url(<%= request.getContextPath() %>/image/authority/book_key.png) no-repeat;
    background-position: top right;
    padding-right: 20px;
}

/* for edit-item-form lock button */
input.ds-authority-lock
  { vertical-align: bottom; height: 24px; width: 24px; margin-right: 8px;
    background-repeat: no-repeat; background-color: transparent; }
input.ds-authority-lock.is-locked
  { background-image: url(<%= request.getContextPath() %>/image/lock24.png); }
input.ds-authority-lock.is-unlocked
  { background-image: url(<%= request.getContextPath() %>/image/unlock24.png); }
