package org.zerock.demo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class LoginDTO {
    private String username;
    private String password;
}
