//로그인 여부 확인
$(function() {
    isLogin();
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
            }
        }
    })
}