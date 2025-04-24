package com.ordem.servico.api.exception.error;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Erro {
        private final Integer status;
        private final LocalDateTime dataHora;
        private final String mensagemTecnica;
        private final String mensagemUsuario;

        public Erro(Integer status, LocalDateTime dataHora, String mensagemTecnica, String mensagemUsuario) {
            this.status = status;
            this.dataHora = dataHora;
            this.mensagemTecnica = mensagemTecnica;
            this.mensagemUsuario = mensagemUsuario;
        }

    }