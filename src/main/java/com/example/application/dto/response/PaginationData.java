/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :9:49 pm
 * Project:shopping-bucket
 **/
package com.example.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationData<T>{
    private List<T> data;
    private int page;
    private Long total;
    private int size;
}
