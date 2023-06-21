# Metric Boleto

```
- inclusao.count{...} 123
- inclusao.custom.count{canal="(bankline|mobile|legado)", pagamento="(parcial|integral)", banco="(itau|outros_bancos|outros_bancos_online)", operacao="(dia|agendada)", contingencia="true"} 123
- - inclusao.custom.sum{...} 1234.56
```
