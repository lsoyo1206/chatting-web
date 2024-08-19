package com.example.chattingweb.api.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.chattingweb.api.dto.PhotoDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Component
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


    public String uploadFile(MultipartFile file, String month, String uuid) throws IOException {     // 파일 업로드 메서드
        // windows
//        String nasPath = "C:" + File.separator + "nas" + File.separator + month + File.separator + uuid;

        // linux
        String nasPath = "/usr/local/toyproject/nas/file_manage" + File.separator + month + File.separator + uuid;

        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(nasPath, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }


    public Map<String,Object> createDirectory() throws IOException {    //폴더 생성
        HashMap<String,Object> result = new HashMap<>();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String nowTime1 = dateFormat.format(date);

        //1.디렉토리 생성 ex. 2023-11

        // windows
//        String nasPath = "C:" + File.separator + "nas" + File.separator;

        // linux
        String nasPath = "/usr/local/toyproject/nas/file_manage" + File.separator;
//                "/usr/local/toyproject/nas/file_manage" + File.separator;
        //LOGGER.info("nasPath = {}", nasPath);

        Path path = Paths.get(nasPath+nowTime1);

        if(!Files.exists(path)) 			Files.createDirectory(path);

        //2. 파일 uuid 디렉토리 생성
        String uuid = UUID.randomUUID().toString();

        // windows
//        String filePath = "C:" + File.separator + "nas" + File.separator + nowTime1 + File.separator + uuid;

        // linux
        String filePath = "/usr/local/toyproject/nas/file_manage" + File.separator + nowTime1 + File.separator + uuid;
        //LOGGER.info("nasFilePath = {}", filePath);

        Path path1 = Paths.get(filePath);
        Files.createDirectory(path1);

        result.put("month", nowTime1);
        result.put("uuid", uuid);

        return result;
    }

    public static void deletePhoto(List<PhotoDto> photoList) throws IOException {
        try {
            for (PhotoDto photo : photoList) {
                // 데이터베이스에서 파일 경로를 가져옴
                Path filePath = Paths.get("/usr/local/toyproject/nas/file_manage/", photo.getFilePath() + File.separator + photo.getFileName() + photo.getFileExtension());

                // 파일 삭제
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    logger.info("Deleted file: {}", filePath);
                } else {
                    logger.warn("File not found: {}", filePath);
                }

                // 폴더 삭제
                Path parentDir = filePath.getParent();
                if (parentDir != null) {
                    try (Stream<Path> files = Files.list(parentDir)) {
                        if (files.count() == 0) {
                            Files.delete(parentDir);
                            logger.info("Deleted directory: {}", parentDir);
                        }
                    }
                }
            }
        } catch (NoSuchFileException e) {
            logger.error("File not found: {}", e.getMessage());
        } catch (AccessDeniedException e) {
            logger.error("Access denied: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("I/O error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        }
    }

}
