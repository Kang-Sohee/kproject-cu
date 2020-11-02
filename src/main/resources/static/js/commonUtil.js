function cf_Nvl(obj) {
    if (obj.length > 0) {
        var s = $.trim(obj.val());
        if (s.length < 1) {
            /*
            if (msg != undefined) {
                try{ obj.focus(); }catch(e){}
            }
            */
            return false;
        }
        return true;
    }
    return false;
}

/*
function cf_Nvl_Alert(obj, msg){
	if(obj.length > 0) {
		var s = $.trim(obj.val());
		if(s.length < 1){
			if (msg != undefined) {
				alert(msg+" 은(는) 필수입력 사항입니다!");
				try{ obj.focus(); }catch(e){}
			}
			return false;
		}
		return true;
	}

	return false;
}
*/

function cf_Nvl2(obj, msg) {
    if (obj != undefined) {
        var s = obj.value;
        if (s.length < 1) {
            if (msg != undefined) {
                alert(msg + " 은(는) 필수입력 사항입니다!");
                try {
                    obj.focus();
                } catch (e) {
                }
            }
            return false;
        }
        return true;
    }

    return false;
}

function cf_Nvl3(obj, msg) {
    if (obj.length > 0) {
        var s = $.trim(obj.val());
        if (s.length < 1) {
            if (msg != undefined) {
                alert(msg);
                try {
                    obj.focus();
                } catch (e) {
                }
            }
            return false;
        }
        return true;
    }

    return false;
}

function cf_Nvl_CheckSpace(obj, msg) {
    if (obj.length > 0) {
        var s = $.trim(obj.val());
        if (s.length < 1) {
            if (msg != undefined) {
                alert(msg);
                try {
                    obj.focus();
                } catch (e) {
                }
            }
            return false;
        }
        if (obj.val().search(/\s/) != -1) {
            alert(msg + "에 공백을 입력 할 수 없습니다.");
            obj.focus();
            return false;
        }
        return true;
    }

    return false;
}

function cf_CheckSpace(obj, msg) {
    if (obj.val().search(/\s/) != -1) {
        alert(msg + "에 공백을 입력 할 수 없습니다.");
        obj.focus();
        return false;
    }
    return true;
}

/*
 * 넘어온 문자열들이 모두 숫자인가를 확인
 */
function cf_IsNumber(strInput) {
    var cValue;
    for (var i = 0; i < strInput.length; i++) {
        cValue = strInput.charAt(i);
        if (cValue < '0' || cValue > '9') {
            return true;
        }
    }
    return false;
}

/**
 * 이메일 체크하는 함수] alert('이메일 형식이 잘못되었습니다.');
 */
function cf_IsMailCheck(sVal) {
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    try {
        // 정규식 지원하는 경우
        if (!regExp.test(sVal)) {
            // alert('이메일 형식이 잘못되었습니다.');
            return false;
        }
        return regExp.test(sVal);
    } catch (e) {
        // 정규식 지원하지 않는 경우
        var tmpArray = new Array();
        var lCma, lStr, tmpStr;
        tmpArray = sVal.split("@");
        if (tmpArray.length != 2) return false;
        // 이메일 처음, 끝 문자 제한
        for (var i = 0; i < tmpArray.length; i++) {
            for (var j = 0; j < tmpArray[i].length; j++) {
                tmpStr = tmpArray[i].charCodeAt(j);
                if (tmpStr == 45 || tmpStr == 46 || tmpStr == 95 || (tmpStr >= 48 && tmpStr <= 57) || (tmpStr >= 65 && tmpStr <= 90) || (tmpStr >= 97 && tmpStr <= 122)) {
                    if (j == 0 && (tmpStr == 45 || tmpStr == 46 || tmpStr == 95)) return false;
                    if (j == tmpArray[i].length - 1 && (tmpStr == 45 || tmpStr == 46 || tmpStr == 95)) return false;

                    continue;
                } else {
                    return false;
                }
            }
        }
        // 이메일 뒷자리수 제한
        lCma = tmpArray[1].lastIndexOf(".");
        if (lCma == -1 || lCma == 0) return false;
        lStr = tmpArray[1].substring(lCma + 1).length;
        if (!(lStr > 0 && lStr <= 3)) return false;

        return true;
    }
}


/**
 * 숫자만 입력하는 함수
 */
function cf_NumbersOnly(obj, e, decimal) {
    var key;
    var keychar;
    if (window.event) {
        // IE에서 이벤트를 확인하기 위한 설정
        key = window.event.keyCode;
    } else if (e) {
        // FireFox에서 이벤트를 확인하기 위한 설정
        key = e.which;
    } else {
        return true;
    }

    keychar = String.fromCharCode(key);
    if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13)
        || (key == 27)) {
        return true;
    } else if ((("0123456789").indexOf(keychar) > -1)) {
        return true;
    } else if (decimal && (keychar == ".")) {
        if (obj.value.indexOf(".") > -1) {
            return false;
        }
        return true;
    } else
        return false;
}

/**
 *
 */
function cf_GetEvent(e) {
    if (navigator.appName == 'Netscape') {
        keyVal = e.which;
    } else {
        keyVal = event.keyCode;
    }
    return keyVal;
}

/**
 * 숫자만 입력하는 함수
 */
function cf_NumbersOnly(evt) {
    var myEvent = window.event ? window.event : evt;
    var isWindowEvent = window.event ? true : false;
    var keyVal = cf_GetEvent(evt);
    var result = false;

    if (myEvent.shiftKey) {
        result = false;
    } else {
        if ((keyVal >= 48 && keyVal <= 57) || (keyVal >= 96 && keyVal <= 105) || (keyVal == 8) || (keyVal == 9)) {
            result = true;
        } else {
            result = false;
        }
    }
    if (!result) {
        myEvent.preventDefault();
    }
}

/**
 * 입력한 값을 길이 제한하는 함수 1번째 alert(_msg + '은(는) 최대 입력 자리 수(' + _toLen+ ') 범위보다
 * 큽니다.'); 2번째 alert(_msg + '은(는) 최소 입력 자리 수('+ _fromLen +') 범위보다 작습니다.');
 *
 */
function cf_ScopeLen(obj, _fromLen, _toLen, minmsg, maxmsg) {
    if (obj.val().length >= _fromLen) {
        if (obj.val().length <= _toLen)
            return true;
        else
            alert(maxmsg);
    } else {
        alert(minmsg);
    }
    try {
        obj.focus();
    } catch (e) {
    }

    return false;
}

/**
 * 날짜를 비교하는 함수 1번째 alert(msg+" 은(는) 필수입력 사항입니다!"); 2번째 alert(msg+" 은(는) 필수입력
 * 사항입니다!"); 3번째 alert(msg+" 은(는) 확인해 주시기 바랍니다!");
 */
function cf_DateComparison(obj1, obj2, msg) {
    if (obj1.length > 0 && obj2.length) {

        var s1 = $.trim(obj1.val());
        var s2 = $.trim(obj2.val());
        if (s1.length < 1) {
            if (msg != undefined) {
                alert(msg + " 은(는) 필수입력 사항입니다!");
                try {
                    obj1.focus();
                } catch (e) {
                }
            }
            return false;
        } else if (s2.length < 1) {
            if (msg != undefined) {
                alert(msg + " 은(는) 필수입력 사항입니다!");
                try {
                    obj2.focus();
                } catch (e) {
                }
            }
            return false;
        } else {
            var fr = s1.replace('.', '');
            var fl = s2.replace('.', '');
            if (fr > fl) {
                alert(msg + " 은(는) 확인해 주시기 바랍니다!");
                try {
                    obj1.focus();
                } catch (e) {
                }
                return false;
            }
        }
        return true;
    }

    return false;

}

/*
 * 팝업창을 띄우는 함수
 */
function cf_OpenWin(url, target, iWidth, iHeight, opt) {

    var sConfig = "";
    sConfig = "top=" + (screen.height - iHeight) / 2 + ","
        + "left=" + (screen.width - iWidth) / 2 + ","
        + "width=" + iWidth + ","
        + "height=" + iHeight + ","
        + opt;
    popup1 = window.open(url, target, sConfig);

}

/*
 * 전화번호/핸드폰번호 체크하는 함수 alert(msg + "(을)를 확인하여 주십시요.");
 */
/*
 * function cf_IsTelNoChk(msg, need) { var obj = event.srcElement; var val =
 * obj.value; var arrTel = (val + "- ").split("-"); var chk = true; if (need !=
 * undefined) { if (!cf_Nvl(obj, msg)) return false; }
 * 
 * if ($.trim(obj.value) != "") { if (cf_IsNumber(arrTel[0])) chk = false; else
 * if (cf_IsNumber(arrTel[1])) chk = false; else if (cf_IsNumber(arrTel[2])) chk =
 * false; else if (arrTel.length != 4) chk = false; else if (arrTel[0].length <
 * 2 || arrTel[0].length > 3) chk = false; else if (arrTel[1].length < 3 ||
 * arrTel[1].length > 4) chk = false; else if (arrTel[2].length < 4 ||
 * arrTel[2].length > 4) chk = false;
 * 
 * if(!chk) { alert(msg + "(을)를 확인하여 주십시요."); obj.focus(); obj.value = ""; } }
 * return chk; }
 */

/*
 * 전화번호/핸드폰번호 체크하는 함수 alert(msg + "(을)를 확인하여 주십시요.");
 */
function cf_IsTelNoChk(val) {

    var chk = true;

    var regExp = /^01([0|1|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})$/;

    if (val != "") {
        if (!regExp.test(val)) {

            var arrTel = (val + "- ").split("-");

            if ($.trim(val) != "") {
                if (cf_IsNumber(arrTel[0])) chk = false;
                else if (cf_IsNumber(arrTel[1])) chk = false;
                else if (cf_IsNumber(arrTel[2])) chk = false;
                else if (arrTel.length != 4) chk = false;
                else if (arrTel[0].length < 2 || arrTel[0].length > 3) chk = false;
                else if (arrTel[1].length < 3 || arrTel[1].length > 4) chk = false;
                else if (arrTel[2].length < 4 || arrTel[2].length > 4) chk = false;

                if (!chk) {
                    return false;
                }
            } else {
                return false;
            }
            return chk;

        }

        return regExp.test(val);
    }

}

/*
 * 전화번호/핸드폰번호 체크하는 함수 alert(msg + "(을)를 확인하여 주십시요.");
 */
function cf_PhoneCheck(obj) {
    var regExp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
    var vVal = $(obj).val();

    if (vVal != "") {
        if (!regExp.test(vVal)) {
            /*
             * alert("잘못된 전화번호 입니다. 확인해 주시기 바랍니다."); $(obj).focus();
             * $(obj).val("");
             */
            return false;
        }

        return regExp.test(vVal);

    } else {
        return false;
    }
}

/*
 * 전송시간을 체크하는 함수 alert("전송 시간을 확인하여 주십시요.");
 */
function cf_IsTrnsTimeChk() {
    var obj = event.srcElement;
    var val = obj.value;
    var arrTime = (val + ": ").split(":");
    var chk = true;

    if ($.trim(obj.value) != "") {
        if (cf_IsNumber(arrTime[0])) chk = false;
        else if (cf_IsNumber(arrTime[1])) chk = false;
        else if (arrTime.length != 3) chk = false;
        else if (arrTime[0].length != 2) chk = false;
        else if (arrTime[1].length != 2) chk = false;

        if (!chk) {
            alert("시간 형식을 확인하여 주십시요.");
            obj.focus();
            obj.value = "";
        }
    }
    return chk;
}

/**
 * 이메일 체크하는 함수 alert('이메일 형식이 잘못되었습니다.');
 */
/*
 * function cf_IsMailCheck() { var obj = event.srcElement; var sVal = obj.value;
 * var regExp =
 * /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
 * try { if($.trim(obj.value) != "") { // 정규식 지원하는 경우 if ( !regExp.test(sVal) ) {
 * alert('이메일 형식이 잘못되었습니다.'); obj.focus(); obj.value = ""; } } return
 * regExp.test(sVal); } catch (e) { // 정규식 지원하지 않는 경우 var tmpArray = new
 * Array(); var lCma, lStr, tmpStr; tmpArray = sVal.split("@"); if
 * (tmpArray.length != 2) return false; // 이메일 처음, 끝 문자 제한 for (var i = 0; i <
 * tmpArray.length; i++) { for (var j = 0; j < tmpArray[i].length; j++) { tmpStr =
 * tmpArray[i].charCodeAt(j); if (tmpStr == 45 || tmpStr == 46 || tmpStr == 95 ||
 * (tmpStr >= 48 && tmpStr <= 57) || (tmpStr >= 65 && tmpStr <= 90) || (tmpStr >=
 * 97 && tmpStr <= 122)) { if (j == 0 && (tmpStr == 45 || tmpStr == 46 || tmpStr ==
 * 95)) return false; if (j == tmpArray[i].length-1 && (tmpStr == 45 || tmpStr ==
 * 46 || tmpStr == 95)) return false; continue; } else { return false; } } } //
 * 이메일 뒷자리수 제한 lCma = tmpArray[1].lastIndexOf("."); if (lCma == -1 || lCma == 0)
 * return false; lStr = tmpArray[1].substring(lCma+1).length; if (!(lStr > 0 &&
 * lStr <= 3)) return false;
 * 
 * return true; } }
 */

/*
 * function cf_distance between_Period(startDate, endDate){ //현재일 var
 * current_date = moment().format("YYYY.MM.DD");
 * 
 * var current_day = cf_DisplayDay(current_date);
 * 
 * return ""; }
 */

/*
 * 현재날짜표기 moment에서 지원하는 format형태로 반환
 */
function cf_DisplayDate(format_type) {

    var current_date = moment().format(format_type);

    return current_date;
}

/*
 * 현재날짜의 요일을 표기
 */
function cf_DisplayDay(strDate) {

    var strDate = strDate;
    var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/gi;

    strDate = strDate.replace(regExp, '');

    var str_yyyy = strDate.substring(0, 4) + '/';
    var str_mm = strDate.substring(4, 6) + '/';
    var str_dd = strDate.substring(6, 8);
    var setDate = String(str_yyyy + str_mm + str_dd + ' 00:00:00');

    var week = ['일', '월', '화', '수', '목', '금', '토'];
    var getday = new Date(setDate).getDay();
    var day = week[getday];

    return day;
}

/*
 * 현재시간 표기
 */
function cf_DisplayTime() {
    var date = new Date();
    var current_Hour = date.getHours();
    var current_Minute = date.getMinutes() > 9 ? date.getMinutes() : '0' + date.getMinutes();
    var time = current_Hour + ":" + current_Minute;
    return time;
}

/*
 * 현재시간을 기준으로 am인지 pm인지 계산함 hh:mm 형식으로 입력
 */
function cf_Display_AmPm_By_Time(hours, minutes) {

    var date = new Date();

    if (hours == '') {
        hours = date.getHours();
    } else if (minutes == '') {
        minutes = date.getMinutes() > 9 ? date.getMinutes() : '0' + date.getMinutes();
    }

    var ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var strTime = ampm;

    return strTime;
}


function cf_Calculate_between_date_period_Momth(startTime, EndTime) {


    // 시작일시
    var startDate = new Date(parseInt(startTime.substring(0, 4), 10),
        parseInt(startTime.substring(4, 6), 10) - 1,
        parseInt(startTime.substring(6, 8), 10),
        parseInt(startTime.substring(8, 10), 10),
        parseInt(startTime.substring(10, 12), 10),
        parseInt(startTime.substring(12, 14), 10));

    // 종료일시
    var endDate = new Date(parseInt(endTime.substring(0, 4), 10),
        parseInt(endTime.substring(4, 6), 10) - 1,
        parseInt(endTime.substring(6, 8), 10),
        parseInt(endTime.substring(8, 10), 10),
        parseInt(endTime.substring(10, 12), 10),
        parseInt(endTime.substring(12, 14), 10));
    f
}


/*
 * 시작일, 종료일사이의 월수 계산
 */
function cf_getBetween_Month(sdate, edate) {
    var value = (cf_getDateDays(edate) - cf_getDateDays(sdate));
    value = Math.round(value / 30);

    return value;
}


function cf_getDateDays(date) {
    var year = new Number(date.substring(0, 4));
    var month = new Number(date.substring(4, 6));
    var day = new Number(date.substring(6, 8));

    var value = year * 12 * 30 + month * 30 + day;

    return value;
}

/*
 * 2019.12.05 (목) PM 04:00 메인페이지에서 날자 요일 시간형식을받아서 숫ㅅ자로 변환햊는 함수
 */
function cf_getOnlyNumber(strDate) {

    var regex = /[+-]?\d+(?:\.\d+)?/g;
    var resultStr = '';
    var match;

    while (match = regex.exec(strDate)) {
        resultStr += match[0];
    }

    return resultStr;
}

/*
 * 날짜를 받아 moment에서 제공하는 원하는 foramt형식으로 변환
 */
function cf_change_Format_monemtJs(date, format) {
    return moment(date).format(format);
}


/*
 * 대여일시를 구하고 1개월,3개월,6개월,12개월에 해당하는 날짜를 구한다.
 * 
 * startDate : 대여일시 division : 구분(ya
 * 
 * 
 */
function ch_get_month_end_Date(startDate, number, division) {

    var divisions = "";
    var value = 0;

    switch (division) {
        case 'month' :
            divisions = 'month';
            break;
        case 'years' :
            divisions = 'years';
            break;
    }

    switch (number) {
        case 1 :
            value = 1;
            break;
        case 3 :
            value = 3;
            break;
        case 6 :
            value = 6;
            break;
        case 12 :
            value = 12;
            break;
    }

    // 일단 해당하는 일자를 구한다.
    var date = moment(startDate, "YYYY.MM.DD").add(value, divisions).toDate();

    return date; // cf_change_Format_monemtJs(endDate, "YYYY.MM.DD");
}


/*
 * 대여일자에서 시작일을 받아 사용자가 클릭한 1개월,3개월,6개월,12개월 날짜를 array형태로 반환한다.
 * 
 * var startDate = new Date("2017-10-01"); //YYYY-MM-DD var endDate = new
 * Date("2017-10-31"); //YYYY-MM-DD var dateArr2 =
 * cf_make_returnDateArray(startDate, endDate); 
 * 
 * for (var i = 0; i < dateArr2.length; i++) { console.log(dateArr2[i]); }
 */
function cf_make_returnDateArray(startDate, endDate) {

    var dates = [],
        currentDate = startDate,

        addDays = function (days) {
            var date = new Date(this.valueOf());
            date.setDate(date.getDate() + days);
            return date;
        };

    while (currentDate <= endDate) {
        dates.push(currentDate);
        currentDate = addDays.call(currentDate, 1);
    }

    return dates;
}


function cf_onlyNumber(str) {
    var res;
    res = str.replace(/[^0-9]/g, "");
    return res;
}


function isEmpty(value) {
    if (value == "" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
        return true
    } else {
        return false
    }

};


/*
 * 현재위치 가져오는 함수 geolocation
 * 
 */
function cf_getLocation() {
    if (navigator.geolocation) { // GPS를 지원하면
        navigator.geolocation.getCurrentPosition(function (position) {
            // alert(position.coords.latitude + ' ' +
            // position.coords.longitude);

        }, function (error) {
            console.error(error);
        }, {
            enableHighAccuracy: false,
            maximumAge: 0,
            timeout: Infinity
        });
    } else {
        // alert('GPS를 지원하지 않습니다');
    }
}

/*
 * ajax 공통 호출
 * 
 */
var requestSync = function (url, param, method, callback) {
    $.ajax({
        method: method,
        url: url,
        dataType: "JSON",
        data: param,
        error: function () {
            alert('error');
        },
        success: callback
    });
}

/*
 * 다음 우편번호 호출 유틸 return받을 elements 입력하면 된다. 사용시 해당 모델도 closeLayer해야한다.
 */
function cf_setupAddress(elements) {
    // 우편번호 찾기 찾기 화면을 넣을 element
    var element_wrap = document.getElementById('wrap');

    // 현재 scroll 위치를 저장해놓는다.
    var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        oncomplete: function (data) {
            // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                //if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                //    extraAddr += data.bname;
                //}
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                //if(extraAddr !== ''){
                //    extraAddr = ' (' + extraAddr + ')';
                // }
                // 조합된 참고항목을 해당 필드에 넣는다.
                $('#' + elements).text(data.zonecode + " " + addr);
                $("#" + elements).css("font-weight", "bold");

            } else {
                // document.getElementById("btnDeliveryAddr").value = '';
            }

            closeLayer("popPlace");// 해당 팝업을 닫는다.모달을 닫고 우편번호 div를 닫는다.

            // iframe을 넣은 element를 안보이게 한다.
            // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
            element_wrap.style.display = 'none';

            // 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
            document.body.scrollTop = currentScroll;
        },
        // 우편번호 찾기 화면 크기가 조정되었을때 실행할 코드를 작성하는 부분. iframe을 넣은 element의 높이값을 조정한다.
        onresize: function (size) {
            element_wrap.style.height = '360px';
        },
        // width : '100%',
        // height : '350px'
        width: '80%',
        height: '350px'
    }).embed(element_wrap);

    // iframe을 넣은 element를 보이게 한다.
    element_wrap.style.display = 'block';
}

function cf_Display_AmPm_By_Times(time) {


    var date = new Date();
    var hours = time.substr(0, 2);
    var minutes = time.substr(0, 4);


    if (hours == '') {
        hours = date.getHours();
    } else if (minutes == '') {
        minutes = date.getMinutes() > 9 ? date.getMinutes() : '0' + date.getMinutes();
    }

    var ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var strTime = ampm;

    return strTime;
}

function calcAge(birth) {

    var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/gi;

    var setBirth = regExp.test(birth) ? birth.replace(regExp, '') : birth;

    if (String(setBirth).length < 8) {
        return '미확인';
    } else {

    }

    var date = new Date();
    var year = date.getFullYear();
    var month = (date.getMonth() + 1);
    var day = date.getDate();

    if (month < 10) {
        month = '0' + month;
    }

    if (day < 10) {
        day = '0' + day;
    }

    var monthDay = String(month) + String(day);

    birth = setBirth;

    var birthdayy = birth.substr(0, 4);
    var birthdaymd = birth.substr(4, 4);

    var age = monthDay < birthdaymd ? year - birthdayy - 1 : year - birthdayy;
    return age;

}

/*
 * 년, 월을 입력받아 마지막일자를 구한다.
 * 
 */
function getMonthDateRange(year, month) {

    // month in moment is 0 based, so 9 is actually october, subtract 1 to compensate
    // array is 'year', 'month', 'day', etc
    var startDate = moment([year, month]).add(-1, "month");

    // Clone the value before .endOf()
    var endDate = moment(startDate).endOf('month');

    // make sure to call toDate() for plain JavaScript date type
    return {start: startDate, end: endDate};
}


//날자 차이 변환기 
//return List
//[0] : String 월 
//[1] : String 일
function dateDiff(_date1, _date2) {


    var rtValue = [];

    var diffDate_1 = _date1 instanceof Date ? _date1 : new Date(_date1.substring(0, 4), (_date1.substring(4, 6) - 1), _date1.substring(6, 8));
    var diffDate_2 = _date2 instanceof Date ? _date2 : new Date(_date2.substring(0, 4), (_date2.substring(4, 6) - 1), _date2.substring(6, 8));

    diffDate_1 = new Date(diffDate_1.getFullYear(), diffDate_1.getMonth() + 1, diffDate_1.getDate());
    diffDate_2 = new Date(diffDate_2.getFullYear(), diffDate_2.getMonth() + 1, diffDate_2.getDate());

    var diff = (diffDate_2.getTime() - diffDate_1.getTime());

    var nTotalSec = parseInt(diff / 1000);
    var nDay = parseInt(((nTotalSec / 60) / 60) / 24);
    var nMonth = parseInt((((nTotalSec / 60) / 60) / 24) / 30);
    var nYear = parseInt(((((nTotalSec / 60) / 60) / 24) / 30) / 24);

    var strMonth = nMonth > 0 ? String(nMonth) : '';
    var strDay = nDay > 0 ? String(nDay % 30) : '';

    rtValue[0] = strMonth;
    rtValue[1] = strDay;

    return rtValue;
}

function timeDiff(startTime, endTime) {

    var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/gi;

    var stTm = regExp.test(startTime) ? startTime.replace(regExp, '') : startTime;
    var endTm = regExp.test(endTime) ? endTime.replace(regExp, '') : endTime;

    var timeDiff = Number(stTm - endTm);
    var strTimeDiff = '';

    if (Math.sign(timeDiff) === 1) {
        strTimeDiff = String((2400 - endTime) + startTime);
    } else if (Math.sign(timeDiff) === 0) {
        strTimeDiff = '';
    } else {
        var strMath = String(Math.abs(timeDiff));
        var setHh = strMath.length == 3 ? strMath.substring(0, 1) : strMath.substring(0, 2);
        strTimeDiff = String(setHh);
    }
    return strTimeDiff;
}


// paramType  : Date
// return [0] : 개월  
// return [1] : 일
// return [2] : 시간
// return [3] : 분
// precondition 파라미터는 무조건 yyyy/mm/dd hh:mm:ss 형태로 보내주세요    
// 다음 브라우저의 프로토콜을 준수합니다. ( chrome , ie , safari )
function setDateTimeDiff(stDateTime, endDateTime) {

    //console.log(stDateTime.toString());
    //console.log(endDateTime.toString());

    var rtValue = [];
    var month_valid = false;
    var day_valid = false;
    var is_same_day = false;
    var hour_valid = false;
    var min_valid = false;
    var start_is_lastDay_valid = false;
    var end_is_lastDay_valid = false;

    var startYear = stDateTime.getFullYear();
    var startMonth = stDateTime.getMonth();
    var startDate = stDateTime.getDate();
    var startHours = stDateTime.getHours();
    var startMinute = stDateTime.getMinutes();

    var endYear = endDateTime.getFullYear();
    var endMonth = endDateTime.getMonth();
    var endDate = endDateTime.getDate();
    var endHours = endDateTime.getHours();
    var endMinute = endDateTime.getMinutes();

    is_same_day = endDate == startDate ? true : false;

    // 마지막날은 30일 이하여야함 ( 28~30 ) 카썸정책
    var dayOfLast = Number((new Date(endYear, endMonth + 1, 0)).getDate()) != 31 ? 30 : Number((new Date(endYear, endMonth + 1, 0)).getDate());
    var startDate_dayOfLast = Number((new Date(startYear, startMonth + 1, 0)).getDate());
    var endDate_dayOfLast = Number((new Date(endYear, endMonth + 1, 0)).getDate());

    // 시간 차이 계산 => 밀리세컨드
    var diffMs = (endDateTime.getTime() - stDateTime.getTime());
    // 밀리세컨드를 date 객체로
    var timeGap = new Date(0, 0, 0, 0, 0, 0, diffMs);

    var setMonth = Math.floor((diffMs / (86400000 * 30))); // 개월
    var setDay = Math.floor((diffMs % (86400000 * 30)) / (1000 * 60 * 60 * 24)); // 일수
    var setTime = Math.floor(diffMs / (1000 * 60 * 60)) % 24;
    var setMinute = Math.floor(diffMs / (1000 * 60)) % 60;

    //console.log("월 : " + setMonth +"\n일 : " + setDay + "\n 시간 : " + setTime + "\n 분 : " + setMinute + "\n 마지막일 : " + startDate_dayOfLast );

    rtValue.push(setMonth);
    rtValue.push(setDay);
    rtValue.push(setTime);
    rtValue.push(setMinute);

    return rtValue;
}

//param date
//return [0] : 개월  
//return [1] : 일
//return [2] : 시간
//return [3] : 분
//precondition 무조건 yyyy/mm/dd hh:mm:ss    
//다음 브라우저의 프로토콜을 준수합니다. ( chrom , ie , safari )
function setDateTimeDiff1(stDateTime, endDateTime) {

    var rtValue = [];
    var month_valid = false;
    var day_valid = false;
    var hour_valid = false;
    var min_valid = false;
    var start_lastDay_valid = false;
    var end_lastDay_valid = false;

    var startYear = stDateTime.getFullYear();
    var startMonth = stDateTime.getMonth();
    var startDate = stDateTime.getDate();
    var startHours = stDateTime.getHours();
    var startMinute = stDateTime.getMinutes();

    var endYear = endDateTime.getFullYear();
    var endMonth = endDateTime.getMonth();
    var endDate = endDateTime.getDate();
    var endHours = endDateTime.getHours();
    var endMinute = endDateTime.getMinutes();

    day_valid = endDate == startDate ? true : false;

    // 마지막날은 30일 이하여야함 ( 28~30 ) 카썸정책
    var dayOfLast = Number((new Date(endYear, endMonth + 1, 0)).getDate()) == 31 ? 30 : Number((new Date(endYear, endMonth + 1, 0)).getDate());
    var startDate_dayOfLast = Number((new Date(startYear, startMonth + 1, 0)).getDate());
    var endDate_dayOfLast = Number((new Date(endYear, endMonth + 1, 0)).getDate());

    start_lastDay_valid = startDate == startDate_dayOfLast ? true : false;
    end_lastDay_valid = endDate == endDate_dayOfLast ? true : false;


    // 시간 차이 계산 => 밀리세컨드
    var diffMs = endDateTime.getTime() - stDateTime.getTime();
    // 밀리세컨드를 date 객체로
    var timeGap = new Date(0, 0, 0, 0, 0, 0, diffMs);

    var setMonth = Math.floor((diffMs / (86400000 * 30))); // 개월
    var setDay = Math.floor((diffMs % (86400000 * 30)) / (1000 * 60 * 60 * 24)); // 일수
    var setTime = Math.floor(diffMs / (1000 * 60 * 60)) % 24;
    var setMinute = Math.floor(diffMs / (1000 * 60)) % 60;

    //console.log("월 : " + setMonth +"\n일 : " + setDay + "\n 시간 : " + setTime + "\n 분 : " + setMinute + "\n 마지막일 : " + startDate_dayOfLast );

    setDay = day_valid ? 0 : setDay;

    // 종료일이 시작일보다 큰 경우
    if (startDate < endDate) {
        var local_dayDiff = 0;
        if (end_lastDay_valid) {
            month_valid = true;
        }
        local_dayDiff = (endDate - startDate);
        setDay = local_dayDiff;
    }

    // 시작일이 종료일보다 큰 경우
    if (startDate > endDate) {

        if (start_lastDay_valid) {
            setDay = endDate;
        } else {
            var local_dayDiff = Number(startDate - endDate);
            setDay = (Number(dayOfLast.toString()) - local_dayDiff);
        }
    }

    // 종료 시간이 시작 시간보다 클 경우
    if (endHours > startHours) {

    }

    // 종료 시간이 시작 시간보다 작을 경우
    if (endHours < startHours) {
        hour_valid = true;
    }

    // 종료 분이 시작 분보다 클 경우
    if (endMinute > startMinute) {
    }

    // 종료분이 시작 분보다 작을 경우
    if (endMinute < startMinute) {
        min_valid = true;
    }


    if (day_valid && hour_valid || day_valid && min_valid) {
        setDay = (30 - 1);
    } else {
        if (hour_valid || min_valid) {
            setDay = (setDay - 1) == 0 ? 0 : (setDay - 1);
        }
    }

    // 시작달과 종료달의 마지막날이 31일 경우
    if (startDate_dayOfLast == endDate_dayOfLast) {

        if (day_valid && hour_valid || day_valid && min_valid) {
            month_valid = true;
        }
        if (startDate > endDate) {
            month_valid = true;
        }
    }

    if (month_valid) {
        setMonth = setMonth == 0 ? 0 : (setMonth - 1);
    }


    //console.log("월 : " + setMonth +"\n일 : " + setDay + "\n 시간 : " + setTime + "\n 분 : " + setMinute + "\n 마지막일 : " + startDate_dayOfLast );

    rtValue.push(setMonth);
    rtValue.push(setDay);
    rtValue.push(setTime);
    rtValue.push(setMinute);

    return rtValue;
}


//금액 콤마찍기
function objectConvertToPriceFormat(obj) {

    var resValue = obj.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    if (obj.length == 0) {
        resValue = 0;
    }

    return resValue;
}


/**
 *  yyyy.MM.dd 포맷으로 반환
 */
function getFormatDate(date) {
    var year = date.getFullYear();              //yyyy
    var month = (1 + date.getMonth());          //M
    month = month >= 10 ? month : '0' + month;  //month 두자리로 저장
    var day = date.getDate();                   //d
    day = day >= 10 ? day : '0' + day;          //day 두자리로 저장
    return year + '' + month + '' + day;
}


// 24hhmm 를 12hhmm
function convertTimeFormat12MIS(_time) {
    var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/gi;

    var setTime = regExp.test(_time) ? _time.replace(regExp, '') : _time;
    var hh;
    var mm;
    if (setTime.length < 4) {
        hh = setTime.substring(0, 1);
        mm = setTime.substring(1, 3);
    } else {
        hh = setTime.substring(0, 2);
        mm = setTime.substring(2, 4);
    }

    if (Number(hh) == 12) {

    } else {
        hh = Number(hh % 12);
    }

    if (mm == 60) {
        mm = 0;
    }

    hh = String(hh).length == 1 ? '0' + hh : String(hh);
    mm = String(mm).length == 1 ? '0' + mm : String(mm);

    return hh + ':' + mm;
}

//12hhmm 를 24hhmm
function convertTimeFormat24MIS(_time) {

    var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/gi;

    var setTime = regExp.test(_time) ? _time.replace(regExp, '') : _time;
    var hh;
    var mm;
    if (setTime.length < 4) {
        hh = setTime.substring(0, 1);
        mm = setTime.substring(1, 3);
    } else {
        hh = setTime.substring(0, 2);
        mm = setTime.substring(2, 4);
    }

    if (Number(hh) >= 12) {

    } else {
        hh = Number(hh) + 12;
    }

    hh = String(hh).length == 1 ? '0' + hh : String(hh);
    mm = String(mm).length == 1 ? '0' + mm : String(mm);

    return hh + ':' + mm;
}


// 모바일인지 아닌지여부
function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}


// 공통코드 ( 나중에 데이터베이스로 변경해야함 )
function getCodeValue(strCode, cmd) {

    var returnValue = '';
    // FL : 연료구분
    //
    //


    if (cmd == 'FL') {
        switch (strCode) {
            case 'FGS' :
                returnValue = '휘발류';
                break;
            case 'FDS' :
                returnValue = '경유';
                break;
            case 'FLP' :
                returnValue = 'LPG';
                break;
            case 'FHB' :
                returnValue = '하이브리드';
                break;
            case 'FEL' :
                returnValue = '전기';
                break;
            case 'FHD' :
                returnValue = '수소';
                break;
            default :
                break;
        }
    }
    return returnValue;
}


function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


function go_main() {
    location.href = "/user/main.do";
}

function go_ca_main() {
    location.href = "/rentcar/estimatList.do";
}


// todo alert (alertify 사용)
// function alert(msg) {
// 	 alertify.alert('', msg);
// }

/*
 * 건물명으로 우편번호 검색기.
 * */
function searchAddrByText(searchText) {
    var listHtml = "";

    // 검색 단어 2글자 이상부터 검색되도록
    if (searchText.length > 1) {
        // 인코딩을 수행하지 않으면 IE에서 검색이 되지 않음
        var iptValue = encodeURI(searchText);

        //url : "https://dapi.kakao.com/v2/local/search/keyword.json?category_group_code=MT1,CS2,PS3,SC4,AC5,PK6,SW8,BK9,CT1,AG2,PO3,AD5,FD6,CE7,HP8&query=" + iptValue,

        $.ajax({
            type: "GET",
            url: "https://dapi.kakao.com/v2/local/search/keyword.json?&query=" + iptValue,
            dataType: "json",
            async: false,
            headers: {
                "Authorization": "KakaoAK 3a7b91b7c9baf944de3ad1273e4c3c11"
            },
            success: function (res) {
                var items = res.documents;

                for (var i = 0; i < items.length; i++) {
                    listHtml += "<li><span  style='display:inline-block;width: 76%;cursor: pointer;' data-lat='" + items[i].y + "' data-lng='" + items[i].x + "' data-addr='" + items[i].address_name + "'>" + items[i].place_name + "</span>" +
                        "<spna class='locationButton' data-lat='" + items[i].y + "' data-lng='" + items[i].x + "' data-addr='" + items[i].address_name + "'  " + "' data-place='" + items[i].place_name + "'  " +
                        "style='margin-right: -5px;display: inline-block;float:right;box-sizing: border-box;font-size: 12px;line-height: 18px;color: #614aff;margin-top: 8px;border: 1px solid #614aff;border-radius: 3px;padding: 2px 6px;cursor: pointer;'>이 장소로 선택</spna></li>";
                }
            }
        });

        $.ajax({
            type: "GET",
            url: "https://dapi.kakao.com/v2/local/search/address.json?query=" + iptValue,
            dataType: "json",
            async: false,
            headers: {
                "Authorization": "KakaoAK 3a7b91b7c9baf944de3ad1273e4c3c11"
            },
            success: function (res) {
                var items = res.documents;

                for (var i = 0; i < items.length; i++) {
                    listHtml += "<li><span  style='display:inline-block;width: 76%;cursor: pointer;' data-lat='" + items[i].y + "' data-lng='" + items[i].x + "' data-addr='" + items[i].address.address_name + "'>" + items[i].address_name + "</span>" +
                        "<spna class='locationButton' data-lat='" + items[i].y + "' data-lng='" + items[i].x + "' data-addr='" + items[i].address.address_name + "'  " + "' data-place='" + items[i].address.address_name + "'  " +
                        "style='margin-right: -5px;display: inline-block;float:right;box-sizing: border-box;font-size: 12px;line-height: 18px;color: #614aff;margin-top: 8px;border: 1px solid #614aff;border-radius: 3px;padding: 2px 6px;cursor: pointer;'>이 장소로 선택</spna></li>";
                }
            }
        });

        $('.searchListWrap > ul').html(listHtml);
    }
}

function fn_limit_age_format(_ageLimit) {

    var ageLimit = _ageLimit;
    var validation = ageLimit.indexOf(',');
    if (validation > -1) {
        ageLimit = '월대여 만 ' + ageLimit.split(',')[0] + '세 일대여 만 ' + ageLimit.split(',')[1] + '세 이상 대여 가능 차량';
        return ageLimit;
    } else {
        return '만 ' + ageLimit + '세 이상 대여 가능 차량';
    }

}

function validateEmail(elementValue) {
    var retValue = false;
    var regExp = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;

    if (!regExp.test(elementValue)) {
        retValue = false;
    } else {
        retValue = true;
    }

    return retValue;
}


//자바스크립트 null check
function nullCheck(value) {
    var resData = '';
    if (value == undefined || value == null || value == 'null') {
        return resData;
    } else {
        return value;
    }
}

