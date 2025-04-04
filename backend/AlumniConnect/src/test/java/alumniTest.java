//
//import com.cmc.pojo.Alumni;
//import com.cmc.repository.impl.AlumniRepositoryImpl;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//
///**
// *
// * @author FPTSHOP
// */
//public class alumniTest {
//
//    private SessionFactory sessionFactory;
//    private Session session;
//    private AlumniRepositoryImpl alumniRepository;
//
//    @Before
//    public void setUp() {
//        // Set up Hibernate session factory
//        sessionFactory = new Configuration()
//                .configure("hibernate.cfg.xml") // Đảm bảo bạn có cấu hình Hibernate đúng
//                .addAnnotatedClass(Alumni.class)
//                .addAnnotatedClass(User.class)
//                .buildSessionFactory();
//
//        // Mở một session
//        session = sessionFactory.getCurrentSession();
//
//        // Khởi tạo đối tượng repository (nếu cần)
//        alumniRepository = new AlumniRepositoryImpl(session);
//    }
//
//    @Test
//    public void testGetAlumniById() {
//        // Bắt đầu transaction
//        session.beginTransaction();
//
//        // Thêm một bản ghi để kiểm tra
//        Alumni newAlumni = new Alumni();
//        newAlumni.setStudentCode("S12345");
//        newAlumni.setIsVerified(true);
//        // Giả sử bạn đã có cách để tạo User, hoặc thêm user vào trong test này.
//        User user = new User();
//        user.setUsername("student1");
//        newAlumni.setUserId(user);
//
//        session.save(user);
//        session.save(newAlumni);
//
//        // Lưu id của alumni để dùng trong phương thức kiểm tra
//        Long alumniId = newAlumni.getId();
//
//        // Commit transaction
//        session.getTransaction().commit();
//
//        // Mở lại session và kiểm tra
//        session = sessionFactory.getCurrentSession();
//        session.beginTransaction();
//
//        Alumni retrievedAlumni = alumniRepository.getAlumniById(alumniId);
//
//        assertNotNull(retrievedAlumni);
//        assertEquals(newAlumni.getId(), retrievedAlumni.getId());
//        assertEquals(newAlumni.getStudentCode(), retrievedAlumni.getStudentCode());
//        assertTrue(retrievedAlumni.getIsVerified());
//
//        session.getTransaction().commit();
//    }
//
//    @After
//    public void tearDown() {
//        session.close();
//        sessionFactory.close();
//    }
//}
