import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';



Future<DateTime?> selectDate(
    BuildContext context, {
    DateTime? initialDate,
    DateTime? firstDate,
    DateTime? lastDate,
  }) async {
  final DateTime? picked = await showDatePicker(
    context: context,
    initialDate: initialDate?? DateTime.now(),
    firstDate: firstDate ?? DateTime(2000),
    lastDate: lastDate ?? DateTime(2101),
  );

  return picked;
}
