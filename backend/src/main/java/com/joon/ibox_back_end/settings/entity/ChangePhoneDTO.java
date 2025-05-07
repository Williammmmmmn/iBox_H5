package com.joon.ibox_back_end.settings.entity;

import lombok.Data;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-0611:50
 **/
@Data
public class ChangePhoneDTO {
    private String newPhone;
    private String code;
}
