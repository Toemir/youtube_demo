package com.company.controller;

import com.company.dto.SubscriptionCreateDTO;
import com.company.dto.SubscriptionInfoDTO;
import com.company.dto.SubscriptionUpdateNotificationTypeDTO;
import com.company.dto.SubscriptionUpdateStatusDTO;
import com.company.service.SubscriptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @ApiOperation(value = "Create", notes="Method for creating a subscription")
    @PostMapping("/public/create")
    public ResponseEntity<Void> create(@RequestBody @Valid SubscriptionCreateDTO dto) {
        subscriptionService.create(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change subscription status", notes="Method for changing subscription status")
    @PutMapping("/public/change/status")
    public ResponseEntity<Void> changeStatus(@RequestBody @Valid SubscriptionUpdateStatusDTO dto) {
        subscriptionService.changeStatus(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change subscription notification type",
            notes="Method for changing subscription notification type")
    @PutMapping("/public/change/type")
    public ResponseEntity<Void> changeNotificationType(@RequestBody
                                                       @Valid SubscriptionUpdateNotificationTypeDTO dto) {
        subscriptionService.changeNotificationType(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get list for profile",
            notes="Method for getting subscription list for a user")
    @GetMapping("/public/list")
    public ResponseEntity<List<SubscriptionInfoDTO>> listForUser(@RequestParam("p") Integer profileId) {
        return ResponseEntity.ok().body(subscriptionService.listByProfileId(profileId));
    }

    @ApiOperation(value = "Get list for profile",
            notes="Method for getting subscription list for an admin")
    @GetMapping("/adm/list")
    private ResponseEntity<List<SubscriptionInfoDTO>> listForAdmin(@RequestParam("p") Integer profileId) {
        return ResponseEntity.ok().body(subscriptionService.listByProfileId(profileId));
    }


}
