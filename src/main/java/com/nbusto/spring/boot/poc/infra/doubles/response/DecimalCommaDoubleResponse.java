package com.nbusto.spring.boot.poc.infra.doubles.response;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Optional;

public record DecimalCommaDoubleResponse(String value) {

  public static DecimalCommaDoubleResponse fromDouble(Double value) {
    return new DecimalCommaDoubleResponse(
      Optional.of(value)
        .map(DecimalCommaDoubleResponse::applyFormat)
        .get()
    );
  }

  private static String applyFormat(Double value) {
    final var symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator(',');
    symbols.setGroupingSeparator('.');

    // TODO try to use pattern that returns all decimals (size independent)
    return new DecimalFormat("#,###,###0.0##", symbols).format(value);
  }
}
