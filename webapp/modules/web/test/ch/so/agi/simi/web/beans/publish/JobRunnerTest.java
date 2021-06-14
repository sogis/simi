package ch.so.agi.simi.web.beans.publish;

import ch.so.agi.simi.web.beans.style.StyleDbContent;
import ch.so.agi.simi.web.beans.style.StyleStorageBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JobRunnerTest {

    //@Test
    public void job_startInsideTimeWindow_OK(){
        JobRunner runner = new JobRunner();

        boolean inTime = runner.runJob(new PublishConfigForTest(
                "http://localhost:8082/job/confjob",
                10000,
                "mytoken"
        ));

        Assertions.assertTrue(inTime, "Job must start before timeout");
    }

    //@Test
    public void job_startOutsideTimeWindow_OK(){
        JobRunner runner = new JobRunner();

        boolean inTime = runner.runJob(new PublishConfigForTest(
                "http://localhost:8082/job/confjob",
                0,
                "mytoken"
        ));

        Assertions.assertFalse(inTime, "Job must not start before timeout");
    }

    //@Test
    public void job_withWrongUrl_Throws(){
        JobRunner runner = new JobRunner();

        PublishConfig conf = new PublishConfigForTest(
                "http://fuubar:8082/job/confjob",
                5000,
                "mytoken");

        Assertions.assertThrows(RuntimeException.class, () -> runner.runJob(conf));
    }
}
