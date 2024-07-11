package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
    @RequestMapping("consultas")
    public class ConsultaController {

        @PostMapping
        @Transactional
        public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
            System.out.println(dados);
            return ResponseEntity.ok(new DadosDetalhamentoConsulta(null, null, null, null));
        }

        private record DadosDetalhamentoConsulta(Long id, Long idMedico, Long idPaciente, LocalDateTime data) {
        }
    }

