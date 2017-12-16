package com.tefnut.dev.scheduled;

import com.tefnut.dev.constants.ApiConstants;
import com.tefnut.dev.models.Patch;
import com.tefnut.dev.repository.LoLPatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.tefnut.dev.constants.ApiConstants.PATCH_URL;

@Component
public class DBUpdateScheduler {

    public static final Logger logger = LoggerFactory.getLogger(DBUpdateScheduler.class);
    private static final String LEAGUE_PATCH_VERSION_ENV = "lol_patch_version";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    LoLPatchRepository loLPatchRepository;

    /**
     * This function runs on a fixed time. TODO: Discuss timing of how often to check for updates
     * So next I do an http call to get the info I need. Compare it to the system variable.
     * If it is equal, then just end the function. If not,update system variable, then send call to update db function.
     */
    @Scheduled(fixedRate = ApiConstants.UPDATE_RATE)
    public void checkForDBUpdate() {
        System.setProperty("java.net.preferIPv4Stack" , "true");

        String latestPatch = fetchLatestPatchNumber();

        loLPatchRepository.findByGameNameOrderByIdDesc(ApiConstants.GAME_NAME)
                          .map(Patch::getCurrentPatch)
                          .filter(p -> !latestPatch.equals(p))
                          .subscribe(p -> {
                                logger.info("CURRENT PATCH IS: " + latestPatch);
                                updatePatch(latestPatch);
                                updateItems();
                                updateChamps();
                          });
    }

    private String fetchLatestPatchNumber() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<String>> request = new HttpEntity<>(getHeaders());
        ResponseEntity<List> response = restTemplate.exchange(PATCH_URL, HttpMethod.GET, request, List.class);
        List<String> patches = response.getBody();
        return patches.get(0);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(ApiConstants.RIOT_API_KEY, ApiConstants.RIOT_API_VALUE);
        return headers;
    }

    private void updatePatch(String latestPatch) {
        Patch newPatch = new Patch();
        newPatch.setCurrentPatch(latestPatch);
        newPatch.setGameName(ApiConstants.GAME_NAME);
        loLPatchRepository.save(newPatch);
    }

    private void updateItems() {

    }

    private void updateChamps() {

    }
}
