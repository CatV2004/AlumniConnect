package com.cmc.components;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author FPTSHOP
 */
import com.cmc.dtos.InvitationDTO;
import com.cmc.dtos.PostResponseDTO;
import com.cmc.dtos.SurveyDTO;
import com.cmc.dtos.SurveyOptionDTO;
import com.cmc.dtos.SurveyQuestionDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.InvitationPost;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.pojo.SurveyPost;
import com.cmc.pojo.SurveyQuestion;
import com.cmc.pojo.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostResponseDTO toPostResponseDTO(Post post) {
        if (post == null) {
            return null;
        }

        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setLockComment(post.getLockComment());
        dto.setCreatedDate(post.getCreatedDate());
        dto.setUpdatedDate(post.getUpdatedDate());
        dto.setDeletedDate(post.getDeletedDate());
        dto.setActive(post.getActive());

        dto.setUserId(toUserDTO(post.getUserId()));

        List<String> imageUrls = post.getPostImageSet().stream()
                .map(PostImage::getImage)
                .collect(Collectors.toList());
        dto.setPostImages(imageUrls);

        dto.setInvitationPost(toInvitationDTO(post.getInvitationPost()));
        dto.setSurveyPost(toSurveyDTO(post.getSurveyPost()));

        return dto;
    }

    private UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setAvatar(user.getAvatar());
        dto.setRole(user.getRole());
        return dto;
    }

    private InvitationDTO toInvitationDTO(InvitationPost invitation) {
        if (invitation == null) {
            return null;
        }

        InvitationDTO dto = new InvitationDTO();
        dto.setEventName(invitation.getEventName());
        dto.setEventTime(invitation.getEventTime());
        List<Long> invitedIds = invitation.getUserSet().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        dto.setInvitedUserIds(invitedIds);
        return dto;
    }

    private SurveyDTO toSurveyDTO(SurveyPost survey) {
        if (survey == null) {
            return null;
        }

        SurveyDTO dto = new SurveyDTO();
        dto.setSurveyType(survey.getSurveyType());
        dto.setId(survey.getId());
        dto.setEndTime(survey.getEndTime());

        List<SurveyQuestionDTO> questionDTOs = survey.getSurveyQuestionSet().stream()
                .map(this::toSurveyQuestionDTO)
                .collect(Collectors.toList());

        dto.setQuestions(questionDTOs);
        return dto;
    }

    private SurveyQuestionDTO toSurveyQuestionDTO(SurveyQuestion question) {
        SurveyQuestionDTO dto = new SurveyQuestionDTO();
        
        dto.setId(question.getId());
        dto.setQuestion(question.getQuestion());
        dto.setMultiChoice(question.getMultiChoice());

        List<SurveyOptionDTO> optionDTOs = question.getSurveyOptionSet().stream()
                .map(option -> {
                    SurveyOptionDTO o = new SurveyOptionDTO();
                    o.setId(option.getId());
                    o.setOptionText(option.getOptionText());
                    return o;
                })
                .collect(Collectors.toList());

        dto.setOptions(optionDTOs);
        return dto;
    }
}
