<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/resources/css/theme.css" type="text/css">
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">

    <%--chart.js--%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.13.0/moment.min.js"></script>

    <script src="/resources/js/utils.js"></script>
    <script src="/resources/js/Chart.min.js"></script>

    <style>
        canvas {
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
        }
    </style>

    <title>Worker details</title>
</head>

<body>
    <div class="container container-fluid">
        <div class="row">
            <h2>Wallet: ${wallet}</h2>
        </div>

        <div class="row">
            <div class="col-lg-10 col-lg-offset-1">
                <canvas id="canvas"></canvas>
            </div>
        </div>

        <script>
            function newDateString(millis) {
                return moment(millis).format(timeFormat);
            }

            var timeFormat = 'MM/DD/YYYY HH:mm';
            var ctx = document.getElementById("workerChart");
            var config = {
                type: 'line',
                data: {
                    labels: [
                        <c:forEach items="${workerStatistic.hr}" var="hr">
                            newDateString(${hr.timestamp}),
                        </c:forEach>
                    ],
                    datasets: [{
                        label: "${workerName}",
                        backgroundColor:'rgba(255, 99, 132, 0.2)',
                        borderColor: 'rgba(255,99,132,1)',
                        borderWidth: 1,
                        data: [
                            <c:forEach items="${workerStatistic.hr}" var="hr">
                                ${hr.value},
                            </c:forEach>
                        ],
                        fill: true
                    }]
                },
                options: {
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
                var ctx = document.getElementById("canvas").getContext("2d");
                window.myLine = new Chart(ctx, config);

            };
        </script>

    </div>
</body>
</html>
