
import 'package:flutter/services.dart';

class CurrencyInputFormatter extends TextInputFormatter{
  @override
  TextEditingValue formatEditUpdate(TextEditingValue oldValue, TextEditingValue newValue) {

    final String digits = newValue.text.replaceAll(RegExp(r'[^0-9]'), '');

    if (digits.isEmpty) {
      return TextEditingValue(
        text: '', selection: TextSelection.collapsed(offset: 0)
      );
    }

    final int cents = int.tryParse(digits) ?? 0;

    final double value = cents / 100.0;

    final String newText = value.toStringAsFixed(2);

    return TextEditingValue(
      text: newText,
      selection: TextSelection.collapsed(offset: newText.length)
    );

  }

}