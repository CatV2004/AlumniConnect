package com.cmc.controllers;

import com.cmc.dtos.MultiSelectOptionRequest;
import com.cmc.dtos.ResponseDTO;
import com.cmc.dtos.SelectOptionRequest;
import com.cmc.dtos.SurveyDTO;
import com.cmc.dtos.SurveyOptionStatisticDTO;
import com.cmc.dtos.SurveyStatisticsResponseDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.SurveyPost;
import com.cmc.pojo.SurveyQuestion;
import com.cmc.pojo.User;
import com.cmc.repository.PostRepository;
import com.cmc.repository.SurveyPostRepository;
import com.cmc.repository.SurveyQuestionRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.PostImageService;
import com.cmc.service.SurveyPostService;
import com.cmc.service.SurveyStatisticService;
import com.cmc.service.UserService;
import com.cmc.service.UserSurveyOptionService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author FPTSHOP
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiSurveyPostController {

    @Autowired
    private SurveyPostService surveyPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostImageService postImageService;

    @Autowired
    private SurveyPostRepository surveyPostRepository;

    @Autowired
    private UserSurveyOptionService userSurveyOptionService;

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    private SurveyStatisticService surveyStatisticService;

    @Autowired
    private PostRepository postRepo;

    @PostMapping("/survey-posts")
    public ResponseEntity<ResponseDTO<Void>> createSurveyPost(
            @Valid @RequestBody SurveyDTO surveyDTO,
            Principal principal
    ) {

        String username = principal.getName();

        User user = userRepo.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity
                    .status(401)
                    .body(ResponseDTO.failure(401, "Không xác định được người dùng đăng nhập."));
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        surveyDTO.setUserId(userDTO);

        boolean success = surveyPostService.saveOrUpdate(surveyDTO);

        if (success) {
            return ResponseEntity.ok(ResponseDTO.success("Tạo khảo sát thành công", null));
        } else {
            return ResponseEntity
                    .status(500)
                    .body(ResponseDTO.failure(500, "Tạo khảo sát thất bại. Vui lòng thử lại sau."));
        }
    }

    @PostMapping(value = "/survey-posts/{postId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Void>> uploadImagesForSurveyPost(
            @PathVariable Long postId,
            @RequestPart("images") List<MultipartFile> images
    ) {
        Post post = postRepo.getPostId(postId);
        postImageService.uploadAndSaveImages(post, images);

        return ResponseEntity.ok(ResponseDTO.success("Upload ảnh thành công", null));
    }

    @PostMapping("/user-survey-options/select-multiple")
    public ResponseEntity<?> selectMultipleSurveyOptions(
            @RequestBody MultiSelectOptionRequest request,
            Principal principal
    ) {
        User user = userRepo.getUserByUsername(principal.getName());

        try {
            userSurveyOptionService.selectMultipleOptions(user, request.getAnswers());
            return ResponseEntity.ok("Lưu toàn bộ lựa chọn thành công");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/user-survey-options/has-answered")
    public ResponseEntity<Boolean> hasUserAnsweredSurvey(
            @RequestParam("surveyPostId") Long surveyPostId,
            Principal principal
    ) {
        User user = userRepo.getUserByUsername(principal.getName());
        boolean hasAnswered = userSurveyOptionService.hasUserAnsweredSurvey(user.getId(), surveyPostId);
        return ResponseEntity.ok(hasAnswered);
    }

    @GetMapping("/statistics/{surveyPostId}")
    public ResponseEntity<SurveyStatisticsResponseDTO> getSurveyStatistics(@PathVariable Long surveyPostId) {
        SurveyStatisticsResponseDTO response = surveyStatisticService.getSurveyStatisticsWithParticipants(surveyPostId);
        return ResponseEntity.ok(response);
    }

}
