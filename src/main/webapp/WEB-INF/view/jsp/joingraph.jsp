<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Join Graph</title>
    <jsp:include page="header.jsp"/>
    <link rel="stylesheet" href="/resources/css/theme.css"/>
</head>
<body>
<jsp:include page="topnavigation.jsp"/>
<div class="row">
    <div class="col-lg-10 col-lg-offset-1">
        <div class="page-header">
            <h2>Join graphs</h2>
        </div>
    </div>
</div>
<br>
    <div class="container container-fluid">
        <div class="row">
            <div class="col-lg-10 col-lg-offset-1">
                <form action="joingraph" method="get">
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
        <div class="col-lg-12">
            <canvas id="workersChartCanvas"></canvas>
        </div>
    </div>
</div>

<%--chart.js--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.13.0/moment.min.js"></script>
<script src="/resources/js/Chart.min.js"></script>
<script src="/resources/js/utils.js"></script>
<script>
    function getRandomColor() {
        var letters = '0123456789ABCDEF';
        var color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

    function newDateString(millis) {
        return moment(millis).format(timeFormat);
    }

    var timeFormat = 'MM/DD/YYYY HH:mm';

    //----------------- Generate chart for all workers
    var workersConfig = {
        type: 'line',
        data: {
            labels: [
                <c:forEach items="${workerStatisticSet}" var="worker">
                    <c:forEach items="${worker.hr}" var="hr">
                        newDateString(${hr.timestamp}),
                    </c:forEach>
                </c:forEach>
            ],
            datasets: [
                <c:forEach items="${workerStatisticSet}" var="worker">
                {   label: "${worker.name}",
                    backgroundColor: getRandomColor(),
                    borderColor: 'rgba(22,22,22,1)',
                    borderWidth: 1,
                    fill: false,
                    data: [
                        <c:forEach items="${worker.hr}" var="hr">
                            ${hr.value},
                        </c:forEach>
                    ]
            },</c:forEach>
            ]
        },
        options: {
            legend: {
                display: true
            },
            elements: {
                line: {
                    tension: 0
                }
            },
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
    window.onload = function() {
        var overallWalletCtx = document.getElementById("workersChartCanvas").getContext("2d");
        window.myLine = new Chart(overallWalletCtx, workersConfig);
    };
</script>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<%--<script src="/WEB-INF/view/js/bootstrap.min.js"></script>--%>
</body>

</html>
