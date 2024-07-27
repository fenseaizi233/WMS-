package com.fenseaizi.wmsdemo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.fenseaizi.wmsdemo.common.GetPath;
import com.fenseaizi.wmsdemo.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private GetPath getPath;
    // 注入 GetPath 对象，用于获取项目文件夹路径

    // 获取文件扩展名
    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index > 0 && index < fileName.length() - 1) {
            return fileName.substring(index + 1);
        }
        return "";
    }

    @PostMapping("/image")
    public Result imageUpload(String fileName, @RequestParam MultipartFile file) {
        if (file == null) {
            return Result.fail("文件为空，请上传文件");
        }
        synchronized (UploadController.class) {
            // 获取当前时间戳作为文件名前缀
            String flag = System.currentTimeMillis() + "";
            String extension = getFileExtension(fileName);  // 获取文件扩展名
            try {
                // 如果没有 image 文件夹，会在项目根目录下创建一个 image 文件夹
                if (!FileUtil.isDirectory(getPath.getProjectAbsolutePath() + "/image/")) {
                    FileUtil.mkdir(getPath.getProjectAbsolutePath() + "/image/");
                }
                // 文件存储形式：时间戳-文件名.扩展名
                FileUtil.writeBytes(
                        file.getBytes(),
                        getPath.getProjectAbsolutePath() + "/image/" + flag + "." + extension);
                System.out.println(fileName + "--上传成功");
                return Result.suc(flag);  // 返回上传成功信息和文件名
            } catch (Exception e) {
                System.err.println(fileName + "--文件上传失败");
                return Result.fail("文件上传失败");
            }
        }
    }

    @PostMapping("/avatar")
    public Result avatarUpload(String fileName, @RequestParam MultipartFile file) {
        if (file == null) {
            return Result.fail("文件为空，请上传文件");
        }
        synchronized (UploadController.class) {
            // 获取当前时间戳作为文件名前缀
            String flag = System.currentTimeMillis() + "";
            String extension = getFileExtension(fileName);  // 获取文件扩展名
            try {
                // 如果没有 image 文件夹，会在项目根目录下创建一个 image 文件夹
                if (!FileUtil.isDirectory(getPath.getProjectAbsolutePath() + "/avatar/")) {
                    FileUtil.mkdir(getPath.getProjectAbsolutePath() + "/avatar/");
                }
                // 文件存储形式：时间戳-文件名.扩展名
                FileUtil.writeBytes(
                        file.getBytes(),
                        getPath.getProjectAbsolutePath() + "/avatar/" + flag + "." + extension);
                System.out.println(fileName + "--上传成功");
                return Result.suc(flag);  // 返回上传成功信息和文件名
            } catch (Exception e) {
                System.err.println(fileName + "--文件上传失败");
                return Result.fail("文件上传失败");
            }
        }
    }

    @GetMapping("/image/{flag}")
    public void imagePath(@PathVariable String flag, HttpServletResponse response)
            throws IOException {
        String filePath = getPath.getProjectAbsolutePath() + "/image/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(filePath);
        // 筛选出符合特定标识符 flag 的文件名
        String targetFileName = fileNames.stream()
                .filter(name -> name.contains(flag))
                .findAny()
                .orElse("");
        try {
            if (StrUtil.isNotEmpty(targetFileName)) {
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(targetFileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + targetFileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                System.out.println("文件下载成功");
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    @GetMapping("/avatar/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response)
            throws IOException {
        String filePath = getPath.getProjectAbsolutePath() + "/avatar/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(filePath);
        // 筛选出符合特定标识符 flag 的文件名
        String targetFileName = fileNames.stream()
                .filter(name -> name.contains(flag))
                .findAny()
                .orElse("");
        try {
            if (StrUtil.isNotEmpty(targetFileName)) {
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(targetFileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + targetFileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                System.out.println("文件下载成功");
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    @GetMapping("/file/{flag}")
    public void filePath(@PathVariable String flag, HttpServletResponse response)
            throws IOException {
        String filePath = getPath.getProjectAbsolutePath() + "/file/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(filePath);
        // 筛选出符合特定标识符 flag 的文件名
        String targetFileName = fileNames.stream()
                .filter(name -> name.contains(flag))
                .findAny()
                .orElse("");
        try {
            if (StrUtil.isNotEmpty(targetFileName)) {
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(targetFileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + targetFileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                System.out.println("文件下载成功");
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }
}