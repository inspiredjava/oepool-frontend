<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="description" content="Frontend module for Statistic Application for Open Ethereum Pool"/>
    <title>EthPool Statistics</title>
    <jsp:include page="header.jsp"/>
    <script src="/resources/js/utils.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
</head>
<body>
<jsp:include page="topnavigation.jsp"/>
<div class="row">
    <div class="col-lg-10 col-lg-offset-1">
        <div class="page-header">
            <h2>Working time</h2>
        </div>
    </div>
</div>
<br>
    <c:choose>
        <c:when test="${errorStatus == true}">
            <div class="col-lg-8 col-lg-offset-2 alert alert-danger" role="alert">
                Check date range!
            </div>
        </c:when>
    </c:choose>

    <div class="container container-fluid">
        <div class="col-lg-10 col-lg-offset-1">
            <form:form action="/" method="get">
                <div class="row">
                    <div class="col-lg-6">
                        <label for="walletSelectorId">Wallet address</label>
                        <select id="walletSelectorId" name="walletSelector" class="form-control">
                            <c:forEach items="${wallets}" var="wallet">
                                <option value="${wallet}">${wallet}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-lg-2 form-group ${errorStatus ? 'has-error' : ''}">
                        <label for="start-datepicker">From:</label>
                        <input class="form-control" type="text" id="start-datepicker" name="startDate"/>
                    </div>
                    <div class="col-lg-2 form-group ${errorStatus ? 'has-error' : ''}">
                        <label for="end-datepicker">To:</label>
                        <input class="form-control" type="text" id="end-datepicker" name="endDate">
                    </div>
                    <div class="col-lg-1 col-lg-offset-1">
                        <br>
                        <button class="btn btn-primary" type="submit">Collect</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>

<div class="container container-fluid" ${results ? "" : 'hidden'}>
    <div class="row">
        <div class="col-lg-10 col-lg-offset-1">
            <table class="table table-striped table-hover">
                <tr>
                    <th>Worker</th>
                    <th>Working time statistics</th>
                    <th>Online period</th>
                </tr>
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

    <jsp:include page="footer.jsp"/>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script>
        $( function() {
            $( "#start-datepicker" ).datepicker();
        } );

        $( function() {
            $( "#end-datepicker" ).datepicker();
        } );

    </script>
</body>
</html>