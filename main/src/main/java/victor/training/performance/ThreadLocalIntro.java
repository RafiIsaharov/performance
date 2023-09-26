package victor.training.performance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static victor.training.performance.util.PerformanceUtil.sleepMillis;

@Slf4j
public class ThreadLocalIntro {
    private final AController controller = new AController(new AService(new ARepo()));
    public static void main(String[] args) {
        ThreadLocalIntro app = new ThreadLocalIntro();
        System.out.println("Imagine incoming HTTP requests...");
        app.httpRequest("alice", "alice's data");
    }

    public void httpRequest(String currentUser, String data) {
        log.info("Current user is read from the HTTP Header Bearer (JWT) " + currentUser);
        controller.create(data, currentUser);
    }
    public static String staticCurrentUser;
}
// ---------- end of framework -----------

// ---------- Controller -----------
@RestController
@RequiredArgsConstructor
class AController {
    private final AService service;

    public void create(String data, String username) {
        service.create(data, username);
    }
}

// ----------- Service ------------
@Service
@RequiredArgsConstructor
class AService {
    private final ARepo repo;

    // what is Thread Local used in practice for: metadata:
    // - SecurityContext, @CreatedBy JPA annotation
    // - @Transactional + JDBC Connection
    // - Logback MDC (stuff to appear on every log line, set via MDC.set("prop",value")
    // - TraceID/correlationId
    // - TenantId
    public void create(String data, String username) {
        sleepMillis(10); // some delay, to reproduce the race bug
        repo.save(data, username);
    }
}

// ----------- Repository ------------
@Repository
@Slf4j
class ARepo {
    public void save(String data, String username) {
        String currentUser = username;
        log.info("INSERT INTO A(data, created_by) VALUES ({}, {})", data, currentUser);
    }
}
