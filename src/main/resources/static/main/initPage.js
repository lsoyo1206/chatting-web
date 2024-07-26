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

$("#searchInput").on('input', searchFunction);

// 검색 입력 필드 클릭 시 입력 그룹 보이기
$("#searchInput").on('focus', function() {
    $("#suggestion_box").show();
});
// 문서의 빈 공간 클릭 시 suggestion_box 숨기기
$(document).on('mousedown', function(event) {
    var $suggestionBox = $("#suggestion_box");

    // 클릭된 위치가 #suggestion_box 내부가 아닌 경우에만 숨기기
    if (!$(event.target).closest($suggestionBox).length) {
        $suggestionBox.hide();
    }
//    $("#suggestion_box").hide();
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
        data: $.param(data),
        success:function(response){
           console.log("response ===> ", response);
           var result = JSON.parse(response);

           var $recommendBox = $("#suggestion_box");
           $recommendBox.removeClass('invisible');
           $recommendBox.empty();


           var html = "";
           // 응답 데이터 구조 확인
           if (result && Array.isArray(result.items) && result.items.length > 0) {

               // 검색 결과를 반복문을 통해 화면에 출력
               for(let i = 0; i < result.items.length; i++){
                   html += "<div class='search-result-item'>";
                   html += "<h5>" + result.items[i].title + "</h5>";
                   html += "<p>" + result.items[i].address + "</p>";
                   if (result.items[i].link != null && result.items[i].link != "") {
                           html += "<a href='" + result.items[i].link + "' target='_blank'>"+ result.items[i].title +" 바로가기</a>";
                       }
                   html += "</div>";
               }
           }else {
                //검색 결과가 없을 때 처리
                if($("#searchInput").val().length > 0){
                    html += "<div class='search-result-item'>";
                    html += "<h5>검색결과가 없습니다.</h5>";
                    html += "</div>";
                }
           }
           // 결과를 화면에 출력
           $recommendBox.append(html);


        },
        error:function(request,status,error){
//            alert('인증 실패하였습니다. 다시 입력해주세요.')
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