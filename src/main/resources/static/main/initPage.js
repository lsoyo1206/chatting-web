$(document).ready(function() {
    $('#content').summernote({
        toolbar: [
        		    ['fontname', ['fontname']],
        		    ['fontsize', ['fontsize']],
        		    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
        		    ['color', ['forecolor','color']],
        		    ['table', ['table']],
        		    ['para', ['ul', 'ol', 'paragraph']],
        		    ['height', ['height']],
        		    ['view', ['fullscreen', 'help']]
          ],
          height: 300,                 // 에디터 높이
          minHeight: null,             // 최소 높이
          maxHeight: null,             // 최대 높이
          focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
          lang: "ko-KR",					// 한글 설정
          placeholder: '최대 1000자까지 쓸 수 있습니다'	//placeholder 설정
    });
});

function searchFunction() {  // 검색 기능
    // 검색어를 가져오는 코드
    let data = {
        "searchQuery" : $("#searchInput").val()
    }

    console.log("검색어: " + data.searchQuery);

    $.ajax({
        url:"/api/server/initSettingMap",
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
            window.location.href = "/"; // 홈페이지로 리다이렉트
        }
    });
}
function goToHomePage() {
    window.location.href = "/";
}