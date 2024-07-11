package com.example.chattingweb.api.common;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class ApiExplorer {
    public String carMaintenanceInsert() throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_public_auto_maintenance_company_api"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=LK9pJtUYRuvbAUqM%2FQ9rQCpFkhycz4W7v8J%2FtWHWqbuCln%2BthwSOr7vbN4V3aJxQsnjmm5Td3YhChRJL6txzcA%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*XML/JSON 여부*/
        urlBuilder.append("&" + URLEncoder.encode("inspofcNm","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*자동차정비업체명*/
        urlBuilder.append("&" + URLEncoder.encode("inspofcType","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*자동차정비업체종류*/
        urlBuilder.append("&" + URLEncoder.encode("rdnmadr","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*소재지도로명주소*/
        urlBuilder.append("&" + URLEncoder.encode("lnmadr","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*소재지지번주소*/
        urlBuilder.append("&" + URLEncoder.encode("latitude","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*위도*/
        urlBuilder.append("&" + URLEncoder.encode("longitude","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*경도*/
        urlBuilder.append("&" + URLEncoder.encode("bizrnoDate","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*사업등록일자*/
        urlBuilder.append("&" + URLEncoder.encode("ar","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*면적*/
        urlBuilder.append("&" + URLEncoder.encode("bsnSttus","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*영업상태*/
        urlBuilder.append("&" + URLEncoder.encode("clsbizDate","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*폐업일자*/
        urlBuilder.append("&" + URLEncoder.encode("sssBeginDate","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*휴업시작일자*/
        urlBuilder.append("&" + URLEncoder.encode("sssEndDate","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*휴업종료일자*/
        urlBuilder.append("&" + URLEncoder.encode("operOpenHm","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*운영시작시각*/
        urlBuilder.append("&" + URLEncoder.encode("operCloseHm","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*운영종료시각*/
        urlBuilder.append("&" + URLEncoder.encode("phoneNumber","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*전화번호*/
        urlBuilder.append("&" + URLEncoder.encode("institutionNm","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*관리기관명*/
        urlBuilder.append("&" + URLEncoder.encode("institutionPhoneNumber","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*관리기관전화번호*/
        urlBuilder.append("&" + URLEncoder.encode("referenceDate","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*데이터기준일자*/
        urlBuilder.append("&" + URLEncoder.encode("instt_code","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*제공기관코드*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        return "result";
    }

}
