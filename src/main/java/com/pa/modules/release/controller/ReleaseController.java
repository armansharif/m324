package com.pa.modules.release.controller;

import com.pa.config.JsonResponseBodyTemplate;

import com.pa.modules.release.model.ReleaseApps;
import com.pa.modules.release.service.ReleaseService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ReleaseController {

    private ReleaseService releaseService;

    @Autowired
    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }


    @GetMapping("/download")
    public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
        Path path = Paths.get("/upload/apk/" + "m324.apk");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.android.package-archive"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping(value = {"/admin/release", "/admin/release/"})
    public ResponseEntity<Object> getRelease(HttpServletResponse response) {
        try {
            List<ReleaseApps> releaseAppsList = releaseService.findAllRelease();
            return ResponseEntity.ok()
                    .body(releaseAppsList);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping(value = {"/admin/release", "/admin/release/"})
    // @PreAuthorize("hasAuthority('OP_ACCESS_ADD')")
    public ResponseEntity<Object> addRelease(@NotNull @ModelAttribute ReleaseApps releaseApps, HttpServletResponse response) {
        try {
            ReleaseApps release_Apps_saved = releaseService.addRelease(releaseApps);
            return ResponseEntity.ok()
                    .body(release_Apps_saved);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
