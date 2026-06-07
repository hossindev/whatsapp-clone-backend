package com.ryzzlab.chatapp;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateGroupRequest {
    String name;
    List<UUID> memberIds;
}
