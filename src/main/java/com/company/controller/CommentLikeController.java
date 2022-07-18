package com.company.controller;

import com.company.dto.CommentLikeCreateDTO;
import com.company.dto.CommentLikeDTO;
import com.company.service.CommentLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/comment/like")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService) {
        this.commentLikeService = commentLikeService;
    }

    @PostMapping("/public")
    public ResponseEntity<Void> like(@RequestBody @Valid CommentLikeCreateDTO dto) {
        commentLikeService.like(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/public/dislike")
    public ResponseEntity<Void> dislike(@RequestBody @Valid CommentLikeCreateDTO dto) {
        commentLikeService.dislike(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/public/remove")
    public ResponseEntity<Void> remove(@RequestParam("p") Integer profileId,
                                       @RequestParam("c") Integer commentId) {
        commentLikeService.remove(profileId,commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public/list")
    public ResponseEntity<List<CommentLikeDTO>> list() {
        return ResponseEntity.ok(commentLikeService.list());
    }

    @GetMapping("/adm/list")
    public ResponseEntity<List<CommentLikeDTO>> list(@RequestParam("p") Integer profileId) {
        return ResponseEntity.ok(commentLikeService.list(profileId));
    }

}
