//로그인 여부 확인
$(document).ready(function() {
    // isLogin 함수를 한 번 호출한 후, 로그인 여부를 확인하지 않도록 함
});

function searchFunction() {  // 검색 기능
    // 검색어를 가져오는 코드
    let data = {
        "searchQuery" : $("#searchInput").val()
    }

    console.log("검색어: " + data.searchQuery);

    $.ajax({
        url:"/api/server/foodSearch",
        type:"get",
        contentType:"application/json",
        data: data,
        success:function(response){
            var html = "";
            console.log("response ===> "+response)

            // for(let i=0 ; i<response.items.length ; i++){
            //     html += "<div>" + item[i].title + "</div>";
            //     html += "<div>" + item[i].address + "</div>";
            // }

            // 결과를 화면에 출력
            // $("#resultContainer").html(html);
        },
        error:function(request,status,error){
            alert('인증 실패하였습니다. 다시 입력해주세요.')
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
            window.location.href = "/"; // 홈페이지로 리다이렉트
        }
    });
}
function goToHomePage() {
    window.location.href = "/";
}