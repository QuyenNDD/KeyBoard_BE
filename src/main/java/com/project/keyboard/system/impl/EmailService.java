package com.project.keyboard.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOrderConfirmation(String toEmail, String customerPhone, String address, List<String> itemLines, BigDecimal totalPrice, String orderCode, BigDecimal shippingFee) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");  // Trùng với cấu hình
        message.setTo(toEmail);
        message.setSubject("Xác nhận đơn hàng #" + orderCode);

        StringBuilder sb = new StringBuilder();
        sb.append("Xin chào,\n\n");
        sb.append("Cảm ơn bạn đã đặt hàng tại Cửa Hàng Bàn Phím!\n\n");
        sb.append("Mã đơn hàng: ").append(orderCode).append("\n");
        sb.append("Số điện thoại: ").append(customerPhone).append("\n");
        sb.append("Địa chỉ nhận hàng: ").append(address).append("\n");
        sb.append("Ngày đặt: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");

        sb.append("Sản phẩm đã đặt:\n");
        for (String line : itemLines) {
            sb.append("- ").append(line).append("\n");
        }
        sb.append("- Phí ship: ").append(shippingFee).append(".\n");
        sb.append("\nTổng cộng: ").append(String.format("%,1f", totalPrice)).append(" VNĐ\n\n");
        sb.append("Chúng tôi sẽ xử lý đơn hàng trong thời gian sớm nhất.\n");
        sb.append("Trân trọng,\nCửa Hàng Bàn Phím.");

        message.setText(sb.toString());
        javaMailSender.send(message);
    }

    public void sendTestMail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
