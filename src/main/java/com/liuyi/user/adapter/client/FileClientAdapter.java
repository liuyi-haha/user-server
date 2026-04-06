package com.liuyi.user.adapter.client;

import com.liuyi.user.domain.exception.AvatarTooLargeException;
import com.liuyi.user.port.client.FileClient;
import lombok.RequiredArgsConstructor;
import org.liuyi.common.domain.exception.DomainException;
import org.liuyi.file.api.UploadFileRequest;
import org.liuyi.file.api.UploadFileResponse;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FileClientAdapter implements FileClient {
    private final FileApiGateway fileApiGateway;

    @Override
    public String uploadAvatar(byte[] avatar) {
        UploadFileRequest req = new UploadFileRequest();

        req.setContent(avatar);
        UploadFileResponse resp = fileApiGateway.uploadFile(req);
        if (!resp.isSuccess()) {
            if (resp.getErrorType() == UploadFileResponse.UploadErrorType.FILE_TOO_LARGE) {
                throw new AvatarTooLargeException();
            }
            throw new DomainException("上传头像失败");
        }
        return resp.getFileId();
    }
}


