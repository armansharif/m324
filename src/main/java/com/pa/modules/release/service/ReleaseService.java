package com.pa.modules.release.service;


import com.pa.modules.release.model.ReleaseApps;
import com.pa.modules.release.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class ReleaseService {

    private ReleaseRepository releaseRepository;
    private Environment env;
    @Autowired
    public ReleaseService(ReleaseRepository releaseRepository, Environment env) {
        this.releaseRepository = releaseRepository;
        this.env = env;
    }

    public ReleaseApps addRelease(ReleaseApps releaseApps) throws IOException {
        String urlReleaseFile = env.getProperty("conf.urlReleaseFile");
        String rootReleaseFileStr = env.getProperty("conf.rootReleaseFile");
        Path rootReleaseFile = Paths.get(rootReleaseFileStr);
        String path = rootReleaseFile.toFile().getAbsolutePath();
        if (releaseApps.getFile() != null) {
            if (releaseApps.getFile().getSize() > 0) {
                byte[] bytes = releaseApps.getFile().getBytes();
                String fileName = releaseApps.getVersionNumber() + "." + Objects.requireNonNull(releaseApps.getFile().getContentType()).split("/")[1];
                Files.write(Paths.get(path + File.separator + fileName), bytes);
                releaseApps.setReleaseFile(urlReleaseFile + "/" + fileName+ "." + Objects.requireNonNull(releaseApps.getFile().getContentType()).split("/")[1]);
            }
        }
        return this.releaseRepository.save(releaseApps);
    }
    public List<ReleaseApps> findAllRelease() {
        return this.releaseRepository.findAll();
    }
}
