package com.dam.modules.video.controller;

import com.dam.config.JsonResponseBodyTemplate;
import com.dam.modules.jwt.JwtUtils;
import com.dam.modules.user.model.Users;
import com.dam.modules.user.service.UserService;
import com.dam.modules.video.model.LocationPath;
import com.dam.modules.video.model.Video;
import com.dam.modules.video.service.LocationPathService;
import com.dam.modules.video.service.VideoService;
import com.dam.utils.MultipartFileSender;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private UserService userService;
    private Environment env;
    private Path rootImages;
    private final JwtUtils jwtUtils;

    @Autowired
    public VideoController(VideoService videoService, LocationPathService locationPathService, UserService userService, Environment env, JwtUtils jwtUtils) {
        this.videoService = videoService;
        this.locationPathService = locationPathService;
        this.userService = userService;
        this.env = env;
        this.jwtUtils = jwtUtils;
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

    @GetMapping(value = "/csv/{video_id}")
    @ResponseBody
    public ResponseEntity<FileSystemResource> csv(@PathVariable("video_id") String video_id) {
        String rootCSVProperty = env.getProperty("road.marking.rootCSVProperty");
        String filePathString = rootCSVProperty + "/" + video_id + ".csv";
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/csv");
        responseHeaders.add("Content-Disposition", "attachment; filename=\"" + video_id + ".csv" + "\"");
        return new ResponseEntity<>(new FileSystemResource(filePathString), responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/csv_create")
    @ResponseBody
    public ResponseEntity<Object> csvCreate(HttpServletResponse response) {


        try {
            List<Video> VideoList = this.videoService.findAllVideo("id", 0, 1000);
            for (Video v : VideoList) {
                String fileName = "";
                if (v.getVideoUrl() != null) {
                    String[] paths = v.getVideoUrl().split("/");
                    if (paths.length > 1) {
                        fileName = paths[paths.length - 1];
                    }
                }
                if (fileName.length() > 0) {
                    List<LocationPath> locationPathList = v.getLocationPath();
                    String rootCSVProperty = env.getProperty("road.marking.rootCSVProperty");
                    FileWriter fileWriter = new FileWriter(rootCSVProperty + System.getProperty("file.separator") + fileName + ".csv");
                    CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);
                    csvPrinter.printRecord("lat",
                            "lang",
                            "time",
                            "title",
                            "address",
                            "description");
                    String urlCSV = env.getProperty("road.marking.urlCSVProperty");
                    v.setCsvUrl(urlCSV + "/" + fileName);
                    v.setVideoFileName(fileName);
                    videoService.saveVideo(v);
                    for (LocationPath l : locationPathList) {
                        csvPrinter.printRecord(l.getLat(),
                                l.getLng(),
                                l.getTime(),
                                l.getTitle(),
                                l.getAddress(),
                                l.getDescription());
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    csvPrinter.close();
                }
            }

            return ResponseEntity.ok()
                    .body(JsonResponseBodyTemplate
                            .createResponseJson("success", response.getStatus(), "create CSVs successfully").toString());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @DeleteMapping(value = {"/video", "/video/"})
    public ResponseEntity<Object> deleteVideo(@RequestParam(required = true) Long id, HttpServletResponse response) {
        try {
            videoService.deleteVideo(id);
            return ResponseEntity.ok()
                    .body(JsonResponseBodyTemplate
                            .createResponseJson("success", response.getStatus(), "Video deleted successfully").toString());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping(value = {"/video", "/video/"})
    public ResponseEntity<Object> addVideo(String json, MultipartFile fileImage, MultipartFile fileVideo, HttpServletResponse response, HttpServletRequest request) {
        try {

            String rootVideosProperty = env.getProperty("road.marking.rootVideosProperty");
            String rootImagesProperty = env.getProperty("road.marking.rootImagesProperty");
            String urlVideos = env.getProperty("road.marking.urlVideosProperty");
            String urlImages = env.getProperty("road.marking.urlImagesProperty");

            String urlCSV = env.getProperty("road.marking.urlCSVProperty");

            Path rootVideos = Paths.get(rootVideosProperty);
            Path rootImages = Paths.get(rootImagesProperty);
            //String urlVideos = "http://www.httpamlakradical.ir/play_video";//urlVideosProperty;//
            //String urlImages = "http://www.httpamlakradical.ir/upload/images";//urlImagesProperty;//


            JSONObject video_JsonObject = new JSONObject(json);
            Video video = new Video();
            if (video_JsonObject.has("city"))
                video.setCity((video_JsonObject.getString("city")) == null ? "" : video_JsonObject.getString("city"));


            if (video_JsonObject.has("created_at"))
                video.setCreatedAt((video_JsonObject.getString("created_at")) == null ? "" : video_JsonObject.getString("created_at"));

            Long user_id = jwtUtils.getUserId(request);
            if (user_id != null) {
                Optional<Users> user = this.userService.findUser(user_id);
                if (user.isPresent()) {
                    video.setUsers(user.get());
                }
            }


            if (video_JsonObject.has("device_id"))
                video.setDeviceId((video_JsonObject.getString("device_id")) == null ? "" : video_JsonObject.getString("device_id"));
            if (fileImage != null)
                video.setFileImage(fileImage);
            if (fileVideo != null)
                video.setFileVideo(fileVideo);


            Video video_saved = videoService.addVideo(video, rootVideos, rootImages, urlVideos, urlImages, urlCSV);
            JSONArray locationPathArray = new JSONArray(video_JsonObject.getJSONArray("location_path"));
            List<LocationPath> locationPathList = new ArrayList<LocationPath>();
            String rootCSVProperty = env.getProperty("road.marking.rootCSVProperty");
            FileWriter fileWriter = new FileWriter(rootCSVProperty + System.getProperty("file.separator") + video_saved.getVideoFileName() + ".csv");
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);
//                for (Employee employee : employees) {
//                    csvPrinter.printRecord(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getDepartment());
//                }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String createdAtServer = video.getCreatedAtServer().format(formatter);

            String createdAt= video.getCreatedAt() ;

            csvPrinter.printRecord("lat",
                    "lang",
                    "time",
                    "createdAt",
                    "createdAtServer",
                    "title",
                    "address",
                    "description");
            for (Object o : locationPathArray) {
                if (o instanceof JSONObject) {
                    LocationPath locationPath = new LocationPath();
                    csvPrinter.printRecord((((JSONObject) o).getString("lat")) == null ? "" : ((JSONObject) o).getString("lat"),
                            (((JSONObject) o).getString("lng") == null ? "" : ((JSONObject) o).getString("lng")),
                            (((JSONObject) o).getString("time") == null ? "" : ((JSONObject) o).getString("time")),
                            (createdAt == null ? "" : createdAt),
                            (createdAtServer == null ? "" : createdAtServer),
                            (((JSONObject) o).getString("title") == null ? "" : ((JSONObject) o).getString("title")),
                            (((JSONObject) o).getString("address") == null ? "" : ((JSONObject) o).getString("address")),
                            (((JSONObject) o).getString("description") == null ? "" : ((JSONObject) o).getString("description")));
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

            fileWriter.flush();
            fileWriter.close();
            csvPrinter.close();


            return ResponseEntity.ok()
                    .body(video_saved);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


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
            System.out.println("==================== videoService.findAll");
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

    @PutMapping(value = {"/video/check", "/video/check/"})
    public ResponseEntity<Object> checkVideo(
            @RequestParam Long video_id,
            @RequestParam boolean is_check,
            HttpServletResponse response) {
        try {
            Video video = videoService.findVideo(video_id).orElse(null);
            if (video != null) {

                 video.setChecked(is_check);
                 videoService.saveVideo(video);
                return ResponseEntity.ok()
                        .body(video);
            }else{
                return new ResponseEntity<>(
                        JsonResponseBodyTemplate.
                                createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), "video not found").toString(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
