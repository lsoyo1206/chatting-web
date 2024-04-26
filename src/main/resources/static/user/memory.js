 $(document).ready(function () {


 });
var files = [];
document.getElementById('photos').addEventListener('change', function(event) {
    files = [];
    var input = event.target;
    var label = input.nextElementSibling;
    var fileContainer = document.getElementById('thumbnail-container');
    fileContainer.innerHTML = ''; // 컨테이너 초기화

    if (input.files && input.files.length > 0) {
        for (var i = 0; i < input.files.length; i++) {
            var reader = new FileReader();
            reader.onload = function(e) {
                var thumbnail = document.createElement('img');
                thumbnail.classList.add('thumbnail');
                thumbnail.setAttribute('src', e.target.result);
                fileContainer.appendChild(thumbnail);
            }
            reader.readAsDataURL(input.files[i]);
        }
    }
    // 파일 선택 시 선택한 파일 이름을 표시
    if (input.files.length > 1) {
        label.innerHTML = input.files.length + '개의 파일 선택';
    } else {
        label.innerHTML = input.files[0].name;
    }

    //파일 배열에 미리 넣어놓기
    for (let i = 0; i < input.files.length; i++) {
        files.push(input.files[i].name);
        console.log(input.files[i].name)
    }
});

$('#locationRegistered').click(function(){
    if($(this).is(':checked')){
        $("#mapSearch").show();
        $(".placeName").show();
    }else{
        $("#mapSearch").hide();
        $(".placeName").hide();
    }
});

function postInsert() {  // 검색 기능
    var isSpaceChecked = $("#locationRegistered").prop("checked");
    var placeName = "";
    var address= "";
    let spaceDto = {};
    let photoUpload = {};
    if (files.length !== 0 || files.length !== undefined) {
        for(let i=0 ; i<files.length ; i++){
            photoUpload['fileName' + (i + 1)] = files[i];
        }
    }

    if($("#placeName").val() !== "" && isSpaceChecked === true){
        var placeInfoArray = $("#placeName").val().split('|');
        placeName = placeInfoArray[0].trim(); // 가게 이름
        address = placeInfoArray[1].trim(); // 주소

        spaceDto.placeName = placeName;
        spaceDto.address = address;
        spaceDto.latitude = $("#latitude").val();    //위도
        spaceDto.longitude = $("#longitude").val();   //경도
    }

    // 검색어를 가져오는 코드
    let postDto = {
        "title" : $("#title").val(),
        "content" : $("#content").val(),
        "visitedFriends" : $("#visitedFriends").val(),
        "locationRegistered" : isSpaceChecked
    }

    let data ={
        "postDto" : postDto,
        "spaceDto" : spaceDto,
        "photoUpload" : photoUpload
    }

    $.ajax({
        url:"/api/server/insertPost",
        type:"get",
        contentType:"application/json",
        data: data,
        success:function(response){
            alert("추억이 또 한 건 쌓였습니다 !@!")
            window.location.href = "/";
        },
        error:function(request,status,error){
            alert('추억 저장에 실패했습니다 ㅜ.ㅜ 다시 시도해주세요!')
        }
    })
}



// 마커를 담을 배열입니다
var markers = [];

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

// 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
var infowindow = new kakao.maps.InfoWindow({zIndex:1});

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {

    var keyword = document.getElementById('keyword').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch( keyword, placesSearchCB);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {

        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);

        // 페이지 번호를 표출합니다
        displayPagination(pagination);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

        alert('검색 결과가 존재하지 않습니다.');
        return;

    } else if (status === kakao.maps.services.Status.ERROR) {

        alert('검색 결과 중 오류가 발생했습니다.');
        return;

    }
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

    var listEl = document.getElementById('placesList'),
        menuEl = document.getElementById('menu_wrap'),
        fragment = document.createDocumentFragment(),
        bounds = new kakao.maps.LatLngBounds(),
        listStr = '';

    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();

    for ( var i=0; i<places.length; i++ ) {

        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i),
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        (function(marker, title) {
            kakao.maps.event.addListener(marker, 'mouseover', function() {
                displayInfowindow(marker, title);
            });

            kakao.maps.event.addListener(marker, 'mouseout', function() {
                infowindow.close();
            });

            itemEl.onmouseover =  function () {
                displayInfowindow(marker, title);
            };

            itemEl.onmouseout =  function () {
                infowindow.close();
            };
        })(marker, places[i].place_name);

        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    var el = document.createElement('li'),
        itemStr = '<span id="markerbg marker_' + (index+1) + '" class="markerbg marker_' + (index+1) + '"></span>' +
            '<div class="info">' +
            '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
            '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>';
    }

    itemStr += '  <span class="tel">' + places.phone  + '</span>' +
        '</div>';

    el.innerHTML = itemStr;
    el.className = 'item';

    //추가
    el.addEventListener('click', function() {
        // 해당 항목의 정보 가져오기
        var placeName = places.place_name;
        var roadAddress = places.road_address_name || '';
        var address = places.address_name || '';
        var phone = places.phone || '';

        $("#placeName").val(placeName+' | '+places.road_address_name);

        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(address, (result, status) => {
            if (status === kakao.maps.services.Status.OK) {
                console.log('위도 : ' + result[0].y);
                console.log('경도 : ' + result[0].x);

                $("#latitude").val(result[0].y);
                $("#longitude").val(result[0].x);
            }
        });

        // 가져온 정보 콘솔에 출력
        console.log('가게 이름:', placeName);
        console.log('도로명 주소:', roadAddress);
        console.log('지번 주소:', address);
        console.log('전화번호:', phone);
    });

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

    infowindow.setContent(content);
    infowindow.open(map, marker);
}

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}

// Latitude : 위도 , longitude: 경도
function findLatitudeLongitude(address){
    return new Promise((resolve, reject) => {
        // 위도 및 경도 좌푯값 구하기
        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(address, (result, status) => {
            if (status === kakao.maps.services.Status.OK) {
                console.log('위도 : ' + result[0].y);
                console.log('경도 : ' + result[0].x);

                $("#latitude").val(result[0].y);
                $("#longitude").val(result[0].x);
            }
        });
    });
}
