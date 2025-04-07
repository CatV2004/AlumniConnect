/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.components.MailUtils;
import com.cmc.service.MailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FPTSHOP
 */
@Service
public class MailServicesImpl implements MailServices {
        
    @Autowired
    private MailUtils mailUtils;
    
    @Override
    public void notifyAlumniOnApproval(String alumniEmail, String alumniName) {
        String subject = "Thông Báo: Bạn đã được duyệt";
        String body = "Chào " + alumniName + ",\n\n"
                + "Chúng tôi rất vui thông báo rằng bạn đã được duyệt bởi admin. Bạn có thể truy cập hệ thống và sử dụng các tính năng mới.\n\n"
                + "Cảm ơn bạn đã tham gia với chúng tôi.\n\n"
                + "Trân trọng,\n"
                + "Team AlumniConnect";

        mailUtils.sendEmail(alumniEmail, subject, body);
    }
    
    @Override
    public void notifyTeacherAccountCreation(String teacherEmail, String teacherName, String username, String defaultPassword) {
        String subject = "Thông Báo: Tạo tài khoản giảng viên";
        String body = "Chào " + teacherName + ",\n\n"
                + "Chúng tôi đã tạo tài khoản giảng viên cho bạn trên hệ thống AlumniConnect.\n\n"
                + "Thông tin tài khoản của bạn là:\n"
                + "Tên đăng nhập: " + username + "\n"
                + "Mật khẩu mặc định: " + defaultPassword + "\n\n"
                + "Vui lòng thay đổi mật khẩu của bạn trong vòng 24 giờ, nếu không tài khoản sẽ bị khoá.\n\n"
                + "Cảm ơn bạn đã tham gia với chúng tôi.\n\n"
                + "Trân trọng,\n"
                + "Team AlumniConnect";

        mailUtils.sendEmail(teacherEmail, subject, body);
    }
}
