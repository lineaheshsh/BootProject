var news_main = {
    init : function () {
        var _this = this;
        
        // 옵션 > 신문사 전체 체크 클릭 시
        // 일반 체크박스 해제
        $('#totalCompany').on('change', function () {
            if($(this).is(":checked")) {
                _this.disableOtherCompany();
            }
        });

        // 옵션 > 신문사 개별 체크 클릭 시
        // 전체 체크박스 해제
        $("input:checkbox[name='company']").on('change', function () {
            if($(this).is(":checked")) {
                _this.disableTotalCompany();
            }
        });

        // 검색 버튼 클릭 시
        $('#searchBtn').on('click', function () {
            _this.search();
        });

        _this.initCheckCompany();
        _this.initCheckOrderBy();
    },
    initCheckCompany : function () {
        var _selCompany = $('#his_company').val();

        var _companyList = _selCompany.split(",");
        if ( _selCompany == "" ) {
            $("input:checkbox[name='totalCompany']").prop("checked", true);
        } else {
            $.each(_companyList, function (index, item) {
                $("input:checkbox[id='" + item + "']").prop("checked", true);
            });
        }
    },
    initCheckOrderBy : function () {
        var _selOrderBy = $('#his_orderby').val();

        if ( _selOrderBy == "" || _selOrderBy == "r" ) {
            $("input:radio[name='orderby']:radio[value='r']").prop("checked", true);
        } else {
            $("input:radio[name='orderby']:radio[value='d']").prop("checked", true);
        }
    },
    disableOtherCompany : function () {
        $("input:checkbox[name='company']").prop("checked", false);
    },
    disableTotalCompany : function () {
        $("input:checkbox[name='totalCompany']").prop("checked", false);
    },
    search : function () {
        var _keyword = $('#keyword').val();
        var _company = $("input:checkbox[name='totalCompany']").is(":checked") ? "" : this.companyCheckList();
        var _orderby = $("input[name='orderby']:checked").val();

        $('#his_keyword').val(_keyword);
        $('#his_company').val(_company);
        $('#his_orderby').val(_orderby);

        $('#hiddenForm').submit();
    },
    companyCheckList: function () {
        var _companyList = [];
        $("input[name='company']:checked").each(function () {
            var _company = $(this).val();
            _companyList.push(_company);
        });

        return _companyList.join(",");
    }
};

news_main.init();