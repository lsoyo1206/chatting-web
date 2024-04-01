// 회원가입
$("#joinBtn").on("click",function(){

    let user = {
        userName : $("#userName").val(),
        email : $("#email").val(),
        password : $("#password").val()
    }

    console.log("data = " + user)

    $.ajax({
        url:"/register",
        type:"post",
        contentType:"application/json",
        data: JSON.stringify(user),
        success:function(response){
            console.log("111111111111111")
        }
    })
})

// 이메일 인증
$("#certificationEmail").on("click",function(){
    let data = {};
    data.email = $("#email").val();

    $.ajax({
        url:"/mailSend",
        type:"post",
        contentType:"application/json",
        data: JSON.stringify(data),
        success:function(response){
            console.log("222222222222")
            console.log(response);
            console.log("222222222222")


        }
    })
})

// 이메일 인증번호 확인
$("#checkAutoEmail").on("click",function(){
    let data = {};
    data.email = $("#email").val();
    data.authNumber = $("#autoEmail").val();

    $.ajax({
        url:"/AuthCheck",
        type:"post",
        contentType:"application/json",
        data: JSON.stringify(data),
        success:function(response){
            console.log("AuthCheck")
            console.log(response);

        }
    })
})
