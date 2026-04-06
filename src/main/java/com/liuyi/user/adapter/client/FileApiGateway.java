package com.liuyi.user.adapter.client;

import org.liuyi.file.api.DownloadFileRequest;
import org.liuyi.file.api.DownloadFileResponse;
import org.liuyi.file.api.UploadFileRequest;
import org.liuyi.file.api.UploadFileResponse;

public interface FileApiGateway {
    UploadFileResponse uploadFile(UploadFileRequest request);

    DownloadFileResponse downloadFile(DownloadFileRequest request);
}
