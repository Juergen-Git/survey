<%@ include file="/globalNoAuthRequired.jsp" %><%

  String pageTitle = Config.serviceName + Config.msg(69);
%><survey:pageHeader title="" headerTitle="<%=pageTitle%>" headerKeywords="<%=Config.msg(70)%>" />
<center>
<br>
<br>
<br>
<%=Config.formatFeedback("error", Config.msg(71))%>
<br><br>
<%=Config.msg(72)%> <a href="login.jsp"><%=Config.msg(73)%></a>.
</center>
<survey:pageFooter />
