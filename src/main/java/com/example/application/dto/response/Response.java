/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :11:59 am
 * Project:shopping-bucket
 **/
package com.example.application.dto.response;

import com.example.application.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String code = Constants.SUCCESS_CODE;
    private Constants.Status status = Constants.Status.SUCCESS;
    private String message = Constants.OPERATION_SUCCESS;
    private Object data;

    public Response(String message){
        this.message = message;
    }

    public Response(String message,Object data){
        this.message = message;
        this.data = data;
    }

    public Response(Constants.Status status,String code,  String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
