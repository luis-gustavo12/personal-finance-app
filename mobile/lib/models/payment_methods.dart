
import 'dart:ffi';

class PaymentMethods {
  final int id;
  final String description;

  const PaymentMethods({required this.id, required this.description});

  factory PaymentMethods.fromJson(Map<String, dynamic> json) {
    return PaymentMethods(id: json['id'], description: json['description']);
  }

}