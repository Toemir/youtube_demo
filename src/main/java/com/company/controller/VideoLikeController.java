package com.company.controller;

import com.company.dto.VideoLikeCreateDTO;
import com.company.dto.VideoLikeInfoDTO;
import com.company.dto.VideoLikeRemoveDTO;
import com.company.dto.video.VideoLikeDTO;
import com.company.dto.video.VideoTagDTO;
import com.company.service.VideoLikeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/video/like")
public class VideoLikeController {
    private final VideoLikeService videoLikeService;

    public VideoLikeController(VideoLikeService videoLikeService) {
        this.videoLikeService = videoLikeService;
    }

    @ApiOperation(value = "Like",notes = "Method for liking a video")
    @PostMapping("/public/like")
    public ResponseEntity<Void> like(@RequestBody @Valid VideoLikeCreateDTO dto) {
        videoLikeService.like(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Like",notes = "Method for liking a video")
    @PostMapping("/public/dislike")
    public ResponseEntity<Void> dislike(@RequestBody @Valid VideoLikeCreateDTO dto) {
        videoLikeService.dislike(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Create",notes = "Method for liking a video")
    @DeleteMapping("/public/remove")
    public ResponseEntity<Void> remove(@RequestBody @Valid VideoLikeRemoveDTO dto) {
        videoLikeService.remove(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Create",notes = "Method for liking a video")
    @GetMapping("/public/list")
    public ResponseEntity<List<VideoLikeInfoDTO>> listForUser() {
        return ResponseEntity.ok().body(videoLikeService.list());
    }

    @ApiOperation(value = "Create",notes = "Method for liking a video")
    @GetMapping("/adm/list")
    public ResponseEntity<List<VideoLikeInfoDTO>> listForAdmin(@RequestParam("p") Integer profileId) {
        return ResponseEntity.ok().body(videoLikeService.list(profileId));
    }
}
