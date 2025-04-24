package com.ordem.servico.api.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ordem.servico.api.dto.RelatorioDTO;
import com.ordem.servico.api.model.ImageEntity;
import com.ordem.servico.api.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final float LOGO_WIDTH = 80f;
    private static final float LOGO_HEIGHT = 80f;
    private static final float IMAGE_WIDTH = 200f;
    private static final float IMAGE_HEIGHT = 200f;
    private static final String LOGO_FILENAME = "logo_empresa.jpg";

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
    private static final Font COMPANY_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
    private static final Font INFO_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
    private static final Font TABLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8);

    private final ImageRepository imageRepository;

    @Autowired
    public ReportService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] criarRelatorio() throws DocumentException, IOException {
        List<byte[]> validImages = loadAndValidateImages();
        RelatorioDTO relatorio = RelatorioDTO.criarRelatorioMock(validImages);
        return generatePdfDocument(relatorio);
    }

    private List<byte[]> loadAndValidateImages() {
        return imageRepository.findAll().stream()
                .filter(img -> img.getImageData() != null
                        && !img.getFileName().equals(LOGO_FILENAME)
                        && isImageValid(img.getImageData()))
                .map(ImageEntity::getImageData)
                .collect(Collectors.toList());
    }

    private boolean isImageValid(byte[] imageData) {
        try {
            Image.getInstance(imageData);
            return true;
        } catch (Exception e) {
            System.err.println("Imagem inválida: " + e.getMessage());
            return false;
        }
    }

    private byte[] generatePdfDocument(RelatorioDTO relatorio) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        addReportContent(document, relatorio);
        document.close();

        return outputStream.toByteArray();
    }

    private void addReportContent(Document document, RelatorioDTO relatorio) throws DocumentException {
        addTitleSection(document);
        addHeaderSection(document, relatorio);
        addTechnicalDataSection(document, relatorio);
        addEquipmentDataSection(document, relatorio);
        addClientDataSection(document, relatorio);
        addServicesSection(document, relatorio);
        addWorkedHoursSection(document, relatorio);
        addDisplacementSection(document, relatorio);
        addPartsSection(document, relatorio);
        document.newPage();
        addImagesSection(document, relatorio);
    }

    private void addTitleSection(Document document) throws DocumentException {
        PdfPTable titleTable = createSingleCellTable("RELATÓRIO DE ASSISTÊNCIA TÉCNICA", TITLE_FONT);
        titleTable.setSpacingAfter(10);
        document.add(titleTable);
    }

    private void addHeaderSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(15);
        headerTable.setWidths(new float[]{20f, 50f, 30f});

        headerTable.addCell(createLogoCell());
        headerTable.addCell(createCompanyInfoCell(relatorio));
        headerTable.addCell(createServiceOrderCell(relatorio));

        document.add(headerTable);
    }

    private PdfPCell createLogoCell() {
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setPadding(3);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        try {
            ImageEntity logoEntity = imageRepository.findByFileName(LOGO_FILENAME);
            if (logoEntity != null && isImageValid(logoEntity.getImageData())) {
                Image logo = Image.getInstance(logoEntity.getImageData());
                logo.scaleToFit(LOGO_WIDTH, LOGO_HEIGHT);
                logoCell.addElement(logo);
            } else {
                logoCell.addElement(new Paragraph("LOGO", INFO_FONT));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo: " + e.getMessage());
            logoCell.addElement(new Paragraph("LOGO", INFO_FONT));
        }

        return logoCell;
    }

    private PdfPCell createCompanyInfoCell(RelatorioDTO relatorio) {
        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.setPadding(1);
        companyCell.setVerticalAlignment(5);

        companyCell.addElement(new Paragraph(relatorio.getEmpresa(), COMPANY_FONT));
        companyCell.addElement(new Paragraph(relatorio.getEnderecoEmpresa(), INFO_FONT));
        companyCell.addElement(new Paragraph("Tel: " + relatorio.getTelefoneEmpresa(), INFO_FONT));

        return companyCell;
    }

    private PdfPCell createServiceOrderCell(RelatorioDTO relatorio) {
        PdfPCell osCell = new PdfPCell();
        osCell.setBorder(Rectangle.BOX);
        osCell.setPadding(5);
        osCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        osCell.addElement(new Paragraph("ORDEM DE SERVIÇOS " + relatorio.getOrdemServico(), COMPANY_FONT));
        osCell.addElement(new Paragraph("Status OS: " + relatorio.getStatusOS(), INFO_FONT));
        osCell.addElement(new Paragraph("Data de Abertura: " + relatorio.getDataAbertura(), INFO_FONT));
        osCell.addElement(new Paragraph("Data de Fechamento: " + relatorio.getDataFechamento(), INFO_FONT));
        osCell.addElement(new Paragraph("Data/Hora Emissão: " + relatorio.getDataEmissao(), INFO_FONT));

        return osCell;
    }

    private void addTechnicalDataSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "DADOS DO TÉCNICO");
        PdfPTable table = createDataTable(
                new String[]{"Cracha", "Nome"},
                new String[]{relatorio.getCrachaTecnico(), relatorio.getNomeTecnico()}
        );
        document.add(table);
    }

    private void addEquipmentDataSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "DADOS DO EQUIPAMENTO");
        PdfPTable table = createDataTable(
                new String[]{"Equipamento", "Chassi", "Horímetro", "Data Atendimento"},
                new String[]{relatorio.getEquipamento(), relatorio.getChassi(), relatorio.getHorimetro(), relatorio.getDataAtendimento()}
        );
        document.add(table);
    }

    private void addClientDataSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "DADOS DO CLIENTE");
        PdfPTable table = createDataTable(
                new String[]{"Cliente", "Endereço", "CEP", "Bairro", "Cidade", "Telefone", "E-mail"},
                new String[]{relatorio.getCliente(), relatorio.getEnderecoCliente(), relatorio.getCepCliente(),
                        relatorio.getBairroCliente(), relatorio.getCidadeCliente(), relatorio.getTelefoneCliente(),
                        relatorio.getEmailCliente()}
        );
        document.add(table);
    }

    private void addServicesSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "SERVIÇOS REALIZADOS");
        PdfPTable table = createDataTable(
                new String[]{"Reclamação Cliente", "Análise da Falha", "Solução"},
                new String[]{relatorio.getReclamacaoCliente(), relatorio.getAnaliseFalha(), relatorio.getSolucao()}
        );
        document.add(table);
    }

    private void addWorkedHoursSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "HORAS TRABALHADAS");
        PdfPTable table = createMultiColumnTable(
                new String[]{"Data", "Serviço", "Técnico", "Início", "Término", "Horas Trabalhadas"},
                relatorio.getHorasTrabalhadas()
        );
        document.add(table);
    }

    private void addDisplacementSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "DESLOCAMENTO");
        PdfPTable table = createMultiColumnTable(
                new String[]{"Data", "Placa", "Saída De", "Km Inicial", "Chegada Em", "Km Final", "Total de Km"},
                relatorio.getDeslocamento()
        );
        document.add(table);
    }

    private void addPartsSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "PEÇAS UTILIZADAS");
        PdfPTable table = createMultiColumnTable(
                new String[]{"Código", "Descrição", "Quantidade Requisitada", "Quantidade Utilizada", "Quantidade Devolvida"},
                relatorio.getPecasUtilizadas()
        );
        document.add(table);
    }

    private void addImagesSection(Document document, RelatorioDTO relatorio) throws DocumentException {
        addSectionTitle(document, "FOTOS");
        PdfPTable imagesTable = new PdfPTable(2);
        imagesTable.setWidthPercentage(100);
        imagesTable.setSpacingBefore(10);

        for (byte[] image : relatorio.getImagens()) {
            imagesTable.addCell(createImageCell(image));
        }

        document.add(imagesTable);
    }

    private PdfPCell createImageCell(byte[] imageData) {
        PdfPCell cell = new PdfPCell();

        try {
            if (imageData != null && isImageValid(imageData)) {
                Image img = Image.getInstance(imageData);
                img.scaleToFit(IMAGE_WIDTH, IMAGE_HEIGHT);
                cell = new PdfPCell(img, true);
            } else {
                cell = createErrorCell("Imagem inválida");
            }
        } catch (Exception e) {
            System.err.println("Erro ao adicionar imagem: " + e.getMessage());
            cell = createErrorCell("Erro ao carregar imagem");
        }

        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell createErrorCell(String message) {
        PdfPCell cell = new PdfPCell(new Phrase(message, INFO_FONT));
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        PdfPTable sectionTable = createSingleCellTable(title, SECTION_FONT);
        sectionTable.setSpacingBefore(10);
        document.add(sectionTable);
    }

    private PdfPTable createSingleCellTable(String content, Font font) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        table.addCell(cell);
        return table;
    }

    private PdfPTable createDataTable(String[] headers, String[] values) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        for (int i = 0; i < headers.length; i++) {
            table.addCell(createTableCell(headers[i], true));
            table.addCell(createTableCell(values[i], false));
        }

        return table;
    }

    private PdfPTable createMultiColumnTable(String[] headers, List<String[]> rows) {
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);

        for (String header : headers) {
            table.addCell(createTableCell(header, true));
        }

        for (String[] row : rows) {
            for (String cellValue : row) {
                table.addCell(createTableCell(cellValue, false));
            }
        }

        return table;
    }

    private PdfPCell createTableCell(String text, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(text, TABLE_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(3);

        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }

        return cell;
    }
}