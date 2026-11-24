package com.bagsys.authentication.dto;

import java.util.List;

public record CurrentUserResponse(String username, List<String> roles) {}
