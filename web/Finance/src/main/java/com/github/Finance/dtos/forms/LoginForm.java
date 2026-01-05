package com.github.Finance.dtos.forms;

public record LoginForm(
    String email,
    String password
) {
    @Override
    public String toString() {
        return "LoginForm[" +
                "email=" + email +
                ", password=********" + // Mask the sensitive data
                "]";
    }
}
