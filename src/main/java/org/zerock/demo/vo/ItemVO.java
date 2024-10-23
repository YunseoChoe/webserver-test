package org.zerock.demo.vo;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ItemVO {
    private ArrayList<String> vote_items;
    private int vote_vote_id;
    private String item;
    private int item_count;
}
