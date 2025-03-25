<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( request.getParameter ( "backToMenu" ) != null ) { response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  String pageTitle = Config.msg(136);
  mysession.setAttribute(surveyId,  "cookietrail", "<a target=\"_top\" href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a target=\"_top\" href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<script language="JavaScript" type="text/javascript"><!--
function isIE4()
{ return( navigator.appName.indexOf("Microsoft") != -1 && (navigator.appVersion.charAt(0)=='4') ); }

function new_window(freshurl) {
  SmallWin = window.open(freshurl, 'Export','scrollbars=yes,resizable=yes,status=yes,toolbar=no,menubar=yes,left=150,top=100,height=200,width=350');
  if (!isIE4())	{
    if (window.focus) { SmallWin.focus(); }
  }
  if (SmallWin.opener == null) SmallWin.opener = window;
  SmallWin.opener.name = "Main";
	return SmallWin;
}

function submitForm(form) {
  w.close();
	form.submit();
}

var exportUrl = "export.jsp?surveyId=<%=surveyId%>";
//-->
</script>
<form name="exportinfo" target="_top" method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
 <input type="hidden" name="surveyId" value="<%=surveyId%>">
 <%=Config.msg(147)%>
 <br>
 <br>
 <input type="submit" name="backToMenu" value="<%=Config.msg(82)%>" class="button" onClick="javascript:submitForm(document.exportinfo);">&nbsp;&nbsp;
</form>
<%
//  session.removeAttribute( "shortScreen" );
%>
<survey:pageFooter />