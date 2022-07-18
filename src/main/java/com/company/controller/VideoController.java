package com.company.controller;

import com.company.dto.playlist.PlaylistVideoInfoDTO;
import com.company.dto.video.*;
import com.company.service.VideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Controller
@RequestMapping("api/v1/video")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @ApiOperation(value = "Create", notes="Method for creating a video")
    @PostMapping("/public/create")
    public ResponseEntity<String> create(@RequestBody @Valid VideoDTO dto) {
        return ResponseEntity.ok(videoService.create(dto));
    }

    @ApiOperation(value = "Update", notes="Method for updating a video")
    @PutMapping("/public/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") String id,
                                         @RequestBody @Valid VideoUpdateDTO dto) {
        return ResponseEntity.ok(videoService.update(dto,id));
    }

    @ApiOperation(value = "Change status", notes="Method for changing a video's status")
    @PutMapping("/public/change/status/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") String id,
                                         @RequestBody @Valid VideoStatusDTO dto) {
        return ResponseEntity.ok(videoService.changeStatus(dto,id));
    }

//    @ApiOperation(value = "increase view", notes="Method for increasing video's view count")
//    @PutMapping("/public/increase/view_count/{id}")
//    public ResponseEntity<String> changeStatus(@PathVariable("id") String id) {
//        return ResponseEntity.ok(videoService.increaseViewCount(id));
//    }

    @ApiOperation(value = "pagination_by_category", notes="Method for pagination_by_category")
    @GetMapping("/public/pagination/category")
    public ResponseEntity<List<VideoShortInfoDTO>> paginationByCategory(@RequestParam("c") Integer id,
                                                                        @RequestParam("size") int size,
                                                                        @RequestParam("page") int page) {
        return ResponseEntity.ok(videoService.paginationByCategory(id,size,page));
    }

    @ApiOperation(value = "pagination_by_tag", notes="Method for pagination_by_tag")
    @GetMapping("/public/pagination/tag")
    public ResponseEntity<List<VideoShortInfoDTO>> paginationByTag(@RequestParam("t") Integer id,
                                                                   @RequestParam("size") int size,
                                                                   @RequestParam("page") int page) {
        return ResponseEntity.ok(videoService.paginationByTag(id,size,page));
    }

    @ApiOperation(value = "getByName", notes="Method for getting a video by name")
    @GetMapping("/public/get_by_name")
    public ResponseEntity<List<VideoShortInfoDTO>> getByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(videoService.getByName(name));
    }

    @ApiOperation(value = "Get Video", notes="Method for getting a video by id")
    @GetMapping("/public/{id}")
    public ResponseEntity<VideoFullInfoDTO> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(videoService.getById(id));
    }

    @ApiOperation(value = "Get Playlist video list", notes="Method for getting a video by id")
    @GetMapping("/public/channel")
    public ResponseEntity<List<ChannelVideoInfoDTO>> getByPlaylistVideoList(@RequestParam("c") String id,
                                                                             @RequestParam("size") int size,
                                                                             @RequestParam("page") int page) {
        return ResponseEntity.ok(videoService.getByPlaylistVideoList(id,size,page));
    }






}
