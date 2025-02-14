package com.photosystem.dto;

import lombok.Data;
import java.util.Set;

@Data
public class BatchOperationRequest {
    private Set<Long> photoIds;
    private Long targetAlbumId; // 用于移动到相册操作
    private Set<String> tags; // 用于批量添加标签
}