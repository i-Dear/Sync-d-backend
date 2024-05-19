package adaptor.in.web;

import com.syncd.application.port.in.GetAllRoomsByUserIdUsecase.*;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase.*;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class RoomControllerTest {
    private Validator validator;

    @BeforeEach
    void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Get Room Auth Token - Invalid Request - Blank RoomId")
    void testGetRoomAuthToken_InvalidRequest_BlankRoomId() {
        TestDto requestDto = new TestDto("");

        Set<ConstraintViolation<TestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.ROOM_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Get Room Auth Token - Valid Request")
    void testGetRoomAuthToken_ValidRequest() {
        TestDto requestDto = new TestDto("validRoomId");

        Set<ConstraintViolation<TestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }


    @Test
    @DisplayName("Get All Rooms By User Id - Invalid Request - Blank UserId")
    void testGetAllRoomsByUserId_InvalidRequest_BlankUserId() {
        GetAllRoomsByUserIdRequestDto requestDto = new GetAllRoomsByUserIdRequestDto("");

        Set<ConstraintViolation<GetAllRoomsByUserIdRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.USER_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Get All Rooms By User Id - Valid Request")
    void testGetAllRoomsByUserId_ValidRequest() {
        GetAllRoomsByUserIdRequestDto requestDto = new GetAllRoomsByUserIdRequestDto("validUserId");

        Set<ConstraintViolation<GetAllRoomsByUserIdRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

}
