package com.company.dto.video;

import com.company.dto.AttachDTO;
import com.company.dto.CategoryDTO;
import com.company.dto.LikeDTO;
import com.company.dto.TagDTO;
import com.company.dto.channel.ChannelDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoFullInfoDTO {
    private VideoDTO video;

    private AttachDTO preview;

    private AttachDTO attach;

    private CategoryDTO category;

    private List<TagDTO> tagList;

    private ChannelDTO channel;

    private LikeDTO like;

}
