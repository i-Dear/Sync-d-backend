package Dummy.Stub.application.out.s3;

import Dummy.ProjectDummyData;
import Dummy.UserDummyData;
import com.syncd.application.port.out.s3.S3Port;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class StubS3Port implements S3Port {

    @Override
    public Optional<String> uploadMultipartFileToS3(MultipartFile multipartFile) {
        if(multipartFile.getName() == ProjectDummyData.getImageFile().getName()){
            return Optional.of(ProjectDummyData.ProjectImage.getValue());
        }
        if(multipartFile.getName() == UserDummyData.getImageFile().getName()){
            return Optional.of(UserDummyData.UserImage.getValue());
        }
        return Optional.of(ProjectDummyData.S3Link.getValue());
    }

    @Override
    public Optional<Boolean> deleteFileFromS3(String filename) {
        return Optional.of(true);
    }
}
