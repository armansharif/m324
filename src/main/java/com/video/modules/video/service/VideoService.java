package com.video.modules.video.service;

import com.video.modules.video.model.Video;
import com.video.modules.video.repository.LocationPathRepository;
import com.video.modules.video.repository.VideoRepository;
import liquibase.util.file.FilenameUtils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class VideoService {

    private static final Logger LOG = Logger.getLogger(VideoService.class.getName());
    private VideoRepository videoRepository;

    private LocationPathRepository locationPathRepository;
    private Java2DFrameConverter aa;

    @Autowired
    public VideoService(VideoRepository videoRepository, LocationPathRepository locationPathRepository) {
        this.videoRepository = videoRepository;
        this.locationPathRepository = locationPathRepository;
    }

    public List<Video> findAllVideo(String sort,
                                    int page,
                                    int perPage) {
        Pageable videoSortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).ascending());

        return videoRepository.getVideos(videoSortedAndPagination);
    }

    @Transactional
    public Video addVideo(Video video, Path rootVideos, Path rootImages, String urlVideos, String urlImages) throws IOException {

        Video video_saved = this.videoRepository.save(video);
        String videoPath = rootVideos.toFile().getAbsolutePath();
        String imagePath = rootImages.toFile().getAbsolutePath();
        if (video.getFileVideo() != null) {
            MultipartFile file = video.getFileVideo();
            byte[] bytes = file.getBytes();
            // String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
            String fileName = UUID.randomUUID() + "";

            Files.write(Paths.get(videoPath + File.separator + fileName + ".mp4"), bytes);
            video.setVideoUrl(urlVideos + "/" + fileName);

            String imageFileName = UUID.randomUUID() + "";
            if (video.getFileImage() == null || video.getFileImage().isEmpty()) {
//                try {
//                    FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoPath + File.separator + fileName + ".mp4");
//                    frameGrabber.start();
//                    Java2DFrameConverter aa = new Java2DFrameConverter();
//                    BufferedImage bi;
//                    Frame f = frameGrabber.grabKeyFrame();
//                    bi = aa.convert(f);
//                    while (bi != null) {
//                        ImageIO.write(bi, "png", new File(imagePath + File.separator + imageFileName + ".png"));
//                        f = frameGrabber.grabKeyFrame();
//                        bi = aa.convert(f);
//                    }
//                    frameGrabber.stop();
//                    video.setBackgroundImageUrl(urlImages + "/" + imageFileName + ".png");
//                } catch (Exception e) {
//                }
            } else {
                MultipartFile fileImage = video.getFileImage();
                byte[] bytesImages = fileImage.getBytes();
                String fileNameImage = UUID.randomUUID() + "." + Objects.requireNonNull(fileImage.getContentType()).split("/")[1];
                Files.write(Paths.get(imagePath + File.separator + fileNameImage), bytesImages);
                video.setBackgroundImageUrl(urlImages + "/" + fileNameImage);

            }

        }


        return video_saved;
    }

    public void deleteVideo(Long id) throws Exception {
        this.videoRepository.deleteById(id);
    }

    public List<Video> findAll(String sort,
                               int page,
                               int perPage,
                               Specification<Video> videoSpec) {
        Pageable videoSortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).ascending());
        return videoRepository.findAll(videoSpec, videoSortedAndPagination);
    }
}
