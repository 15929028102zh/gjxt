package com.gjxt.inspection.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Component
public class FileUploadUtil {

    @Value("${file.upload.real-path}")
    private String uploadPath;

    @Value("${file.upload.path}")
    private String accessPath;

    /**
     * 同步创建目录，避免并发问题
     * @param savePath 目录路径
     * @return 创建的目录对象
     */
    private synchronized File createDirectoryIfNotExists(String savePath) {
        System.out.println("尝试创建目录: " + savePath);
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            boolean created = saveDir.mkdirs();
            if (!created) {
                System.err.println("目录创建失败: " + savePath);
                System.err.println("父目录存在: " + saveDir.getParentFile().exists());
                System.err.println("父目录可写: " + saveDir.getParentFile().canWrite());
                throw new RuntimeException("创建上传目录失败: " + savePath);
            } else {
                System.out.println("目录创建成功: " + savePath);
            }
        } else {
            System.out.println("目录已存在: " + savePath);
        }
        return saveDir;
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param subDir 子目录
     * @return 文件访问路径
     */
    public String uploadFile(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }

        // 添加调试日志
        System.out.println("=== FileUploadUtil 调试信息 ===");
        System.out.println("原始文件名: " + originalFilename);
        System.out.println("子目录: " + subDir);

        // 获取文件扩展名
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
        }
        System.out.println("文件扩展名: " + extension);

        // 生成新的文件名
        String newFileName = generateFileName() + extension;
        System.out.println("生成的新文件名: " + newFileName);

        // 构建文件保存路径
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fullSubDir = subDir != null ? subDir + File.separator + datePath : datePath;
        // 确保路径使用正确的分隔符
        String savePath = uploadPath + fullSubDir;
        
        System.out.println("日期路径: " + datePath);
        System.out.println("完整子目录: " + fullSubDir);
        System.out.println("保存路径: " + savePath);
        
        // 使用同步方法创建目录
        File saveDir = createDirectoryIfNotExists(savePath);

        // 保存文件
        File saveFile = new File(saveDir, newFileName);
        System.out.println("完整文件路径: " + saveFile.getAbsolutePath());
        
        try {
            file.transferTo(saveFile);
            System.out.println("文件保存成功");
        } catch (IOException e) {
            System.err.println("文件保存失败: " + e.getMessage());
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }

        // 返回相对路径，用于前端访问
        // 例如: /files/images/20250818/xxx.jpg
        String relativePath = "/files/" + fullSubDir.replace(File.separator, "/") + "/" + newFileName;
        System.out.println("返回的相对路径: " + relativePath);
        System.out.println("=== FileUploadUtil 调试信息结束 ===");
        
        return relativePath;
    }

    /**
     * 上传文件（支持自定义文件名和文件夹路径）
     *
     * @param file 文件
     * @param customFileName 自定义文件名
     * @param customFolderPath 自定义文件夹路径
     * @return 文件访问路径
     */
    public String uploadFileWithCustomPath(MultipartFile file, String customFileName, String customFolderPath) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 使用自定义文件名，如果没有提供则生成默认文件名
        String fileName;
        if (customFileName != null && !customFileName.trim().isEmpty()) {
            fileName = customFileName;
        } else {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                throw new RuntimeException("文件名不能为空");
            }

            // 获取文件扩展名
            String extension = "";
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex);
            }

            // 生成新的文件名
            fileName = generateFileName() + extension;
        }

        // 使用自定义文件夹路径，如果没有提供则使用默认路径
        String folderPath;
        if (customFolderPath != null && !customFolderPath.trim().isEmpty()) {
            folderPath = "images" + File.separator + customFolderPath;
        } else {
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            folderPath = "images" + File.separator + datePath;
        }

        // 确保路径使用正确的分隔符
        String savePath = uploadPath + folderPath;
        
        // 使用同步方法创建目录
        File saveDir = createDirectoryIfNotExists(savePath);

        // 保存文件
        File saveFile = new File(saveDir, fileName);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }

        // 返回完整的物理文件路径
        return saveFile.getAbsolutePath();
    }

    /**
     * 上传图片文件
     *
     * @param file 图片文件
     * @return 图片访问路径
     */
    public String uploadImage(MultipartFile file) {
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("只能上传图片文件");
        }

        return uploadFile(file, "images");
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        try {
            // 将访问路径转换为实际文件路径
            String realFilePath = filePath.replace(accessPath.replace("/**", "/"), uploadPath);
            File file = new File(realFilePath);
            
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    // 删除成功
                } else {
                    // 删除失败
                }
                return deleted;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成文件名
     *
     * @return 文件名
     */
    private String generateFileName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取文件大小描述
     *
     * @param size 文件大小（字节）
     * @return 文件大小描述
     */
    public static String getFileSizeDescription(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

}