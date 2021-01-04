Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

var newsAnalysis_main = {
    init : function () {
        var _this = this;

        _this.initPieAnalysis();
        _this.initLineAnalysis();
    },
    initPieAnalysis : function () {
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

                Highcharts.chart('donutChart', {
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },
                    // title: {
                    //     text: 'Browser market shares in January, 2018'
                    // },
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
    initLineAnalysis : function () {
        var ctx = document.getElementById("newsCompanyAnalysis");

        $.ajax({
            url: '/newsCompanyDateCount',
            type: 'GET',
            success: function onData (data) {
                console.log(data);

                var _list = [];
                var _obj = {};
                var _date = [];
                var _value = [];



                $.each(data, function (index, item) {
                    _date.push(item.date);
                    _value.push(item.count);
                });

                console.log(_date);
                console.log(_value);

                _obj.name = "naver";
                _obj.data = _value;
                _obj.color = "#19ce60"; // naver color

                _list.push(_obj);
                console.log(_obj);

                Highcharts.chart('lineChart', {
                    chart: {
                        type: 'line'
                    },
                    // title: {
                    //     text: '도메인 별 Crawling Data 추이'
                    // },
                    xAxis: {
                        categories: _date
                    },
                    yAxis: {
                        title: {
                            text: 'Temperature (°C)'
                        }
                    },
                    plotOptions: {
                        line: {
                            dataLabels: {
                                enabled: true
                            },
                            enableMouseTracking: false
                        }
                    },
                    series: _list
                });
            },
            error: function onError (error) {
                console.error(error);
            }
        });

    }
};

newsAnalysis_main.init();