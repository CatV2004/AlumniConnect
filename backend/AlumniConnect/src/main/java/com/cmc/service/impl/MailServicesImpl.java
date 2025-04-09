/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.components.MailUtils;
import com.cmc.service.MailServices;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author FPTSHOP
 */
@Service
public class MailServicesImpl implements MailServices {

    @Autowired
    private MailUtils mailUtils;

    @Async
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

    @Async
    @Override
    public void notifyTeacherAccountCreation(String teacherEmail, String teacherName, String username, String defaultPassword) {
        String subject = "Thông Báo: Tạo tài khoản giảng viên";
        String body = "Chào " + teacherName + ",\n\n"
                + "Tài khoản giảng viên của bạn đã được thiết lập trên hệ thống AlumniConnect.\n\n"
                + "👉 Tên đăng nhập: " + username + "\n"
                + "👉 Mật khẩu tạm thời: " + defaultPassword + "\n\n"
                + "Hãy đăng nhập và cập nhật lại mật khẩu trong vòng 24h để đảm bảo an toàn.\n\n"
                + "Nếu cần hỗ trợ, bạn có thể liên hệ admin bất kỳ lúc nào.\n\n"
                + "Trân trọng,\n"
                + "🎓 Đội ngũ AlumniConnect";

        mailUtils.sendEmail(teacherEmail, subject, body);
    }

    @Async
    @Override
    public void notifyTeacherPasswordReset(String teacherEmail, String teacherName, LocalDateTime passwordChangeDeadline) {
        String subject = "Thông Báo: Cập nhật thời gian thay đổi mật khẩu";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDeadline = passwordChangeDeadline.format(dateFormatter);

        String body = "Chào " + teacherName + ",\n\n"
                + "Chúng tôi thông báo rằng thời gian thay đổi mật khẩu của bạn đã được thiết lập lại.\n\n"
                + "Vui lòng cập nhật mật khẩu của bạn trong vòng 24 giờ từ bây giờ, trước " + formattedDeadline + " để đảm bảo an toàn tài khoản của bạn.\n\n"
                + "Nếu bạn có bất kỳ câu hỏi nào, xin vui lòng liên hệ với quản trị viên.\n\n"
                + "Trân trọng,\n"
                + "🎓 Đội ngũ AlumniConnect";

        mailUtils.sendEmail(teacherEmail, subject, body);
    }
}
