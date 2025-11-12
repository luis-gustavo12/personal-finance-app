
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'currency_input_formatter.dart';

class AmountFormField extends StatelessWidget {

  final TextEditingController controller;
  final String label;

  const AmountFormField({
    Key? key,
    required this.controller,
    this.label = "Amount", // Default value
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return TextFormField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
        border: OutlineInputBorder()
      ),
      validator: (value) {
        if (value == null || value.isEmpty) {
          return "Please, provide a value!!";
        }
        if (double.tryParse(value) == 0.0) {
          return "$label cannot be 0.0!!";
        }
        return null;
      },
      keyboardType: TextInputType.numberWithOptions(decimal: false),
      inputFormatters: [
        FilteringTextInputFormatter.digitsOnly,
        CurrencyInputFormatter(),
      ],
    );

  }

}