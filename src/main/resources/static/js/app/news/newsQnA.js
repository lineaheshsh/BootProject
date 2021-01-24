var newsQnA_main = {
    init : function () {
        var _this = this;

        // 검색 버튼 클릭 시
        $('#add').on('click', function () {
            _this.categoryAdd();
        });
    },
    categoryAdd : function () {

        var data = {
            category_nm: $('#categoryNm').val()
        };

        $.ajax({
            url: '/categoryAdd',
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            if ( data.msg == "ok" ) {
                alert('인덱스가 생성되었습니다');
                location.href = "newsQnA";
            } else {
                alert('인덱스 생성 실패');
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    }
};

newsQnA_main.init();