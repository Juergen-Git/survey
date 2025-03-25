<%@ include file="/globalAuthRequired.jsp" %><%
  Category log = Category.getInstance(adminStatsUpdateNow_jsp.class.getName());

  if ( !pid.equals("admin") ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect("index.jsp"); return; }

  if ( request.getParameter ( "ok" ) != null ) {
    if ( !Config.statsUpdateRunning ) {
      // run update stats in background
      UpdateStatsTimerTask updateStatsTimerTask = new UpdateStatsTimerTask();
      Config.timer.schedule(updateStatsTimerTask, 1000); // start after 1 second delay
    }
    else {
      log.error("Not starting usage statistics update because it is already running.");
    }
    response.sendRedirect("index.jsp");
    return;
  } // end: if ( request.getParameter ( "ok" ) != null )

  String pageTitle = Config.msg(39);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<%=Config.msg(40)%>
<br>
<br>
 <input type="submit" name="ok" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />
