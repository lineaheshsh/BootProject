var newsQnA_main = {
    init : function () {
        var _this = this;

        // 카테고리 등록 버튼 클릭 시
        $('#add').on('click', function () {
            _this.categoryAdd();
        });

        // 게시판 등록 버튼 클릭
        $('#qnaAdd').on('click', function () {
            _this.qnaAdd();
        });
    },
    categoryAdd : function () {

        var data = {
            category_nm: $('#categoryNm').val()
        };

        $.ajax({
            url: '/qna/categoryAdd',
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            if ( data.msg == "ok" ) {
                alert('인덱스가 생성되었습니다');
                location.href = "categoryList";
            } else {
                alert('인덱스 생성 실패');
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });

    },
    qnaAdd: function () {

        var data = {
            'ttl': $('#boardTTL').val(),
            'contents': $('#boardContents').val(),
            'category_id': $('#boardCategory option:selected').val()
        }

        $.ajax({
            url: '/qna/qnaAdd',
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            if ( data.msg == "ok" ) {
                alert('Q&A 게시글이 등록 되었습니다');
                location.href = "qnaList";
            } else {
                alert('Q&A 게시글 등록 실패!');
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

newsQnA_main.init();