package com.example.task_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Schema(description = "Tên đăng nhập")
    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự")
    private String username;

    @Schema(description = "Mật khẩu chưa mã hóa")
    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải ít nhất 6 ký tự")
    private String password;

    @Schema(description = "Vai trò: USER hoặc ADMIN", defaultValue = "USER")
    @Pattern(regexp = "USER|ADMIN", message = "Role chỉ được là USER hoặc ADMIN")
    private String role;
}
