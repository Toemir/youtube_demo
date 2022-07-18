package com.company.controller;

import com.company.dto.ReportCreateDTO;
import com.company.dto.ReportDTO;
import com.company.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/public")
    public ResponseEntity<Void> report(@RequestBody @Valid ReportCreateDTO dto) {
        reportService.create(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/adm/pagination")
    public ResponseEntity<List<ReportDTO>> pagination(@RequestParam("size") int size,
                                                  @RequestParam("page") int page) {
        return ResponseEntity.ok(reportService.pagination(size,page));
    }

    @GetMapping("/adm/list")
    public ResponseEntity<List<ReportDTO>> pagination(@RequestParam("p") Integer profileId){
        return ResponseEntity.ok(reportService.list(profileId));
    }


}
