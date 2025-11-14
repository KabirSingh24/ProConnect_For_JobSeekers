package com.example.backend.service;


import com.example.backend.model.Education;
import com.example.backend.model.Profile;
import com.example.backend.model.User;
import com.example.backend.model.Work;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    public byte[] generateResumePdf(User user, Profile profile) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add profile picture (if stored as URL or base64)
            if (user.getProfilePicture() != null) {
                Image img = Image.getInstance(user.getProfilePicture());
                img.scaleToFit(100, 100);
                document.add(img);
            }

            // Add basic user info
            document.add(new Paragraph("Name: " + user.getName()));
            document.add(new Paragraph("Email: " + user.getEmail()));
            document.add(new Paragraph(" "));

            // Add profile info
            document.add(new Paragraph("Bio: " + profile.getBio()));
            document.add(new Paragraph("Current Post: " + profile.getCurrentPost()));
            document.add(new Paragraph(" "));

            // Add Work Experience
            document.add(new Paragraph("Work Experience:"));
            for (Work work : profile.getPastWork()) {
                document.add(new Paragraph("- " + work.getCompany() + " | " + work.getPosition()));
            }

            document.add(new Paragraph(" "));

            // Add Education
            document.add(new Paragraph("Education:"));
            for (Education edu : profile.getEducation()) {
                document.add(new Paragraph("- " + edu.getSchool() + " | " + edu.getDegree()));
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }

        return outputStream.toByteArray();
    }
}
