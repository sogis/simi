package ch.so.agi.simi.web.beans.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component(JobRunner.NAME)
public class JobRunner {
    public static final String NAME = "simi_JobRunner";

    private static final int POLL_INTERVAL_MILLIS = 500;

    private static final String START_SUFFIX = "/buildWithParameters?token={token}&cause={cause}&uuid={uuid}";

    private static final String STATUS_SUFFIX = "/api/json";

    private static final Logger log = LoggerFactory.getLogger(JobRunner.class);

    private RestTemplate restTemplate = new RestTemplate();

    public boolean runJob(PublishConfig config){
        UUID newJobId = queueJob(config);

        boolean startedInTime = pollForStart(newJobId, config);

        log.info("Ran job with url {} and timeout {}. Boolean startedInTime: {}", config.getJobBaseUrl(), config.getTimeOutMillis(), startedInTime);
        return startedInTime;
    }

    private UUID queueJob(PublishConfig config){


        UUID uid = UUID.randomUUID();

        Map<String, String> para = new HashMap<>();
        para.put("token", config.getSecToken());
        para.put("cause", buildCauseString());
        para.put("uuid", uid.toString());

        try {
            String startUrl = config.getJobBaseUrl() + START_SUFFIX;

            ResponseEntity<String> resp = restTemplate.getForEntity(
                    startUrl,
                    String.class,
                    para);

            assertResponseStatus(resp);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        return uid;
    }

    private boolean pollForStart(UUID jobId, PublishConfig config){

        boolean started = false;

        try{
            String statusUrl = config.getJobBaseUrl() + STATUS_SUFFIX;

            int sumWaitTime = 0;
            while (sumWaitTime + POLL_INTERVAL_MILLIS < config.getTimeOutMillis()){
                long start = Instant.now().toEpochMilli();

                ResponseEntity<String> resp = restTemplate.getForEntity(statusUrl, String.class);
                assertResponseStatus(resp);

                started = hasUuid(resp.getBody(), jobId);
                if(started)
                    break;

                Thread.sleep(POLL_INTERVAL_MILLIS);

                int totalDurationMillis = (int)(Instant.now().toEpochMilli() - start);
                sumWaitTime = sumWaitTime + totalDurationMillis;
                log.debug("sumWaitTime {}, totalDurationMillis {}", sumWaitTime, totalDurationMillis);
            }
            Thread.sleep(500);
            log.debug("Waited on jenkins job for {} millis. Job started? {}. Timoutsetting {}", sumWaitTime, started, config.getTimeOutMillis());
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        return started;
    }

    private static boolean hasUuid(String responseBody, UUID jobUid){
        //json structure is complicated --> brute forcing with String.contains()
        if(responseBody == null || jobUid == null)
            return false;

        String uidString = jobUid.toString();
        boolean contained = responseBody.contains(uidString);

        return contained;
    }

    private static void assertResponseStatus(ResponseEntity<String> resp){
        if(resp.getStatusCodeValue() > 399) {
            throw new RuntimeException(MessageFormat.format(
                    "Server returned error http status code {0}",
                    resp.getStatusCode().toString()
            ));
        }
    }

    private String buildCauseString(){

        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        return MessageFormat.format("Beauftragt via simi am {0} um {1}", date, time);
    }
}