/**
 * author @bhupendrasambare
 * Date   :19/01/25
 * Time   :7:10 pm
 * Project:shopping-bucket
 **/
package com.example.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartEndDateRequest {
    private Date startDate;
    private Date endDate;
}
