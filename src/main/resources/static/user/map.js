$(document).ready(function () {
    initSetting();

    $(".btn-in").click(function(){ //postList 보이게
        $('#post-column').show();
//        $('#post-column').animate({width: '100%'}, 500)
        $('.btn-in').css("display","none");
        $('.btn-out').css("display","block");
        $('.slide-btn').css("left","16%");
        $('#memorySave').removeClass('col-md-12').addClass('col-md-10');
    });
    $(".btn-out").click(function(){ //postList 안보이게
        $('#post-column').hide();
//        $('#post-column').animate({width: '10%'}, 500)
        $('.slide-btn').animate({left: '1%'}, 500);
        $('.btn-in').css("display","block");
        $('.btn-out').css("display","none");
        $('.slide-btn').css("left","0.5%");
        $('#memorySave').removeClass('col-md-10').addClass('col-md-12');
    });

});
let imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";  //커서 이미지 주소
var map; // 전역 변수로 선언

window.onload = function() {
    var postElement = document.querySelector('.포스트');
    var paginationElement = document.querySelector('.pagination');
    var mapColumnElement = document.querySelector('#map-column');

    window.addEventListener('scroll', function() {
        adjustPaginationPosition();
    });

    adjustPaginationPosition();

    function adjustPaginationPosition() {
        var viewportHeight = window.innerHeight;
        var postHeight = postElement.clientHeight;
        var mapColumnHeight = mapColumnElement.clientHeight; // 지도 엘리먼트의 높이
        var paginationHeight = paginationElement.clientHeight;
        var scrollHeight = postHeight - viewportHeight;

        if (scrollHeight > 0) {
            var postBottom = postElement.offsetTop + postHeight;
            var newPaginationTop = Math.min(viewportHeight - paginationHeight, postBottom);

            paginationElement.style.position = 'absolute';
            // newPaginationTop = newPaginationTop + 60
            paginationElement.style.top = newPaginationTop + 'px';
            paginationElement.style.bottom = 'auto';
            // 지도 엘리먼트의 높이를 지정하여 고정되도록 함
            mapColumnElement.style.height = (viewportHeight - paginationHeight) + 'px';


        } else {
            paginationElement.style.position = 'fixed';
            paginationElement.style.top = 'auto';
            paginationElement.style.bottom = '0';
            paginationElement.style.left = 'auto';
            // 스크롤이 없을 때는 지도 엘리먼트가 전체 화면을 차지하도록 높이를 조절
            mapColumnElement.style.height = '100vh';

        }
    }
};

function initSetting(){
    let dataList = [];
    let title = document.querySelectorAll('.title');
    title.forEach(function(title){
        // data-value의 속성들을 가져올 수 있음
        let placeId = title.getAttribute('data-placeId');
        let placeName = title.getAttribute('data-placeName');
        let address = title.getAttribute('data-address');
        let roadAddress = title.getAttribute('data-roadAddress');
        let longitude = title.getAttribute('data-longitude');
        let latitude = title.getAttribute('data-latitude');
        let filePullPath = title.getAttribute('data-filePath');

        console.log('roadAddress ===>'+roadAddress)

        if (placeId && longitude && latitude) {
            let data = {
                placeId   : placeId,
                placeName : placeName,
                filePullPath : filePullPath,
                address   : address,
                roadAddress : roadAddress,
                longitude : longitude,
                latitude  : latitude,
                latlng    : new kakao.maps.LatLng(latitude, longitude)
            };
            dataList.push(data);
        }
    })
    console.log('dataList ==>' + dataList);

    if (dataList.length === 0) {
        document.querySelector('.no_place').style.display = 'block';
    } else {
        document.querySelector('.no_place').style.display = 'none'; // dataList가 비어있지 않을 때 no_place 클래스를 가진 div를 숨김
    }

    var container = document.getElementById('map');
    var paginationElement = document.querySelector('.pagination');
    var viewportHeight = window.innerHeight;
    var paginationHeight = paginationElement.clientHeight;
    container.style.height = (viewportHeight - paginationHeight) + 'px';

    var options = {
        center: new kakao.maps.LatLng(dataList[0].latitude, dataList[0].longitude),
        level: 3
    };

    //지도 열기
    map = new kakao.maps.Map(container, options);

    // 커스텀 오버레이 생성
    for(let i=0 ; i<dataList.length ; i++){

        //지도에 마커 표시
        var imageSize = new kakao.maps.Size(24, 35);
        var fileImgSrc = dataList[i].filePullPath;
        console.log('fileImgSrc==>'+fileImgSrc)
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
        var marker = new kakao.maps.Marker({
           map: map, // 마커를 표시할 지도
           position: dataList[i].latlng, // 마커를 표시할 위치
           image : markerImage
       });

        var data = dataList[i];
        settingMapOverRay(data, map, marker, fileImgSrc)
    }
}

function settingMapOverRay(data, map, marker, fileImgSrc){
    var content = document.createElement('div');
    content.classList.add('wrap');

    var infoDiv = document.createElement('div');
    infoDiv.classList.add('info');

    var titleDiv = document.createElement('div');
    titleDiv.classList.add('title');
    titleDiv.classList.add('locationTitle');
    titleDiv.textContent = data.placeName;
    titleDiv.style.cssText = "background-color: #eee !important; color: #333;";

    var closeDiv = document.createElement('div');
    closeDiv.classList.add('close');
    closeDiv.setAttribute('title', '닫기');
//    closeDiv.textContent = 'X';
    closeDiv.onclick = function() {
        overlay.setMap(null);
    };
    titleDiv.appendChild(closeDiv);

    var bodyDiv = document.createElement('div');
    bodyDiv.classList.add('body');

    var imgDiv = document.createElement('div');
    imgDiv.classList.add('img');
    var img = document.createElement('img');
    img.setAttribute('src', fileImgSrc);  //marker 클릭시 나오는 이미지
    img.setAttribute('width', '73');
    img.setAttribute('height', '70');
    imgDiv.appendChild(img);

    var descDiv = document.createElement('div');
    descDiv.classList.add('desc');

    if(data.address != null){
        var doroDiv = document.createElement('div');
        doroDiv.classList.add('jibun', 'ellipsis');
        doroDiv.textContent = "도로명 : " + data.address;
        descDiv.appendChild(doroDiv);
    }
    if(data.roadAddress != null){
        var breakDiv = document.createElement('div');
        doroDiv.style.marginBottom = '5px';
        descDiv.appendChild(breakDiv);

        var jibunDiv = document.createElement('div');
        jibunDiv.classList.add('jibun', 'ellipsis');
        jibunDiv.textContent = "지번  : " + data.roadAddress;
        doroDiv.style.marginBottom = '5px';
        descDiv.appendChild(jibunDiv);
    }


//    var linkDiv = document.createElement('div');
//    var linkA = document.createElement('a');
//    linkA.setAttribute('href', 'https://www.kakaocorp.com/main');
//    linkA.setAttribute('target', '_blank');
//    linkA.classList.add('link');
//    linkA.textContent = '홈페이지';
//    linkDiv.appendChild(linkA);

//    descDiv.appendChild(linkDiv);

    bodyDiv.appendChild(imgDiv);
    bodyDiv.appendChild(descDiv);

    infoDiv.appendChild(titleDiv);
    infoDiv.appendChild(bodyDiv);

    content.appendChild(infoDiv);

    let overlay = new kakao.maps.CustomOverlay({
            content: content,
//            map: map,     활성화시 map 처음로드할 때 커스텀 오버레이 발생
            position: marker.getPosition()
    });

    //마커를 클릭했을 떄 커스텀 오버레이 표시
    kakao.maps.event.addListener(marker, 'click', function(){
        overlay.setMap(map);
    })
}

function mapSearch(locationId){

    let data = {
        "locationId" : locationId
    }

    $.ajax({
        url:"/api/server/selectLocationInfo.do",
        type:"post",
        contentType:"application/json",
        data: JSON.stringify(data),
        success:function(response){
            var html = "";

            // 이동할 위도 경도 위치를 생성합니다
             var moveLatLon = new kakao.maps.LatLng(response.latitude, response.longitude);

            // 지도 중심을 부드럽게 이동시킵니다
            // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
             map.panTo(moveLatLon);
        }
    })
}

$(".title").click(function() {      /* 상세페이지로 이동 */
    var postId = $(this).attr("data-postId");
    console.log("Clicked post ID: " + postId);

    var newUrl = "/api/server/memoryEdit.do?postId=" + postId;
    window.location.href = newUrl
});

function deletePost(button) {
    if (confirm("게시물을 삭제하시겠습니까?")) {
        var postId = $(button).attr("data-postId");
        console.log("Deleting post with ID: " + postId);

        $.ajax({
            url: "/api/server/deletePost.do",
            type: "post",
            data: { postId: postId },
            success: function(response) {
                console.log(response.code)
                if(response.code == 'R000'){
                    alert("삭제 성공했습니다.");
                    window.location.href = "/api/server/map.do"; // 추억저장소 페이지로 redirect
                }else{
                    alert("삭제에 실패했습니다 네트워크를 확인해주세요");
                }

            },
            error: function(xhr, status, error) {
                console.error("Error deleting post: " + error);
            }
        });
    }
}
