package com.syncd.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syncd.adapter.in.web.ProjectController;
import com.syncd.application.domain.project.Project;
import com.syncd.application.port.in.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
@DataMongoTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAllRoomsByUserIdUsecase getAllRoomsByUserIdUsecase;
    @MockBean
    private GetRoomAuthTokenUsecase getRoomAuthTokenUsecase;
    @MockBean
    private CreateProjectUsecase createProjectUsecase;
    @MockBean
    private DeleteProjectUsecase deleteProjectUsecase;
    @MockBean
    private WithdrawUserInProjectUsecase withdrawUserInProjectUsecase;
    @MockBean
    private InviteUserInProjectUsecase inviteUserInProjectUsecase;
    @MockBean
    private UpdateProjectDetailUsecase updateProjectDetailUsecase;
    @MockBean
    private MongoTemplate mongoTemplate;

    private CreateProjectUsecase.CreateProjectRequestDto createRequestDto;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        createRequestDto = new CreateProjectUsecase.CreateProjectRequestDto("user1", "New Project", "Description");
        when(createProjectUsecase.createProject(any())).thenReturn(new CreateProjectUsecase.CreateProjectResponseDto("12345"));
    }
    @Test
    public void testCreateProject() throws Exception {
        CreateProjectUsecase.CreateProjectRequestDto requestDto = new CreateProjectUsecase.CreateProjectRequestDto("user1", "New Project", "Description");
        when(createProjectUsecase.createProject(any())).thenReturn(new CreateProjectUsecase.CreateProjectResponseDto("12345"));

        mockMvc.perform(post("/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value("12345"));

        verify(createProjectUsecase).createProject(any());
    }

    @Test
    public void testDeleteProject() throws Exception {
        DeleteProjectUsecase.DeleteProjectRequestDto requestDto = new DeleteProjectUsecase.DeleteProjectRequestDto("12345");
        when(deleteProjectUsecase.deleteProject(any())).thenReturn(new DeleteProjectUsecase.DeleteProjectResponseDto("12345"));

        mockMvc.perform(post("/v1/project/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value("12345"));

        verify(deleteProjectUsecase).deleteProject(any());
    }

    @Test
    public void testWithdrawUserInProject() throws Exception {
        WithdrawUserInProjectUsecase.WithdrawUserInProjectRequestDto requestDto = new WithdrawUserInProjectUsecase.WithdrawUserInProjectRequestDto("12345", List.of("user1", "user2"));
        when(withdrawUserInProjectUsecase.withdrawUserInProject(any())).thenReturn(new WithdrawUserInProjectUsecase.WithdrawUserInProjectResponseDto("12345"));

        mockMvc.perform(post("/v1/project/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value("12345"));

        verify(withdrawUserInProjectUsecase).withdrawUserInProject(any());
    }

    @Test
    public void testInviteUserInProject() throws Exception {
        InviteUserInProjectUsecase.InviteUserInProjectRequestDto requestDto = new InviteUserInProjectUsecase.InviteUserInProjectRequestDto("12345", List.of("user3", "user4"));
        when(inviteUserInProjectUsecase.inviteUserInProject(any())).thenReturn(new InviteUserInProjectUsecase.InviteUserInProjectResponseDto("12345"));

        mockMvc.perform(post("/v1/project/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value("12345"));

        verify(inviteUserInProjectUsecase).inviteUserInProject(any());
    }

    @Test
    public void testUpdateProjectDetail() throws Exception {
        UpdateProjectDetailUsecase.UpdateProjectDetailRequestDto requestDto = new UpdateProjectDetailUsecase.UpdateProjectDetailRequestDto("12345", "Updated Project Name", "Updated Description", "new-image.png");
        when(updateProjectDetailUsecase.updateProjectDetail(any())).thenReturn(new UpdateProjectDetailUsecase.UpdateProjectDetailResponseDto("12345"));

        mockMvc.perform(post("/v1/project/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value("12345"));

        verify(updateProjectDetailUsecase).updateProjectDetail(any());
    }
}
