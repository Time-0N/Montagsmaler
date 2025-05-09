package com.example.model.dto.game;

import lombok.Data;

@Data
public class DrawingData {
    private String roomId;
    private String senderSessionId;
    private String color;
    private float width;
    private Point from;
    private Point to;
}
