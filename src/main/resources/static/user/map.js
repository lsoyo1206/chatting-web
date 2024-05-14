$(document).ready(function () {
    initSetting();
});
let imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";  //커서 이미지 주소

function initSetting(){
    let dataList = [];
    let title = document.querySelectorAll('.title');
    title.forEach(function(title){
        // data-value의 속성들을 가져올 수 있음
        let placeId = title.getAttribute('data-placeId');
        let placeName = title.getAttribute('data-placeName');
        let address = title.getAttribute('data-address');
        let longitude = title.getAttribute('data-longitude');
        let latitude = title.getAttribute('data-latitude');

        if (placeId && longitude && latitude) {
            let data = {
                placeId   : placeId,
                placeName : placeName,
                address   : address,
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
    var options = {
        center: new kakao.maps.LatLng(dataList[0].latitude, dataList[0].longitude),
        level: 3
    };
    //지도 열기
    var map = new kakao.maps.Map(container, options);

    // 커스텀 오버레이 생성
    for(let i=0 ; i<dataList.length ; i++){

        //지도에 마커 표시
        var imageSize = new kakao.maps.Size(24, 35);
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
        var marker = new kakao.maps.Marker({
           map: map, // 마커를 표시할 지도
           position: dataList[i].latlng, // 마커를 표시할 위치
           image : markerImage
       });

        var data = dataList[i];
        settingMapOverRay(data, map, marker)
    }
}

function settingMapOverRay(data, map, marker){
    var content = document.createElement('div');
    content.classList.add('wrap');

    var infoDiv = document.createElement('div');
    infoDiv.classList.add('info');

    var titleDiv = document.createElement('div');
    titleDiv.classList.add('title');
    titleDiv.textContent = data.placeName;

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
    img.setAttribute('src', 'https://www.highziumstudio.com/wp-content/uploads/2022/11/03_1345-copy-1-scaled-1-486x657.jpg');
    img.setAttribute('width', '73');
    img.setAttribute('height', '70');
    imgDiv.appendChild(img);

    var descDiv = document.createElement('div');
    descDiv.classList.add('desc');

    var jibunDiv = document.createElement('div');
    jibunDiv.classList.add('jibun', 'ellipsis');
    jibunDiv.textContent = data.address;

    var linkDiv = document.createElement('div');
    var linkA = document.createElement('a');
    linkA.setAttribute('href', 'https://www.kakaocorp.com/main');
    linkA.setAttribute('target', '_blank');
    linkA.classList.add('link');
    linkA.textContent = '홈페이지';
    linkDiv.appendChild(linkA);

    descDiv.appendChild(jibunDiv);
    descDiv.appendChild(linkDiv);

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

function mapSearch(placeId){

    let data = {
        "placeId" : placeId
    }
    $.ajax({
        url:"/api/server/selectPlaceInfo.do",
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
        }
    })
    // 클릭된 요소에서 데이터 가져오기
    // let data = {
    //     placeId   : placeId,
    //     longitude : longitude,
    //     latitude  : latitude,
    //     latlng    : new kakao.maps.LatLng(latitude, longitude)
    // };

    // 이동할 위도 경도 위치를 생성합니다
    // var moveLatLon = new kakao.maps.LatLng(latitude, longitude);

    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    // map.panTo(moveLatLon);

}