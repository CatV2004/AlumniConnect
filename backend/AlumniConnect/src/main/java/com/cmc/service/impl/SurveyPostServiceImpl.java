/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.dtos.SurveyDTO;
import com.cmc.dtos.SurveyOptionDTO;
import com.cmc.dtos.SurveyQuestionDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.pojo.SurveyOption;
import com.cmc.pojo.SurveyPost;
import com.cmc.pojo.SurveyQuestion;
import com.cmc.pojo.User;
import com.cmc.repository.PostRepository;
import com.cmc.repository.SurveyOptionRepository;
import com.cmc.repository.SurveyPostRepository;
import com.cmc.repository.SurveyQuestionRepository;
import com.cmc.service.PostImageService;
import com.cmc.service.SurveyPostService;
import com.cmc.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class SurveyPostServiceImpl implements SurveyPostService {

    @Autowired
    private SurveyPostRepository surveyPostRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    private SurveyOptionRepository surveyOptionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public boolean saveOrUpdate(SurveyDTO dto) {
        try {
            Post post = new Post();
            post.setContent(dto.getContent());
            post.setLockComment(Boolean.FALSE);
            post.setCreatedDate(LocalDateTime.now());
            post.setActive(Boolean.TRUE);
            User u = userService.getUserById(dto.getUserId().getId());
            post.setUserId(u);

            Post savedPost = postRepository.addPost(post);
            if (savedPost == null) {
                return false;
            }

            SurveyPost surveyPost = new SurveyPost();
            surveyPost.setEndTime(dto.getEndTime());
            surveyPost.setSurveyType(dto.getSurveyType());
            surveyPost.setPost(savedPost);
            savedPost.setSurveyPost(surveyPost);

            surveyPostRepository.saveOrUpdate(surveyPost);

            if (dto.getQuestions() != null) {
                for (SurveyQuestionDTO questionDTO : dto.getQuestions()) {
                    SurveyQuestion question = new SurveyQuestion();
                    question.setQuestion(questionDTO.getQuestion());
                    question.setMultiChoice(questionDTO.getMultiChoice());
                    question.setSurveyPostId(surveyPost);

                    surveyQuestionRepository.saveOrUpdate(question);

                    if (questionDTO.getOptions() != null) {
                        for (SurveyOptionDTO optionDTO : questionDTO.getOptions()) {
                            SurveyOption option = new SurveyOption();
                            option.setOptionText(optionDTO.getOptionText());
                            option.setSurveyQuestionId(question);

                            surveyOptionRepository.saveOrUpdate(option);
                        }
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
