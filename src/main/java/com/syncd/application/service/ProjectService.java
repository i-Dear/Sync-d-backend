package com.syncd.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.TextAlignment;
import com.syncd.application.port.in.*;
import com.syncd.application.port.out.gmail.SendMailPort;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.openai.ChatGPTPort;
import com.syncd.application.port.out.persistence.project.ReadProjectPort;
import com.syncd.application.port.out.persistence.project.WriteProjectPort;
import com.syncd.application.port.out.persistence.user.ReadUserPort;
import com.syncd.application.port.out.persistence.user.WriteUserPort;
import com.syncd.application.port.out.s3.S3Port;
import com.syncd.domain.project.*;
import com.syncd.domain.user.User;
import com.syncd.dto.MakeUserStoryResponseDto;
import com.syncd.dto.UserRoleDto;
import com.syncd.enums.Role;
import com.syncd.exceptions.*;
import com.syncd.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;


@Service
@Primary
@RequiredArgsConstructor
public class ProjectService implements CreateProjectUsecase, GetAllRoomsByUserIdUsecase, GetRoomAuthTokenUsecase, UpdateProjectUsecase, WithdrawUserInProjectUsecase, InviteUserInProjectUsecase, DeleteProjectUsecase, SyncProjectUsecase, MakeUserstoryUsecase, JoinProjectUsecase, GetResultPdfUsecase {

    private final ReadProjectPort readProjectPort;
    private final WriteProjectPort writeProjectPort;
    private final ReadUserPort readUserPort;
    private final WriteUserPort writeUserPort;
    private final LiveblocksPort liveblocksPort;
    private final SendMailPort sendMailPort;
    private final ChatGPTPort chatGPTPort;
    private final S3Port s3Port;

    private final ProjectMapper projectMappers;
    public static final String FONT_PATH = "/font/GmarketSansTTFMedium.ttf";

    @Override
    public CreateProjectResponseDto createProject(String hostId, String hostName, String projectName, String description, MultipartFile img, List<String> userEmails){
        User user = readUserPort.findByUserId(hostId);
        if (user.getNumberOfLeftHostProjects() < 1) {
            throw new CustomException(ErrorInfo.NOT_LEFT_HOST_PROJECTS, "Left Host Projects: " + user.getNumberOfLeftHostProjects());
        }

        List<User> users = new ArrayList<>();
        if(userEmails!=null){
            users = readUserPort.usersFromEmails(userEmails);
        }
        String imgURL = uploadFileToS3(img);

        Project project = new Project();
        project = project.createProjectDomain(projectName, description, imgURL, hostId);
        if (userEmails != null && !userEmails.isEmpty()){
            project.addUsers(userInProjectFromEmail(userEmails));
            sendMailPort.sendIviteMailBatch(hostName, projectName, userEmails, project.getId());
        }
        CreateProjectResponseDto createProjectResponseDto = new CreateProjectResponseDto(writeProjectPort.CreateProject(project));
        user.setNumberOfLeftHostProjects(user.getNumberOfLeftHostProjects() - 1);
        writeUserPort.updateUser(user);

//        User host = readUserPort.findByUserId(hostId);
//        List<UserInProject> members = new ArrayList<>();
//        if (userEmails != null && !userEmails.isEmpty()) {
//            members = userEmails.stream()
//                    .map(email -> createUserInProjectWithRoleMember(email, host.getName(), projectName, createProjectResponseDto.projectId()))
//                    .collect(Collectors.toList());
//        }


        return createProjectResponseDto;
    }

    private List<UserInProject> userInProjectFromEmail(List<String> userEmails) {
        return userEmails.stream()
                .map(email -> new UserInProject(email, Role.MEMBER))
                .collect(Collectors.toList());
    }

    @Override
    public JoinProjectUsecase.JoinProjectResponseDto joinProject(String userId, String projectId) {
        UserInProject userInProject = new UserInProject(userId, Role.MEMBER);
        Project project = readProjectPort.findProjectByProjectId(projectId);

        List<UserInProject> users = project.getUsers();
        users.add(userInProject);
        project.setUsers(users);

        writeProjectPort.UpdateProject(project);

        return new JoinProjectUsecase.JoinProjectResponseDto(projectId);
    }

    @Override
    public GetAllRoomsByUserIdResponseDto getAllRoomsByUserId(String userId) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(userId);
        System.out.println(projects);
        // GetAllRoomsByUserIdResponseDto responseDto = projectMappers.mapProjectsToGetAllRoomsByUserIdResponseDto(userId, projects);
        GetAllRoomsByUserIdResponseDto responseDto = mapProjectsToResponse(userId, projects);
        System.out.println(responseDto);
        return responseDto;
    }


    @Override
    public GetRoomAuthTokenResponseDto getRoomAuthToken(String userId) {
        List<Project> projects = readProjectPort.findAllProjectByUserId(userId);
//        List<String> projectIds = projectMappers.mapProjectToProjectId(projects);
        List<String> projectIds = projects.stream()
                .map(Project::getId)
                .collect(Collectors.toList());
        User userInfo = readUserPort.findByUserId(userId);
        return new GetRoomAuthTokenResponseDto(liveblocksPort.GetRoomAuthToken(userId, userInfo.getName(), userInfo.getProfileImg(), projectIds).token());
    }

    @Override
    public DeleteProjectResponseDto deleteProject(String userId, String projectId) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        if (project == null) {
            throw new CustomException(ErrorInfo.PROJECT_NOT_FOUND, "Project ID: " + projectId);
        }

        String imgFileName = project.getImgFileName();

        if (imgFileName != null) {
            Optional<Boolean> deletionResult = s3Port.deleteFileFromS3(imgFileName);
        }

        writeProjectPort.RemoveProject(projectId);
        return new DeleteProjectResponseDto(projectId);
    }


    @Override
    public InviteUserInProjectResponseDto inviteUserInProject(String userId,String hostName, String projectId, List<String> userEmails) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);
        if (userEmails != null && !userEmails.isEmpty()){
            project.addUsers(userInProjectFromEmail(userEmails));
            sendMailPort.sendIviteMailBatch(hostName, project.getName(), userEmails, project.getId());
        }

        return new InviteUserInProjectResponseDto(projectId);
    }

    @Override
    public UpdateProjectResponseDto updateProject(String userId,  String projectId,
                                                  String projectName,
                                                  String description,
                                                  String image ){
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);

        project.updateProjectInfo(projectName,description, image);
        writeProjectPort.UpdateProject(project);
        return new UpdateProjectResponseDto(projectId);
    }

    @Override
    public WithdrawUserInProjectResponseDto withdrawUserInProject(String userId, String projectId, List<String> userIds) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        checkHost(project, userId);
        project.withdrawUsers(userIds);

        writeProjectPort.UpdateProject(project);
        return new WithdrawUserInProjectResponseDto(projectId);
    }

    @Override
    public SyncProjectResponseDto syncProject(String userId, String projectId, int projectStage,
                                              String problem,
                                              String personaInfosJson,
                                              MultipartFile whyWhatHowImage,
                                              String coreDetailsJson,
                                              MultipartFile businessModelImage,
                                              String epicsJson,
                                              MultipartFile menuTreeImage) {
        ObjectMapper objectMapper = new ObjectMapper();
        CoreDetails coreDetails = null;
        List<PersonaInfo> personaInfos = null;
        List<Epic> epics = null;

        try {
            if(projectStage == 4){
                personaInfos = objectMapper.readValue(personaInfosJson, new TypeReference<List<PersonaInfo>>() {});
            }
            if(projectStage == 8) {
                coreDetails = objectMapper.readValue(coreDetailsJson, CoreDetails.class);
            }
            if(projectStage == 11){
                epics = objectMapper.readValue(epicsJson, new TypeReference<List<Epic>>() {});
            }
          } catch (Exception e) {
            throw new CustomException(ErrorInfo.JSON_PARSE_ERROR, "Failed to parse JSON for coreDetails or epics: " + e.getMessage());
        }

        Project project = readProjectPort.findProjectByProjectId(projectId);
        switch (projectStage) {
            case 1:
            case 2:
                break;
            case 3:
                project.setProblem(problem);
                break;
            case 4:
                project.setPersonaInfos(personaInfos);
                break;
            case 5:
            case 6:
                break;
            case 7:
                project.setWhyWhatHowImage(uploadFileToS3(whyWhatHowImage));
                break;
            case 8:
                project.setCoreDetails(coreDetails);
                break;
            case 9:
                project.setBusinessModelImage(uploadFileToS3(businessModelImage));
                break;
            case 10:
                break;
            case 11:
                project.setEpics(epics);
                break;
            case 12:
                project.setMenuTreeImage(uploadFileToS3(menuTreeImage));
                break;
            default:
                throw new IllegalArgumentException("Invalid project stage: " + projectStage);
        }
        writeProjectPort.AddProgress(projectId, projectStage);
        writeProjectPort.updateLastModifiedDate(projectId);
        writeProjectPort.UpdateProject(project);
        return new SyncProjectResponseDto(projectId);
    }

    @Override
    @Transactional
    public MakeUserStoryResponseDto makeUserstory(String userId, String projectId, List<String> senarios){
        Project project = readProjectPort.findProjectByProjectId(projectId);
        if(project.getLeftChanceForUserstory() < 1){
            throw new CustomException(ErrorInfo.NOT_LEFT_CHANCE, "project id" +  projectId);
        }
        System.out.println(userId);
        boolean containsUserIdA = project.getUsers().stream()
                .anyMatch(user -> user.getUserId().equals(userId));

        if(!containsUserIdA){
            System.out.println(project);
            throw new CustomException(ErrorInfo.NOT_INCLUDE_PROJECT, "project id" +  projectId);
        }
        project.setScenarios(senarios);
        project.subLeftChanceForUserstory();
        writeProjectPort.UpdateProject(project);
        System.out.println(senarios);
        return chatGPTPort.makeUserstory(senarios);
    }

    // ======================================
    // private methods (implements)
    // ======================================

    private void checkHost(Project project, String userId){
        if(!project.getHost().equals(userId)){
            throw new CustomException(ErrorInfo.PROJECT_ALREADY_EXISTS, "project id" +  project.getId());
        }
    }
    private UserInProject createUserInProjectWithRoleMember(String userEmail, String hostName, String projectName,String projectId) {
        // 여기에 사용자 생성 및 역할 부여 로직 추가
        User user = readUserPort.findByEmail(userEmail);
        sendMailPort.sendInviteMail(userEmail, hostName, user.getName(), projectName,projectId);
        return new UserInProject(user.getId(), Role.MEMBER);
    }

    private GetAllRoomsByUserIdResponseDto mapProjectsToResponse(String userId, List<Project> projects) {
        List<ProjectForGetAllInfoAboutRoomsByUserIdResponseDto> projectDtos = projects.stream()
                .map(project -> convertProjectToDto(userId, project))
                .filter(dto -> dto != null)  // Ensure that only relevant projects are included
                .collect(Collectors.toList());

        return new GetAllRoomsByUserIdResponseDto(userId, projectDtos);
    }

    private ProjectForGetAllInfoAboutRoomsByUserIdResponseDto convertProjectToDto(String userEmail, Project project) {
        Role userRole = project.getUsers().stream()
                .filter(user -> userEmail.equals(user.getUserId()))
                .map(UserInProject::getRole)
                .findFirst()
                .orElse(null);

        if (userRole == null) return null;

        List<UserInProject> usersInProject = project.getUsers();

        List<String> userEmails = usersInProject.stream()
                .map(UserInProject::getUserId)
                .map(userId -> {
                    User user = readUserPort.findByUserId(userId);
                    return user != null ? user.getEmail() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ProjectForGetAllInfoAboutRoomsByUserIdResponseDto(
                project.getName(),
                project.getId(),
                project.getDescription(),
                userRole,
                userEmails,
                project.getProgress(),
                project.getLastModifiedDate(),
                project.getImg()
        );
    }

    private UserRoleDto convertUserToUserRoleDto(String projectId, UserInProject user) {
        return new UserRoleDto(projectId, user.getUserId(), user.getRole());
    }

    private String uploadFileToS3(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            Optional<String> optionalFileUrl = s3Port.uploadMultipartFileToS3(file);
            return optionalFileUrl.orElseThrow(() -> new IllegalStateException("Failed to upload file to S3"));
        }
        return "";
    }

    @Override
    public GetResultPdfUsecaseResponseDto getResultPdfUsecaseProject(String userId, String projectId) {
        Project project = readProjectPort.findProjectByProjectId(projectId);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4, false);
            PdfFont koreanFont = PdfFontFactory.createFont(FONT_PATH, PdfEncodings.IDENTITY_H);
            document.setFont(koreanFont);

            String[] personaImages = {"https://velog.velcdn.com/images/donggni0712/post/a966f84e-02b9-471d-9f8d-382f3a8c3745/image.jpeg", "https://velog.velcdn.com/images/donggni0712/post/6317cbb8-91de-4da3-9acb-516eecd54fe0/image.jpeg", "https://velog.velcdn.com/images/donggni0712/post/09002beb-053c-4b4f-9032-7a247e74ad31/image.jpeg", "https://velog.velcdn.com/images/donggni0712/post/c25c461d-4e43-4439-abcb-acc11164be14/image.jpeg","https://velog.velcdn.com/images/donggni0712/post/c83441de-a699-49b6-9b3f-d43362411f6e/image.jpeg"};
            Random random = new Random();

            // Add title
            document.add(new Paragraph("프로젝트 기획 보고서")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Add problem
            document.add(new Paragraph("문제:")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));
            document.add(new Paragraph(project.getProblem())
                    .setMarginBottom(20));

            // Add personas
            document.add(new Paragraph("페르소나:")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));

// Create a table with 2 columns
            Table personaTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

            for (PersonaInfo persona : project.getPersonaInfos()) {
                // Create a cell for each persona
                Cell personaCell = new Cell().setBorder(Border.NO_BORDER);

                // Add persona image
                String randomImage = personaImages[random.nextInt(personaImages.length)];
                Image personaImage = new Image(ImageDataFactory.create(randomImage)).scaleToFit(100, 100);
                personaCell.add(personaImage);

                // Add persona details
                personaCell.add(new Paragraph("정보: " + persona.getInfo()));
                personaCell.add(new Paragraph("설명: " + persona.getPersonality()));
                personaCell.add(new Paragraph("설명: " + persona.getDetail()));

                // Add the cell to the table
                personaTable.addCell(personaCell);
            }

// Add the table to the document
            document.add(personaTable);
            document.add(new Paragraph(" ")); // Add some space after the personas


            // Add whyWhatHowImage
            document.add(new Paragraph("문제 정의 과정:")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));
            addImageToDocument(document, project.getWhyWhatHowImage(), 500, 300);
            document.add(new Paragraph(" "));

            // Add core details
            document.add(new Paragraph("문제 정의:")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));
            document.add(new Paragraph("핵심 타겟: " + project.getCoreDetails().getCoreTarget())
                    .setMarginBottom(5));
            document.add(new Paragraph("핵심 문제: " + project.getCoreDetails().getCoreProblem())
                    .setMarginBottom(5));
            document.add(new Paragraph("문제 원인: " + project.getCoreDetails().getCoreCause())
                    .setMarginBottom(5));
            document.add(new Paragraph("해결 방안: " + project.getCoreDetails().getSolution())
                    .setMarginBottom(5));
            document.add(new Paragraph("핵심 가치: " + project.getCoreDetails().getCoreValue())
                    .setMarginBottom(20));

            // Add business model image
            document.add(new Paragraph("비즈니스 모델:")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));
            addImageToDocument(document, project.getBusinessModelImage(), 500, 300);
            document.add(new Paragraph(" "));

            // Add scenarios
            document.add(new Paragraph("시나리오:")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));
            for (String scenario : project.getScenarios()) {
                document.add(new Paragraph(scenario)
                        .setMarginBottom(5));
            }
            document.add(new Paragraph(" "));

            for (Epic epic : project.getEpics()) {
                Div epicDiv = new Div()
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setBorderRadius(new BorderRadius(10))
                        .setPadding(10)
                        .setMarginBottom(10);
                epicDiv.add(new Paragraph(epic.getName())
                        .setFontSize(12)
                        .setBold()
                        .setMarginBottom(10));
                com.itextpdf.layout.element.List userStoriesList = new com.itextpdf.layout.element.List();
                for (UserStory userStory : epic.getUserStories()) {
                    userStoriesList.add(new ListItem(userStory.getName()));
                }
                epicDiv.add(userStoriesList);
                document.add(epicDiv);
            }

            document.add(new Paragraph("").setMarginBottom(10));
            // Add menu tree image
            document.add(new Paragraph("메뉴 트리:")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));
            addImageToDocument(document, project.getMenuTreeImage(), 500, 300);

            document.close();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            String fileName = projectId + "-result.pdf";
            MultipartFile multipartFile = new MockMultipartFile("file", fileName, MediaType.APPLICATION_PDF_VALUE, byteArrayInputStream);
            String fileUrl = uploadFileToS3(multipartFile);

            return new GetResultPdfUsecaseResponseDto(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorInfo.JSON_PARSE_ERROR, "Failed to parse JSON for coreDetails or epics: " + e.getMessage());
        }
    }

    private void addImageToDocument(Document document, String imagePath, float width, float height) {
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData).scaleToFit(width, height).setMarginBottom(20);
            document.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addImageToDocument(Document document, String imageUrl) throws MalformedURLException {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ImageData imageData = ImageDataFactory.create(new URL(imageUrl));
            Image image = new Image(imageData);
            image.setWidth(UnitValue.createPercentValue(100));
            document.add(image);
        }
    }
}
