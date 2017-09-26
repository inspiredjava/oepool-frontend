<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List</title>
    <jsp:include page="header.jsp"/>
    <link rel="stylesheet" href="/resources/css/theme.css"/>
    <script src="/resources/js/utils.js"></script>
</head>
<body>
    <jsp:include page="topnavigation.jsp"/>
    <div class="row">
        <div class="col-lg-10 col-lg-offset-1">
            <div class="page-header">
                <h2>List</h2>
            </div>
        </div>
    </div>
    <br>
    <div class="container container-fluid">
        <div class="row">
            <div class="col-lg-10 col-lg-offset-1">
                <form action="list" method="get">
                    <div class="col-lg-8">
                        <label class="control-label" for="walletSelectorId">Wallet address</label>
                        <select id="walletSelectorId" name="walletSelector" class="form-control">
                            <c:forEach items="${wallets}" var="wallet">
                                <option value="${wallet}">${wallet}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-lg-1 col-lg-offset-1">
                        <label class="control-label" for="periodSelectorId">Period</label>
                        <select name="periodSelector" id="periodSelectorId">
                            <option value="1">1 day</option>
                            <option value="2">2 days</option>
                            <option value="3">3 days</option>
                            <option value="7">1 week</option>
                        </select>
                    </div><br>

                    <div class="col-lg-1 col-lg-offset-1">
                        <input class="btn btn-primary" type="submit" value="Collect">
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="container container-fluid" ${results ? "" : 'hidden'}>
        <div class="row">
            <div class="container">
                <div class="row">
                    <div class="col-lg-8 col-lg-offset-2">
                        <canvas id="overallWalletCanvas"></canvas>
                    </div>
                </div>
                <br>

                <div class="row">
                    <div class="col-lg-10 col-lg-offset-1">
                        <table class="table table-striped table-hover">
                            <tr>
                                <th>Worker</th>
                                <th>Status</th>
                                <th>Last hashrate</th>
                                <th>Active period</th>
                                <th>Last online period</th>
                            </tr>
                            <c:forEach items="${workerStatisticSet}" var="workerStat">
                                <tr>
                                    <td>${workerStat.name}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${workerStat.offline == true}">
                                                    <span class="label label-danger">Offline</span>
                                                </c:when>
                                                <c:when test="${workerStat.offline == false}">
                                                    <span class="label label-success">Online</span>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <script type="text/javascript">
                                                var hashrate = Math.round(${workerStat.hr.get(workerStat.hr.size()-1).value} / 1000000);
                                                document.write(hashrate.toString()+" MH/s");
                                            </script>
                                        </td>
                                        <td>
                                            <script type="text/javascript">
                                                var t = getDisplayableTimeInterval(${workerStat.incessantlyPeriodMins});
                                                document.write(t.toString());
                                            </script>
                                        </td>
                                    <td>
                                        <script type="text/javascript">
                                            var t = getDisplayableTimeInterval(${workerStat.onlinePeriodMins});

                                            document.write(t.toString());
                                        </script>
                                    </td>
                                    <td>
                                        <c:url value="/${walletAddress}/${workerStat.name}" var="workerDetails"/>
                                        <a class="btn btn-default" type="button" onclick="location.href='${workerDetails}'">
                                        <span class="glyphicon glyphicon-stats" aria-hidden="true"></span> View chart</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>

                <div class="row">
                    <c:forEach items="${workerStatisticSet}" var="workerStat">
                        <div class="col-lg-4">
                            <div class="thumbnail">
                                <div class="caption">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <c:url value="/${walletAddress}/${workerStat.name}" var="workerDetails"/>
                                            <canvas onclick="location.href='${workerDetails}'" id="worker_${workerStat.name}_Canvas"></canvas>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>


    <%--chart.js--%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.13.0/moment.min.js"></script>
    <script src="/resources/js/Chart.min.js"></script>

    <script>
        function newDateString(millis) {
            return moment(millis).format(timeFormat);
        }

        var timeFormat = 'MM/DD/YYYY HH:mm';
        var ctx = document.getElementById("workerChart");
        var walletConfig = {
            type: 'line',
            data: {
                labels: [
                    <c:forEach items="${walletOverallData}" var="wallet">
                    newDateString(${wallet.record_timestamp}),
                    </c:forEach>
                ],
                datasets: [{
                    hidden: true,
                    label: "Accurate, long average",
                    backgroundColor:'rgba(255, 99, 132, 0.2)',
                    borderColor: 'rgba(255,99,132,1)',
                    borderWidth: 0,
                    fill: true,
                    pointRadius: 0,
                    data: [
                        <c:forEach items="${walletOverallData}" var="walletData">
                        ${walletData.hashrate},
                        </c:forEach>
                    ]},
                    {
                        label: "Rough, short average",
                        backgroundColor:'rgba(212,52,255, 0.2)',
                        borderColor: 'rgba(212,52,255,1)',
                        borderWidth: 0,
                        fill: true,
                        pointRadius: 0,
                        data: [
                            <c:forEach items="${walletOverallData}" var="walletData">
                            ${walletData.currentHashrate},
                            </c:forEach>
                        ]}
                ]
            },
            options: {
                responsive: true,
                scales: {
                    xAxes: [{
                        display: true,
                        type: 'time',
                        time: {
                            tooltipFormat: 'DD/MM HH:mm',
                            displayFormats: {
                                hour: 'HH:mm'
                            }
                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Date'
                        }
                    }],
                    yAxes: [{
                        scaleLabel: {
                            display: true
                        },
                        ticks:{
                            min: 0,
                            callback: function (value) {
                                var v = value / 1000000;
                                if (v < 1000) {
                                    return Number(v) + " MH/s"
                                }
                                if (v > 1000) {
                                    return Number(v / 1000) + " Gh/s";
                                }
                            }
                        }
                    }]
                }
            }
        };
        //----------------- Generate charts for all workers
        var color;
        <c:forEach items="${workerStatisticSet}" var="worker">
        <c:if test="${worker.offline == true}">
        color = 'rgba(232,41,17, 0.3)';
        </c:if>
        <c:if test="${worker.offline == false}">
        color = 'rgba(17,232,122, 0.3)';
        </c:if>
        var workersConfig_${worker.name} = {
            type: 'line',
            data: {
                labels: [
                    <c:forEach items="${worker.hr}" var="hr">
                    newDateString(${hr.timestamp}),
                    </c:forEach>
                ],
                datasets: [{
                    label: "${worker.name}",
                    backgroundColor: color,
                    borderColor: color,
                    borderWidth: 1,
                    pointRadius: 0,
                    fill: true,
                    data: [
                        <c:forEach items="${worker.hr}" var="hr">
                        ${hr.value},
                        </c:forEach>
                    ]}
                ]
            },
            options: {
                title: {
                    display: true,
                    text: "${worker.name}"
                },
                legend: {
                    display: false
                },
                elements: {
                    line: {
                        tension: 0
                    }
                },
//                    onClick: onClickEvent,
                responsive: true,
                scales: {
                    xAxes: [{
                        display: true,
                        type: 'time',
                        time: {
                            tooltipFormat: 'DD/MM HH:mm',
                            displayFormats: {
                                hour: 'HH:mm'
                            }
                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Date'
                        }
                    }],
                    yAxes: [{
                        scaleLabel: {
                            display: true
                        },
                        ticks:{
                            min: 0,
                            callback: function (value) {
                                var v = value / 1000000;
                                if (v < 1000) {
                                    return Number(v) + " MH/s"
                                }
                                if (v > 1000) {
                                    return Number(v / 1000) + " Gh/s";
                                }
                            }
                        }
                    }]
                }
            }
        };

        function onClickEvent_${worker.name}() {

        }
        </c:forEach>

        window.onload = function() {
            var overallWalletCtx = document.getElementById("overallWalletCanvas").getContext("2d");
            window.myLine = new Chart(overallWalletCtx, walletConfig);

            <c:forEach items="${workerStatisticSet}" var="worker">
            var worker_${worker.name}_Ctx = document.getElementById("worker_${worker.name}_Canvas")
            window.myLine = new Chart(worker_${worker.name}_Ctx, workersConfig_${worker.name});
            </c:forEach>
        };
    </script>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <%--<script src="/WEB-INF/view/js/bootstrap.min.js"></script>--%>
</body>
</html>
