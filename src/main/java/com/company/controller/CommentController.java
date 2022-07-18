package com.company.controller;

import com.company.dto.comment.CommentCreateDTO;
import com.company.dto.comment.CommentDTO;
import com.company.dto.comment.CommentInfoDTO;
import com.company.dto.comment.CommentUpdateDTO;
import com.company.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Create",notes = "Method for creating a comment")
    @PostMapping("/public/create")
    public ResponseEntity<Void> create(@RequestBody @Valid CommentCreateDTO dto) {
        commentService.create(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "update",notes = "Method for updating a comment")
    @PutMapping("/public/update")
    public ResponseEntity<Void> update(@RequestParam("c") Integer id,
                                       @RequestBody @Valid CommentUpdateDTO dto) {
        commentService.update(id,dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete",notes = "Method for deleting a comment")
    @DeleteMapping("/public/delete")
    public ResponseEntity<Void> delete(@RequestParam("c") Integer id) {
        commentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "pagination",notes = "Method for pagination")
    @GetMapping("/adm/pagination")
    public ResponseEntity<List<CommentDTO>> pagination(@RequestParam("size") int size,
                                                       @RequestParam("page") int page) {
        return ResponseEntity.ok().body(commentService.pagination(size,page));
    }

    @ApiOperation(value = "get by profile",notes = "Method for getting comment list by profile")
    @GetMapping("/public/profile/list")
    public ResponseEntity<List<CommentDTO>> listByProfile() {
        return ResponseEntity.ok().body(commentService.list());
    }

    @ApiOperation(value = "get by video",notes = "Method for getting comment list by video")
    @GetMapping("/public/video/list")
    public ResponseEntity<List<CommentInfoDTO>> listByVideo(@RequestParam("v") String id) {
        return ResponseEntity.ok().body(commentService.listByVideo(id));
    }

    @ApiOperation(value = "get reply list by comment",notes = "Method for getting reply list by comment id")
    @GetMapping("/public/reply/list")
    public ResponseEntity<List<CommentInfoDTO>> replyListByComment(@RequestParam("c") Integer id) {
        return ResponseEntity.ok().body(commentService.replyListByComment(id));
    }




}
