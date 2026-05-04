package br.com.controledegastos.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LancamentoTest {

    @Test
    void gettersAndSettersShouldWork() {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(10L);
        lancamento.setDescricao("Teste");
        lancamento.setValor(BigDecimal.valueOf(150.75));
        lancamento.setData(LocalDate.of(2026, 5, 4));
        lancamento.setTipo(TipoLancamento.RECEITA);

        assertEquals(10L, lancamento.getId());
        assertEquals("Teste", lancamento.getDescricao());
        assertEquals(BigDecimal.valueOf(150.75), lancamento.getValor());
        assertEquals(LocalDate.of(2026, 5, 4), lancamento.getData());
        assertEquals(TipoLancamento.RECEITA, lancamento.getTipo());
    }

    @Test
    void defaultDateShouldBeToday() {
        Lancamento lancamento = new Lancamento();

        assertNotNull(lancamento.getData());
        assertEquals(LocalDate.now(), lancamento.getData());
    }
}
