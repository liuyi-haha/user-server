package com.liuyi.user.adapter.client;

import org.apache.dubbo.config.annotation.DubboReference;
import org.liuyi.file.api.*;
import org.springframework.stereotype.Component;

@Component
public class FileApiGatewayImpl implements FileApiGateway {
    @DubboReference
    FileService fileService;

    public FileApiGatewayImpl() {
        super();
    }

    @Override
    public UploadFileResponse uploadFile(UploadFileRequest request) {
        return fileService.uploadFile(request);
    }

    @Override
    public DownloadFileResponse downloadFile(DownloadFileRequest request) {
        return fileService.downloadFile(request);
    }
}
