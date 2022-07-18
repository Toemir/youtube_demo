package com.company.dto;

import com.company.entity.ReportEntity;
import com.company.enums.EntityType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReportCreateDTO {
    @NotBlank(message = "Entity shouldn't be empty")
    private String entityId;

    @NotBlank(message = "Content shouldn't be empty")
    private String content;

    @NotNull(message = "Type shouldn't be empty")
    private EntityType type;

    @NotNull(message = "Profile shouldn't be empty")
    private Integer profileId;


}
