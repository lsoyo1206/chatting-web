//로그인 여부 확인
$(document).ready(function() {
    // isLogin 함수를 한 번 호출한 후, 로그인 여부를 확인하지 않도록 함
});

function isLogin(){
    $.ajax({
        url:"/isLogin",
        type:"get",
        dataType: "json",
        success:function(response){
            console.log("response ->" + response);
            if(response.isLogin === true){
                $(".notLogin").hide();
                $(".login").show();

                $("#userName").html("<span style='font-weight: bold; text-align: center; display: block;'>" + response.userName + " 님</span>");
            }
        }
    })
}

function logout(){
    $.ajax({
        url: "/logout",
        type: "POST",
        success: function(response) {
            alert("로그아웃 되었습니다.");
            alert(response);
            window.location.href = "/home"; // 홈페이지로 리다이렉트
        }
    });
}