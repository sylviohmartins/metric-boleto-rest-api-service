package io.sylviohmartins.metric.repository;

import io.sylviohmartins.metric.domain.document.Boleto;
import io.sylviohmartins.metric.domain.enumeration.MethodEnum;
import io.sylviohmartins.metric.domain.enumeration.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BoletoRepository {

    private final static UUID ID = UUID.fromString("3f1eeb33-8624-41f0-a1c8-0132ae813596");

    @SneakyThrows
    public Boleto save(final Boleto boleto) {
        if (!ID.equals(boleto.getId())) {
            throw new NullPointerException("Só um teste, viu?!");
        }

        return boleto;
    }

    public Optional<Boleto> findById(final UUID id) {
        if (ID.equals(id)) {
            final Boleto boleto = new Boleto();

            boleto.setId(ID);
            boleto.setDate(LocalDate.now());
            boleto.setMethod(MethodEnum.ITAU);
            boleto.setStatus(StatusEnum.INCLUIDO);

            return Optional.of(boleto);
        }

        return Optional.empty();
    }
}
