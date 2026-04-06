package com.liuyi.user.adapter;

import com.liuyi.user.adapter.client.FileApiGateway;
import lombok.Data;
import org.apache.dubbo.config.annotation.DubboService;
import org.liuyi.common.domain.object.RandomIdGenerator;
import org.liuyi.file.api.*;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Profile("test")
@Primary
@Data
@DubboService(interfaceClass = FileService.class)  // 添加这个注解
public class FakeFileService implements FileApiGateway {
    // 文件大小限制：5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private HashMap<String, byte[]> files = new HashMap<>();
    private List<UploadFileRequest> uploadFileRequestList = new ArrayList<>();
    private List<DownloadFileRequest> downloadFileRequests = new ArrayList<>();

    @Override
    public UploadFileResponse uploadFile(UploadFileRequest request) {
        // 1. 记录请求
        uploadFileRequestList.add(request);

        UploadFileResponse response = new UploadFileResponse();

        // 2. 检查文件大小是否超过5MB

        if (request.getContent().length > MAX_FILE_SIZE) {
            response.setSuccess(false);
            response.setErrorType(UploadFileResponse.UploadErrorType.FILE_TOO_LARGE);
            return response;
        }

        // 3. 生成随机ID并保存文件
        String fileId = RandomIdGenerator.generate();
        files.put(fileId, request.getContent());

        // 4. 返回成功响应
        response.setSuccess(true);
        response.setFileId(fileId);

        return response;
    }

    @Override
    public DownloadFileResponse downloadFile(DownloadFileRequest request) {
        // 1. 记录请求
        downloadFileRequests.add(request);

        DownloadFileResponse response = new DownloadFileResponse();
        return response;
    }

    public void reset() {
        files.clear();
        uploadFileRequestList.clear();
        downloadFileRequests.clear();
    }
}