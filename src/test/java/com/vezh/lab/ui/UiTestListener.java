package com.vezh.lab.ui;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@Slf4j
public class UiTestListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String fileName = "error_" + context.getTestMethod().get().getName();
        String pngName = Selenide.screenshot(fileName);
        log.info("Error screenshot has been saved to: " + pngName);
        saveScreenshotPNG(fileName);
    }

    /**
     * Saves screenshot as PNG attachment to Allure
     * @param filename - filename of PNG file
     * @return
     */
    @Step("Attach screenshot")
    @SneakyThrows
    @Attachment(value = "Page Screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(String filename) {
        File file = new File("./build/reports/tests/" + filename + ".png");
        BufferedImage bufferedImage = ImageIO.read(file);

        byte[] image = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", bos);
            image = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }
}
