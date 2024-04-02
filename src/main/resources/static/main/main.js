// 회원가입
$("#joinBtn").on("click",function(){
    let regexResult = regex();
    let check = $("#checkAutoEmail").attr("disabled")

    if(!check){
        alert('이메일 인증을 완료해주세요!');
        return;
    }

    if(regexResult !== false){
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
                alert('회원가입 완료하였습니다.')
                location.href = "/home";
            }
        })
    }
})

// 이메일 인증
$("#certificationEmail").on("click",function(){
    let regexResult = regex();

    if(regexResult !== false){
        let data = {};
        data.email = $("#email").val();

        $.ajax({
            url:"/mailSend",
            type:"post",
            contentType:"application/json",
            data: JSON.stringify(data),
            success:function(response){

                if(response === "Fail"){
                    alert("이미 사용 중인 이메일 입니다.")
                    return;
                }
                alert("인증번호를 이메일로 전송하였습니다.")
                console.log(response);

            }
        })
    }

})

// 이메일 인증번호 확인
$("#checkAutoEmail").on("click",function(){
    let regexResult = regex();
    if(regexResult !== false){
        let data = {};
        data.email = $("#email").val();
        data.authNumber = $("#autoEmail").val();

        $.ajax({
            url:"/AuthCheck",
            type:"post",
            contentType:"application/json",
            data: JSON.stringify(data),
            success:function(response){
                console.log("response -->",response)

                $("#email").attr("readonly",true);
                $("#checkAutoEmail").attr("disabled",true);
                $("#certificationEmail").attr("disabled",true);
                $("#autoEmail").attr("readonly",true);

                alert("이메일 인증 완료되었습니다.")
            },
            error:function(request,status,error){
                alert('인증 실패하였습니다. 다시 입력해주세요.')
            }
        })
    }
})

function regex(){
    let regexResult = true;
    let reason = "";

    if($("#userName").val() === ""){
        reason = "이름을 작성해주세요";
        $("#userName").focus();
    }else if($("#email").val() === ""){
        reason = "이메일을 작성해주세요";
        $("#email").focus();
    }else if($("#password").val() === ""){
        reason = "비밀번호를 작성해주세요";
        $("#password").focus();
    }

    if(reason !== ""){
        alert(reason);
        regexResult = false
        return regexResult;
    }
}