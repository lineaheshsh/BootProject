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
                    _title.push(item.company);
                    _count.push(item.count);
                    // 랜덤 색상
                    _color.push('#'+Math.floor(Math.random()*16777215).toString(16));
                });

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
            },
            error: function onError (error) {
                console.error(error);
            }
        });

    },
};

newsAnalysis_main.init();