package com.company.dto.playlist;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PlaylistVideoDeleteDTO {
    @NotEmpty(message = "Video shouldn't be empty")
    private String videoId;

    @NotEmpty(message = "Playlist shouldn't be empty")
    private String playlistId;
}
