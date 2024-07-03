$(document).ready(function () {
    $('#joinForm').submit(function (event) { // 폼을 서브밋하기 전에 입력값을 확인하는 함수 호출

        let check = $("#checkAutoEmail").attr("disabled")

         if(!check || $("#password").val() === ""){
            if(!check)                              alert('이메일 인증을 완료해주세요!');
            else if($("#password").val() === "")    alert('비밀번호를 작성해주세요!');

             event.preventDefault();
         }
    });
});

// 이메일 인증
$("#certificationEmail").on("click",function(){

    if($("#email").val() != ""){
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
            }
        })
    }else{
        alert('이메일 입력해주세요');
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