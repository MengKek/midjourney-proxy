package com.github.novicezk.midjourney.request;

import lombok.Data;

@Data
public class AttachmentRequest {

    private String code;

    // @NotNull(message = "width is null", groups = {Create.class})
    private Integer width;

    // @NotNull(message = "height is null", groups = {Create.class})
    private Integer height;

    // @NotNull(message = "imageSize is null", groups = {Create.class})
    private Integer imageSize;

    private String address;

}
