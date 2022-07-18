package com.company.controller;

import com.company.dto.video.VideoTagDTO;
import com.company.service.VideoTagService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/video/tag")
public class VideoTagController {
    private final VideoTagService videoTagService;

    public VideoTagController(VideoTagService videoTagService) {
        this.videoTagService = videoTagService;
    }

    @ApiOperation(value = "Create",notes = "Method for adding a tag to a video")
    @PostMapping("/public/create")
    public ResponseEntity<Void> addTagToVideo(@RequestBody @Valid VideoTagDTO dto) {
        videoTagService.save(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete",notes = "Method for updating a video tag")
    @DeleteMapping("/public/delete")
    public ResponseEntity<Void> delete(@RequestBody @Valid VideoTagDTO dto) {
        videoTagService.delete(dto);
        return ResponseEntity.ok().build();
    }



    @ApiOperation(value = "Get video tag list",notes = "Method for getting a video tag list by video id")
    @GetMapping("/public/list")
    public ResponseEntity<List<VideoTagDTO>> list(@RequestParam("v") String videoId) {
        return ResponseEntity.ok().body(videoTagService.list(videoId));
    }
}
