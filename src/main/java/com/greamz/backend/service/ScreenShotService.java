package com.greamz.backend.service;

import com.greamz.backend.model.Screenshot;
import com.greamz.backend.repository.IScreenshotRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScreenShotService {
    private final IScreenshotRepo screenshotRepo;
    public void saveScreenshot(Screenshot screenshot) {
        screenshotRepo.save(screenshot);
    }
    public void saveAllScreenshots(List<Screenshot> screenshots) {
        screenshotRepo.saveAll(screenshots);
    }
}
