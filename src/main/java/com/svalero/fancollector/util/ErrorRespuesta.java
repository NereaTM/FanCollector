package com.svalero.fancollector.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRespuesta {
    private int statusCode;
    private String message;
    private Map<String, String> errors = new HashMap<>();

    public ErrorRespuesta(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = new HashMap<>();
    }

    public static ErrorRespuesta error400(String message) {return new ErrorRespuesta(400, message);}

    public static ErrorRespuesta error404(String message) {return new ErrorRespuesta(404, message);}

    public static ErrorRespuesta error500(String message) {return new ErrorRespuesta(500, message);}

}