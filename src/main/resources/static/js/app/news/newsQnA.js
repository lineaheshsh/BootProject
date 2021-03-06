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

        // 게시판 수정 버튼 클릭
        $('#boardEdit').on('click', function () {
           _this.qnaEdit();
        });

        // 게시판 저장 버튼 클릭
        $('#boardSave').on('click', function () {
           _this.qnaSave();
        });

        // 게시판 삭제 버튼 클릭
        $('#boardDelete').on('click', function () {
            _this.qnaDelete();
        });

        // 취소 버튼
        $('#boardCancel').on('click', function () {
            location.href = "/qna/board";
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
            url: '/qna/board',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            if ( data.msg == "ok" ) {
                alert('Q&A 게시글이 등록 되었습니다');
                location.href = "/qna/board";
            } else {
                alert('Q&A 게시글 등록 실패!');
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    qnaEdit: function () {

        $('#boardCategory').attr('disabled', false);
        $('#boardTTL').attr('disabled', false);
        $('#boardContents').attr('disabled', false);

        $('#boardSubject').innerText = "Q&A 게시글 수정";

        $('#boardEdit').css('display', 'none');
        $('#boardSave').css('display', 'block');
    },
    qnaSave: function () {

        var data = {
            'seq': $('#boardSeq').val(),
            'ttl': $('#boardTTL').val(),
            'contents': $('#boardContents').val(),
            'category_id': $('#boardCategory option:selected').val(),
            'doc_id': $('#boardDocId').val()
        }

        $.ajax({
            url: '/qna/board',
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            if ( data.msg == "ok" ) {
                alert('Q&A 게시글이 수정 되었습니다');
                location.href = "/qna/board";
            } else {
                alert('Q&A 게시글 수정 실패!');
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    qnaDelete: function () {

        var data = {
            'seq': $('#boardSeq').val(),
            'doc_id': $('#boardDocId').val()
        }

        $.ajax({
            url: '/qna/board',
            type: 'DELETE',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data) {
            if ( data.msg == "ok" ) {
                alert('Q&A 게시글이 삭제 되었습니다');
                location.href = "/qna/board";
            } else {
                alert('Q&A 게시글 삭제 실패!');
            }

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

newsQnA_main.init();