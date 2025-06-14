package org.polytech.reservation.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.polytech.reservation.service.DTO.classes.CoursDTO;
import org.polytech.reservation.models.Formation;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGeneratorServiceFormation {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Génère un PDF pour le récapitulatif horaire d'une formation
     */
    public byte[] generateRecapHorairePdf(Formation formation, List<CoursDTO> cours, int totalHeures) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // En-tête du document
            addHeader(document, "RÉCAPITULATIF HORAIRE DE FORMATION");

            // Informations générales de la formation
            addFormationInfo(document, formation, cours.size(), totalHeures);

            // Tableau des cours
            addCoursTable(document, cours, "Détail des cours");

            // Pied de page
            addFooter(document);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF récapitulatif", e);
        }
    }

    /**
     * Génère un PDF pour le planning d'une formation
     */
    public byte[] generatePlanningPdf(Formation formation, List<CoursDTO> cours) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // En-tête du document
            addHeader(document, "PLANNING DE FORMATION");

            // Informations générales de la formation
            addFormationBasicInfo(document, formation);

            // Tableau du planning
            addPlanningTable(document, cours);

            // Pied de page
            addFooter(document);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF planning", e);
        }
    }

    private void addHeader(Document document, String title) {
        // Logo ou nom de l'établissement
        Paragraph header = new Paragraph("POLYTECH - SYSTÈME DE RÉSERVATION")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(20);
        document.add(header);

        // Titre principal
        Paragraph titleParagraph = new Paragraph(title)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(titleParagraph);
    }

    private void addFormationInfo(Document document, Formation formation, int nombreCours, int totalHeures) {
        // Informations de la formation
        Table infoTable = new Table(2);
        infoTable.setWidth(UnitValue.createPercentValue(100));

        addInfoRow(infoTable, "Formation :", formation.getTheme());
        addInfoRow(infoTable, "Responsable :",
                formation.getEnseignantResponsable().getNom() + " " +
                        formation.getEnseignantResponsable().getPrenom());
        addInfoRow(infoTable, "Nombre de places :", String.valueOf(formation.getNombreDePlaces()));
        addInfoRow(infoTable, "Nombre de cours :", String.valueOf(nombreCours));
        addInfoRow(infoTable, "Total d'heures :", totalHeures + " heures");

        document.add(infoTable);
        document.add(new Paragraph("\n"));
    }

    private void addFormationBasicInfo(Document document, Formation formation) {
        Table infoTable = new Table(2);
        infoTable.setWidth(UnitValue.createPercentValue(100));

        addInfoRow(infoTable, "Formation :", formation.getTheme());
        addInfoRow(infoTable, "Responsable :",
                formation.getEnseignantResponsable().getNom() + " " +
                        formation.getEnseignantResponsable().getPrenom());
        addInfoRow(infoTable, "Nombre de places :", String.valueOf(formation.getNombreDePlaces()));

        document.add(infoTable);
        document.add(new Paragraph("\n"));
    }

    private void addInfoRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label).setBold())
                .setPadding(5)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph(value))
                .setPadding(5));
    }

    private void addCoursTable(Document document, List<CoursDTO> cours, String tableTitle) {
        // Titre du tableau
        Paragraph tableHeader = new Paragraph(tableTitle)
                .setFontSize(14)
                .setBold()
                .setMarginBottom(10)
                .setMarginTop(20);
        document.add(tableHeader);

        // Création du tableau
        Table table = new Table(6);
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes
        String[] headers = {"Sujet", "Date", "Heure", "Durée (h)", "Enseignant", "Salle"};
        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header).setBold())
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setFontColor(ColorConstants.WHITE)
                    .setPadding(8)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        // Données
        for (CoursDTO coursDTO : cours) {
            table.addCell(new Cell().add(new Paragraph(coursDTO.getTitre())).setPadding(5));
            table.addCell(new Cell().add(new Paragraph(coursDTO.getJour().format(DATE_FORMATTER))).setPadding(5));
            table.addCell(new Cell().add(new Paragraph(coursDTO.getHeure().toString())).setPadding(5));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(coursDTO.getNbHeure())))
                    .setPadding(5)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(
                            coursDTO.getEnseignant().getNom() + " " + coursDTO.getEnseignant().getPrenom()))
                    .setPadding(5));
            table.addCell(new Cell().add(new Paragraph(
                            coursDTO.getSalle() != null ? coursDTO.getSalle().getNumSalle().toString() : "Non assignée"))
                    .setPadding(5));
        }

        document.add(table);
    }

    private void addPlanningTable(Document document, List<CoursDTO> cours) {
        // Titre du tableau
        Paragraph tableHeader = new Paragraph("Planning des cours")
                .setFontSize(14)
                .setBold()
                .setMarginBottom(10)
                .setMarginTop(20);
        document.add(tableHeader);

        // Création du tableau
        Table table = new Table(5);
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes
        String[] headers = {"Date", "Heure", "Sujet", "Enseignant", "Salle"};
        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header).setBold())
                    .setBackgroundColor(ColorConstants.BLUE)
                    .setFontColor(ColorConstants.WHITE)
                    .setPadding(8)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        // Données triées par date
        cours.stream()
                .sorted((c1, c2) -> {
                    int dateComparison = c1.getJour().compareTo(c2.getJour());
                    if (dateComparison != 0) return dateComparison;
                    return c1.getHeure().compareTo(c2.getHeure());
                })
                .forEach(coursDTO -> {
                    table.addCell(new Cell().add(new Paragraph(coursDTO.getJour().format(DATE_FORMATTER)))
                            .setPadding(5));
                    table.addCell(new Cell().add(new Paragraph(coursDTO.getHeure().toString()))
                            .setPadding(5));
                    table.addCell(new Cell().add(new Paragraph(coursDTO.getTitre()))
                            .setPadding(5));
                    table.addCell(new Cell().add(new Paragraph(
                                    coursDTO.getEnseignant().getNom() + " " + coursDTO.getEnseignant().getPrenom()))
                            .setPadding(5));
                    table.addCell(new Cell().add(new Paragraph(
                                    coursDTO.getSalle() != null ? coursDTO.getSalle().getNumSalle().toString() : "Non assignée"))
                            .setPadding(5));
                });

        document.add(table);
    }

    private void addFooter(Document document) {
        Paragraph footer = new Paragraph("Document généré le " + LocalDate.now().format(DATE_FORMATTER))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(30);
        document.add(footer);
    }
}
