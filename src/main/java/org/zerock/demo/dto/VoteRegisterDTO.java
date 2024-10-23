package org.zerock.demo.dto;

import lombok.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class VoteRegisterDTO {
    private String vote_title;
    private String vote_writer;
    private ArrayList<String> vote_items;
}
