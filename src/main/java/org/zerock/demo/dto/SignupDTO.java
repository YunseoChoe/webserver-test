package org.zerock.demo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class SignupDTO {
    private String username;
    private String password;
}
