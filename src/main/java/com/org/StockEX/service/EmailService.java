package com.org.StockEX.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Image;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // === 1. Send OTP Email ===
    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("OTP Verification - StockEX");
        message.setText("Your OTP is: " + otp + "\nIt is valid for 3 minutes.");

        mailSender.send(message);
        System.out.println("OTP sent to " + toEmail);
    }

    // === 2. Send Invoice Email with PDF Attachment ===
    public void sendInvoice(String toEmail, String username, String stockName, int quantity, BigDecimal total) {
        try {
            // 1. Generate PDF
            byte[] pdfBytes = generateInvoice(username, stockName, quantity, total);

            // 2. Create MIME email
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // 'true' enables multipart

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("📄 Your StockEX Invoice");

            // 3. Load logo image from resources
            ClassPathResource logo = new ClassPathResource("static/logoStockEX.jpg");

            // 4. Email Body in HTML
            String htmlBody = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <div style="border: 1px solid #ccc; padding: 20px; border-radius: 8px; max-width: 600px;">
                    <img src='cid:logoImage' style='width: 150px; margin-bottom: 20px;' />

                    <h2 style="color: green;">✅ Transaction Successful</h2>
                    <h3>Transaction Summary</h3>

                    <table style="width: 100%%; font-size: 14px;">
                        <tr><td><strong>User</strong></td><td>%s</td></tr>
                        <tr><td><strong>Stock</strong></td><td>%s</td></tr>
                        <tr><td><strong>Quantity</strong></td><td>%d</td></tr>
                        <tr><td><strong>Total</strong></td><td>₹ %s</td></tr>
                        <tr><td><strong>Date</strong></td><td>%s</td></tr>
                    </table>

                    <p style="margin-top: 20px;">This is your transaction receipt. You can also find the PDF attached.</p>
                    <p style="font-size: 12px; color: gray;">This email was generated automatically by StockEX. For help, contact support@stockex.com</p>
                </div>
            </body>
            </html>
        """.formatted(username, stockName, quantity, total, java.time.LocalDateTime.now().toString());

            helper.setText(htmlBody, true); // Enable HTML

            // 5. Attach image inline
            helper.addInline("logoImage", logo);

            // 6. Attach PDF
            DataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            helper.addAttachment("Invoice.pdf", dataSource);

            mailSender.send(mimeMessage);
            System.out.println("Styled Invoice sent to " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send styled invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // === 3. Invoice Generator (Inner Class) ===
    public  byte[] generateInvoice(String username, String stockName, int quantity, BigDecimal total) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // 1. Add the logo image
            String logoPath = "src/main/resources/static/logoStockEX.jpg"; // Make sure it's present
            ImageData logoData = ImageDataFactory.create(logoPath);
            Image logo = new Image(logoData).scaleToFit(150, 100);
            document.add(logo);

            // 2. Add title
            document.add(new Paragraph("StockEX Invoice")
                    .setFontSize(18)
                    .setBold()
                    .setMarginTop(10)
                    .setMarginBottom(20));

            // 3. Add invoice details
            document.add(new Paragraph("User: " + username));
            document.add(new Paragraph("Stock Sold: " + stockName));
            document.add(new Paragraph("Quantity: " + quantity));
            document.add(new Paragraph("Total Received: ₹" + total));
            document.add(new Paragraph("Date: " + java.time.LocalDateTime.now().toString()));

            // 4. Footer note
            document.add(new Paragraph("\n\nNote: This receipt is system generated and does not require a signature.")
                    .setFontSize(9)
                    .setFontColor(ColorConstants.GRAY));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
