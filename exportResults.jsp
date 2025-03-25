<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( request.getParameter ( "backToMenu" ) != null ) { response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
  String onLoad = "";
  if ( request.getParameter ( "export" ) != null ) {
    onLoad = "JavaScript:new_window('preview.jsp?surveyId="+surveyId+"&url="+java.net.URLEncoder.encode("previewEntryForm.jsp?surveyId="+surveyId)+"')";

    String exportDelimiter = request.getParameter ( "exportDelimiter" );
    if ( exportDelimiter.equals ("tab") ) { entryForm.setExportDelimiter ( Config.tab ); }
    else if ( exportDelimiter.equals ("semicolon") ) { entryForm.setExportDelimiter ( ";" ); }
    else if ( exportDelimiter.equals ("comma") ) { entryForm.setExportDelimiter ( "," ); }
    else if ( exportDelimiter.equals ("pipe") ) { entryForm.setExportDelimiter ( "|" ); }
    else { entryForm.setExportDelimiter ( "," ); }

    String exportIncludeQuestions = request.getParameter ( "exportIncludeQuestions" );
    entryForm.setExportIncludeQuestions ( exportIncludeQuestions.equals("1") );

    Section section = entryForm.getSection(0);
    for ( int questionNr = 0; questionNr < section.getNumQuestions (); questionNr++ ) {
      Question question = section.getQuestion ( questionNr );

      String exportInclude = (String) request.getParameter ( "include_0_" + questionNr );
      if ( exportInclude != null && exportInclude.equals("1") ) { question.setExportInclude(true); }
      else { question.setExportInclude(false); }

      String exportExpand = (String) request.getParameter ( "expand_0_" + questionNr );
      if ( exportExpand != null && exportExpand.equals("1") ) { question.setExportExpand(true); }
      else { question.setExportExpand(false); }
    } // end: for ...

    entryForm.save ();

    response.sendRedirect("exportHeader.jsp?surveyId="+surveyId);
    return;
  }

  String pageTitle = Config.msg(136);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="<%=onLoad%>" />
<script language="JavaScript" type="text/javascript"><!--
function isIE4()
{ return( navigator.appName.indexOf("Microsoft") != -1 && (navigator.appVersion.charAt(0)=='4') ); }

function new_window(freshurl) {
  SmallWin = window.open(freshurl, 'Survey','scrollbars=yes,resizable=yes,toolbar=no,height=400,width=600');
  if (!isIE4())	{
    if (window.focus) { SmallWin.focus(); }
  }
  if (SmallWin.opener == null) SmallWin.opener = window;
  SmallWin.opener.name = "Main";
}
//-->
</script>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
 <input type="hidden" name="surveyId" value="<%=surveyId%>">
 <input type="submit" name="export" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;
 <input type="submit" name="backToMenu" value="<%=Config.msg(24)%>" class="button"><br>
 <br>
 <b><%=Config.msg(137)%></b><br>
 <input type="radio" name="exportDelimiter" value="semicolon"<%
 if ( entryForm.getExportDelimiter().equals (";") ) out.print(" checked"); %>><%=Config.msg(138)%> <font color="#999999"><%=Config.msg(139)%></font><br>
 <input type="radio" name="exportDelimiter" value="comma"<%
 if ( entryForm.getExportDelimiter().equals (",") ) out.print(" checked"); %>><%=Config.msg(140)%> <font color="#999999"><%=Config.msg(141)%></font><br>
 <input type="radio" name="exportDelimiter" value="pipe"<%
 if ( entryForm.getExportDelimiter().equals ("|") ) out.print(" checked"); %>><%=Config.msg(142)%> <font color="#999999"><%=Config.msg(143)%></font><br>
 <br>
 <b><%=Config.msg(144)%></b><br>
 <input type="radio" name="exportIncludeQuestions" value="1"<%
 if ( entryForm.getExportIncludeQuestions() ) out.print(" checked"); %>>Yes<br>
 <input type="radio" name="exportIncludeQuestions" value="0"<%
 if ( !entryForm.getExportIncludeQuestions() ) out.print(" checked"); %>>No<br>
 <br>
 <%=Config.msg(145)%><br>
<%
    out.print ( entryForm.getHTML ( survey, SurveyEntryForm.modeResultsExport ) );
%>
 <img src="images/trans.gif" width="1" height="7" alt=""><br>
 <input type="submit" name="export" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;
 <input type="submit" name="backToMenu" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />