package io.sylviohmartins.metric.controller;

import io.sylviohmartins.metric.domain.document.Boleto;
import io.sylviohmartins.metric.domain.dto.BoletoDTO;
import io.sylviohmartins.metric.domain.mapper.BoletoMapper;
import io.sylviohmartins.metric.domain.vo.BoletoVO;
import io.sylviohmartins.metric.exception.AlreadyExistsException;
import io.sylviohmartins.metric.service.BoletoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/boletos")
@RequiredArgsConstructor
public class BoletoController {

    private final BoletoService boletoService;

    private final BoletoMapper boletoMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoletoDTO include(@Valid @RequestBody final BoletoVO data) throws AlreadyExistsException {
        final Boleto boleto = boletoService.include(boletoMapper.toEntity(data));

        return boletoMapper.toDTO(boleto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = BaseController.ID_PATH_VARIABLE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoletoDTO authorize(@PathVariable("id") final UUID id, @Valid @RequestBody final BoletoVO data) throws AlreadyExistsException {
        final Boleto boleto = boletoService.authorize(boletoMapper.toEntity(data, id));

        return boletoMapper.toDTO(boleto);
    }

    /*@ResponseStatus(HttpStatus.OK)
    @PutMapping(value = ID_PATH_VARIABLE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoletoDTO alter(@PathVariable("id") final Long id, @Valid @RequestBody final BoletoVO data) throws AlreadyExistsException {
        final Boleto boleto = boletoService.alter(boletoMapper.toEntity(data, id));

        return boletoMapper.toDTO(boleto);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{id}/cancel")
    public void cancel(@PathVariable("id") final Long id) {
        boletoService.cancel(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = ID_PATH_VARIABLE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoletoDTO findById(@PathVariable("id") final Long id) {
        final Boleto boleto = boletoService.findById(id);

        return boletoMapper.toDTO(boleto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<BoletoDTO> findByFiltro(final FiltroBoletoVO filtro, @RequestParam final int page, @RequestParam final int size) {
        final Page<Boleto> boletoPage = boletoService.findByFiltro(filtro, PageRequest.of(page, size));

        return Page.map(boletoMapper::toDTO);
    }*/

}
