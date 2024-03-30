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

})


