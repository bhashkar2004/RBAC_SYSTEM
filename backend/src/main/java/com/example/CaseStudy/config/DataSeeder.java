package com.example.CaseStudy.config;

import com.example.CaseStudy.entity.Permission;
import com.example.CaseStudy.entity.Resource;
import com.example.CaseStudy.entity.Roles;
import com.example.CaseStudy.entity.User;
import com.example.CaseStudy.repository.PermissionRepository;
import com.example.CaseStudy.repository.ResourceRepository;
import com.example.CaseStudy.repository.RoleRepository;
import com.example.CaseStudy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import com.example.CaseStudy.repository.AccessLogRepository;
import com.example.CaseStudy.entity.AccessLogs;
import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ResourceRepository resourceRepository,
            PermissionRepository permissionRepository,
            AccessLogRepository accessLogRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // ----------------------------------------------------------------
            // 1. Seed Resources
            // ----------------------------------------------------------------
            Resource resReports = seedResource(resourceRepository, "REPORTS", "Company reports and analytics");
            Resource resDocuments = seedResource(resourceRepository, "DOCUMENTS", "Internal company documents");
            Resource resDatabase = seedResource(resourceRepository, "DATABASE", "Core company database");
            Resource resUsers = seedResource(resourceRepository, "USERS", "User management system");
            Resource resAudit = seedResource(resourceRepository, "AUDIT", "Audit logs and compliance reports");

            // ----------------------------------------------------------------
            // 2. Seed Permissions
            // ----------------------------------------------------------------
            Permission pReadReports = seedPermission(permissionRepository, Permission.Action.READ, resReports);
            Permission pReadDocuments = seedPermission(permissionRepository, Permission.Action.READ, resDocuments);
            Permission pWriteDocuments = seedPermission(permissionRepository, Permission.Action.WRITE, resDocuments);
            Permission pReadDatabase = seedPermission(permissionRepository, Permission.Action.READ, resDatabase);
            Permission pWriteDatabase = seedPermission(permissionRepository, Permission.Action.WRITE, resDatabase);
            Permission pDeleteDatabase = seedPermission(permissionRepository, Permission.Action.DELETE, resDatabase);
            Permission pReadUsers = seedPermission(permissionRepository, Permission.Action.READ, resUsers);
            Permission pWriteUsers = seedPermission(permissionRepository, Permission.Action.WRITE, resUsers);
            Permission pDeleteUsers = seedPermission(permissionRepository, Permission.Action.DELETE, resUsers);
            Permission pReadAudit = seedPermission(permissionRepository, Permission.Action.READ, resAudit);
            Permission pApproveAudit = seedPermission(permissionRepository, Permission.Action.APPROVE, resAudit);

            // ----------------------------------------------------------------
            // 3. Seed Roles
            // ----------------------------------------------------------------
            Roles adminRole = seedRole(roleRepository, "ADMIN", "Full system access",
                    Set.of(pReadReports, pReadDocuments, pWriteDocuments,
                            pReadDatabase, pWriteDatabase, pDeleteDatabase,
                            pReadUsers, pWriteUsers, pDeleteUsers, pReadAudit, pApproveAudit));

            Roles securityRole = seedRole(roleRepository, "SECURITY", "Monitor access and audit logs",
                    Set.of(pReadReports, pReadDocuments, pReadDatabase,
                            pReadUsers, pReadAudit, pApproveAudit));

            Roles hrRole = seedRole(roleRepository, "HR", "Manage employee roles and permissions",
                    Set.of(pReadReports, pReadUsers, pWriteUsers, pReadDocuments));

            Roles employeeRole = seedRole(roleRepository, "EMPLOYEE", "Standard employee access",
                    Set.of(pReadReports, pReadDocuments));

            Roles partnerRole = seedRole(roleRepository, "PARTNER", "Limited external partner access",
                    Set.of(pReadDocuments));

            Roles itRole = seedRole(roleRepository, "IT", "Access control systems implementation and maintenance",
                    Set.of(pReadReports, pReadDocuments, pWriteDocuments,
                            pReadDatabase, pWriteDatabase, pDeleteDatabase,
                            pReadUsers, pWriteUsers, pDeleteUsers, pReadAudit, pApproveAudit));

            // ----------------------------------------------------------------
            // 4. Seed Users
            // ----------------------------------------------------------------
            if (userRepository.count() == 0) {
                seedUser(userRepository, passwordEncoder,
                        "admin", "admin@secureworks.com", "password",
                        User.UserType.EMPLOYEE, User.UserStatus.ACTIVE,
                        Set.of(adminRole));

                seedUser(userRepository, passwordEncoder,
                        "hr_user", "hr@secureworks.com", "password",
                        User.UserType.EMPLOYEE, User.UserStatus.ACTIVE,
                        Set.of(hrRole));

                seedUser(userRepository, passwordEncoder,
                        "security_analyst", "security@secureworks.com", "password",
                        User.UserType.EMPLOYEE, User.UserStatus.ACTIVE,
                        Set.of(securityRole));

                seedUser(userRepository, passwordEncoder,
                        "employee1", "emp1@secureworks.com", "password",
                        User.UserType.EMPLOYEE, User.UserStatus.ACTIVE,
                        Set.of(employeeRole));

                seedUser(userRepository, passwordEncoder,
                        "partner1", "partner1@external.com", "password",
                        User.UserType.PARTNER, User.UserStatus.ACTIVE,
                        Set.of(partnerRole));

                seedUser(userRepository, passwordEncoder,
                        "it_admin", "it@secureworks.com", "password",
                        User.UserType.EMPLOYEE, User.UserStatus.ACTIVE,
                        Set.of(itRole));
            }

            System.out.println("=======================================");
            System.out.println("  DATA SEEDER - System Initialized     ");
            System.out.println("  Credentials (all use 'password'):    ");
            System.out.println("  admin / hr_user / security_analyst   ");
            System.out.println("  employee1 / partner1 / it_admin      ");
            System.out.println("=======================================");

            // ----------------------------------------------------------------
            // 5. Seed Access Logs
            // ----------------------------------------------------------------
            if (accessLogRepository.count() == 0) {
                User adminUser = userRepository.findByUserName("admin").orElse(null);
                User hrUser = userRepository.findByUserName("hr_user").orElse(null);
                User empUser = userRepository.findByUserName("employee1").orElse(null);

                if (adminUser != null && hrUser != null && empUser != null) {
                    seedLog(accessLogRepository, adminUser, "SYSTEM", AccessLogs.Action.LOGIN, LocalDateTime.now().minusHours(4), AccessLogs.Result.SUCCESS);
                    seedLog(accessLogRepository, hrUser, "USERS", AccessLogs.Action.READ, LocalDateTime.now().minusHours(2), AccessLogs.Result.SUCCESS);
                    seedLog(accessLogRepository, empUser, "DATABASE", AccessLogs.Action.WRITE, LocalDateTime.now().minusMinutes(45), AccessLogs.Result.FAILURE);
                    seedLog(accessLogRepository, adminUser, "AUDIT", AccessLogs.Action.READ, LocalDateTime.now().minusMinutes(5), AccessLogs.Result.SUCCESS);
                }
            }
        };
    }

    // ---- Helper Methods ----

    private Resource seedResource(ResourceRepository repo, String name, String description) {
        return repo.findByResourceName(name).orElseGet(() -> {
            Resource r = new Resource();
            r.setResourceName(name);
            r.setResourceDescription(description);
            return repo.save(r);
        });
    }

    private Permission seedPermission(PermissionRepository repo,
            Permission.Action action, Resource resource) {
        return repo.findByActionAndResource(action, resource).orElseGet(() -> {
            Permission p = new Permission();
            p.setAction(action);
            p.setResource(resource);
            return repo.save(p);
        });
    }

    private Roles seedRole(RoleRepository repo, String name, String description,
            Set<Permission> permissions) {
        return repo.findByRoleName(name).orElseGet(() -> {
            Roles role = new Roles();
            role.setRoleName(name);
            role.setRoleDescription(description);
            role.setPermissions(permissions);
            return repo.save(role);
        });
    }

    private void seedUser(UserRepository repo, PasswordEncoder encoder,
            String username, String email, String rawPassword,
            User.UserType type, User.UserStatus status,
            Set<Roles> roles) {
        repo.findByUserName(username).ifPresentOrElse(
            user -> {
                // Force update roles for seeded users to ensure consistency
                user.setRoles(roles);
                repo.save(user);
            },
            () -> {
                User user = new User();
                user.setUserName(username);
                user.setUserEmail(email);
                user.setPassword(encoder.encode(rawPassword));
                user.setUserType(type);
                user.setUserStatus(status);
                user.setRoles(roles);
                repo.save(user);
            }
        );
    }

    private void seedLog(AccessLogRepository repo, User user, String resource, AccessLogs.Action action, LocalDateTime time, AccessLogs.Result result) {
        AccessLogs log = new AccessLogs();
        log.setUser(user);
        log.setResource(resource);
        log.setAction(action);
        log.setTimeStamp(time);
        log.setResult(result);
        repo.save(log);
    }
}
