package com.ims.initializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.ims.Model.User;
import com.ims.repository.UserRepository;
import com.ims.service.impl.AuthServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(AdminUserInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Value("${ADMIN_EMAIL:admin@nucleusteq.com}")
    private String email;

    @Value("${ADMIN_PASSWORD:Admin@12345}")
    private String password;

    @Value("${ADMIN_FIRST_NAME:Admin}")
    private String firstName;

    @Value("${ADMIN_LAST_NAME:User}")
    private String lastName;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Request for creating admin user with email: {}", email);
        try {
            if (!userRepository.existsByEmailIgnoreCase(email)) {
                User adminUser = new User();
                adminUser.setFname(firstName);
                adminUser.setLname(lastName);
                adminUser.setEmail(email);
                adminUser.setAge(22);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dob = dateFormat.parse("2001-11-10");
                adminUser.setDob(dob);
                adminUser.setMobile(9876543210L);
                adminUser.setPassword(AuthServiceImpl.encryptPassword(password));
                adminUser.setRole("admin");

                userRepository.save(adminUser);
                logger.info("Admin user created successfully!");
            } else {
                logger.error("Admin user already exists.");
            }
        } catch (Exception e) {
            logger.error("Error while creating new admin user", e);
        }
    }
}
