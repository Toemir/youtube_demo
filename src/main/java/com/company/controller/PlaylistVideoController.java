package com.company.controller;

import com.company.dto.playlist.PlaylistVideoDTO;
import com.company.dto.playlist.PlaylistVideoDeleteDTO;
import com.company.dto.playlist.PlaylistVideoInfoDTO;
import com.company.service.PlaylistVideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/playlist/video")
public class PlaylistVideoController {
    @Autowired
    @Lazy
    private PlaylistVideoService playlistVideoService;

//    public PlaylistVideoController(PlaylistVideoService playlistVideoService) {
//        this.playlistVideoService = playlistVideoService;

//    }
    @ApiOperation(value = "Create", notes="Method for creating a playlist video")
    @PostMapping("/public/create")
    public ResponseEntity<String> create(@RequestBody @Valid PlaylistVideoDTO dto) {
        return ResponseEntity.ok(playlistVideoService.create(dto));

    }

    @ApiOperation(value = "Update", notes="Method for updating a playlist video")
    @PutMapping("/public/update")
    public ResponseEntity<String> update(@RequestBody @Valid PlaylistVideoDTO dto) {
        return ResponseEntity.ok(playlistVideoService.update(dto));
    }

    @ApiOperation(value = "Delete", notes="Method for deleting a playlist video")
    @DeleteMapping("/public/delete")
    public ResponseEntity<String> delete(@RequestBody @Valid PlaylistVideoDeleteDTO dto) {
        return ResponseEntity.ok(playlistVideoService.delete(dto));
    }

    @ApiOperation(value = "Get video list by playlist", notes="Method for getting  video")
    @GetMapping("/public/playlist")
    public ResponseEntity<List<PlaylistVideoInfoDTO>> list(@RequestParam("list") String playlistId) {
        return ResponseEntity.ok(playlistVideoService.list(playlistId));
    }
}
