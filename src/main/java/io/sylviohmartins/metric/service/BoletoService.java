package io.sylviohmartins.metric.service;

import io.sylviohmartins.metric.constant.MessageConstants;
import io.sylviohmartins.metric.constant.MetricConstants;
import io.sylviohmartins.metric.domain.document.Boleto;
import io.sylviohmartins.metric.domain.enumeration.StatusEnum;
import io.sylviohmartins.metric.domain.mapper.BoletoMapper;
import io.sylviohmartins.metric.exception.AlreadyExistsException;
import io.sylviohmartins.metric.exception.RecordNotFoundException;
import io.sylviohmartins.metric.metric.Counter;
import io.sylviohmartins.metric.metric.Timer;
import io.sylviohmartins.metric.repository.BoletoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoletoService {

    private final BoletoRepository boletoRepository;

    private final BoletoMapper boletoMapper;

    @Counter(name = MetricConstants.INCLUSAO, shouldGenerate = true)
    @Timer(name = MetricConstants.INCLUSAO)
    public Boleto include(final Boleto boleto) throws AlreadyExistsException {
        boleto.setStatus(StatusEnum.INCLUIDO);

        try {
            return boletoRepository.save(boleto);

        } catch (final Exception unknownException) {
            throw new AlreadyExistsException(MessageConstants.ALREADY_EXISTS);
        }
    }

    @Counter(name = MetricConstants.AUTORIZACAO)
    @Timer(name = MetricConstants.AUTORIZACAO)
    public Boleto authorize(final Boleto boleto) throws RecordNotFoundException {
        boleto.setStatus(StatusEnum.AUTORIZADO);

        final Boleto persisted = boletoRepository.findById(boleto.getId()).orElseThrow(RecordNotFoundException::new);

        try {
            boletoMapper.merge(persisted, boleto);

            return boletoRepository.save(persisted);

        } catch (final Exception unknownException) {
            throw new RecordNotFoundException();
        }
    }

}
