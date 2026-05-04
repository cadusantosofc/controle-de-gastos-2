package br.com.controledegastos.controller;

import br.com.controledegastos.model.Lancamento;
import br.com.controledegastos.model.TipoLancamento;
import br.com.controledegastos.repository.LancamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LancamentoControllerTest {

    @Mock
    private LancamentoRepository lancamentoRepository;

    @Mock
    private Model model;

    @InjectMocks
    private LancamentoController controller;

    @Captor
    private ArgumentCaptor<List<Lancamento>> lancamentoListCaptor;

    @Test
    void indexShouldPopulateModelAndReturnIndex() {
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao("Teste");
        when(lancamentoRepository.findAll()).thenReturn(List.of(lancamento));

        String viewName = controller.index(model);

        assertEquals("index", viewName);
        verify(lancamentoRepository).findAll();
        verify(model).addAttribute(eq("lancamentos"), lancamentoListCaptor.capture());
        verify(model).addAttribute(eq("novoLancamento"), any(Lancamento.class));
        verify(model).addAttribute(eq("tipos"), eq(TipoLancamento.values()));
        verify(model).addAttribute(eq("lancamentoParaEditar"), any(Lancamento.class));
        assertEquals(1, lancamentoListCaptor.getValue().size());
    }

    @Test
    void addLancamentoShouldSaveAndReturnFragment() {
        Lancamento novoLancamento = new Lancamento();
        novoLancamento.setDescricao("Teste") ;
        when(lancamentoRepository.findAll()).thenReturn(List.of(novoLancamento));

        String viewName = controller.addLancamento(novoLancamento, model);

        assertEquals("index :: lista-lancamentos", viewName);
        verify(lancamentoRepository).save(novoLancamento);
        verify(lancamentoRepository).findAll();
    }

    @Test
    void deleteLancamentoShouldDeleteByIdAndReturnFragment() {
        String viewName = controller.deleteLancamento(42L, model);

        assertEquals("index :: lista-lancamentos", viewName);
        verify(lancamentoRepository).deleteById(42L);
        verify(lancamentoRepository).findAll();
    }

    @Test
    void editLancamentoWhenFoundShouldReturnFormEdition() {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(5L);
        when(lancamentoRepository.findById(5L)).thenReturn(Optional.of(lancamento));

        String viewName = controller.editLancamento(5L, model);

        assertEquals("index :: form-edicao", viewName);
        verify(model).addAttribute("lancamentoParaEditar", lancamento);
        verify(model).addAttribute("tipos", TipoLancamento.values());
    }

    @Test
    void editLancamentoWhenNotFoundShouldReturnListFragment() {
        when(lancamentoRepository.findById(99L)).thenReturn(Optional.empty());

        String viewName = controller.editLancamento(99L, model);

        assertEquals("index :: lista-lancamentos", viewName);
    }

    @Test
    void updateLancamentoWhenFoundShouldSaveUpdatedLancamento() {
        Lancamento lancamentoExistente = new Lancamento();
        lancamentoExistente.setId(1L);
        lancamentoExistente.setDescricao("Antigo");
        lancamentoExistente.setValor(BigDecimal.valueOf(10.0));
        lancamentoExistente.setTipo(TipoLancamento.DESPESA);
        lancamentoExistente.setData(LocalDate.of(2026, 5, 4));

        Lancamento lancamentoAtualizado = new Lancamento();
        lancamentoAtualizado.setDescricao("Novo");
        lancamentoAtualizado.setValor(BigDecimal.valueOf(20.0));
        lancamentoAtualizado.setTipo(TipoLancamento.RECEITA);
        lancamentoAtualizado.setData(LocalDate.of(2026, 5, 5));

        when(lancamentoRepository.findById(1L)).thenReturn(Optional.of(lancamentoExistente));

        String viewName = controller.updateLancamento(1L, lancamentoAtualizado, model);

        assertEquals("index :: lista-lancamentos", viewName);
        verify(lancamentoRepository).save(lancamentoExistente);
        assertEquals("Novo", lancamentoExistente.getDescricao());
        assertEquals(BigDecimal.valueOf(20.0), lancamentoExistente.getValor());
        assertSame(TipoLancamento.RECEITA, lancamentoExistente.getTipo());
        assertEquals(LocalDate.of(2026, 5, 5), lancamentoExistente.getData());
    }
}
