package com.ordem.servico.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RelatorioDTO {
    private String empresa;
    private String enderecoEmpresa;
    private String telefoneEmpresa;
    private String ordemServico;
    private String statusOS;
    private String dataAbertura;
    private String dataFechamento;
    private String dataEmissao;
    private String crachaTecnico;
    private String nomeTecnico;
    private String equipamento;
    private String chassi;
    private String horimetro;
    private String dataAtendimento;
    private String cliente;
    private String enderecoCliente;
    private String cepCliente;
    private String bairroCliente;
    private String cidadeCliente;
    private String telefoneCliente;
    private String emailCliente;
    private String reclamacaoCliente;
    private String analiseFalha;
    private String solucao;
    private List<String[]> horasTrabalhadas;
    private List<String[]> deslocamento;
    private List<String[]> pecasUtilizadas;
    private List<byte[]> imagens;

    public static RelatorioDTO criarRelatorioMock(List<byte[]> imagensBytes) {
        RelatorioDTO relatorio = new RelatorioDTO();
        relatorio.setEmpresa("NORDESTE SERVIÇOS");
        relatorio.setEnderecoEmpresa("RODOVIA BR.116, 10.001, KM 10, PARQUE SANTA MARIA - FORTALEZA - CEP: 60.873-155 - CE");
        relatorio.setTelefoneEmpresa("(85) 4011-6400");
        relatorio.setOrdemServico("30098");
        relatorio.setStatusOS("Encerrada");
        relatorio.setDataAbertura("28/10/2024");
        relatorio.setDataFechamento("28/10/2024");
        relatorio.setDataEmissao("21/01/2025 15:38:36");
        relatorio.setCrachaTecnico("220");
        relatorio.setNomeTecnico("MARCIO CONRADO FONTINELE");
        relatorio.setEquipamento("EMPILHADEIRA HS0FT");
        relatorio.setChassi("A977Y11631H");
        relatorio.setHorimetro("04897");
        relatorio.setDataAtendimento("28/10/2024");
        relatorio.setCliente("CARMEHIL COMERCIAL ELETRICA LTDA.");
        relatorio.setEnderecoCliente("BEZERRA DE MENEZES, 170");
        relatorio.setCepCliente("60325003");
        relatorio.setBairroCliente("FARIAS BRITO");
        relatorio.setCidadeCliente("FORTALEZA - CE");
        relatorio.setTelefoneCliente("85 32323232");
        relatorio.setEmailCliente("fiscal@carmehil.com.br");
        relatorio.setReclamacaoCliente("MÁQUINA PARADA");
        relatorio.setAnaliseFalha("MANGUEIRA DO BOTIJÃO E SENSOR DA BORBOLETA");
        relatorio.setSolucao("FOI REALIZADO A MANGUEIRA DO BOTIJÃO DE GÁS QUE APRESENTAVA VAZAMENTO E SUBSTITUIDO SENSOR DA BORBOLETA DO CARBURADOR QUE APRESENTA FALHA INTERMITENTE");
        relatorio.setHorasTrabalhadas(List.of(
                new String[]{"28/10/2024", "DESLOCAMENTO", "MARCIO CONRADO F", "11:51:51", "12:26:23", "00:34:32"},
                new String[]{"28/10/2024", "MAO DE OBRA", "MARCIO CONRADO F", "12:26:23", "13:16:20", "00:49:57"},
                new String[]{"28/10/2024", "DESLOCAMENTO", "MARCIO CONRADO F", "13:16:20", "14:03:48", "00:47:28"}
        ));
        relatorio.setDeslocamento(List.of(
                new String[]{"28/10/2024", "SAT-4F05", "DCDN", "99.542,00", "CARMEHILL", "99.560,00", "18,00"},
                new String[]{"28/10/2024", "SAT-4F05", "CARMEHILL", "99.560,00", "DCDN", "99.580,00", "20,00"}
        ));
        List<String[]> pecasUtilizadas = new ArrayList<>();
        pecasUtilizadas.add(new String[]{"1639791H", "SENSOR DE POSICAD", "1", "1", "0"});
        relatorio.setPecasUtilizadas(pecasUtilizadas);
        relatorio.setImagens(imagensBytes);
        return relatorio;
    }
}