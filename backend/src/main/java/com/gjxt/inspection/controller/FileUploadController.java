package com.gjxt.inspection.controller;

import com.gjxt.inspection.common.Result;
import com.gjxt.inspection.util.FileUploadUtil;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传控制器
 *
 * @author gjxt
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/upload")
// @Api(tags = "文件上传管理")
public class FileUploadController {

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 上传单个文件
     */
    @PostMapping("/file")
    // @ApiOperation("上传单个文件")
    public Result<Map<String, Object>> uploadFile(
            /* @ApiParam("文件") */ @RequestParam("file") MultipartFile file,
            /* @ApiParam("子目录") */ @RequestParam(value = "subDir", required = false) String subDir,
            /* @ApiParam("自定义文件名") */ @RequestParam(value = "fileName", required = false) String customFileName,
            /* @ApiParam("自定义文件夹路径") */ @RequestParam(value = "folderPath", required = false) String customFolderPath) {
        
        try {
            String filePath;
            
            // 如果提供了自定义文件名或文件夹路径，使用新的上传方法
            if ((customFileName != null && !customFileName.trim().isEmpty()) || 
                (customFolderPath != null && !customFolderPath.trim().isEmpty())) {
                filePath = fileUploadUtil.uploadFileWithCustomPath(file, customFileName, customFolderPath);
            } else {
                filePath = fileUploadUtil.uploadFile(file, subDir);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", file.getOriginalFilename());
            result.put("url", filePath);  // 修改为url字段，与前端期望一致
            result.put("filePath", filePath);
            result.put("fileSize", file.getSize());
            result.put("fileSizeDesc", FileUploadUtil.getFileSizeDescription(file.getSize()));
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传多个文件
     */
    @PostMapping("/files")
    // @ApiOperation("上传多个文件")
    public Result<List<Map<String, Object>>> uploadFiles(
            /* @ApiParam("文件列表") */ @RequestParam("files") MultipartFile[] files,
            /* @ApiParam("子目录") */ @RequestParam(value = "subDir", required = false) String subDir) {
        
        if (files == null || files.length == 0) {
            return Result.error("请选择要上传的文件");
        }
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (MultipartFile file : files) {
            try {
                String filePath = fileUploadUtil.uploadFile(file, subDir);
                
                Map<String, Object> result = new HashMap<>();
                result.put("fileName", file.getOriginalFilename());
                result.put("filePath", filePath);
                result.put("fileSize", file.getSize());
                result.put("fileSizeDesc", FileUploadUtil.getFileSizeDescription(file.getSize()));
                result.put("success", true);
                
                results.add(result);
            } catch (Exception e) {                
                Map<String, Object> result = new HashMap<>();
                result.put("fileName", file.getOriginalFilename());
                result.put("error", e.getMessage());
                result.put("success", false);
                
                results.add(result);
            }
        }
        
        return Result.success(results);
    }

    /**
     * 上传图片
     */
    @PostMapping("/image")
    // @ApiOperation("上传图片")
    public Result<Map<String, Object>> uploadImage(
            /* @ApiParam("图片文件") */ @RequestParam("file") MultipartFile file) {
        
        try {
            // 添加调试日志
            System.out.println("=== 图片上传调试信息 ===");
            System.out.println("原始文件名: " + file.getOriginalFilename());
            System.out.println("文件大小: " + file.getSize());
            System.out.println("文件类型: " + file.getContentType());
            
            String filePath = fileUploadUtil.uploadImage(file);
            
            System.out.println("生成的文件路径: " + filePath);
            System.out.println("=== 图片上传调试信息结束 ===");
            
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", file.getOriginalFilename());
            result.put("url", filePath);  // 修改为url字段，与前端期望一致
            result.put("filePath", filePath);
            result.put("fileSize", file.getSize());
            result.put("fileSizeDesc", FileUploadUtil.getFileSizeDescription(file.getSize()));
            
            return Result.success(result);
        } catch (Exception e) {
            System.err.println("图片上传失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传多张图片
     */
    @PostMapping("/images")
    // @ApiOperation("上传多张图片")
    public Result<List<Map<String, Object>>> uploadImages(
            /* @ApiParam("图片文件列表") */ @RequestParam("files") MultipartFile[] files) {
        
        if (files == null || files.length == 0) {
            return Result.error("请选择要上传的图片");
        }
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (MultipartFile file : files) {
            try {
                String filePath = fileUploadUtil.uploadImage(file);
                
                Map<String, Object> result = new HashMap<>();
                result.put("fileName", file.getOriginalFilename());
                result.put("filePath", filePath);
                result.put("fileSize", file.getSize());
                result.put("fileSizeDesc", FileUploadUtil.getFileSizeDescription(file.getSize()));
                result.put("success", true);
                
                results.add(result);
            } catch (Exception e) {                
                Map<String, Object> result = new HashMap<>();
                result.put("fileName", file.getOriginalFilename());
                result.put("error", e.getMessage());
                result.put("success", false);
                
                results.add(result);
            }
        }
        
        return Result.success(results);
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/file")
    // @ApiOperation("删除文件")
    public Result<Void> deleteFile(
            /* @ApiParam("文件路径") */ @RequestParam("filePath") String filePath) {
        
        try {
            boolean success = fileUploadUtil.deleteFile(filePath);
            if (success) {
                return Result.success();
            } else {
                return Result.error("文件删除失败");
            }
        } catch (Exception e) {            return Result.error("文件删除失败: " + e.getMessage());
        }
    }

}