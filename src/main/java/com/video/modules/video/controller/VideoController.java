package com.video.modules.video.controller;

import com.video.config.JsonResponseBodyTemplate;
import com.video.modules.video.model.LocationPath;
import com.video.modules.video.model.Video;
import com.video.modules.video.service.LocationPathService;
import com.video.modules.video.service.VideoService;
import com.video.utils.MultipartFileSender;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json")

public class VideoController {


//    private final Path rootVideos = Paths.get("/usr/local/tomcat9/webapps/rm/upload/videos");//Paths.get(rootVideosProperty);//
//    private final Path rootImages =Paths.get("/usr/local/tomcat9/webapps/rm/upload/images");// Paths.get(rootImagesProperty);//


    /*
    private final Path rootVideos = Paths.get("F:\\Personal\\uploads\\videos");
    private final Path rootImages = Paths.get("F:\\Personal\\uploads\\images");
    */
    //private final String pathVideosString = "/usr/local/tomcat9/webapps/rm/upload/videos";
    private VideoService videoService;
    private LocationPathService locationPathService;
    private Environment env;
    private Path rootImages;

    @Autowired
    public VideoController(VideoService videoService, LocationPathService locationPathService, Environment env) {
        this.videoService = videoService;
        this.locationPathService = locationPathService;
        this.env = env;
    }

    @GetMapping(value = {"/videoOld", "/videoOld/"})
    public ResponseEntity<Object> findAllVideo(
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int perPage,
            HttpServletResponse response) {
        try {
            List<Video> VideoList = this.videoService.findAllVideo(sort, page, perPage);
            return ResponseEntity.ok()
                    .body(VideoList);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/play_video/{video_id}")
    @ResponseBody
    public ResponseEntity<FileSystemResource> stream(@PathVariable("video_id") String video_id) {
        String rootVideosProperty = env.getProperty("road.marking.rootVideosProperty");
        String filePathString = rootVideosProperty + "/" + video_id + ".mp4";
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "video/mp4");
        return new ResponseEntity<>(new FileSystemResource(filePathString), responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/play_video2/{video_id}")
    public void stream2(@PathVariable("video_id") String video_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rootVideosProperty = env.getProperty("road.marking.rootVideosProperty");
        String filePathString = rootVideosProperty + "/" + video_id + ".mp4";
        MultipartFileSender.fromPath(Paths.get(filePathString))
                .with(request)
                .with(response)
                .serveResource();
    }


    @DeleteMapping(value = {"/video", "/video/"})
    public ResponseEntity<Object> deleteUsers(@RequestParam(required = true) Long id, HttpServletResponse response) {
        try {
            videoService.deleteVideo(id);
            return ResponseEntity.ok()
                    .body(JsonResponseBodyTemplate
                            .createResponseJson("success", response.getStatus(), "کاربر با موفقیت حذف شد").toString());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping(value = {"/video", "/video/"})
    public ResponseEntity<Object> addVideo(String json, MultipartFile fileImage, MultipartFile fileVideo, HttpServletResponse response) {
        try {

            String rootVideosProperty = env.getProperty("road.marking.rootVideosProperty");
            String rootImagesProperty = env.getProperty("road.marking.rootImagesProperty");
            String urlVideos = env.getProperty("road.marking.urlVideosProperty");
            String urlImages = env.getProperty("road.marking.urlImagesProperty");


            Path rootVideos = Paths.get(rootVideosProperty);
            Path rootImages = Paths.get(rootImagesProperty);
            //String urlVideos = "http://www.httpamlakradical.ir/play_video";//urlVideosProperty;//
            //String urlImages = "http://www.httpamlakradical.ir/upload/images";//urlImagesProperty;//


            JSONObject video_JsonObject = new JSONObject(json);
            Video video = new Video();
            if(video_JsonObject.has("city"))
            video.setCity((video_JsonObject.getString("city")) == null ? "" : video_JsonObject.getString("city"));

            if(video_JsonObject.has("device_id"))
            video.setDeviceId((video_JsonObject.getString("device_id")) == null ? "" : video_JsonObject.getString("device_id"));
            if (fileImage != null)
                video.setFileImage(fileImage);
            if (fileVideo != null)
                video.setFileVideo(fileVideo);

            Video video_saved = videoService.addVideo(video, rootVideos, rootImages, urlVideos, urlImages);
            JSONArray locationPathArray = new JSONArray(video_JsonObject.getJSONArray("location_path"));
            List<LocationPath> locationPathList = new ArrayList<LocationPath>();
            for (Object o : locationPathArray) {
                if (o instanceof JSONObject) {
                    LocationPath locationPath = new LocationPath();
                    locationPath.setLat((((JSONObject) o).getString("lat")) == null ? "" : ((JSONObject) o).getString("lat"));
                    locationPath.setLng((((JSONObject) o).getString("lng") == null ? "" : ((JSONObject) o).getString("lng")));
                    locationPath.setTime((((JSONObject) o).getString("time") == null ? "" : ((JSONObject) o).getString("time")));
                    locationPath.setTitle((((JSONObject) o).getString("title") == null ? "" : ((JSONObject) o).getString("title")));
                    locationPath.setAddress((((JSONObject) o).getString("address") == null ? "" : ((JSONObject) o).getString("address")));
                    locationPath.setDescription((((JSONObject) o).getString("description") == null ? "" : ((JSONObject) o).getString("description")));
                    locationPath.setVideo(video_saved);
                    locationPathService.addLocationPath(locationPath);
                }
            }

            return ResponseEntity.ok()
                    .body(video_saved);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping(value = {"/video2", "/video2/"})
//    public Page<Video> findVideos(
//            @And({
//                    @Spec(path = "deviceId", spec = Equal.class)
//            }) Specification<Video> videoSpec,
//            Pageable pageable) {
//
//        return videoService.findAll(videoSpec, pageable);
//    }

    @GetMapping(value = {"/video", "/video/"})
    public ResponseEntity<Object> findVideo(
            @And({
                    @Spec(path = "deviceId", spec = Equal.class),
                    @Spec(path = "city", spec = Like.class)
            }) Specification<Video> videoSpec,

            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int perPage,
            HttpServletResponse response) {
        try {
            List<Video> VideoList = this.videoService.findAll(sort, page, perPage, videoSpec);
            return ResponseEntity.ok()
                    .body(VideoList);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
