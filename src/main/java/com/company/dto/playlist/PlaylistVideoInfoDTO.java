package com.company.dto.playlist;

import com.company.dto.video.VideoDTO;
import com.company.dto.channel.ChannelDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistVideoInfoDTO {
    private PlaylistVideoDTO playlistVideo;

    private PlaylistDTO playlist;

    private VideoDTO video;

    private ChannelDTO channel;

//    playlist_id,video(id,preview_attach(id,key,url),title,duration),
//    channel(id,name,key),created_date, order_num
}
