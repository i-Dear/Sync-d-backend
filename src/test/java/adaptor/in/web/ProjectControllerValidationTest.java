package adaptor.in.web;

import com.syncd.application.port.in.CreateProjectUsecase.*;
import com.syncd.application.port.in.DeleteProjectUsecase.*;
import com.syncd.application.port.in.InviteUserInProjectUsecase.*;
import com.syncd.application.port.in.JoinProjectUsecase.*;
import com.syncd.application.port.in.SyncProjectUsecase.*;
import com.syncd.application.port.in.UpdateProjectUsecase.*;
import com.syncd.application.port.in.WithdrawUserInProjectUsecase.*;
import com.syncd.dto.MakeUserStoryReauestDto;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectControllerValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ======================================
    // CreateProject
    // ======================================

    @Test
    @DisplayName("Create Project - Invalid Request - Blank Project Name")
    void testCreateProject_InvalidRequest_BlankName() {
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto("", "description", null, Collections.emptyList());

        Set<ConstraintViolation<CreateProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.NAME_NOT_BLANK);
    }

    @Test
    @DisplayName("Create Project - Valid Request - Only Project Name Provided")
    void testCreateProject_ValidRequest_OnlyName() {
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto("Valid Project", "", null, Collections.emptyList());

        Set<ConstraintViolation<CreateProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

    // ======================================
    // JoinProject
    // ======================================

    @Test
    @DisplayName("Join Project - Invalid Request - Blank ProjectId")
    void testJoinProject_InvalidRequest_BlankProjectId() {
        JoinProjectRequestDto requestDto = new JoinProjectRequestDto("");

        Set<ConstraintViolation<JoinProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Join Project - Valid Request")
    void testJoinProject_ValidRequest() {
        JoinProjectRequestDto requestDto = new JoinProjectRequestDto("validProjectId");

        Set<ConstraintViolation<JoinProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

    // ======================================
    // InviteUser
    // ======================================

    @Test
    @DisplayName("Invite User - Invalid Request - Blank ProjectId and Empty Users")
    void testInviteUser_InvalidRequest_BlankProjectIdAndEmptyUsers() {
        InviteUserInProjectRequestDto requestDto = new InviteUserInProjectRequestDto("", Collections.emptyList());

        Set<ConstraintViolation<InviteUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(2);

        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertThat(messages).contains(
                ValidationMessages.PROJECT_ID_NOT_BLANK,
                ValidationMessages.USERS_SIZE
        );
    }

    @Test
    @DisplayName("Invite User - Invalid Request - Blank ProjectId")
    void testInviteUser_InvalidRequest_BlankProjectId() {
        InviteUserInProjectRequestDto requestDto = new InviteUserInProjectRequestDto("", List.of("user1@example.com"));

        Set<ConstraintViolation<InviteUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Invite User - Invalid Request - Empty Users")
    void testInviteUser_InvalidRequest_EmptyUsers() {
        InviteUserInProjectRequestDto requestDto = new InviteUserInProjectRequestDto("validProjectId", Collections.emptyList());

        Set<ConstraintViolation<InviteUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.USERS_SIZE);
    }

    @Test
    @DisplayName("Invite User - Invalid Request - Less Than 1 User")
    void testInviteUser_InvalidRequest_LessThanOneUser() {
        InviteUserInProjectRequestDto requestDto = new InviteUserInProjectRequestDto("validProjectId", Collections.emptyList());

        Set<ConstraintViolation<InviteUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.USERS_SIZE);
    }

    @Test
    @DisplayName("Invite User - Valid Request")
    void testInviteUser_ValidRequest() {
        InviteUserInProjectRequestDto requestDto = new InviteUserInProjectRequestDto("validProjectId", List.of("user1@example.com"));

        Set<ConstraintViolation<InviteUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

    // ======================================
    // WithdrawUser
    // ======================================

    @Test
    @DisplayName("Withdraw User - Invalid Request - Blank ProjectId and Empty Users")
    void testWithdrawUser_InvalidRequest_BlankProjectIdAndEmptyUsers() {
        WithdrawUserInProjectRequestDto requestDto = new WithdrawUserInProjectRequestDto("", Collections.emptyList());

        Set<ConstraintViolation<WithdrawUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(2);

        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertThat(messages).contains(
                ValidationMessages.PROJECT_ID_NOT_BLANK
        );
    }

    @Test
    @DisplayName("Withdraw User - Invalid Request - Blank ProjectId")
    void testWithdrawUser_InvalidRequest_BlankProjectId() {
        WithdrawUserInProjectRequestDto requestDto = new WithdrawUserInProjectRequestDto("", List.of("user1@example.com"));

        Set<ConstraintViolation<WithdrawUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Withdraw User - Invalid Request - Empty Users")
    void testWithdrawUser_InvalidRequest_EmptyUsers() {
        WithdrawUserInProjectRequestDto requestDto = new WithdrawUserInProjectRequestDto("validProjectId", Collections.emptyList());

        Set<ConstraintViolation<WithdrawUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.USERS_SIZE);
    }

    @Test
    @DisplayName("Withdraw User - Invalid Request - Less Than 1 User")
    void testWithdrawUser_InvalidRequest_LessThanOneUser() {
        WithdrawUserInProjectRequestDto requestDto = new WithdrawUserInProjectRequestDto("validProjectId", Collections.emptyList());

        Set<ConstraintViolation<WithdrawUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.USERS_SIZE);
    }

    @Test
    @DisplayName("Withdraw User - Valid Request")
    void testWithdrawUser_ValidRequest() {
        WithdrawUserInProjectRequestDto requestDto = new WithdrawUserInProjectRequestDto("validProjectId", List.of("user1@example.com"));

        Set<ConstraintViolation<WithdrawUserInProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

    // ======================================
    // DeleteProject
    // ======================================

    @Test
    @DisplayName("Delete Project - Invalid Request - Blank ProjectId")
    void testDeleteProject_InvalidRequest_BlankProjectId() {
        DeleteProjectRequestDto requestDto = new DeleteProjectRequestDto("");

        Set<ConstraintViolation<DeleteProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Delete Project - Valid Request")
    void testDeleteProject_ValidRequest() {
        DeleteProjectRequestDto requestDto = new DeleteProjectRequestDto("validProjectId");

        Set<ConstraintViolation<DeleteProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

    // ======================================
    // UpdateProject
    // ======================================

    @Test
    @DisplayName("Update Project - Invalid Request - Blank ProjectId")
    void testUpdateProject_InvalidRequest_BlankProjectId() {
        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto("", "name", "desc", null);

        Set<ConstraintViolation<UpdateProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Update Project - Valid Request")
    void testUpdateProject_ValidRequest() {
        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto("validProjectId", "newName", "newDesc", null);

        Set<ConstraintViolation<UpdateProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

    // ======================================
    // SyncProject
    // ======================================

    @Test
    @DisplayName("Sync Project - Invalid Request - Blank ProjectId")
    void testSyncProject_InvalidRequest_BlankProjectId() {
        SyncProjectRequestDto requestDto = new SyncProjectRequestDto("", 10);

        Set<ConstraintViolation<SyncProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Sync Project - Invalid Request - Null Project Stage")
    void testSyncProject_InvalidRequest_NullProjectStage() {
        SyncProjectRequestDto requestDto = new SyncProjectRequestDto("validProjectId", null);

        Set<ConstraintViolation<SyncProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_PROGRESS_NOT_NULL);
    }

    @Test
    @DisplayName("Sync Project - Valid Request")
    void testSyncProject_ValidRequest() {
        SyncProjectRequestDto requestDto = new SyncProjectRequestDto("validProjectId", 10);

        Set<ConstraintViolation<SyncProjectRequestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }

    // ======================================
    // MakeUserStory
    // ======================================

    @Test
    @DisplayName("Make User Story - Invalid Request - Blank ProjectId")
    void testMakeUserStory_InvalidRequest_BlankProjectId() {
        MakeUserStoryReauestDto requestDto = new MakeUserStoryReauestDto();
        requestDto.setProjectId("");
        requestDto.setScenario(List.of("Scenario 1"));

        Set<ConstraintViolation<MakeUserStoryReauestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.PROJECT_ID_NOT_BLANK);
    }

    @Test
    @DisplayName("Make User Story - Invalid Request - Null Scenarios")
    void testMakeUserStory_InvalidRequest_NullScenarios() {
        MakeUserStoryReauestDto requestDto = new MakeUserStoryReauestDto();
        requestDto.setProjectId("projectId");
        requestDto.setScenario(null);

        Set<ConstraintViolation<MakeUserStoryReauestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(2);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        assertThat(messages).contains(
                ValidationMessages.SCENARIOS_NOT_NULL,
                ValidationMessages.SCENARIOS_NOT_EMPTY
        );
    }

    @Test
    @DisplayName("Make User Story - Invalid Request - Empty Scenarios")
    void testMakeUserStory_InvalidRequest_EmptyScenarios() {
        MakeUserStoryReauestDto requestDto = new MakeUserStoryReauestDto();
        requestDto.setProjectId("projectId");
        requestDto.setScenario(Collections.emptyList());

        Set<ConstraintViolation<MakeUserStoryReauestDto>> violations = validator.validate(requestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(ValidationMessages.SCENARIOS_NOT_EMPTY);
    }

    @Test
    @DisplayName("Make User Story - Valid Request")
    void testMakeUserStory_ValidRequest() {
        MakeUserStoryReauestDto requestDto = new MakeUserStoryReauestDto();
        requestDto.setProjectId("validProjectId");
        requestDto.setScenario(List.of("Scenario 1"));

        Set<ConstraintViolation<MakeUserStoryReauestDto>> violations = validator.validate(requestDto);
        assertThat(violations).isEmpty();
    }
}