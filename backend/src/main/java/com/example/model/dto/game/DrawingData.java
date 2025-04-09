package com.example.model.dto.game;

import lombok.Data;

@Data
public class DrawingData {
    private String roomId;
    private String senderSessionId;
    private String strokeData;
    private String color;
    private float lineWidth;
}
