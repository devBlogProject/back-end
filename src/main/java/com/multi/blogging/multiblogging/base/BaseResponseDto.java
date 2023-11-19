package com.multi.blogging.multiblogging.base;

import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

public abstract class BaseResponseDto {
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;

}
