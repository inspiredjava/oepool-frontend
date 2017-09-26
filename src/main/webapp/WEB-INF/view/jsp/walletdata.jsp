<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>All workers data</title>
    <jsp:include page="header.jsp"/>
    <script src="/resources/js/utils.js"></script>
</head>
<body>
<jsp:include page="topnavigation.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-lg-10 col-lg-offset-1">
            <table class="table table-striped table-hover">
                <c:forEach items="${workerStatisticSet}" var="workerStat">
                    <tr>
                        <td>${workerStat.name}</td>
                        <td>
                            <div class="progress">
                                <div class="progress-bar" id="progressBar_${workerStat.name}" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                                    <script>
                                        document.write(getTimeRatio(${workerStat.activePeriod}, ${startDate}, ${endDate}));
                                    </script>
                                    <script>
                                        var persent = Math.round((${workerStat.activePeriod}) / (3600000)) *100 / ((${endDate} - ${startDate})/3600000);
                                        if (persent >= 75) {
                                            document.getElementById('progressBar_${workerStat.name}').setAttribute("class", "progress-bar progress-bar-success");
                                        }
                                        if (persent < 75 && persent >= 35) {
                                            document.getElementById('progressBar_${workerStat.name}').setAttribute("class", "progress-bar progress-bar-warning");
                                        }
                                        if (persent < 35) {
                                            document.getElementById('progressBar_${workerStat.name}').setAttribute("class", "progress-bar progress-bar-danger");
                                        }
                                        document.getElementById('progressBar_${workerStat.name}').setAttribute("aria-valuenow", persent.toString());
                                        document.getElementById('progressBar_${workerStat.name}').style.width = "" + persent.toString() + "%";
                                    </script>
                                </div>
                            </div>
                        </td>
                        <td>
                            <script type="text/javascript">
                                var t = getDisplayableTimeInterval(${workerStat.onlinePeriodMins});

                                document.write(t.toString());
                            </script>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<%--<script src="/WEB-INF/view/js/bootstrap.min.js"></script>--%>
</body>
</html>
