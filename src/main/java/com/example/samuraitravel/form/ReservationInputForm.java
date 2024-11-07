package com.example.samuraitravel.form;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReservationInputForm {
    @NotBlank(message = "チェックイン日とチェックアウト日を選択してください。")
    private String fromCheckinDateToCheckoutDate;

    @NotNull(message = "宿泊人数を入力してください。")
    @Min(value = 1, message = "宿泊人数は1人以上に設定してください。")
    private Integer numberOfPeople;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // チェックイン日を取得する
    public LocalDate getCheckinDate() {
        if (fromCheckinDateToCheckoutDate == null || fromCheckinDateToCheckoutDate.trim().isEmpty()) {
            return null;
        }
        try {
            String[] checkinDateAndCheckoutDate = fromCheckinDateToCheckoutDate.split("から");
            return LocalDate.parse(checkinDateAndCheckoutDate[0].trim(), DATE_FORMATTER);
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid check-in date format: " + fromCheckinDateToCheckoutDate);
            return null;
        }
    }

    // チェックアウト日を取得する
    public LocalDate getCheckoutDate() {
        if (fromCheckinDateToCheckoutDate == null || fromCheckinDateToCheckoutDate.trim().isEmpty()) {
            return null;
        }
        try {
            String[] checkinDateAndCheckoutDate = fromCheckinDateToCheckoutDate.split("から");
            return LocalDate.parse(checkinDateAndCheckoutDate[1].trim(), DATE_FORMATTER);
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid check-out date format: " + fromCheckinDateToCheckoutDate);
            return null;
        }
    }
}
