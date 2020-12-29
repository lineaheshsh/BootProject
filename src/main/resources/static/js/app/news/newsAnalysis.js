Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

var newsAnalysis_main = {
    init : function () {
        var _this = this;

        _this.initCompanyAnalysis();
    },
    initCompanyAnalysis : function () {
        var ctx = document.getElementById("newsCompanyAnalysis");

        $.ajax({
            url: '/newsCompanyCount',
            type: 'GET',
            success: function onData (data) {
                console.log(data);

                var _title = [];
                var _count = [];
                var _color = [];

                $.each(data, function (index, item) {
                    var _obj = {};
                    _obj.name = item.company;
                    _obj.y = item.count;
                    _title.push(_obj);

                    //_title.push(item.company);
                    //_count.push(item.count);
                    // 랜덤 색상
                    _color.push('#'+Math.floor(Math.random()*16777215).toString(16));
                });

                /**
                var myPieChart = new Chart(ctx, {
                    type: 'doughnut',
                    data: {
                        labels: _title,
                        datasets: [{
                            data: _count,
                            backgroundColor: _color
                        }],
                    },
                    options: {
                        maintainAspectRatio: false,
                        tooltips: {
                            backgroundColor: "rgb(255,255,255)",
                            bodyFontColor: "#858796",
                            borderColor: '#dddfeb',
                            borderWidth: 1,
                            xPadding: 15,
                            yPadding: 15,
                            displayColors: false,
                            caretPadding: 10,
                        },
                        legend: {
                            display: false
                        },
                        cutoutPercentage: 80,
                    },
                });
                 **/

                Highcharts.chart('donutChart', {
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },
                    title: {
                        text: 'Browser market shares in January, 2018'
                    },
                    tooltip: {
                        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                    },
                    accessibility: {
                        point: {
                            valueSuffix: '%'
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: false
                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: 'Brands',
                        colorByPoint: true,
                        data: _title
                    }]
                });
            },
            error: function onError (error) {
                console.error(error);
            }
        });

    },
};

newsAnalysis_main.init();