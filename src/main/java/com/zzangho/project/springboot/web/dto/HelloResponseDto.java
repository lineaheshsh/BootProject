package com.zzangho.project.springboot.web.dto;

// 선언된 모든 필드의 get 메소드를 생성해 줍니다.
// 선언된 모든 final 필드가 포함된 생성자를 생성해 줍니다.
public class HelloResponseDto {

    private final String name;
    private final int amount;

    public HelloResponseDto(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }
}
