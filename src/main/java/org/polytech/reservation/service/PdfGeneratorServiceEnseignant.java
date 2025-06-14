package org.polytech.reservation.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import org.polytech.reservation.service.DTO.classes.CoursDTO;
import org.polytech.reservation.models.Enseignant;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class PdfGeneratorServiceEnseignant {

    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(51, 122, 183);
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(248, 248, 248);
    private static final DeviceRgb WHITE = new DeviceRgb(255, 255, 255);

    public byte[] generateRecapitulatifHorairePdf(Enseignant enseignant, List<CoursDTO> cours) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            // Polices
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // En-tête du document
            addHeader(document, titleFont);

            // Informations de l'enseignant
            addEnseignantInfo(document, enseignant, cours, headerFont, normalFont);

            // Tableau des cours
            if (!cours.isEmpty()) {
                addCoursTable(document, cours, headerFont, normalFont);
            } else {
                document.add(new Paragraph("Aucun cours assigné à cet enseignant.")
                        .setFont(normalFont)
                        .setFontSize(12)
                        .setMarginTop(20));
            }

            // Pied de page
            addFooter(document, normalFont);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private void addHeader(Document document, PdfFont titleFont) {
        Paragraph title = new Paragraph("RÉCAPITULATIF HORAIRE ENSEIGNANT")
                .setFont(titleFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(HEADER_COLOR)
                .setMarginBottom(20);
        document.add(title);
    }

    private void addEnseignantInfo(Document document, Enseignant enseignant, List<CoursDTO> cours,
                                   PdfFont headerFont, PdfFont normalFont) {

        // Calcul du total d'heures
        int totalHeures = cours.stream().mapToInt(CoursDTO::getNbHeure).sum();

        // Tableau d'informations
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(20);

        addInfoRow(infoTable, "Matricule:", enseignant.getMatricule(), headerFont, normalFont);
        addInfoRow(infoTable, "Nom:", enseignant.getNom(), headerFont, normalFont);
        addInfoRow(infoTable, "Prénom:", enseignant.getPrenom(), headerFont, normalFont);
        addInfoRow(infoTable, "Grade:", enseignant.getGrade(), headerFont, normalFont);
        addInfoRow(infoTable, "Nombre de cours:", String.valueOf(cours.size()), headerFont, normalFont);
        addInfoRow(infoTable, "Total d'heures:", totalHeures + " heures", headerFont, normalFont);

        document.add(infoTable);
    }

    private void addInfoRow(Table table, String label, String value, PdfFont headerFont, PdfFont normalFont) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(headerFont).setFontSize(10))
                .setBackgroundColor(LIGHT_GRAY)
                .setBorder(Border.NO_BORDER)
                .setPadding(8);

        Cell valueCell = new Cell()
                .add(new Paragraph(value).setFont(normalFont).setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPadding(8);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addCoursTable(Document document, List<CoursDTO> cours, PdfFont headerFont, PdfFont normalFont) {
        // Titre de la section
        Paragraph coursTitle = new Paragraph("DÉTAIL DES COURS")
                .setFont(headerFont)
                .setFontSize(14)
                .setFontColor(HEADER_COLOR)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(coursTitle);

        // Tableau des cours
        Table coursTable = new Table(UnitValue.createPercentArray(new float[]{25, 35, 15, 12, 13}))
                .setWidth(UnitValue.createPercentValue(100));

        // En-têtes
        addTableHeader(coursTable, "Date", headerFont);
        addTableHeader(coursTable, "Sujet", headerFont);
        addTableHeader(coursTable, "Heure", headerFont);
        addTableHeader(coursTable, "Durée", headerFont);
        addTableHeader(coursTable, "Salle", headerFont);

        // Données
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean alternateRow = false;

        for (CoursDTO coursDto : cours) {
            DeviceRgb bgColor = alternateRow ? LIGHT_GRAY : WHITE;

            addTableCell(coursTable, coursDto.getJour().format(dateFormatter), normalFont, bgColor);
            addTableCell(coursTable, coursDto.getTitre(), normalFont, bgColor);
            addTableCell(coursTable, coursDto.getHeure().toString(), normalFont, bgColor);
            addTableCell(coursTable, coursDto.getNbHeure() + "h", normalFont, bgColor);
            addTableCell(coursTable, coursDto.getSalle() != null ? coursDto.getSalle().getNomSalle() : "Non assignée", normalFont, bgColor);

            alternateRow = !alternateRow;
        }

        document.add(coursTable);
    }

    private void addTableHeader(Table table, String text, PdfFont font) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(HEADER_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
        table.addHeaderCell(cell);
    }

    private void addTableCell(Table table, String text, PdfFont font, DeviceRgb backgroundColor) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(9))
                .setBackgroundColor(backgroundColor)
                .setPadding(6)
                .setTextAlignment(TextAlignment.LEFT);
        table.addCell(cell);
    }

    private void addFooter(Document document, PdfFont font) {
        Paragraph footer = new Paragraph("Document généré le " +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " - Système de Réservation Polytech")
                .setFont(font)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY)
                .setMarginTop(30);
        document.add(footer);
    }
}
