

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

Future<void> showAndSelectDateUtil({
  required BuildContext context,
  required DateTime? currentDateTime,
  required Function(DateTime) onDateSelected,
  required TextEditingController dateController,
}) async {

  final DateTime? picked = await showDatePicker(
    context: context,
    initialDate: currentDateTime ?? DateTime.now(),
    firstDate: DateTime(2000),
    lastDate: DateTime(2101),
  );


  if (picked != null && picked != currentDateTime) {

    onDateSelected(picked);

    dateController.text = DateFormat('yyyy-MM-dd').format(picked);
  }
}