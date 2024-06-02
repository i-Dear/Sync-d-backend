package Dummy.Stub.application.out.s3;

import Dummy.Consistent;
import com.syncd.application.port.out.s3.S3Port;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class StubS3Port implements S3Port {

    @Override
    public Optional<String> uploadMultipartFileToS3(MultipartFile multipartFile) {
        return Optional.of(Consistent.S3Link.getValue());
    }

    @Override
    public Optional<Boolean> deleteFileFromS3(String filename) {
        return Optional.of(true);
    }
}
